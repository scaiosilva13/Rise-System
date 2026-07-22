package com.caio.risesystem.domain.adherence

import com.caio.risesystem.domain.model.DailyTask
import com.caio.risesystem.domain.model.DailyTaskStatus
import com.caio.risesystem.domain.model.DailyTaskType
import org.junit.Assert.assertEquals
import org.junit.Test

class DailyAdherenceCalculatorTest {

    private fun createTask(status: DailyTaskStatus) = DailyTask(
        id = 0,
        date = "2026-07-22",
        type = DailyTaskType.OTHER,
        title = "",
        summary = "",
        suggestedTime = "",
        status = status,
        sortOrder = 0
    )

    @Test
    fun `1 - calculate - empty list - returns 0 percent and dia nao cumprido`() {
        val result = DailyAdherenceCalculator.calculate(emptyList())
        assertEquals(0, result.percentage)
        assertEquals("Dia não cumprido", result.classification)
        assertEquals(0, result.totalCount)
    }

    @Test
    fun `2 - calculate - none completed - returns 0 percent`() {
        val tasks = List(5) { createTask(DailyTaskStatus.PENDING) }
        val result = DailyAdherenceCalculator.calculate(tasks)
        assertEquals(0, result.percentage)
        assertEquals(0, result.completedCount)
    }

    @Test
    fun `3 - calculate - part completed - returns correct percentage`() {
        val tasks = listOf(
            createTask(DailyTaskStatus.COMPLETED),
            createTask(DailyTaskStatus.PENDING),
            createTask(DailyTaskStatus.PENDING),
            createTask(DailyTaskStatus.PENDING)
        )
        val result = DailyAdherenceCalculator.calculate(tasks)
        assertEquals(25, result.percentage)
    }

    @Test
    fun `4 - calculate - exactly 70 percent - returns dia parcial`() {
        val tasks = List(7) { createTask(DailyTaskStatus.COMPLETED) } + 
                    List(3) { createTask(DailyTaskStatus.PENDING) }
        val result = DailyAdherenceCalculator.calculate(tasks)
        assertEquals(70, result.percentage)
        assertEquals("Dia parcial", result.classification)
    }

    @Test
    fun `5 - calculate - exactly 90 percent - returns dia cumprido`() {
        val tasks = List(9) { createTask(DailyTaskStatus.COMPLETED) } + 
                    List(1) { createTask(DailyTaskStatus.PENDING) }
        val result = DailyAdherenceCalculator.calculate(tasks)
        assertEquals(90, result.percentage)
        assertEquals("Dia cumprido", result.classification)
    }

    @Test
    fun `6 - calculate - exactly 100 percent - returns dia cumprido`() {
        val tasks = List(5) { createTask(DailyTaskStatus.COMPLETED) }
        val result = DailyAdherenceCalculator.calculate(tasks)
        assertEquals(100, result.percentage)
        assertEquals("Dia cumprido", result.classification)
    }

    @Test
    fun `7 - calculate - NOT_COMPLETED status - counts in total but not as completed`() {
        val tasks = listOf(
            createTask(DailyTaskStatus.COMPLETED),
            createTask(DailyTaskStatus.NOT_COMPLETED),
            createTask(DailyTaskStatus.PENDING)
        )
        val result = DailyAdherenceCalculator.calculate(tasks)
        // 1 of 3 = 33%
        assertEquals(33, result.percentage)
        assertEquals(1, result.completedCount)
        assertEquals(3, result.totalCount)
    }

    @Test
    fun `8 - calculate - below 70 percent - classification dia nao cumprido`() {
        val tasks = List(6) { createTask(DailyTaskStatus.COMPLETED) } + 
                    List(4) { createTask(DailyTaskStatus.PENDING) }
        val result = DailyAdherenceCalculator.calculate(tasks)
        assertEquals(60, result.percentage)
        assertEquals("Dia não cumprido", result.classification)
    }

    @Test
    fun `9 - calculate - 70 to 89 percent - classification dia parcial`() {
        val tasks = List(8) { createTask(DailyTaskStatus.COMPLETED) } + 
                    List(2) { createTask(DailyTaskStatus.PENDING) }
        val result = DailyAdherenceCalculator.calculate(tasks)
        assertEquals(80, result.percentage)
        assertEquals("Dia parcial", result.classification)
    }

    @Test
    fun `10 - calculate - 90 to 100 percent - classification dia cumprido`() {
        val tasks = List(19) { createTask(DailyTaskStatus.COMPLETED) } + 
                    List(1) { createTask(DailyTaskStatus.PENDING) }
        val result = DailyAdherenceCalculator.calculate(tasks)
        // 19/20 = 95%
        assertEquals(95, result.percentage)
        assertEquals("Dia cumprido", result.classification)
    }
}
