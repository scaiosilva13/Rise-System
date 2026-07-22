package com.caio.risesystem.domain.biomechanics

import com.google.mediapipe.tasks.components.containers.NormalizedLandmark
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult

/**
 * Camada de integração final entre o MediaPipe e a Engine de Agachamentos.
 */
class MediaPipeSquatProcessor {

    private val squatPoseProcessor = SquatPoseProcessor()

    /**
     * Processa o resultado bruto do MediaPipe e retorna a análise consolidada.
     * @param result O resultado da detecção de poses do MediaPipe.
     * @return [SquatAnalysisResult] contendo o estado atual do exercício.
     */
    fun process(result: PoseLandmarkerResult): SquatAnalysisResult {
        return processLandmarks(result.landmarks())
    }

    /**
     * Processa as listas de landmarks extraídas do resultado.
     * Método interno para facilitar a testabilidade sem mocks complexos de classes do MediaPipe.
     */
    internal fun processLandmarks(allPoses: List<List<NormalizedLandmark>>): SquatAnalysisResult {
        // Se houver pelo menos uma pose detectada, pegamos a primeira
        val landmarksMap = if (allPoses.isNotEmpty() && allPoses[0].isNotEmpty()) {
            MediaPipeLandmarkMapper.map(allPoses[0])
        } else {
            emptyMap()
        }

        return squatPoseProcessor.process(landmarksMap)
    }

    /**
     * Reseta a sessão de exercício através do processador de pose.
     */
    fun reset() {
        squatPoseProcessor.reset()
    }
}
