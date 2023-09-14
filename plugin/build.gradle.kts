plugins {
    `java-gradle-plugin`
    //id("org.jetbrains.kotlin.jvm") version "1.9.0"
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useKotlinTest("1.9.0")
        }

        val functionalTest by registering(JvmTestSuite::class) {
            useKotlinTest("1.9.0")

            dependencies {
                implementation(project())
            }

            targets {
                all {
                    testTask.configure { shouldRunAfter(test) }
                }
            }
        }
    }
}

gradlePlugin {
    val codenarcprinter by plugins.creating {
        id = "com.github.bjornvester.codenarcprinter"
        implementationClass = "com.github.bjornvester.codenarcprinter.CodenarcViolationsPrinterGradlePlugin"
    }
}

gradlePlugin.testSourceSets.add(sourceSets["functionalTest"])

tasks.named<Task>("check") {
    dependsOn(testing.suites.named("functionalTest"))
}
