package com.caio.risesystem.domain.biomechanics

import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.sqrt

/**
 * Utilitário para cálculos biomecânicos.
 */
object AngleCalculator {

    data class Point(val x: Float, val y: Float)

    /**
     * Calcula o ângulo formado por três pontos (A, B, C) em graus.
     * O ponto B é o vértice do ângulo.
     * O resultado é retornado no intervalo [0, 180].
     */
    fun calculateAngle(a: Point, b: Point, c: Point): Float {
        // Vetor BA
        val baX = a.x - b.x
        val baY = a.y - b.y

        // Vetor BC
        val bcX = c.x - b.x
        val bcY = c.y - b.y

        // Produto escalar (dot product)
        val dotProduct = (baX * bcX) + (baY * bcY)

        // Magnitudes
        val magnitudeBA = sqrt((baX * baX + baY * baY).toDouble())
        val magnitudeBC = sqrt((bcX * bcX + bcY * bcY).toDouble())

        // Evitar divisão por zero se pontos forem coincidentes
        if (magnitudeBA == 0.0 || magnitudeBC == 0.0) {
            return 0f
        }

        // Cosseno do ângulo
        var cosTheta = dotProduct / (magnitudeBA * magnitudeBC)

        // Ajuste de precisão para evitar erros no acos (limitar entre -1 e 1)
        if (cosTheta > 1.0) cosTheta = 1.0
        if (cosTheta < -1.0) cosTheta = -1.0

        // Ângulo em radianos e conversão para graus
        val angleRadians = acos(cosTheta)
        return Math.toDegrees(angleRadians).toFloat()
    }
}
