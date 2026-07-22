package com.caio.risesystem.data.repository

import java.time.DayOfWeek
import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WeeklyTrainingEnrichmentTest {

    @Test
    fun `verify content for all 7 days of the week`() {
        val days = DayOfWeek.values()
        assertEquals(7, days.size)

        days.forEach { day ->
            val content = WeeklyTrainingContent.getContent(day)
            
            // Título padrão
            assertEquals("Treino do dia", content.title)
            
            // Verificar se contém as seções obrigatórias
            val details = content.details
            assertTrue("Details for $day should contain TREINO DO DIA", details.contains("TREINO DO DIA"))
            assertTrue("Details for $day should contain FOCO", details.contains("FOCO"))
            assertTrue("Details for $day should contain DURAÇÃO", details.contains("DURAÇÃO"))
            assertTrue("Details for $day should contain AQUECIMENTO", details.contains("AQUECIMENTO"))
            assertTrue("Details for $day should contain EXERCÍCIOS", details.contains("EXERCÍCIOS") || details.contains("MOBILIDADE"))
            assertTrue("Details for $day should contain CARDIO", details.contains("CARDIO"))
            assertTrue("Details for $day should contain PROGRESSÃO", details.contains("PROGRESSÃO"))
            assertTrue("Details for $day should contain ADAPTAÇÕES", details.contains("ADAPTAÇÕES"))
            assertTrue("Details for $day should contain HIDRATAÇÃO", details.contains("HIDRATAÇÃO"))
            assertTrue("Details for $day should contain OBSERVAÇÕES", details.contains("OBSERVAÇÕES"))
            
            // Verificar Hidratação (igual para todos)
            assertTrue("Details for $day should contain correct hydration text", 
                details.contains("Cumprir a meta diária de água definida no plano"))
        }
    }

    @Test
    fun `verify specific content for Monday`() {
        val content = WeeklyTrainingContent.getContent(DayOfWeek.MONDAY)
        assertEquals("Recuperação e mobilidade leve", content.summary)
        assertEquals("À noite", content.suggestedTime)
        assertTrue(content.details.contains("Recuperação física após a rotina de trabalho"))
    }

    @Test
    fun `verify specific content for Tuesday`() {
        val content = WeeklyTrainingContent.getContent(DayOfWeek.TUESDAY)
        assertEquals("Push — peito, ombros e tríceps", content.summary)
        assertEquals("Pela manhã", content.suggestedTime)
        assertTrue(content.details.contains("EXERCÍCIO 1 — SUPINO MÁQUINA"))
    }

    @Test
    fun `verify adjusted content for Friday`() {
        val content = WeeklyTrainingContent.getContent(DayOfWeek.FRIDAY)
        assertEquals("Pernas — quadríceps, posteriores e glúteos", content.summary)
        // Ajuste de cardio: 8 a 12 minutos, leve, opcional
        assertTrue(content.details.contains("Bicicleta ao final: 8 a 12 minutos"))
        assertTrue(content.details.contains("Intensidade: leve"))
        assertTrue(content.details.contains("Opcional quando houver fadiga elevada"))
    }

    @Test
    fun `verify adjusted content for Saturday`() {
        val content = WeeklyTrainingContent.getContent(DayOfWeek.SATURDAY)
        assertEquals("Superior completo — peito, costas, ombros e braços", content.summary)
        // Ajuste de exercícios: Supino inclinado na máquina (3x8-12), etc.
        assertTrue(content.details.contains("SUPINO INCLINADO NA MÁQUINA"))
        assertTrue(content.details.contains("- Séries: 3."))
        // No crucifixo no sábado (conforme ajustes)
        assertTrue("Saturday should not contain Crucifixo", !content.details.contains("CRUCIFIXO"))
        // Ajuste de cardio
        assertTrue(content.details.contains("Bicicleta ou esteira: 10 a 15 minutos"))
    }

    @Test
    fun `verify adjusted content for Sunday`() {
        val content = WeeklyTrainingContent.getContent(DayOfWeek.SUNDAY)
        assertEquals("Cardio, core e mobilidade", content.summary)
        // Ajuste de Duração: 50 a 65 min
        assertTrue(content.details.contains("DURAÇÃO\n50 a 65 minutos"))
        // Ajuste de Core: Dead bug e Pallof press (No prancha)
        assertTrue(content.details.contains("DEAD BUG"))
        assertTrue(content.details.contains("PALLOF PRESS"))
        assertTrue("Sunday should not contain Prancha", !content.details.contains("PRANCHA"))
        // Ajuste de Cardio: 30 a 40 min
        assertTrue(content.details.contains("Cardio contínuo: 30 a 40 minutos"))
    }

    @Test
    fun `verify date to day of week mapping`() {
        // Segunda-feira
        assertEquals(DayOfWeek.MONDAY, LocalDate.parse("2026-07-20").dayOfWeek)
        // Terça-feira
        assertEquals(DayOfWeek.TUESDAY, LocalDate.parse("2026-07-21").dayOfWeek)
        // Quarta-feira
        assertEquals(DayOfWeek.WEDNESDAY, LocalDate.parse("2026-07-22").dayOfWeek)
        // Quinta-feira
        assertEquals(DayOfWeek.THURSDAY, LocalDate.parse("2026-07-23").dayOfWeek)
        // Sexta-feira
        assertEquals(DayOfWeek.FRIDAY, LocalDate.parse("2026-07-24").dayOfWeek)
        // Sábado
        assertEquals(DayOfWeek.SATURDAY, LocalDate.parse("2026-07-25").dayOfWeek)
        // Domingo
        assertEquals(DayOfWeek.SUNDAY, LocalDate.parse("2026-07-26").dayOfWeek)
    }

    @Test
    fun `verify progression text for strength and recovery`() {
        val strengthDays = listOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY)
        val recoveryDays = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.SUNDAY)
        
        strengthDays.forEach { day ->
            val details = WeeklyTrainingContent.getContent(day).details
            assertTrue("Strength day $day should have correct progression text", 
                details.contains("aumentar levemente a carga na próxima sessão"))
        }

        recoveryDays.forEach { day ->
            val details = WeeklyTrainingContent.getContent(day).details
            assertTrue("Recovery day $day should have correct progression text", 
                details.contains("Aumentar duração ou intensidade somente quando a sessão atual estiver confortável"))
        }
    }
}
