package com.caio.risesystem.ui.validation.pose

import com.caio.risesystem.domain.biomechanics.SquatAnalysisResult
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark

data class PoseDetectionState(
    val detectorReady: Boolean = false,
    val personDetected: Boolean = false,
    val detectedLandmarks: Int = 0,
    val landmarks: List<NormalizedLandmark> = emptyList(),
    val errorMessage: String? = null,
    val squatAnalysisResult: SquatAnalysisResult? = null
)
