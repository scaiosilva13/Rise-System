package com.caio.risesystem.domain.adherence

import com.caio.risesystem.domain.model.DailyTask
import com.caio.risesystem.domain.model.DailyTaskStatus

object DailyAdherenceCalculator {

    fun calculate(tasks: List<DailyTask>): DailyAdherenceResult {
        if (tasks.isEmpty()) {
            return DailyAdherenceResult(
                completedCount = 0,
                totalCount = 0,
                percentage = 0,
                classification = "Dia não cumprido"
            )
        }

        val total = tasks.size
        val completed = tasks.count { it.status == DailyTaskStatus.COMPLETED }
        val percentage = (completed * 100) / total

        val classification = when {
            percentage >= 90 -> "Dia cumprido"
            percentage >= 70 -> "Dia parcial"
            else -> "Dia não cumprido"
        }

        return DailyAdherenceResult(
            completedCount = completed,
            totalCount = total,
            percentage = percentage,
            classification = classification
        )
    }
}
