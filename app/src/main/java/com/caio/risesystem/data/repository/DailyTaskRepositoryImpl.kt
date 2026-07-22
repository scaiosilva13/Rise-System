package com.caio.risesystem.data.repository

import com.caio.risesystem.data.local.RiseDatabase
import com.caio.risesystem.data.local.entity.DailyTaskEntity
import com.caio.risesystem.data.mapper.toDomain
import com.caio.risesystem.domain.model.DailyTask
import com.caio.risesystem.domain.model.DailyTaskStatus
import com.caio.risesystem.domain.model.DailyTaskType
import com.caio.risesystem.domain.repository.DailyTaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DailyTaskRepositoryImpl(
    database: RiseDatabase
) : DailyTaskRepository {

    private val dailyTaskDao = database.dailyTaskDao()

    override fun observeTasksByDate(date: String): Flow<List<DailyTask>> {
        return dailyTaskDao.observeByDate(date).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun observeTaskById(id: Long): Flow<DailyTask?> {
        return dailyTaskDao.observeTaskById(id).map { it?.toDomain() }
    }

    override suspend fun updateTaskStatus(id: Long, status: DailyTaskStatus) {
        dailyTaskDao.updateStatus(id, status.name, System.currentTimeMillis())
    }

    override suspend fun initializeTasksForDate(date: String) {
        val now = System.currentTimeMillis()
        val initialTasks = listOf(
            createDemoTask(date, DailyTaskType.WATER, "Água ao acordar", "07:00", 1, now),
            createDemoTask(date, DailyTaskType.MEAL, "Café da manhã", "07:30", 2, now),
            createDemoTask(date, DailyTaskType.MEAL, "Lanche da manhã", "10:00", 3, now),
            createDemoTask(date, DailyTaskType.MEAL, "Almoço", "12:30", 4, now),
            createDemoTask(date, DailyTaskType.MEAL, "Lanche da tarde", "17:30", 5, now),
            createDemoTask(date, DailyTaskType.WORKOUT, "Treino do dia", "19:00", 6, now),
            createDemoTask(date, DailyTaskType.MEAL, "Jantar", "20:30", 7, now),
            createDemoTask(date, DailyTaskType.WATER, "Meta de água", "22:00", 8, now),
            createDemoTask(date, DailyTaskType.SLEEP, "Meta de sono", "23:30", 9, now)
        )
        dailyTaskDao.initializeTasksIfEmpty(date, initialTasks)
        enrichTasks(date)
    }

    private suspend fun enrichTasks(date: String) {
        val now = System.currentTimeMillis()
        
        // 1. Água ao acordar
        updateTaskDetails(date, 1, DailyTaskType.WATER, 
            "Água ao acordar + Creatina",
            "300-500ml de água + 5g de creatina",
            """
            ORIENTAÇÃO
            - Beber entre 300 e 500 ml de água em temperatura ambiente ao acordar.
            - Tomar 5 g de creatina todos os dias, independentemente de haver treino.
            """.trimIndent(),
            now
        )

        // 2. Café da manhã
        updateTaskDetails(date, 2, DailyTaskType.MEAL,
            "Café da manhã",
            "Pão, ovos/queijo e leite com café",
            """
            BEBIDA
            - 250 ml de leite desnatado com café.

            OPÇÃO 1
            - 1 pão francês ou 2 fatias de pão de forma;
            - 1 ovo;
            - 1 clara;
            ou:
            - 20 g de queijo magro.

            OPÇÃO 2 — CREPIOCA
            - 1 ovo;
            - 1 clara;
            - 15 g de tapioca;
            - pitada de sal;
            - 20 g de muçarela.

            OPÇÃO 3
            - 4 pães de queijo pequenos;
            - 80 g de frango desfiado.

            OBSERVAÇÕES
            - Escolher uma das opções.
            - Manter a bebida indicada.
            """.trimIndent(),
            now
        )

        // 3. Lanche da manhã
        updateTaskDetails(date, 3, DailyTaskType.MEAL,
            "Lanche da manhã",
            "Porção de frutas",
            """
            ORIENTAÇÃO
            Escolher uma opção:
            - 250 g de melão;
            - 1 maçã;
            - 1 pera;
            - 180 g de melancia;
            - 10 morangos;
            - 1 goiaba;
            - 1 laranja.

            OBSERVAÇÕES
            - Dar preferência a frutas com casca quando aplicável.
            """.trimIndent(),
            now
        )

        // 4. Almoço
        updateTaskDetails(date, 4, DailyTaskType.MEAL,
            "Almoço",
            "Prato equilibrado com salada",
            """
            ORIENTAÇÃO
            - Preencher metade do prato com legumes e verduras.

            CARBOIDRATO — ESCOLHER UMA OPÇÃO
            - 120 g de arroz;
            ou:
            - 300 g de batata inglesa ou doce;
            - 140 g de macarrão;
            - 140 g de mandioca.

            ACOMPANHAMENTO
            - 120 g de feijão.

            PROTEÍNA
            - 120 g de proteína (frango, carne bovina magra, patinho, músculo, paleta, lombo suíno).

            SOBREMESA — ESCOLHER UMA OPÇÃO
            - 1 laranja;
            - 1 fatia de abacaxi.

            OBSERVAÇÕES
            - Chocolate somente de forma esporádica.
            """.trimIndent(),
            now
        )

        // 5. Lanche da tarde
        updateTaskDetails(date, 5, DailyTaskType.MEAL,
            "Lanche da tarde",
            "Sanduíche ou Shake",
            """
            OPÇÃO 1
            - 1 pão francês ou 2 fatias de pão de forma;
            - 80 g de frango desfiado ou carne;
            - alface e tomate opcionais.

            OPÇÃO 2 — SANDUÍCHE NATURAL
            - 2 fatias de pão;
            - 1 colher de sopa de creme de ricota ou maionese light;
            - 60 g de frango, atum ou carne;
            - cenoura, alface e tomate opcionais.

            OPÇÃO 3
            - 250 ml de leite desnatado;
            - 1 banana;
            - 1 scoop de whey.

            OPÇÃO 4
            - 1 pão francês ou 2 fatias de pão;
            - 1 ovo;
            - 1 clara;
            ou:
            - 20 g de queijo magro.

            OBSERVAÇÕES
            - Whey é opcional conforme o plano original.
            """.trimIndent(),
            now
        )

        // 6. Treino do dia
        try {
            val localDate = java.time.LocalDate.parse(date)
            val dayOfWeek = localDate.dayOfWeek
            val trainingContent = WeeklyTrainingContent.getContent(dayOfWeek)
            updateTaskDetails(
                date = date,
                sortOrder = 6,
                type = DailyTaskType.WORKOUT,
                newTitle = trainingContent.title,
                newSummary = trainingContent.summary,
                newDetails = trainingContent.details,
                updatedAt = now,
                newSuggestedTime = trainingContent.suggestedTime
            )
        } catch (e: Exception) {
            // Fallback para caso de erro no parse da data
        }

        // 7. Jantar
        updateTaskDetails(date, 7, DailyTaskType.MEAL,
            "Jantar",
            "Arroz/Feijão, Omelete, Wrap ou Pizza",
            """
            OPÇÃO 1
            - metade do prato com salada e legumes;
            - 80 g de arroz;
            - 80 g de feijão;
            - 100 g de proteína.

            OPÇÃO 2 — OMELETE
            - 2 ovos;
            - até meia colher de sopa de tapioca;
            - 60 g de frango;
            - 1 fatia de muçarela;
            - salada opcional.

            OPÇÃO 3 — WRAP
            - 1 disco de Rap10;
            - 80 g de frango ou carne moída;
            - 1 fatia de muçarela;
            - salada opcional.

            OPÇÃO 4 — PIZZA DE FRIGIDEIRA
            - 1 disco de Rap10;
            - molho de tomate;
            - 1 fatia de muçarela;
            - 80 g de frango;
            - temperos opcionais.
            """.trimIndent(),
            now
        )

        // 8. Meta de água
        updateTaskDetails(date, 8, DailyTaskType.WATER,
            "Meta de água",
            "Consumo total de 2,5 litros",
            """
            ORIENTAÇÃO
            - Meta diária inicial: 2,5 litros de água.

            OBSERVAÇÕES
            - Distribuir o consumo ao longo do dia.
            """.trimIndent(),
            now
        )

        // 9. Meta de sono
        updateTaskDetails(date, 9, DailyTaskType.SLEEP,
            "Meta de sono",
            "Higiene do sono e descanso",
            """
            ORIENTAÇÃO
            - Ir para a cama até 23:30.
            - Evitar celular durante uma hora antes de dormir.

            OBSERVAÇÕES
            - Evitar telas durante as refeições e mastigar lentamente.
            - Evitar sucos industrializados, energéticos e beliscar.
            - Preferir refrigerante zero quando consumir.
            """.trimIndent(),
            now
        )
    }

    private suspend fun updateTaskDetails(
        date: String,
        sortOrder: Int,
        type: DailyTaskType,
        newTitle: String,
        newSummary: String,
        newDetails: String,
        updatedAt: Long,
        newSuggestedTime: String? = null
    ) {
        val existing = dailyTaskDao.getTaskByProperties(date, sortOrder, type.name)
        if (existing != null) {
            dailyTaskDao.update(existing.copy(
                title = newTitle,
                summary = newSummary,
                details = newDetails,
                suggestedTime = newSuggestedTime ?: existing.suggestedTime,
                updatedAt = updatedAt
            ))
        }
    }

    private fun createDemoTask(
        date: String,
        type: DailyTaskType,
        title: String,
        time: String,
        order: Int,
        timestamp: Long
    ) = DailyTaskEntity(
        date = date,
        type = type.name,
        title = title,
        summary = "",
        suggestedTime = time,
        status = DailyTaskStatus.PENDING.name,
        sortOrder = order,
        details = "",
        createdAt = timestamp,
        updatedAt = timestamp
    )
}
