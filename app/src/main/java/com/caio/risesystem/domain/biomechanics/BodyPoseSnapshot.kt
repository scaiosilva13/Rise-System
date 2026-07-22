package com.caio.risesystem.domain.biomechanics

/**
 * Representa um snapshot biomecânico consolidado do corpo em um determinado instante.
 * Contém os ângulos calculados de todas as articulações principais monitoradas.
 */
data class BodyPoseSnapshot(
    val leftElbowAngle: Float?,
    val rightElbowAngle: Float?,
    val leftShoulderAngle: Float?,
    val rightShoulderAngle: Float?,
    val leftHipAngle: Float?,
    val rightHipAngle: Float?,
    val leftKneeAngle: Float?,
    val rightKneeAngle: Float?
) {
    companion object {
        /**
         * Cria um [BodyPoseSnapshot] a partir de um mapa de landmarks corporais.
         * Utiliza o [JointAngleCalculator] para realizar os cálculos necessários.
         * 
         * @param landmarks Mapa contendo os pontos corporais detectados.
         * @return Um snapshot consolidado dos ângulos articulares.
         */
        fun fromLandmarks(landmarks: Map<PoseLandmark, AngleCalculator.Point>): BodyPoseSnapshot {
            return BodyPoseSnapshot(
                leftElbowAngle = JointAngleCalculator.calculateLeftElbowAngle(landmarks),
                rightElbowAngle = JointAngleCalculator.calculateRightElbowAngle(landmarks),
                leftShoulderAngle = JointAngleCalculator.calculateLeftShoulderAngle(landmarks),
                rightShoulderAngle = JointAngleCalculator.calculateRightShoulderAngle(landmarks),
                leftHipAngle = JointAngleCalculator.calculateLeftHipAngle(landmarks),
                rightHipAngle = JointAngleCalculator.calculateRightHipAngle(landmarks),
                leftKneeAngle = JointAngleCalculator.calculateLeftKneeAngle(landmarks),
                rightKneeAngle = JointAngleCalculator.calculateRightKneeAngle(landmarks)
            )
        }
    }
}
