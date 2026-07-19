package com.caio.risesystem.domain.biomechanics

import com.google.mediapipe.tasks.components.containers.NormalizedLandmark
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Optional

class MediaPipeLandmarkMapperTest {

    @Test
    fun `map should return empty map for empty list`() {
        val result = MediaPipeLandmarkMapper.map(emptyList())
        assertTrue(result.isEmpty())
    }

    @Test
    fun `map should convert landmarks correctly`() {
        // Criando instâncias reais de NormalizedLandmark usando o método estático create
        val landmark0 = NormalizedLandmark.create(0.1f, 0.2f, 0f, Optional.empty(), Optional.empty())
        val landmark11 = NormalizedLandmark.create(0.5f, 0.6f, 0f, Optional.empty(), Optional.empty())

        val inputList = mutableListOf<NormalizedLandmark>()
        repeat(12) { i ->
            when (i) {
                0 -> inputList.add(landmark0)
                11 -> inputList.add(landmark11)
                else -> inputList.add(NormalizedLandmark.create(0f, 0f, 0f, Optional.empty(), Optional.empty()))
            }
        }

        val result = MediaPipeLandmarkMapper.map(inputList)

        assertEquals(0.1f, result[PoseLandmark.NOSE]?.x)
        assertEquals(0.2f, result[PoseLandmark.NOSE]?.y)
        assertEquals(0.5f, result[PoseLandmark.LEFT_SHOULDER]?.x)
        assertEquals(0.6f, result[PoseLandmark.LEFT_SHOULDER]?.y)
        assertEquals(12, result.size)
    }

    @Test
    fun `map should ignore indices above 32`() {
        val inputList = mutableListOf<NormalizedLandmark>()
        repeat(35) {
            inputList.add(NormalizedLandmark.create(0f, 0f, 0f, Optional.empty(), Optional.empty()))
        }

        val result = MediaPipeLandmarkMapper.map(inputList)

        // Deve mapear apenas os primeiros 33 (0 a 32)
        assertEquals(33, result.size)
        assertTrue(result.containsKey(PoseLandmark.RIGHT_FOOT_INDEX))
    }
}
