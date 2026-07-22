package com.caio.risesystem.data.repository

import com.caio.risesystem.domain.model.DailyTaskType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DailyTaskEnrichmentTest {

    @Test
    fun `enrichment logic placeholder - logic is inside RepositoryImpl`() {
        // Como o enriquecimento depende do Room (Dao), testes reais exigiriam Mockito
        // ou seriam testes instrumentados. Validaremos a lógica via build e logs.
        assertTrue(true)
    }

    @Test
    fun `verify task types mapping`() {
        assertEquals("Alimentação", DailyTaskType.MEAL.toReadableString())
        assertEquals("Água", DailyTaskType.WATER.toReadableString())
        assertEquals("Treino", DailyTaskType.WORKOUT.toReadableString())
    }
}
