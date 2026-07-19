package com.caio.risesystem.ui.state

import com.caio.risesystem.domain.model.Mission
import com.caio.risesystem.domain.model.MissionStatus
import com.caio.risesystem.domain.model.Player

data class RiseUiState(
    val player: Player = Player(),
    val missions: List<Mission> = emptyList()
) {
    val completedMissions: Int
        get() = missions.count {
            it.status == MissionStatus.COMPLETED
        }

    val activeMissions: Int
        get() = missions.count {
            it.status == MissionStatus.ACTIVE
        }

    fun isMissionVisible(
        mission: Mission
    ): Boolean {
        // Missões normais sempre aparecem.
        if (mission.id != 4) {
            return true
        }

        val requiredMissionIds = setOf(1, 2, 3)

        val requiredMissionsCompleted =
            missions
                .filter { it.id in requiredMissionIds }
                .all {
                    it.status == MissionStatus.COMPLETED
                }

        val hasRequiredXp =
            player.totalXp >= 200

        return requiredMissionsCompleted &&
                hasRequiredXp
    }
}

