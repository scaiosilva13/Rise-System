package com.caio.risesystem.domain.biomechanics

import org.junit.Assert.assertEquals
import org.junit.Test

class SquatStateMachineTest {

    private val stateMachine = SquatStateMachine()

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
    fun `should detect standing state`() {
        val state = stateMachine.update(createSnapshot(170f))
        assertEquals(SquatState.STANDING, state)
    }

    @Test
    fun `should detect bottom state`() {
        val state = stateMachine.update(createSnapshot(80f))
        assertEquals(SquatState.BOTTOM, state)
    }

    @Test
    fun `should detect descending state`() {
        stateMachine.update(createSnapshot(170f)) // Standing
        val state = stateMachine.update(createSnapshot(140f)) // Moving down
        assertEquals(SquatState.DESCENDING, state)
    }

    @Test
    fun `should detect ascending state`() {
        stateMachine.update(createSnapshot(80f)) // Bottom
        val state = stateMachine.update(createSnapshot(100f)) // Moving up
        assertEquals(SquatState.ASCENDING, state)
    }

    @Test
    fun `should handle unknown state for null landmarks`() {
        val state = stateMachine.update(createSnapshot(null))
        assertEquals(SquatState.UNKNOWN, state)
    }

    @Test
    fun `should follow full squat sequence`() {
        // 1. Standing
        assertEquals(SquatState.STANDING, stateMachine.update(createSnapshot(175f)))
        
        // 2. Start descending
        assertEquals(SquatState.DESCENDING, stateMachine.update(createSnapshot(150f)))
        assertEquals(SquatState.DESCENDING, stateMachine.update(createSnapshot(120f)))
        
        // 3. Reach bottom
        assertEquals(SquatState.BOTTOM, stateMachine.update(createSnapshot(85f)))
        
        // 4. Start ascending
        assertEquals(SquatState.ASCENDING, stateMachine.update(createSnapshot(110f)))
        assertEquals(SquatState.ASCENDING, stateMachine.update(createSnapshot(140f)))
        
        // 5. Back to standing
        assertEquals(SquatState.STANDING, stateMachine.update(createSnapshot(165f)))
    }

    @Test
    fun `should maintain state when change is within noise threshold`() {
        stateMachine.update(createSnapshot(120f)) // Sets initial trend (descending based on logic)
        val state1 = stateMachine.update(createSnapshot(115f))
        assertEquals(SquatState.DESCENDING, state1)
        
        // Small change (0.5 degrees) should maintain state
        val state2 = stateMachine.update(createSnapshot(114.5f))
        assertEquals(SquatState.DESCENDING, state2)
    }
}
