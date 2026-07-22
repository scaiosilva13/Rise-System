package com.caio.risesystem.domain.biomechanics

/**
 * Máquina de estados para detectar as fases do exercício de agachamento (Squat).
 */
class SquatStateMachine {

    var currentState: SquatState = SquatState.UNKNOWN
        private set

    private var lastKneeAngle: Float? = null

    /**
     * Atualiza o estado do agachamento com base no novo snapshot biomecânico.
     * @param snapshot O estado atual do corpo.
     * @return O novo [SquatState] detectado.
     */
    fun update(snapshot: BodyPoseSnapshot): SquatState {
        val leftKnee = snapshot.leftKneeAngle
        val rightKnee = snapshot.rightKneeAngle

        if (leftKnee == null || rightKnee == null) {
            currentState = SquatState.UNKNOWN
            return currentState
        }

        val currentKneeAngle = (leftKnee + rightKnee) / 2f

        val newState = when {
            // Regra Standing: Joelhos estendidos (> 160 graus)
            currentKneeAngle > STANDING_THRESHOLD -> SquatState.STANDING

            // Regra Bottom: Joelhos flexionados (< 90 graus)
            currentKneeAngle < BOTTOM_THRESHOLD -> SquatState.BOTTOM

            // Regras de Transição (Descending/Ascending) baseadas na tendência
            lastKneeAngle != null -> {
                if (currentKneeAngle < lastKneeAngle!! - NOISE_THRESHOLD) {
                    SquatState.DESCENDING
                } else if (currentKneeAngle > lastKneeAngle!! + NOISE_THRESHOLD) {
                    SquatState.ASCENDING
                } else {
                    currentState // Mantém o estado se a mudança for desprezível
                }
            }

            else -> SquatState.UNKNOWN
        }

        // Se o estado era UNKNOWN e entramos na zona intermediária sem tendência,
        // tentamos uma classificação inicial baseada na posição absoluta.
        if (newState == SquatState.UNKNOWN) {
             currentState = if (currentKneeAngle > 100f) SquatState.STANDING else SquatState.DESCENDING
        } else {
            currentState = newState
        }

        lastKneeAngle = currentKneeAngle
        return currentState
    }

    /**
     * Reseta a máquina de estados.
     */
    fun reset() {
        currentState = SquatState.UNKNOWN
        lastKneeAngle = null
    }

    companion object {
        private const val STANDING_THRESHOLD = 160f
        private const val BOTTOM_THRESHOLD = 90f
        private const val NOISE_THRESHOLD = 1.0f // Margem para ignorar ruído de jitter
    }
}
