package com.caio.risesystem.domain.biomechanics

/**
 * Representa os 33 landmarks oficiais do MediaPipe Pose.
 * Referência: https://developers.google.com/mediapipe/solutions/vision/pose_landmarker
 */
enum class PoseLandmark(val index: Int, val readableName: String) {
    NOSE(0, "Nariz"),
    LEFT_EYE_INNER(1, "Olho Esquerdo (Interno)"),
    LEFT_EYE(2, "Olho Esquerdo"),
    LEFT_EYE_OUTER(3, "Olho Esquerdo (Externo)"),
    RIGHT_EYE_INNER(4, "Olho Direito (Interno)"),
    RIGHT_EYE(5, "Olho Direito"),
    RIGHT_EYE_OUTER(6, "Olho Direito (Externo)"),
    LEFT_EAR(7, "Orelha Esquerda"),
    RIGHT_EAR(8, "Orelha Direita"),
    MOUTH_LEFT(9, "Boca (Esquerda)"),
    MOUTH_RIGHT(10, "Boca (Direita)"),
    LEFT_SHOULDER(11, "Ombro Esquerdo"),
    RIGHT_SHOULDER(12, "Ombro Direito"),
    LEFT_ELBOW(13, "Cotovelo Esquerdo"),
    RIGHT_ELBOW(14, "Cotovelo Direito"),
    LEFT_WRIST(15, "Pulso Esquerdo"),
    RIGHT_WRIST(16, "Pulso Direito"),
    LEFT_PINKY(17, "Mínimo Esquerdo"),
    RIGHT_PINKY(18, "Mínimo Direito"),
    LEFT_INDEX(19, "Indicador Esquerdo"),
    RIGHT_INDEX(20, "Indicador Direito"),
    LEFT_THUMB(21, "Polegar Esquerdo"),
    RIGHT_THUMB(22, "Polegar Direito"),
    LEFT_HIP(23, "Quadril Esquerdo"),
    RIGHT_HIP(24, "Quadril Direito"),
    LEFT_KNEE(25, "Joelho Esquerdo"),
    RIGHT_KNEE(26, "Joelho Direito"),
    LEFT_ANKLE(27, "Tornozelo Esquerdo"),
    RIGHT_ANKLE(28, "Tornozelo Direito"),
    LEFT_HEEL(29, "Calcanhar Esquerdo"),
    RIGHT_HEEL(30, "Calcanhar Direito"),
    LEFT_FOOT_INDEX(31, "Pé Esquerdo (Indicador)"),
    RIGHT_FOOT_INDEX(32, "Pé Direito (Indicador)");

    companion object {
        /**
         * Retorna o enum correspondente ao índice oficial.
         * @param index Índice de 0 a 32.
         * @return [PoseLandmark] ou null se o índice for inválido.
         */
        fun fromIndex(index: Int): PoseLandmark? {
            return values().find { it.index == index }
        }

        /**
         * Verifica se um índice é válido dentro do modelo de 33 pontos.
         */
        fun isValidIndex(index: Int): Boolean {
            return index in 0..32
        }
    }
}
