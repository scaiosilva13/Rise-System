package com.caio.risesystem.domain.biomechanics

import com.caio.risesystem.domain.biomechanics.AngleCalculator.Point
import org.junit.Assert.assertEquals
import org.junit.Test

class AngleCalculatorTest {

    private val delta = 0.1f

    @Test
    fun `angle of 180 degrees - colinear points`() {
        val a = Point(0f, 0f)
        val b = Point(1f, 0f)
        val c = Point(2f, 0f)
        // BA = (-1, 0), BC = (1, 0) -> Angle 180
        assertEquals(180f, AngleCalculator.calculateAngle(a, b, c), delta)
    }

    @Test
    fun `angle of 90 degrees - right angle`() {
        val a = Point(0f, 1f)
        val b = Point(0f, 0f)
        val c = Point(1f, 0f)
        assertEquals(90f, AngleCalculator.calculateAngle(a, b, c), delta)
    }

    @Test
    fun `angle of 45 degrees`() {
        val a = Point(1f, 1f)
        val b = Point(0f, 0f)
        val c = Point(1f, 0f)
        assertEquals(45f, AngleCalculator.calculateAngle(a, b, c), delta)
    }

    @Test
    fun `angle of 0 degrees - overlapping lines`() {
        val a = Point(2f, 0f)
        val b = Point(0f, 0f)
        val c = Point(1f, 0f)
        // BA = (2, 0), BC = (1, 0) -> Angle 0
        assertEquals(0f, AngleCalculator.calculateAngle(a, b, c), delta)
    }

    @Test
    fun `three identical points`() {
        val a = Point(1f, 1f)
        assertEquals(0f, AngleCalculator.calculateAngle(a, a, a), delta)
    }

    @Test
    fun `two identical points - vertex and one end`() {
        val a = Point(1f, 1f)
        val b = Point(1f, 1f)
        val c = Point(2f, 2f)
        assertEquals(0f, AngleCalculator.calculateAngle(a, b, c), delta)
    }

    @Test
    fun `negative coordinates`() {
        val a = Point(-1f, 0f)
        val b = Point(0f, 0f)
        val c = Point(0f, -1f)
        assertEquals(90f, AngleCalculator.calculateAngle(a, b, c), delta)
    }

    @Test
    fun `decimal coordinates`() {
        val a = Point(0.5f, 0.5f)
        val b = Point(0f, 0f)
        val c = Point(0.5f, 0f)
        assertEquals(45f, AngleCalculator.calculateAngle(a, b, c), delta)
    }

    @Test
    fun `angle of 30 degrees`() {
        // Usando trigonometria: se C=(1, 0), B=(0,0), A=(sqrt(3)/2, 0.5)
        val a = Point(0.866025f, 0.5f)
        val b = Point(0f, 0f)
        val c = Point(1f, 0f)
        assertEquals(30f, AngleCalculator.calculateAngle(a, b, c), delta)
    }
}
