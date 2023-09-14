package com.github.bjornvester.codenarcprinter

import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.io.TempDir
import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * A simple functional test for the 'com.github.bjornvester.codenarcprinter.greeting' plugin.
 */
class CodenarcViolationsPrinterGradlePluginFunctionalTest {

    @field:TempDir
    lateinit var projectDir: File

    private val buildFile by lazy { projectDir.resolve("build.gradle") }
    private val settingsFile by lazy { projectDir.resolve("settings.gradle") }
    private val groovyFile by lazy { projectDir.resolve("src/main/groovy/com/example/MyClass.groovy") }
    private val groovyTestFile by lazy { projectDir.resolve("src/test/groovy/com/example/MyTestClass.groovy") }
    private val codenarcConfigFile by lazy { projectDir.resolve("config/codenarc/codenarc.xml") }

    @Test
    fun `can print codenarc violoations to console`() {
        // Set up the test build
        settingsFile.writeText("")

        buildFile.writeText("""
            plugins {
                id 'groovy'
                id 'codenarc'
                id 'com.github.bjornvester.codenarcprinter'
            }
            repositories {
                mavenCentral()
            }
        """.trimIndent())

        codenarcConfigFile.parentFile.mkdirs()
        codenarcConfigFile.writeText("""
<ruleset xmlns="http://codenarc.org/ruleset/1.0"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://codenarc.org/ruleset/1.0 https://raw.githubusercontent.com/CodeNarc/CodeNarc/master/src/main/resources/ruleset-schema.xsd"
        xsi:noNamespaceSchemaLocation="https://raw.githubusercontent.com/CodeNarc/CodeNarc/master/src/main/resources/ruleset-schema.xsd">
    <rule class='org.codenarc.rule.basic.EmptyClassRule'/>
</ruleset>""".trimIndent())

        groovyFile.parentFile.mkdirs()
        groovyFile.writeText("""public class MyClass {}""") // This violates the Codenarc rule "EmptyClassRule" configured in the build

        groovyTestFile.parentFile.mkdirs()
        groovyTestFile.writeText("""public class MyClass {}""") // Same

        // Run the build
        val runner = GradleRunner.create()
        runner.forwardOutput()
        runner.withPluginClasspath()
        runner.withArguments("codenarcMain", "codenarcTest", "--continue") // Default logging level (quiet)
        runner.withProjectDir(projectDir)
        val result = runner.buildAndFail()

        // Verify the result
        assertTrue(result.output.contains("Violation: Rule=EmptyClass P=2 Line=1"))
    }
}
