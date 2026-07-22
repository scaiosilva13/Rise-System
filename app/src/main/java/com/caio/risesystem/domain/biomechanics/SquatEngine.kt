package com.caio.risesystem.domain.biomechanics

/**
 * Engine consolidada para o exercício de agachamento.
 * Centraliza a contagem de repetições e o estado do exercício.
 */
class SquatEngine {

    private val repCounter = SquatRepCounter()

    /**
     * Processa um novo snapshot biomecânico e retorna o status consolidado do exercício.
     * @param snapshot Dados dos ângulos corporais.
     * @return [SquatAnalysisResult] com o estado e contagem atualizados.
     */
    fun process(snapshot: BodyPoseSnapshot): SquatAnalysisResult {
        repCounter.update(snapshot)
        
        val currentState = repCounter.currentState
        
        return SquatAnalysisResult(
            state = currentState,
            repCount = repCounter.repCount,
            hasValidData = currentState != SquatState.UNKNOWN
        )
    }

    /**
     * Reseta completamente a sessão de agachamento (contador e estado).
     */
    fun reset() {
        repCounter.reset()
    }
}
