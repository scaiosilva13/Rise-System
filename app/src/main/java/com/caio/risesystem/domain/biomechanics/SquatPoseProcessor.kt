package com.caio.risesystem.domain.biomechanics

/**
 * Coordenador de alto nível para o processamento de agachamentos.
 * Integra a validação de pose com a engine de exercício.
 */
class SquatPoseProcessor {

    private val poseAnalyzer = PoseAnalyzer()
    private val squatEngine = SquatEngine()

    /**
     * Processa um mapa de landmarks e retorna o resultado consolidado do agachamento.
     * @param landmarks Mapa de pontos corporais tipados.
     * @return [SquatAnalysisResult] contendo estado, contagem e validade dos dados.
     */
    fun process(
        landmarks: Map<PoseLandmark, AngleCalculator.Point>
    ): SquatAnalysisResult {
        val analysis = poseAnalyzer.analyze(landmarks)

        return if (analysis.isValid) {
            val snapshot = BodyPoseSnapshot.fromLandmarks(landmarks)
            squatEngine.process(snapshot)
        } else {
            // Se a pose for inválida, enviamos um snapshot vazio para a engine.
            // Isso permite que a engine lide com a perda de dados (resetando progresso interno).
            val emptySnapshot = BodyPoseSnapshot(
                leftElbowAngle = null,
                rightElbowAngle = null,
                leftShoulderAngle = null,
                rightShoulderAngle = null,
                leftHipAngle = null,
                rightHipAngle = null,
                leftKneeAngle = null,
                rightKneeAngle = null
            )
            squatEngine.process(emptySnapshot)
        }
    }

    /**
     * Reseta completamente a sessão de agachamento.
     */
    fun reset() {
        squatEngine.reset()
    }
}
