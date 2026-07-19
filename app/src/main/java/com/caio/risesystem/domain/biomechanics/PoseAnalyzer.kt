package com.caio.risesystem.domain.biomechanics

/**
 * Engine base para análise biomecânica de poses.
 * Esta classe é responsável por validar e estruturar os dados de landmarks
 * antes do processamento específico de exercícios.
 */
class PoseAnalyzer {

    /**
     * Resultado da análise inicial da pose.
     */
    data class AnalysisResult(
        val isValid: Boolean,
        val message: String,
        val pointCount: Int,
        val missingMandatoryLandmarks: List<PoseLandmark> = emptyList()
    )

    /**
     * Analisa um mapa de pontos corporais (landmarks) identificados por [PoseLandmark].
     * @param landmarks Mapa relacionando o landmark ao seu ponto espacial.
     * @return [AnalysisResult] contendo o status da validação.
     */
    fun analyze(landmarks: Map<PoseLandmark, AngleCalculator.Point>): AnalysisResult {
        if (landmarks.isEmpty()) {
            return AnalysisResult(
                isValid = false,
                message = "Nenhum ponto detectado.",
                pointCount = 0
            )
        }

        val missingMandatory = MANDATORY_LANDMARKS.filter { !landmarks.containsKey(it) }

        if (missingMandatory.isNotEmpty()) {
            return AnalysisResult(
                isValid = false,
                message = "Pontos corporais obrigatórios ausentes.",
                pointCount = landmarks.size,
                missingMandatoryLandmarks = missingMandatory
            )
        }

        return AnalysisResult(
            isValid = true,
            message = "Pose válida para processamento.",
            pointCount = landmarks.size
        )
    }

    companion object {
        /**
         * Conjunto de landmarks considerados essenciais para qualquer análise biomecânica
         * básica de membros superiores e inferiores.
         */
        val MANDATORY_LANDMARKS = listOf(
            PoseLandmark.LEFT_SHOULDER,
            PoseLandmark.RIGHT_SHOULDER,
            PoseLandmark.LEFT_ELBOW,
            PoseLandmark.RIGHT_ELBOW,
            PoseLandmark.LEFT_WRIST,
            PoseLandmark.RIGHT_WRIST,
            PoseLandmark.LEFT_HIP,
            PoseLandmark.RIGHT_HIP,
            PoseLandmark.LEFT_KNEE,
            PoseLandmark.RIGHT_KNEE,
            PoseLandmark.LEFT_ANKLE,
            PoseLandmark.RIGHT_ANKLE
        )
    }
}
