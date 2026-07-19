package com.caio.risesystem.domain.model

data class ProgressionResult(
    val level: Int,
    val rank: String,
    val currentXp: Int,
    val xpToNextLevel: Int,
    val levelsGained: Int
)

