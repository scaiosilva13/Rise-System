package com.caio.risesystem.domain.biomechanics

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class JointAngleCalculatorTest {

    private val delta = 0.1f

    @Test
    fun `should calculate left elbow angle correctly`() {
        val landmarks = mapOf(
            PoseLandmark.LEFT_SHOULDER to AngleCalculator.Point(0f, 1f),
            PoseLandmark.LEFT_ELBOW to AngleCalculator.Point(0f, 0f),
            PoseLandmark.LEFT_WRIST to AngleCalculator.Point(1f, 0f)
        )
        val angle = JointAngleCalculator.calculateLeftElbowAngle(landmarks)
        assertEquals(90f, angle!!, delta)
    }

    @Test
    fun `should calculate right elbow angle correctly`() {
        val landmarks = mapOf(
            PoseLandmark.RIGHT_SHOULDER to AngleCalculator.Point(0f, 1f),
            PoseLandmark.RIGHT_ELBOW to AngleCalculator.Point(0f, 0f),
            PoseLandmark.RIGHT_WRIST to AngleCalculator.Point(-1f, 0f)
        )
        val angle = JointAngleCalculator.calculateRightElbowAngle(landmarks)
        assertEquals(90f, angle!!, delta)
    }

    @Test
    fun `should calculate left knee angle correctly`() {
        val landmarks = mapOf(
            PoseLandmark.LEFT_HIP to AngleCalculator.Point(0f, 1f),
            PoseLandmark.LEFT_KNEE to AngleCalculator.Point(0f, 0f),
            PoseLandmark.LEFT_ANKLE to AngleCalculator.Point(0f, -1f)
        )
        val angle = JointAngleCalculator.calculateLeftKneeAngle(landmarks)
        assertEquals(180f, angle!!, delta)
    }

    @Test
    fun `should calculate right knee angle correctly`() {
        val landmarks = mapOf(
            PoseLandmark.RIGHT_HIP to AngleCalculator.Point(0f, 1f),
            PoseLandmark.RIGHT_KNEE to AngleCalculator.Point(0f, 0f),
            PoseLandmark.RIGHT_ANKLE to AngleCalculator.Point(1f, -1f) // 135 degrees
        )
        val angle = JointAngleCalculator.calculateRightKneeAngle(landmarks)
        assertEquals(135f, angle!!, delta)
    }

    @Test
    fun `should return null if any landmark is missing`() {
        val landmarks = mapOf(
            PoseLandmark.LEFT_SHOULDER to AngleCalculator.Point(0f, 1f),
            PoseLandmark.LEFT_ELBOW to AngleCalculator.Point(0f, 0f)
            // LEFT_WRIST missing
        )
        assertNull(JointAngleCalculator.calculateLeftElbowAngle(landmarks))
    }

    @Test
    fun `should return null for empty map`() {
        assertNull(JointAngleCalculator.calculateLeftHipAngle(emptyMap()))
    }

    @Test
    fun `should verify all joints presence`() {
        val landmarks = mapOf(
            PoseLandmark.LEFT_SHOULDER to AngleCalculator.Point(0f, 0f),
            PoseLandmark.RIGHT_SHOULDER to AngleCalculator.Point(0f, 0f),
            PoseLandmark.LEFT_ELBOW to AngleCalculator.Point(0f, 0f),
            PoseLandmark.RIGHT_ELBOW to AngleCalculator.Point(0f, 0f),
            PoseLandmark.LEFT_WRIST to AngleCalculator.Point(0f, 0f),
            PoseLandmark.RIGHT_WRIST to AngleCalculator.Point(0f, 0f),
            PoseLandmark.LEFT_HIP to AngleCalculator.Point(0f, 0f),
            PoseLandmark.RIGHT_HIP to AngleCalculator.Point(0f, 0f),
            PoseLandmark.LEFT_KNEE to AngleCalculator.Point(0f, 0f),
            PoseLandmark.RIGHT_KNEE to AngleCalculator.Point(0f, 0f),
            PoseLandmark.LEFT_ANKLE to AngleCalculator.Point(0f, 0f),
            PoseLandmark.RIGHT_ANKLE to AngleCalculator.Point(0f, 0f)
        )
        
        assertTrue(JointAngleCalculator.calculateLeftElbowAngle(landmarks) != null)
        assertTrue(JointAngleCalculator.calculateRightElbowAngle(landmarks) != null)
        assertTrue(JointAngleCalculator.calculateLeftShoulderAngle(landmarks) != null)
        assertTrue(JointAngleCalculator.calculateRightShoulderAngle(landmarks) != null)
        assertTrue(JointAngleCalculator.calculateLeftHipAngle(landmarks) != null)
        assertTrue(JointAngleCalculator.calculateRightHipAngle(landmarks) != null)
        assertTrue(JointAngleCalculator.calculateLeftKneeAngle(landmarks) != null)
        assertTrue(JointAngleCalculator.calculateRightKneeAngle(landmarks) != null)
    }
}
