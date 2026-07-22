package com.caio.risesystem.domain.biomechanics

import com.google.mediapipe.tasks.components.containers.NormalizedLandmark
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Optional

class MediaPipeSquatProcessorTest {

    private val processor = MediaPipeSquatProcessor()

    private fun createLandmark(x: Float, y: Float): NormalizedLandmark {
        return NormalizedLandmark.create(x, y, 0f, Optional.empty(), Optional.empty())
    }

    private fun createPose(kneeAngle: Float): List<NormalizedLandmark> {
        val landmarks = mutableListOf<NormalizedLandmark>()
        // Preenche com 33 pontos para passar no PoseAnalyzer
        repeat(33) { landmarks.add(createLandmark(0.5f, 0.5f)) }

        // Ajusta landmarks obrigatórios para o ângulo desejado
        // Referências: HIP(23,24), KNEE(25,26), ANKLE(27,28)
        // Standing (180): (0,1), (0,0), (0,-1)
        if (kneeAngle > 160f) {
            landmarks[23] = createLandmark(0f, 1f)
            landmarks[25] = createLandmark(0f, 0f)
            landmarks[27] = createLandmark(0f, -1f)
            landmarks[24] = createLandmark(0f, 1f)
            landmarks[26] = createLandmark(0f, 0f)
            landmarks[28] = createLandmark(0f, -1f)
        } else if (kneeAngle < 90f) {
            // Bottom (~63): (1, -0.5), (0,0), (0,-1)
            landmarks[23] = createLandmark(1f, -0.5f)
            landmarks[25] = createLandmark(0f, 0f)
            landmarks[27] = createLandmark(0f, -1f)
            landmarks[24] = createLandmark(1f, -0.5f)
            landmarks[26] = createLandmark(0f, 0f)
            landmarks[28] = createLandmark(0f, -1f)
        } else {
            // Intermediate (135): (1,1), (0,0), (0,-1)
            landmarks[23] = createLandmark(1f, 1f)
            landmarks[25] = createLandmark(0f, 0f)
            landmarks[27] = createLandmark(0f, -1f)
            landmarks[24] = createLandmark(1f, 1f)
            landmarks[26] = createLandmark(0f, 0f)
            landmarks[28] = createLandmark(0f, -1f)
        }
        
        // Outros pontos obrigatórios (Ombros e Pulsos) para o PoseAnalyzer não reclamar
        landmarks[11] = createLandmark(0f, 0f)
        landmarks[12] = createLandmark(0f, 0f)
        landmarks[13] = createLandmark(0f, 0f)
        landmarks[14] = createLandmark(0f, 0f)
        landmarks[15] = createLandmark(0f, 0f)
        landmarks[16] = createLandmark(0f, 0f)

        return landmarks
    }

    @Test
    fun `should return invalid result for empty poses list`() {
        val result = processor.processLandmarks(emptyList())
        assertFalse(result.hasValidData)
        assertEquals(SquatState.UNKNOWN, result.state)
    }

    @Test
    fun `should detect standing pose correctly`() {
        val poses = listOf(createPose(180f))
        val result = processor.processLandmarks(poses)
        assertTrue(result.hasValidData)
        assertEquals(SquatState.STANDING, result.state)
    }

    @Test
    fun `should count one complete rep`() {
        processor.processLandmarks(listOf(createPose(180f))) // Standing
        processor.processLandmarks(listOf(createPose(135f))) // Descending
        processor.processLandmarks(listOf(createPose(70f)))  // Bottom
        processor.processLandmarks(listOf(createPose(135f))) // Ascending
        val result = processor.processLandmarks(listOf(createPose(180f))) // Standing (Fim)
        
        assertEquals(1, result.repCount)
        assertEquals(SquatState.STANDING, result.state)
    }

    @Test
    fun `should maintain count after pose loss`() {
        // Completa uma rep
        processor.processLandmarks(listOf(createPose(180f)))
        processor.processLandmarks(listOf(createPose(135f)))
        processor.processLandmarks(listOf(createPose(70f)))
        processor.processLandmarks(listOf(createPose(135f)))
        processor.processLandmarks(listOf(createPose(180f)))
        
        assertEquals(1, processor.processLandmarks(listOf(createPose(180f))).repCount)

        // Perda de pose (lista vazia)
        val lostResult = processor.processLandmarks(emptyList())
        assertFalse(lostResult.hasValidData)
        assertEquals(1, lostResult.repCount) // Contador preservado

        // Recuperação
        val recoveredResult = processor.processLandmarks(listOf(createPose(180f)))
        assertTrue(recoveredResult.hasValidData)
        assertEquals(1, recoveredResult.repCount)
    }

    @Test
    fun `should discard partial rep on pose loss during movement`() {
        processor.processLandmarks(listOf(createPose(180f)))
        processor.processLandmarks(listOf(createPose(135f)))
        processor.processLandmarks(listOf(createPose(70f))) // At bottom
        
        // Perda de pose
        processor.processLandmarks(emptyList())
        
        // Tentativa de completar após recuperar
        processor.processLandmarks(listOf(createPose(135f)))
        val result = processor.processLandmarks(listOf(createPose(180f)))
        
        assertEquals(0, result.repCount) // Resetado pelo UNKNOWN
    }

    @Test
    fun `should reset completely`() {
        processor.processLandmarks(listOf(createPose(180f)))
        processor.processLandmarks(listOf(createPose(135f)))
        processor.processLandmarks(listOf(createPose(70f)))
        processor.processLandmarks(listOf(createPose(135f)))
        processor.processLandmarks(listOf(createPose(180f)))
        
        processor.reset()
        
        val result = processor.processLandmarks(emptyList())
        assertEquals(0, result.repCount)
        assertEquals(SquatState.UNKNOWN, result.state)
    }

    @Test
    fun `should return invalid result when first pose is empty`() {
        val result = processor.processLandmarks(
            listOf(emptyList())
        )
        assertEquals(SquatState.UNKNOWN, result.state)
        assertFalse(result.hasValidData)
        assertEquals(0, result.repCount)
    }
}
