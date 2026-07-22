package com.caio.risesystem.domain.biomechanics

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class BodyPoseSnapshotTest {

    private val delta = 0.1f

    @Test
    fun `fromLandmarks should create snapshot with all angles for complete map`() {
        // Criando um mapa "perfeito" onde todas as articulações estão a 90 ou 180 graus
        val landmarks = mapOf(
            // Membros esquerdos (90 graus)
            PoseLandmark.LEFT_SHOULDER to AngleCalculator.Point(0f, 1f),
            PoseLandmark.LEFT_ELBOW to AngleCalculator.Point(0f, 0f),
            PoseLandmark.LEFT_WRIST to AngleCalculator.Point(1f, 0f),
            PoseLandmark.LEFT_HIP to AngleCalculator.Point(0f, 2f),
            PoseLandmark.LEFT_KNEE to AngleCalculator.Point(1f, 2f),
            PoseLandmark.LEFT_ANKLE to AngleCalculator.Point(1f, 3f),
            
            // Membros direitos (180 graus)
            PoseLandmark.RIGHT_SHOULDER to AngleCalculator.Point(0f, -1f),
            PoseLandmark.RIGHT_ELBOW to AngleCalculator.Point(0f, -2f),
            PoseLandmark.RIGHT_WRIST to AngleCalculator.Point(0f, -3f),
            PoseLandmark.RIGHT_HIP to AngleCalculator.Point(1f, -1f),
            PoseLandmark.RIGHT_KNEE to AngleCalculator.Point(1f, -2f),
            PoseLandmark.RIGHT_ANKLE to AngleCalculator.Point(1f, -3f)
        )

        val snapshot = BodyPoseSnapshot.fromLandmarks(landmarks)

        assertEquals(90f, snapshot.leftElbowAngle!!, delta)
        assertEquals(180f, snapshot.rightElbowAngle!!, delta)
        // Note: leftShoulderAngle usa Left_Hip -> Left_Shoulder -> Left_Elbow
        // Aqui Left_Hip(0,2), Left_Shoulder(0,1), Left_Elbow(0,0) -> Colinear 180
        assertEquals(180f, snapshot.leftShoulderAngle!!, delta)
        assertEquals(180f, snapshot.rightKneeAngle!!, delta)
    }

    @Test
    fun `fromLandmarks should return null fields for empty map`() {
        val snapshot = BodyPoseSnapshot.fromLandmarks(emptyMap())
        
        assertNull(snapshot.leftElbowAngle)
        assertNull(snapshot.rightElbowAngle)
        assertNull(snapshot.leftShoulderAngle)
        assertNull(snapshot.rightShoulderAngle)
        assertNull(snapshot.leftHipAngle)
        assertNull(snapshot.rightHipAngle)
        assertNull(snapshot.leftKneeAngle)
        assertNull(snapshot.rightKneeAngle)
    }

    @Test
    fun `fromLandmarks should handle partial maps`() {
        val landmarks = mapOf(
            PoseLandmark.LEFT_SHOULDER to AngleCalculator.Point(0f, 1f),
            PoseLandmark.LEFT_ELBOW to AngleCalculator.Point(0f, 0f),
            PoseLandmark.LEFT_WRIST to AngleCalculator.Point(1f, 0f)
        )

        val snapshot = BodyPoseSnapshot.fromLandmarks(landmarks)

        assertEquals(90f, snapshot.leftElbowAngle!!, delta)
        assertNull(snapshot.rightElbowAngle)
        assertNull(snapshot.leftKneeAngle)
    }
}
