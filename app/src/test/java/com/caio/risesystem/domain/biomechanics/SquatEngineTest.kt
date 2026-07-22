package com.caio.risesystem.domain.biomechanics

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SquatEngineTest {

    private val engine = SquatEngine()

    private fun createSnapshot(kneeAngle: Float?): BodyPoseSnapshot {
        return BodyPoseSnapshot(
            leftElbowAngle = null,
            rightElbowAngle = null,
            leftShoulderAngle = null,
            rightShoulderAngle = null,
            leftHipAngle = null,
            rightHipAngle = null,
            leftKneeAngle = kneeAngle,
            rightKneeAngle = kneeAngle
        )
    }

    @Test
    fun `should count one complete repetition through engine`() {
        engine.process(createSnapshot(175f)) // STANDING
        engine.process(createSnapshot(140f)) // DESCENDING
        engine.process(createSnapshot(80f))  // BOTTOM
        engine.process(createSnapshot(140f)) // ASCENDING
        val result = engine.process(createSnapshot(175f)) // STANDING (finalizada)
        
        assertEquals(1, result.repCount)
        assertEquals(SquatState.STANDING, result.state)
        assertTrue(result.hasValidData)
    }

    @Test
    fun `should count multiple complete repetitions through engine`() {
        repeat(3) { i ->
            engine.process(createSnapshot(175f)) // STANDING
            engine.process(createSnapshot(140f)) // DESCENDING
            engine.process(createSnapshot(80f))  // BOTTOM
            engine.process(createSnapshot(140f)) // ASCENDING
            val result = engine.process(createSnapshot(175f)) // STANDING (finalizada)
            
            assertEquals(i + 1, result.repCount)
        }
    }

    @Test
    fun `should reset session and state completely`() {
        // Realiza uma repetição completa
        engine.process(createSnapshot(175f))
        engine.process(createSnapshot(140f))
        engine.process(createSnapshot(80f))
        engine.process(createSnapshot(140f))
        engine.process(createSnapshot(175f))
        
        assertEquals(1, engine.process(createSnapshot(175f)).repCount)
        
        // Executa reset
        engine.reset()
        
        // Confirma que a próxima sessão começa zerada
        val resultAfterReset = engine.process(createSnapshot(null))
        assertEquals(0, resultAfterReset.repCount)
        assertEquals(SquatState.UNKNOWN, resultAfterReset.state)
        assertFalse(resultAfterReset.hasValidData)
    }

    @Test
    fun `should recover hasValidData after an unknown state`() {
        // Processa dados inválidos
        val invalidResult = engine.process(createSnapshot(null))
        assertFalse(invalidResult.hasValidData)
        assertEquals(SquatState.UNKNOWN, invalidResult.state)
        
        // Processa dados válidos
        val validResult = engine.process(createSnapshot(175f))
        assertTrue(validResult.hasValidData)
        assertEquals(SquatState.STANDING, validResult.state)
    }

    @Test
    fun `should keep state and repCount synchronized in the same result`() {
        engine.process(createSnapshot(175f)) // Standing
        engine.process(createSnapshot(140f)) // Descending
        engine.process(createSnapshot(80f))  // Bottom
        engine.process(createSnapshot(140f)) // Ascending
        
        // No momento que volta para Standing, o estado deve ser Standing E o repCount deve ser 1
        val result = engine.process(createSnapshot(175f))
        
        assertEquals(SquatState.STANDING, result.state)
        assertEquals(1, result.repCount)
    }

    @Test
    fun `should handle missing data mid-movement and reset progress`() {
        engine.process(createSnapshot(175f)) // Standing
        engine.process(createSnapshot(80f))  // Bottom
        engine.process(createSnapshot(null)) // Data lost -> UNKNOWN
        
        val result = engine.process(createSnapshot(175f)) // Back to Standing
        
        // Não deve contar rep pois o UNKNOWN resetou o progresso
        assertEquals(0, result.repCount)
        assertEquals(SquatState.STANDING, result.state)
    }
}
