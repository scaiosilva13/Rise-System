package com.caio.risesystem.domain.biomechanics

import org.junit.Assert.assertEquals
import org.junit.Test

class SquatRepCounterTest {

    private val counter = SquatRepCounter()

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
    fun `should count one complete rep with full sequence`() {
        // STANDING
        counter.update(createSnapshot(175f))
        
        // DESCENDING
        counter.update(createSnapshot(140f))
        counter.update(createSnapshot(120f))
        
        // BOTTOM
        counter.update(createSnapshot(85f))
        
        // ASCENDING
        counter.update(createSnapshot(110f))
        counter.update(createSnapshot(140f))
        
        // STANDING -> Increment
        counter.update(createSnapshot(170f))
        assertEquals(1, counter.repCount)
    }

    @Test
    fun `should not count if descending phase is missing`() {
        // Pular diretamente de STANDING para BOTTOM (simulado forçando os ângulos)
        counter.update(createSnapshot(175f)) // STANDING
        counter.update(createSnapshot(80f))  // BOTTOM (sem passar por DESCENDING)
        counter.update(createSnapshot(110f)) // ASCENDING
        counter.update(createSnapshot(170f)) // STANDING
        
        // A máquina de estados SquatStateMachine pode detectar transições rápidas, 
        // mas aqui testamos se o RepCounter exige a flag hasDescended.
        assertEquals(0, counter.repCount)
    }

    @Test
    fun `should not count if ascending phase is missing`() {
        counter.update(createSnapshot(175f)) // STANDING
        counter.update(createSnapshot(140f)) // DESCENDING
        counter.update(createSnapshot(80f))  // BOTTOM
        counter.update(createSnapshot(170f)) // STANDING (sem passar por ASCENDING)
        
        assertEquals(0, counter.repCount)
    }

    @Test
    fun `should not count if bottom was not reached`() {
        counter.update(createSnapshot(175f)) // STANDING
        counter.update(createSnapshot(140f)) // DESCENDING
        counter.update(createSnapshot(100f)) // Almost bottom but not quite
        counter.update(createSnapshot(120f)) // ASCENDING
        counter.update(createSnapshot(170f)) // STANDING
        
        assertEquals(0, counter.repCount)
    }

    @Test
    fun `should reset progress if UNKNOWN state occurs`() {
        counter.update(createSnapshot(175f)) // STANDING
        counter.update(createSnapshot(140f)) // DESCENDING
        counter.update(createSnapshot(80f))  // BOTTOM
        counter.update(createSnapshot(null)) // UNKNOWN (perda de sinal)
        counter.update(createSnapshot(140f)) // ASCENDING (tentativa de recuperar)
        counter.update(createSnapshot(170f)) // STANDING
        
        assertEquals(0, counter.repCount)
    }

    @Test
    fun `should count multiple complete reps correctly`() {
        repeat(2) {
            counter.update(createSnapshot(175f)) // STANDING
            counter.update(createSnapshot(140f)) // DESCENDING
            counter.update(createSnapshot(80f))  // BOTTOM
            counter.update(createSnapshot(140f)) // ASCENDING
            counter.update(createSnapshot(175f)) // STANDING
        }
        assertEquals(2, counter.repCount)
    }

    @Test
    fun `should not double count standing state`() {
        counter.update(createSnapshot(175f)) // STANDING
        counter.update(createSnapshot(140f)) // DESCENDING
        counter.update(createSnapshot(80f))  // BOTTOM
        counter.update(createSnapshot(140f)) // ASCENDING
        counter.update(createSnapshot(175f)) // STANDING -> Increment to 1
        
        counter.update(createSnapshot(176f)) // Still STANDING
        counter.update(createSnapshot(174f)) // Still STANDING
        
        assertEquals(1, counter.repCount)
    }

    @Test
    fun `should reset entire counter and progress`() {
        counter.update(createSnapshot(175f))
        counter.update(createSnapshot(140f))
        counter.update(createSnapshot(80f))
        assertEquals(0, counter.repCount)
        
        counter.reset()
        
        // Tenta completar a subida do agachamento resetado
        counter.update(createSnapshot(140f)) // ASCENDING (mas progress foi resetado)
        counter.update(createSnapshot(175f)) // STANDING
        
        assertEquals(0, counter.repCount)
    }
}
