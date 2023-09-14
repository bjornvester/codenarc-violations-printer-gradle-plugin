plugins {
    `java-gradle-plugin`
    //id("org.jetbrains.kotlin.jvm") version "1.9.0"
    `kotlin-dsl`
    id("com.gradle.plugin-publish") version "1.2.1"
}

group = "io.github.bjornvester.codenarcprinter"
version = "1.0"

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
        id = "io.github.bjornvester.codenarcprinter"
        displayName = "CodeNarc violations printer"
        description = "A plugin that prints CodeNarc violations to the console at QUIET level"
        tags.set(listOf("codenarc"))
        implementationClass = "com.github.bjornvester.codenarcprinter.CodenarcViolationsPrinterGradlePlugin"
        website = "https://github.com/bjornvester/codenarc-violations-printer-gradle-plugin"
        vcsUrl = "https://github.com/bjornvester/codenarc-violations-printer-gradle-plugin.git"
    }
}

gradlePlugin.testSourceSets.add(sourceSets["functionalTest"])

tasks.named<Task>("check") {
    dependsOn(testing.suites.named("functionalTest"))
}
