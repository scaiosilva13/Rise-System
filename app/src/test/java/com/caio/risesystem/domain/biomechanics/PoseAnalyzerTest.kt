package com.caio.risesystem.domain.biomechanics

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PoseAnalyzerTest {

    private val analyzer = PoseAnalyzer()

    @Test
    fun `analyze should return invalid for empty map`() {
        val result = analyzer.analyze(emptyMap())
        assertFalse(result.isValid)
        assertEquals("Nenhum ponto detectado.", result.message)
        assertEquals(0, result.pointCount)
    }

    @Test
    fun `analyze should return invalid when mandatory landmarks are missing`() {
        // Mapa contendo apenas o nariz (não é obrigatório para biomecânica básica)
        val incompleteMap = mapOf(
            PoseLandmark.NOSE to AngleCalculator.Point(0.5f, 0.5f)
        )
        val result = analyzer.analyze(incompleteMap)
        
        assertFalse(result.isValid)
        assertEquals("Pontos corporais obrigatórios ausentes.", result.message)
        assertTrue(result.missingMandatoryLandmarks.contains(PoseLandmark.LEFT_SHOULDER))
        assertTrue(result.missingMandatoryLandmarks.size > 0)
    }

    @Test
    fun `analyze should return valid when all mandatory landmarks are present`() {
        val validMap = PoseAnalyzer.MANDATORY_LANDMARKS.associateWith { 
            AngleCalculator.Point(0.5f, 0.5f) 
        }
        
        val result = analyzer.analyze(validMap)
        
        assertTrue(result.isValid)
        assertEquals("Pose válida para processamento.", result.message)
        assertEquals(PoseAnalyzer.MANDATORY_LANDMARKS.size, result.pointCount)
    }

    @Test
    fun `analyze should return valid for all 33 landmarks`() {
        val fullMap = PoseLandmark.values().associateWith { 
            AngleCalculator.Point(0.1f, 0.1f) 
        }
        
        val result = analyzer.analyze(fullMap)
        
        assertTrue(result.isValid)
        assertEquals(33, result.pointCount)
        assertTrue(result.missingMandatoryLandmarks.isEmpty())
    }

    @Test
    fun `analyze should fail if even one mandatory landmark is missing`() {
        val mapMissingOne = PoseAnalyzer.MANDATORY_LANDMARKS
            .filter { it != PoseLandmark.LEFT_KNEE }
            .associateWith { AngleCalculator.Point(0f, 0f) }
            
        val result = analyzer.analyze(mapMissingOne)
        
        assertFalse(result.isValid)
        assertTrue(result.missingMandatoryLandmarks.contains(PoseLandmark.LEFT_KNEE))
    }
}
