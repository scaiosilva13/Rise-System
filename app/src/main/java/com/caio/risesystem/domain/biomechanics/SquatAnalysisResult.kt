package com.caio.risesystem.domain.biomechanics

/**
 * Encapsula o resultado consolidado da análise de um agachamento em um determinado instante.
 */
data class SquatAnalysisResult(
    /** Estado atual do movimento (Standing, Descending, Bottom, Ascending, Unknown). */
    val state: SquatState,
    /** Total de repetições concluídas com sucesso. */
    val repCount: Int,
    /** Indica se os dados de entrada são válidos e suficientes para a análise. */
    val hasValidData: Boolean
)
