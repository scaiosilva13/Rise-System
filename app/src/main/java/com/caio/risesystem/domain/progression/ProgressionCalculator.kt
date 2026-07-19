package com.caio.risesystem.domain.progression

import com.caio.risesystem.domain.model.Player
import com.caio.risesystem.domain.model.ProgressionResult

object ProgressionCalculator {

    fun addXp(
        player: Player,
        receivedXp: Int
    ): ProgressionResult {

        require(receivedXp >= 0) {
            "O XP recebido não pode ser negativo."
        }

        var currentLevel = player.level
        var currentXp = player.currentXp + receivedXp
        var requiredXp = player.xpToNextLevel
        var levelsGained = 0

        while (currentXp >= requiredXp) {
            currentXp -= requiredXp
            currentLevel++
            levelsGained++

            requiredXp = requiredXpForLevel(
                level = currentLevel
            )
        }

        return ProgressionResult(
            level = currentLevel,
            rank = rankForLevel(currentLevel),
            currentXp = currentXp,
            xpToNextLevel = requiredXp,
            levelsGained = levelsGained
        )
    }

    fun requiredXpForLevel(
        level: Int
    ): Int {
        val safeLevel = level.coerceAtLeast(1)

        return 500 + ((safeLevel - 1) * 150)
    }

    fun rankForLevel(
        level: Int
    ): String {
        return when {
            level >= 25 -> "S"
            level >= 20 -> "A"
            level >= 15 -> "B"
            level >= 10 -> "C"
            level >= 5 -> "D"
            else -> "E"
        }
    }
}

