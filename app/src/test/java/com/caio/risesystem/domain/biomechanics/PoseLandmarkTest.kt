package com.caio.risesystem.domain.biomechanics

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PoseLandmarkTest {

    @Test
    fun `should convert all 33 valid indices correctly`() {
        for (i in 0..32) {
            val landmark = PoseLandmark.fromIndex(i)
            assertTrue("Index $i should be valid", PoseLandmark.isValidIndex(i))
            assertEquals("Landmark at index $i should have correct index property", i, landmark?.index)
        }
    }

    @Test
    fun `should return null for invalid indices`() {
        assertNull(PoseLandmark.fromIndex(-1))
        assertNull(PoseLandmark.fromIndex(33))
        assertNull(PoseLandmark.fromIndex(100))
    }

    @Test
    fun `isValidIndex should return false for out of range values`() {
        assertFalse(PoseLandmark.isValidIndex(-1))
        assertFalse(PoseLandmark.isValidIndex(33))
    }

    @Test
    fun `should verify specific key landmarks`() {
        assertEquals(PoseLandmark.NOSE, PoseLandmark.fromIndex(0))
        assertEquals(PoseLandmark.LEFT_SHOULDER, PoseLandmark.fromIndex(11))
        assertEquals(PoseLandmark.RIGHT_SHOULDER, PoseLandmark.fromIndex(12))
        assertEquals(PoseLandmark.LEFT_HIP, PoseLandmark.fromIndex(23))
        assertEquals(PoseLandmark.RIGHT_HIP, PoseLandmark.fromIndex(24))
        assertEquals(PoseLandmark.RIGHT_FOOT_INDEX, PoseLandmark.fromIndex(32))
    }

    @Test
    fun `readableName should not be empty`() {
        PoseLandmark.values().forEach {
            assertTrue(it.readableName.isNotEmpty())
        }
    }
}
