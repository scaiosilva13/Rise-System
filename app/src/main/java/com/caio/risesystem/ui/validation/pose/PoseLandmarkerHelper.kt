package com.caio.risesystem.ui.validation.pose

import android.content.Context
import com.google.mediapipe.framework.image.MPImage
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult

class PoseLandmarkerHelper(
    context: Context,
    private val onResults:
        (PoseLandmarkerResult) -> Unit,
    private val onError:
        (String) -> Unit
) {
    private var poseLandmarker:
            PoseLandmarker? = null

    init {
        setupPoseLandmarker(
            context = context.applicationContext
        )
    }

    private fun setupPoseLandmarker(
        context: Context
    ) {
        try {
            val baseOptions =
                BaseOptions.builder()
                    .setModelAssetPath(
                        MODEL_FILE
                    )
                    .build()

            val options =
                PoseLandmarker
                    .PoseLandmarkerOptions
                    .builder()
                    .setBaseOptions(baseOptions)
                    .setRunningMode(
                        RunningMode.LIVE_STREAM
                    )
                    .setNumPoses(1)
                    .setMinPoseDetectionConfidence(
                        0.35f
                    )
                    .setMinPosePresenceConfidence(
                        0.35f
                    )
                    .setMinTrackingConfidence(
                        0.35f
                    )
                    .setResultListener {
                            result,
                            _ ->

                        onResults(result)
                    }
                    .setErrorListener {
                            exception ->

                        onError(
                            exception.message
                                ?: "Erro desconhecido no MediaPipe."
                        )
                    }
                    .build()

            poseLandmarker =
                PoseLandmarker.createFromOptions(
                    context,
                    options
                )
        } catch (exception: Exception) {
            poseLandmarker = null

            onError(
                exception.message
                    ?: "Não foi possível iniciar o detector corporal."
            )
        }
    }

    fun detectAsync(
        image: MPImage,
        timestampMilliseconds: Long
    ) {
        try {
            poseLandmarker?.detectAsync(
                image,
                timestampMilliseconds
            )
        } catch (exception: Exception) {
            onError(
                exception.message
                    ?: "Erro ao analisar o quadro da câmera."
            )
        }
    }

    fun isReady(): Boolean {
        return poseLandmarker != null
    }

    fun close() {
        poseLandmarker?.close()
        poseLandmarker = null
    }

    companion object {
        private const val MODEL_FILE =
            "pose_landmarker_lite.task"
    }
}
