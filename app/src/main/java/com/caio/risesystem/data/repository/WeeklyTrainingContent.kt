package com.caio.risesystem.data.repository

import java.time.DayOfWeek

data class TrainingDayContent(
    val title: String,
    val summary: String,
    val suggestedTime: String,
    val details: String
)

object WeeklyTrainingContent {

    private const val PROGRESSION_STRENGTH = """
PROGRESSÃO
Quando atingir o limite superior das repetições em todas as séries, com boa técnica e ainda mantendo aproximadamente 2 repetições possíveis, aumentar levemente a carga na próxima sessão.
Se a execução piorar ou não for possível atingir o mínimo de repetições, manter ou reduzir a carga.
"""

    private const val PROGRESSION_RECOVERY = """
PROGRESSÃO
Aumentar duração ou intensidade somente quando a sessão atual estiver confortável e não prejudicar a recuperação dos treinos seguintes.
"""

    private const val HYDRATION = """
HIDRATAÇÃO
Cumprir a meta diária de água definida no plano e beber água durante o treino conforme a necessidade.
"""

    fun getContent(dayOfWeek: DayOfWeek): TrainingDayContent {
        return when (dayOfWeek) {
            DayOfWeek.MONDAY -> getMonday()
            DayOfWeek.TUESDAY -> getTuesday()
            DayOfWeek.WEDNESDAY -> getWednesday()
            DayOfWeek.THURSDAY -> getThursday()
            DayOfWeek.FRIDAY -> getFriday()
            DayOfWeek.SATURDAY -> getSaturday()
            DayOfWeek.SUNDAY -> getSunday()
        }
    }

    private fun getMonday() = TrainingDayContent(
        title = "Treino do dia",
        summary = "Recuperação e mobilidade leve",
        suggestedTime = "À noite",
        details = """
TREINO DO DIA

FOCO
Recuperação física após a rotina de trabalho, Taekwondo e faculdade.

DURAÇÃO
15 a 25 minutos.

AQUECIMENTO
Não programado.

EXERCÍCIOS
1. Mobilidade de tornozelo
- 2 séries de 10 movimentos por lado.
- Movimento lento e sem retirar o calcanhar do chão.

2. Mobilidade de quadril
- 2 séries de 8 movimentos por lado.
- Usar amplitude confortável.

3. Alongamento dinâmico da posterior da coxa
- 2 séries de 10 movimentos por lado.
- Não forçar o chute ou buscar amplitude máxima.

4. Rotação torácica
- 2 séries de 8 movimentos por lado.

CARDIO
Caminhada leve opcional de 10 a 15 minutos somente se estiver bem recuperado.

${PROGRESSION_RECOVERY.trim()}

ADAPTAÇÕES
- Se o joelho estiver sensível, evitar posições profundas.
- Se houver desconforto na posterior esquerda, reduzir a amplitude e não realizar alongamento agressivo.

${HYDRATION.trim()}

OBSERVAÇÕES
- Hoje não há musculação programada.
- A sessão está concluída após realizar a mobilidade ou cumprir conscientemente o descanso programado.
""".trimIndent()
    )

    private fun getTuesday() = TrainingDayContent(
        title = "Treino do dia",
        summary = "Push — peito, ombros e tríceps",
        suggestedTime = "Pela manhã",
        details = """
TREINO DO DIA

FOCO
Peito, ombros e tríceps.

DURAÇÃO
60 a 80 minutos, incluindo a caminhada.

AQUECIMENTO
- Caminhada de aproximadamente 23 minutos até a academia em ritmo confortável.
- Antes do primeiro exercício, realizar 2 séries leves de aquecimento.
- Aumentar a carga gradualmente sem chegar perto da falha.

EXERCÍCIOS
EXERCÍCIO 1 — SUPINO MÁQUINA
- Séries: 3.
- Repetições: 8 a 12.
- Intervalo: 90 segundos.
- Execução: manter as costas apoiadas e controlar a descida.

EXERCÍCIO 2 — SUPINO INCLINADO COM HALTERES OU MÁQUINA
- Séries: 3.
- Repetições: 8 a 12.
- Intervalo: 90 segundos.
- Execução: usar amplitude confortável e evitar abrir excessivamente os cotovelos.

EXERCÍCIO 3 — CRUCIFIXO NA MÁQUINA OU CROSSOVER
- Séries: 2.
- Repetições: 12 a 15.
- Intervalo: 60 segundos.
- Execução: controlar o retorno e não deixar a carga puxar os ombros.

EXERCÍCIO 4 — DESENVOLVIMENTO DE OMBROS NA MÁQUINA
- Séries: 3.
- Repetições: 8 a 12.
- Intervalo: 90 segundos.
- Execução: manter o tronco apoiado e não exagerar na amplitude inferior.

EXERCÍCIO 5 — ELEVAÇÃO LATERAL
- Séries: 3.
- Repetições: 12 a 15.
- Intervalo: 60 segundos.
- Execução: elevar com controle, sem impulsionar o corpo.

EXERCÍCIO 6 — TRÍCEPS NA POLIA COM CORDA
- Séries: 3.
- Repetições: 10 a 15.
- Intervalo: 60 segundos.
- Execução: manter os cotovelos próximos ao corpo.

EXERCÍCIO 7 — TRÍCEPS ACIMA DA CABEÇA NA POLIA
- Séries: 2.
- Repetições: 10 a 15.
- Intervalo: 60 segundos.
- Execução: manter o abdômen firme e evitar compensar com a lombar.

CARDIO
- A caminhada até a academia já conta como cardio leve.
- Ao final, realizar mais 10 minutos de bicicleta ou esteira somente se houver tempo e boa disposição.
- Intensidade: moderada, permitindo falar frases curtas sem ficar ofegante demais.

${PROGRESSION_STRENGTH.trim()}

ADAPTAÇÕES
- Se o tempo estiver curto, retirar o exercício 7 antes de reduzir os exercícios principais.
- Priorizar a execução correta em vez de carga excessiva.

${HYDRATION.trim()}

OBSERVAÇÕES
- Utilizar carga que permita terminar cada série com aproximadamente 2 a 3 repetições possíveis.
- Não é necessário treinar até a falha.
""".trimIndent()
    )

    private fun getWednesday() = TrainingDayContent(
        title = "Treino do dia",
        summary = "Recuperação após trabalho e Taekwondo",
        suggestedTime = "À noite",
        details = """
TREINO DO DIA

FOCO
Recuperação e redução da fadiga acumulada.

DURAÇÃO
15 a 20 minutos.

AQUECIMENTO
Não programado.

EXERCÍCIOS
1. Mobilidade de tornozelo
- 2 séries de 10 movimentos por lado.

2. Mobilidade de quadril
- 2 séries de 8 movimentos por lado.

3. Alongamento dinâmico da posterior da coxa
- 2 séries de 10 movimentos por lado.
- Usar somente amplitude confortável.

4. Mobilidade de ombros
- 2 séries de 10 movimentos controlados.

CARDIO
Caminhada leve opcional de até 15 minutos.

${PROGRESSION_RECOVERY.trim()}

ADAPTAÇÕES
- Não realizar chutes frontais altos sem aquecimento.
- Se houver sensibilidade no joelho ou posterior, limitar a sessão à mobilidade sem dor.

${HYDRATION.trim()}

OBSERVAÇÕES
- A rotina de trabalho e Taekwondo já representa demanda física relevante.
- O objetivo de hoje é chegar recuperado ao treino de Pull de quinta-feira.
""".trimIndent()
    )

    private fun getThursday() = TrainingDayContent(
        title = "Treino do dia",
        summary = "Pull — costas, posterior de ombro e bíceps",
        suggestedTime = "Pela manhã",
        details = """
TREINO DO DIA

FOCO
Costas, posterior de ombro e bíceps.

DURAÇÃO
60 a 80 minutos, incluindo a caminhada.

AQUECIMENTO
- Caminhada de aproximadamente 23 minutos até a academia.
- Realizar mobilidade leve dos ombros.
- Fazer 2 séries leves no primeiro exercício antes das séries principais.

EXERCÍCIOS
EXERCÍCIO 1 — PUXADA FRONTAL NA POLIA
- Séries: 3.
- Repetições: 8 a 12.
- Intervalo: 90 segundos.
- Execução: puxar em direção à parte superior do peito.

EXERCÍCIO 2 — REMADA SENTADA NO CABO
- Séries: 3.
- Repetições: 8 a 12.
- Intervalo: 90 segundos.
- Execução: manter a coluna estável e conduzir os cotovelos para trás.

EXERCÍCIO 3 — REMADA MÁQUINA COM APOIO NO PEITO
- Séries: 3.
- Repetições: 10 a 12.
- Intervalo: 90 segundos.
- Execução: manter o peito apoiado e controlar o retorno.

EXERCÍCIO 4 — PULLOVER NA POLIA OU MÁQUINA
- Séries: 2.
- Repetições: 12 a 15.
- Intervalo: 60 segundos.
- Execução: manter os braços quase estendidos e evitar compensação lombar.

EXERCÍCIO 5 — FACE PULL
- Séries: 3.
- Repetições: 12 a 15.
- Intervalo: 60 segundos.
- Execução: puxar a corda em direção ao rosto e abrir as mãos.

EXERCÍCIO 6 — ROSCA DIRETA NA POLIA OU MÁQUINA
- Séries: 3.
- Repetições: 10 a 12.
- Intervalo: 60 segundos.
- Execução: manter os cotovelos estáveis.

EXERCÍCIO 7 — ROSCA MARTELO
- Séries: 2.
- Repetições: 10 a 15.
- Intervalo: 60 segundos.
- Execução: controlar completamente a descida.

CARDIO
- A caminhada até a academia conta como cardio leve.
- Cardio adicional opcional: 10 minutos de bicicleta em intensidade moderada.

${PROGRESSION_STRENGTH.trim()}

ADAPTAÇÕES
- Se o tempo estiver curto, retirar o exercício 7.
- Se o antebraço limitar o treino, reduzir a carga e reforçar a execução.

${HYDRATION.trim()}

OBSERVAÇÕES
- Manter aproximadamente 2 a 3 repetições em reserva.
- Evitar puxar cargas usando impulso.
""".trimIndent()
    )

    private fun getFriday() = TrainingDayContent(
        title = "Treino do dia",
        summary = "Pernas — quadríceps, posteriores e glúteos",
        suggestedTime = "À tarde",
        details = """
TREINO DO DIA

FOCO
Quadríceps, posteriores da coxa, glúteos e panturrilhas.

DURAÇÃO
65 a 85 minutos.

AQUECIMENTO
- Bicicleta: 7 a 10 minutos em intensidade leve.
- Mobilidade de tornozelos e quadris.
- Flexão e extensão de joelho sem carga.
- Realizar 2 a 3 séries progressivas e leves no leg press.

EXERCÍCIOS
EXERCÍCIO 1 — LEG PRESS
- Séries: 3.
- Repetições: 10 a 15.
- Intervalo: 120 segundos.
- Execução: manter quadril e lombar apoiados.

EXERCÍCIO 2 — CADEIRA FLEXORA
- Séries: 3.
- Repetições: 10 a 15.
- Intervalo: 75 segundos.
- Execução: controlar a subida e a descida.

EXERCÍCIO 3 — CADEIRA EXTENSORA
- Séries: 3.
- Repetições: 12 a 15.
- Intervalo: 75 segundos.
- Execução: usar carga controlada e amplitude sem dor.

EXERCÍCIO 4 — ELEVAÇÃO PÉLVICA NA MÁQUINA OU COM APOIO
- Séries: 3.
- Repetições: 10 a 15.
- Intervalo: 90 segundos.
- Execução: contrair os glúteos no topo.

EXERCÍCIO 5 — ABDUTORA
- Séries: 3.
- Repetições: 12 a 20.
- Intervalo: 60 segundos.
- Execução: manter o tronco estável.

EXERCÍCIO 6 — PANTURRILHA NA MÁQUINA OU LEG PRESS
- Séries: 3.
- Repetições: 12 a 20.
- Intervalo: 60 segundos.
- Execução: evitar movimentos rápidos.

CARDIO
- Bicicleta ao final: 8 a 12 minutos.
- Intensidade: leve.
- Opcional quando houver fadiga elevada ou sensibilidade no joelho.

${PROGRESSION_STRENGTH.trim()}

ADAPTAÇÕES
- Não realizar agachamento com barra.
- Não buscar profundidade máxima no leg press se houver dor no joelho.
- Interromper qualquer exercício que gere dor aguda.

${HYDRATION.trim()}

OBSERVAÇÕES
- Não treinar até a falha.
- Manter 2 a 3 repetições em reserva.
""".trimIndent()
    )

    private fun getSaturday() = TrainingDayContent(
        title = "Treino do dia",
        summary = "Superior completo — peito, costas, ombros e braços",
        suggestedTime = "Pela manhã",
        details = """
TREINO DO DIA

FOCO
Parte superior completa.

DURAÇÃO
65 a 85 minutos.

AQUECIMENTO
- Esteira ou bicicleta: 5 a 7 minutos em intensidade leve.
- Mobilidade leve dos ombros.
- Realizar 2 séries progressivas no primeiro exercício.

EXERCÍCIOS
EXERCÍCIO 1 — SUPINO INCLINADO NA MÁQUINA
- Séries: 3.
- Repetições: 8 a 12.
- Intervalo: 90 segundos.

EXERCÍCIO 2 — REMADA MÁQUINA COM APOIO
- Séries: 3.
- Repetições: 8 a 12.
- Intervalo: 90 segundos.

EXERCÍCIO 3 — PUXADA FRONTAL
- Séries: 3.
- Repetições: 10 a 12.
- Intervalo: 90 segundos.

EXERCÍCIO 4 — ELEVAÇÃO LATERAL
- Séries: 2.
- Repetições: 12 a 15.
- Intervalo: 60 segundos.

EXERCÍCIO 5 — FACE PULL
- Séries: 2.
- Repetições: 12 a 15.
- Intervalo: 60 segundos.

EXERCÍCIO 6 — ROSCA NA POLIA
- Séries: 2.
- Repetições: 10 a 15.
- Intervalo: 60 segundos.

EXERCÍCIO 7 — TRÍCEPS NA CORDA
- Séries: 2.
- Repetições: 10 a 15.
- Intervalo: 60 segundos.

CARDIO
- Bicicleta ou esteira: 10 a 15 minutos.
- Intensidade: leve a moderada.
- Opcional quando houver fadiga acumulada.

${PROGRESSION_STRENGTH.trim()}

ADAPTAÇÕES
- Se houver fadiga elevada, retirar os últimos exercícios de braço (rosca e tríceps).

${HYDRATION.trim()}

OBSERVAÇÕES
- Utilizar cargas controladas.
- Manter 2 a 3 repetições em reserva.
""".trimIndent()
    )

    private fun getSunday() = TrainingDayContent(
        title = "Treino do dia",
        summary = "Cardio, core e mobilidade",
        suggestedTime = "Pela manhã",
        details = """
TREINO DO DIA

FOCO
Condicionamento cardiovascular, estabilidade do tronco e mobilidade.

DURAÇÃO
50 a 65 minutos.

AQUECIMENTO
- Aquecimento: 5 minutos.
- Mobilidade de tornozelos, quadris e ombros.

EXERCÍCIOS
CORE
1. DEAD BUG
- Séries: 3.
- Repetições: 8 a 12 por lado.
- Intervalo: 45 a 60 segundos.

2. PALLOF PRESS NA POLIA
- Séries: 3.
- Repetições: 10 a 12 por lado.
- Intervalo: 45 a 60 segundos.

MOBILIDADE
1. Mobilidade de tornozelo (2x10)
2. Mobilidade de quadril (2x8)
3. Alongamento dinâmico da posterior da coxa (2x10)
4. Mobilidade torácica (2x8)

CARDIO
- Cardio contínuo: 30 a 40 minutos.
- Intensidade: leve a moderada.
- Bicicleta como opção preferencial; esteira apenas como caminhada alternativa.

${PROGRESSION_RECOVERY.trim()}

ADAPTAÇÕES
- Preferir bicicleta caso a caminhada cause desconforto no joelho.
- Interromper movimentos que causem dor aguda.

${HYDRATION.trim()}

OBSERVAÇÕES
- O cardio deve ser contínuo e controlado.
- Esta sessão contribui para o volume aeróbico semanal.
""".trimIndent()
    )
}
