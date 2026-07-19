package com.caio.risesystem.domain.model

data class Player(
    val name: String = "Caio",
    val level: Int = 1,
    val rank: String = "E",
    val currentXp: Int = 0,
    val xpToNextLevel: Int = 500,
    val totalXp: Int = 0,
    val currentStreak: Int = 0
)

