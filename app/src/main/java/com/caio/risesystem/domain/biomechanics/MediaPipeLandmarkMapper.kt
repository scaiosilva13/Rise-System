package com.caio.risesystem.domain.biomechanics

import com.google.mediapipe.tasks.components.containers.NormalizedLandmark

/**
 * Mapper responsável por converter os dados brutos do MediaPipe para o modelo de domínio.
 * Isola a dependência do MediaPipe da Engine de Biomecânica.
 */
object MediaPipeLandmarkMapper {

    /**
     * Converte uma lista de [NormalizedLandmark] para um mapa tipado por [PoseLandmark].
     * @param landmarks Lista de pontos normalizados vinda do MediaPipe.
     * @return Mapa de pontos internos da Engine.
     */
    fun map(landmarks: List<NormalizedLandmark>): Map<PoseLandmark, AngleCalculator.Point> {
        if (landmarks.isEmpty()) return emptyMap()

        val mappedLandmarks = mutableMapOf<PoseLandmark, AngleCalculator.Point>()

        landmarks.forEachIndexed { index, normalizedLandmark ->
            val poseLandmark = PoseLandmark.fromIndex(index)
            if (poseLandmark != null) {
                mappedLandmarks[poseLandmark] = AngleCalculator.Point(
                    x = normalizedLandmark.x(),
                    y = normalizedLandmark.y()
                )
            }
        }

        return mappedLandmarks
    }
}
