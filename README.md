# codenarc-violations-printer-gradle-plugin
A Gradle plugin that prints CodeNarc violations to the console at QUIET level

Without it, violations are only printed at INFO level.
Many like to run a build at the default QUIET level as INFO is _very_ chatty.
This also means that for any CodeNarc failure, you will need to open the generated HTML report to see what the violations are, or re-run it at INFO level.
Both of these options are usually annoying locally and not feasible in a CI environment.

See https://github.com/gradle/gradle/issues/20428 for a Gradle issue about this exact problem.

## Usage
See https://plugins.gradle.org/plugin/io.github.bjornvester.codenarcprinter.