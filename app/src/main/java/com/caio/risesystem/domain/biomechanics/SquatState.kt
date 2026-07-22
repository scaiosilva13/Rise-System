package com.caio.risesystem.domain.biomechanics

/**
 * Representa as fases do movimento de agachamento.
 */
enum class SquatState {
    /** Estado inicial ou dados insuficientes para análise. */
    UNKNOWN,
    /** Usuário em pé, com as pernas estendidas. */
    STANDING,
    /** Fase de descida (flexão dos joelhos). */
    DESCENDING,
    /** Ponto mais baixo do agachamento. */
    BOTTOM,
    /** Fase de subida (extensão dos joelhos). */
    ASCENDING
}
