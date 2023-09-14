package com.github.bjornvester.codenarcprinter

import org.gradle.api.logging.Logging
import org.gradle.api.provider.MapProperty
import org.gradle.api.services.BuildService
import org.gradle.api.services.BuildServiceParameters
import org.gradle.tooling.events.FinishEvent
import org.gradle.tooling.events.OperationCompletionListener
import org.gradle.tooling.events.task.TaskFailureResult
import org.gradle.tooling.events.task.TaskFinishEvent
import org.gradle.tooling.events.task.TaskOperationDescriptor
import java.io.File
import java.nio.charset.StandardCharsets

abstract class TaskEventsService : BuildService<TaskEventsService.Params?>, OperationCompletionListener {
    internal interface Params : BuildServiceParameters {
        val taskPathToReportFilePath: MapProperty<String, File>
    }

    private val logger = Logging.getLogger(this::class.java)

    override fun onFinish(finishEvent: FinishEvent) {
        if (finishEvent is TaskFinishEvent && finishEvent.result is TaskFailureResult && finishEvent.descriptor is TaskOperationDescriptor) {
            // This is an event notifying us of a task failure
            val reportFilePath = parameters.taskPathToReportFilePath.get()[finishEvent.descriptor.taskPath]

            if (reportFilePath != null && reportFilePath.exists()) {
                // This has been configured as a CodeNarc task that we should react to
                logger.quiet("> CodeNarc report for ${finishEvent.descriptor.taskPath} follows:\n" + reportFilePath.readText(StandardCharsets.UTF_8).trim())
            }
        }
    }
}
