package com.caio.risesystem.ui.validation.pose

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark

@Composable
fun PoseOverlay(
    landmarks: List<NormalizedLandmark>,
    modifier: Modifier = Modifier,
    mirrorHorizontally: Boolean = true
) {
    Canvas(modifier = modifier) {
        if (landmarks.isEmpty()) {
            return@Canvas
        }

        fun landmarkOffset(index: Int): Offset? {
            val landmark = landmarks.getOrNull(index)
                ?: return null

            val normalizedX =
                if (mirrorHorizontally) {
                    1f - landmark.x()
                } else {
                    landmark.x()
                }

            return Offset(
                x = normalizedX * size.width,
                y = landmark.y() * size.height
            )
        }

        POSE_CONNECTIONS.forEach { connection ->
            val start = landmarkOffset(connection.first)
            val end = landmarkOffset(connection.second)

            if (start != null && end != null) {
                drawLine(
                    color = Color(0xFF2AF5F5),
                    start = start,
                    end = end,
                    strokeWidth = 6f,
                    cap = StrokeCap.Round
                )
            }
        }

        landmarks.indices.forEach { index ->
            val point = landmarkOffset(index)

            if (point != null) {
                drawCircle(
                    color = Color(0xFF2AF5F5),
                    radius = 8f,
                    center = point
                )

                drawCircle(
                    color = Color.White,
                    radius = 3f,
                    center = point
                )
            }
        }
    }
}

private val POSE_CONNECTIONS = listOf(
    // Tronco
    11 to 12,
    11 to 23,
    12 to 24,
    23 to 24,

    // Braço esquerdo
    11 to 13,
    13 to 15,
    15 to 17,
    15 to 19,
    15 to 21,

    // Braço direito
    12 to 14,
    14 to 16,
    16 to 18,
    16 to 20,
    16 to 22,

    // Perna esquerda
    23 to 25,
    25 to 27,
    27 to 29,
    29 to 31,
    27 to 31,

    // Perna direita
    24 to 26,
    26 to 28,
    28 to 30,
    30 to 32,
    28 to 32,

    // Cabeça
    0 to 1,
    1 to 2,
    2 to 3,
    3 to 7,
    0 to 4,
    4 to 5,
    5 to 6,
    6 to 8,
    9 to 10
)

