package io.github.bjornvester.codenarcprinter

import org.gradle.testfixtures.ProjectBuilder
import kotlin.test.Test
import kotlin.test.assertNotNull

/**
 * A simple unit test for the 'io.github.bjornvester.codenarcprinter' plugin.
 */
class CodenarcViolationsPrinterGradlePluginTest {
    @Test fun `plugin registers task`() {
        // Create a test project and apply the plugin
        val project = ProjectBuilder.builder().build()
        project.plugins.apply("io.github.bjornvester.codenarcprinter")
    }
}
