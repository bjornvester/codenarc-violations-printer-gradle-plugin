package io.github.bjornvester.codenarcprinter

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.quality.CodeNarc
import org.gradle.api.provider.Provider
import org.gradle.build.event.BuildEventsListenerRegistry
import javax.inject.Inject

@Suppress("unused")
abstract class CodenarcViolationsPrinterGradlePlugin : Plugin<Project> {
    @Inject
    abstract fun getEventsListenerRegistry(): BuildEventsListenerRegistry

    override fun apply(project: Project) {
        project.gradle.taskGraph.whenReady {
            val serviceProvider: Provider<TaskEventsService> = project.gradle.sharedServices.registerIfAbsent(
                "CodenarcViolationsPrinter",
                TaskEventsService::class.java
            ) {
                parameters.taskPathToReportFilePath.set(
                    allTasks.filterIsInstance<CodeNarc>()
                        .associate { it.path to it.reports.text.outputLocation.asFile.get() }
                )
            }
            getEventsListenerRegistry().onTaskCompletion(serviceProvider) // Though we add the same instance for each task, it is only registered once
        }
        project.tasks.withType(CodeNarc::class.java).configureEach {
            reports {
                text.required.set(true)
            }
        }
    }
}
