package com.caio.risesystem.domain.biomechanics

/**
 * Contador de repetições para o exercício de agachamento.
 * Utiliza uma [SquatStateMachine] para validar a sequência completa de movimento.
 */
class SquatRepCounter {

    private val stateMachine = SquatStateMachine()
    
    /** Quantidade de repetições válidas concluídas. */
    var repCount: Int = 0
        private set

    /** Estado atual do agachamento. */
    val currentState: SquatState
        get() = stateMachine.currentState

    private var hasDescended = false
    private var hasReachedBottom = false
    private var hasAscended = false

    /**
     * Atualiza o estado da contagem com base em um novo snapshot biomecânico.
     * Uma repetição é contada apenas quando a sequência completa é percorrida:
     * STANDING -> DESCENDING -> BOTTOM -> ASCENDING -> STANDING
     */
    fun update(snapshot: BodyPoseSnapshot) {
        val newState = stateMachine.update(snapshot)

        when (newState) {
            SquatState.STANDING -> {
                // Se percorreu todas as fases anteriores e voltou para STANDING, conta uma rep
                if (hasReachedBottom && hasDescended && hasAscended) {
                    repCount++
                }
                // Em qualquer retorno para STANDING (válido ou não), resetamos o progresso da rep atual
                resetProgress()
            }
            
            SquatState.DESCENDING -> {
                // Se já estiver descendo vindo de STANDING, marcamos a descida
                hasDescended = true
            }
            
            SquatState.BOTTOM -> {
                // Se já desceu, marcamos que atingiu o fundo
                if (hasDescended) {
                    hasReachedBottom = true
                }
            }
            
            SquatState.ASCENDING -> {
                // Se já desceu e atingiu o fundo, marcamos a subida
                if (hasReachedBottom) {
                    hasAscended = true
                }
            }
            
            SquatState.UNKNOWN -> {
                // Perda de dados ou erro reseta o progresso da repetição atual
                resetProgress()
            }
        }
    }

    private fun resetProgress() {
        hasDescended = false
        hasReachedBottom = false
        hasAscended = false
    }

    /**
     * Reseta o contador e a máquina de estados interna.
     */
    fun reset() {
        repCount = 0
        resetProgress()
        stateMachine.reset()
    }
}
