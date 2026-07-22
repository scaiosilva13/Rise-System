package com.caio.risesystem.domain.biomechanics

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SquatPoseProcessorTest {

    private val processor = SquatPoseProcessor()

    private fun createBaseMap(): MutableMap<PoseLandmark, AngleCalculator.Point> {
        return PoseAnalyzer.MANDATORY_LANDMARKS.associateWith {
            AngleCalculator.Point(0.5f, 0.5f)
        }.toMutableMap()
    }

    private fun createStandingMap(): Map<PoseLandmark, AngleCalculator.Point> {
        return createBaseMap().apply {
            // Ângulo de 180 graus (Em pé)
            this[PoseLandmark.LEFT_HIP] = AngleCalculator.Point(0f, 1f)
            this[PoseLandmark.LEFT_KNEE] = AngleCalculator.Point(0f, 0f)
            this[PoseLandmark.LEFT_ANKLE] = AngleCalculator.Point(0f, -1f)
            this[PoseLandmark.RIGHT_HIP] = AngleCalculator.Point(0f, 1f)
            this[PoseLandmark.RIGHT_KNEE] = AngleCalculator.Point(0f, 0f)
            this[PoseLandmark.RIGHT_ANKLE] = AngleCalculator.Point(0f, -1f)
        }
    }

    private fun createIntermediateMap(): Map<PoseLandmark, AngleCalculator.Point> {
        return createBaseMap().apply {
            // Ângulo de 135 graus (Transição)
            this[PoseLandmark.LEFT_HIP] = AngleCalculator.Point(1f, 1f)
            this[PoseLandmark.LEFT_KNEE] = AngleCalculator.Point(0f, 0f)
            this[PoseLandmark.LEFT_ANKLE] = AngleCalculator.Point(0f, -1f)
            this[PoseLandmark.RIGHT_HIP] = AngleCalculator.Point(1f, 1f)
            this[PoseLandmark.RIGHT_KNEE] = AngleCalculator.Point(0f, 0f)
            this[PoseLandmark.RIGHT_ANKLE] = AngleCalculator.Point(0f, -1f)
        }
    }

    private fun createBottomMap(): Map<PoseLandmark, AngleCalculator.Point> {
        return createBaseMap().apply {
            // Ângulo de ~63 graus (Fundo)
            this[PoseLandmark.LEFT_HIP] = AngleCalculator.Point(1f, -0.5f)
            this[PoseLandmark.LEFT_KNEE] = AngleCalculator.Point(0f, 0f)
            this[PoseLandmark.LEFT_ANKLE] = AngleCalculator.Point(0f, -1f)
            this[PoseLandmark.RIGHT_HIP] = AngleCalculator.Point(1f, -0.5f)
            this[PoseLandmark.RIGHT_KNEE] = AngleCalculator.Point(0f, 0f)
            this[PoseLandmark.RIGHT_ANKLE] = AngleCalculator.Point(0f, -1f)
        }
    }

    @Test
    fun `should return invalid result for empty map`() {
        val result = processor.process(emptyMap())
        assertFalse(result.hasValidData)
        assertEquals(SquatState.UNKNOWN, result.state)
    }

    @Test
    fun `should return valid result for standing pose`() {
        val result = processor.process(createStandingMap())
        assertTrue(result.hasValidData)
        assertEquals(SquatState.STANDING, result.state)
    }

    @Test
    fun `should count one rep through complete cycle`() {
        processor.process(createStandingMap())     // STANDING
        processor.process(createIntermediateMap()) // DESCENDING
        processor.process(createBottomMap())       // BOTTOM
        processor.process(createIntermediateMap()) // ASCENDING
        val result = processor.process(createStandingMap()) // STANDING

        assertEquals(1, result.repCount)
        assertTrue(result.hasValidData)
    }

    @Test
    fun `should maintain rep count after invalid frames`() {
        // Realiza uma repetição completa
        processor.process(createStandingMap())
        processor.process(createIntermediateMap())
        processor.process(createBottomMap())
        processor.process(createIntermediateMap())
        processor.process(createStandingMap())
        
        assertEquals(1, processor.process(createStandingMap()).repCount)

        // Frame inválido (perda de detecção)
        val invalidResult = processor.process(emptyMap())
        assertFalse(invalidResult.hasValidData)
        assertEquals(SquatState.UNKNOWN, invalidResult.state)
        assertEquals(1, invalidResult.repCount)

        // Recuperação com pose válida
        val validResult = processor.process(createStandingMap())
        assertTrue(validResult.hasValidData)
        assertEquals(1, validResult.repCount)
    }

    @Test
    fun `should not count rep if data is lost mid-movement`() {
        processor.process(createStandingMap())     // STANDING
        processor.process(createIntermediateMap()) // DESCENDING
        processor.process(createBottomMap())       // BOTTOM
        
        // Perda de detecção no meio do movimento
        processor.process(emptyMap())
        
        // Tenta completar a subida
        processor.process(createIntermediateMap()) // ASCENDING (mas progress resetado)
        val result = processor.process(createStandingMap()) // STANDING
        
        assertEquals(0, result.repCount)
    }

    @Test
    fun `should reset completely`() {
        // Conclui uma repetição válida
        processor.process(createStandingMap())
        processor.process(createIntermediateMap())
        processor.process(createBottomMap())
        processor.process(createIntermediateMap())
        processor.process(createStandingMap())
        assertEquals(1, processor.process(createStandingMap()).repCount)
        
        processor.reset()
        
        // Processa mapa vazio e confirma reset total
        val result = processor.process(emptyMap())
        assertEquals(0, result.repCount)
        assertEquals(SquatState.UNKNOWN, result.state)
    }
}
