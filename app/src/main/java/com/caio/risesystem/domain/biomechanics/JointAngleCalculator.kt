package com.caio.risesystem.domain.biomechanics

/**
 * Calculador de ângulos articulares específicos baseados no modelo de domínio.
 */
object JointAngleCalculator {

    /**
     * Calcula o ângulo do cotovelo esquerdo.
     * Articulação: Ombro -> Cotovelo -> Pulso
     */
    fun calculateLeftElbowAngle(landmarks: Map<PoseLandmark, AngleCalculator.Point>): Float? {
        return calculateJointAngle(
            landmarks,
            PoseLandmark.LEFT_SHOULDER,
            PoseLandmark.LEFT_ELBOW,
            PoseLandmark.LEFT_WRIST
        )
    }

    /**
     * Calcula o ângulo do cotovelo direito.
     * Articulação: Ombro -> Cotovelo -> Pulso
     */
    fun calculateRightElbowAngle(landmarks: Map<PoseLandmark, AngleCalculator.Point>): Float? {
        return calculateJointAngle(
            landmarks,
            PoseLandmark.RIGHT_SHOULDER,
            PoseLandmark.RIGHT_ELBOW,
            PoseLandmark.RIGHT_WRIST
        )
    }

    /**
     * Calcula o ângulo do ombro esquerdo.
     * Articulação: Quadril -> Ombro -> Cotovelo
     */
    fun calculateLeftShoulderAngle(landmarks: Map<PoseLandmark, AngleCalculator.Point>): Float? {
        return calculateJointAngle(
            landmarks,
            PoseLandmark.LEFT_HIP,
            PoseLandmark.LEFT_SHOULDER,
            PoseLandmark.LEFT_ELBOW
        )
    }

    /**
     * Calcula o ângulo do ombro direito.
     * Articulação: Quadril -> Ombro -> Cotovelo
     */
    fun calculateRightShoulderAngle(landmarks: Map<PoseLandmark, AngleCalculator.Point>): Float? {
        return calculateJointAngle(
            landmarks,
            PoseLandmark.RIGHT_HIP,
            PoseLandmark.RIGHT_SHOULDER,
            PoseLandmark.RIGHT_ELBOW
        )
    }

    /**
     * Calcula o ângulo do quadril esquerdo.
     * Articulação: Ombro -> Quadril -> Joelho
     */
    fun calculateLeftHipAngle(landmarks: Map<PoseLandmark, AngleCalculator.Point>): Float? {
        return calculateJointAngle(
            landmarks,
            PoseLandmark.LEFT_SHOULDER,
            PoseLandmark.LEFT_HIP,
            PoseLandmark.LEFT_KNEE
        )
    }

    /**
     * Calcula o ângulo do quadril direito.
     * Articulação: Ombro -> Quadril -> Joelho
     */
    fun calculateRightHipAngle(landmarks: Map<PoseLandmark, AngleCalculator.Point>): Float? {
        return calculateJointAngle(
            landmarks,
            PoseLandmark.RIGHT_SHOULDER,
            PoseLandmark.RIGHT_HIP,
            PoseLandmark.RIGHT_KNEE
        )
    }

    /**
     * Calcula o ângulo do joelho esquerdo.
     * Articulação: Quadril -> Joelho -> Tornozelo
     */
    fun calculateLeftKneeAngle(landmarks: Map<PoseLandmark, AngleCalculator.Point>): Float? {
        return calculateJointAngle(
            landmarks,
            PoseLandmark.LEFT_HIP,
            PoseLandmark.LEFT_KNEE,
            PoseLandmark.LEFT_ANKLE
        )
    }

    /**
     * Calcula o ângulo do joelho direito.
     * Articulação: Quadril -> Joelho -> Tornozelo
     */
    fun calculateRightKneeAngle(landmarks: Map<PoseLandmark, AngleCalculator.Point>): Float? {
        return calculateJointAngle(
            landmarks,
            PoseLandmark.RIGHT_HIP,
            PoseLandmark.RIGHT_KNEE,
            PoseLandmark.RIGHT_ANKLE
        )
    }

    private fun calculateJointAngle(
        landmarks: Map<PoseLandmark, AngleCalculator.Point>,
        first: PoseLandmark,
        mid: PoseLandmark,
        last: PoseLandmark
    ): Float? {
        val p1 = landmarks[first] ?: return null
        val p2 = landmarks[mid] ?: return null
        val p3 = landmarks[last] ?: return null
        
        return AngleCalculator.calculateAngle(p1, p2, p3)
    }
}
