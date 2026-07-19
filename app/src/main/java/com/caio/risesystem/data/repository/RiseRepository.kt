package com.caio.risesystem.data.repository

import androidx.room.withTransaction
import com.caio.risesystem.data.local.RiseDatabase
import com.caio.risesystem.domain.model.Mission
import com.caio.risesystem.domain.model.MissionCategory
import com.caio.risesystem.domain.model.MissionStatus
import com.caio.risesystem.domain.model.Player
import com.caio.risesystem.ui.state.RiseUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import com.caio.risesystem.domain.progression.ProgressionCalculator
import com.caio.risesystem.domain.model.ValidationType

class RiseRepository(
    private val database: RiseDatabase
) {
    private val missionDao = database.missionDao()
    private val playerDao = database.playerDao()

    val uiState: Flow<RiseUiState> =
        combine(
            playerDao.observePlayer(),
            missionDao.observeAllMissions()
        ) { playerEntity, missionEntities ->

            RiseUiState(
                player = playerEntity?.toDomain() ?: Player(),
                missions = missionEntities.map {
                    it.toDomain()
                }
            )
        }

    suspend fun initializeDatabase() {
        database.withTransaction {
            if (playerDao.getPlayer() == null) {
                playerDao.savePlayer(
                    Player(
                        name = "Caio",
                        level = 1,
                        rank = "E",
                        currentXp = 0,
                        xpToNextLevel = 500,
                        totalXp = 0,
                        currentStreak = 0
                    ).toEntity()
                )
            }

            if (missionDao.countMissions() == 0) {
                missionDao.insertAll(
                    initialMissions().map {
                        it.toEntity()
                    }
                )
            }
        }
    }

    suspend fun startMission(
        missionId: Int
    ) {
        database.withTransaction {
            val mission =
                missionDao.getMissionById(missionId)
                    ?: return@withTransaction

            if (missionId == 4) {
                val requiredMissionIds =
                    listOf(1, 2, 3)

                val requiredMissionsCompleted =
                    requiredMissionIds.all { requiredId ->
                        missionDao
                            .getMissionById(requiredId)
                            ?.status ==
                                MissionStatus.COMPLETED.name
                    }

                val player =
                    playerDao.getPlayer()
                        ?: return@withTransaction

                val hasRequiredXp =
                    player.totalXp >= 200

                if (
                    !requiredMissionsCompleted ||
                    !hasRequiredXp
                ) {
                    return@withTransaction
                }
            }

            if (
                mission.status !=
                MissionStatus.PENDING.name
            ) {
                return@withTransaction
            }

            missionDao.updateStatus(
                missionId = missionId,
                newStatus = MissionStatus.ACTIVE.name
            )
        }
    }

    suspend fun cancelMission(
        missionId: Int
    ) {
        val mission =
            missionDao.getMissionById(
                missionId
            ) ?: return

        if (
            mission.status ==
            MissionStatus.ACTIVE.name
        ) {
            missionDao.updateStatus(
                missionId = missionId,
                newStatus =
                    MissionStatus.PENDING.name
            )
        }
    }

    suspend fun completeMission(
        missionId: Int
    ) {
        database.withTransaction {
            val mission =
                missionDao.getMissionById(missionId)
                    ?: return@withTransaction

            if (
                mission.status ==
                MissionStatus.COMPLETED.name
            ) {
                return@withTransaction
            }

            val playerEntity =
                playerDao.getPlayer()
                    ?: return@withTransaction

            val player = playerEntity.toDomain()

            val progression =
                ProgressionCalculator.addXp(
                    player = player,
                    receivedXp = mission.xpReward
                )

            missionDao.updateStatus(
                missionId = missionId,
                newStatus = MissionStatus.COMPLETED.name
            )

            playerDao.savePlayer(
                playerEntity.copy(
                    level = progression.level,
                    rank = progression.rank,
                    currentXp = progression.currentXp,
                    xpToNextLevel =
                        progression.xpToNextLevel,
                    totalXp =
                        playerEntity.totalXp +
                                mission.xpReward
                )
            )
        }
    }

    private fun initialMissions(): List<Mission> {
        return listOf(
            Mission(
                id = 1,
                title = "Flexões",
                description = "Complete 10 repetições",
                category = MissionCategory.STRENGTH,
                xpReward = 100,
                validationType =
                    ValidationType.CAMERA_REPS,
                targetValue = 10,
                targetUnit = "repetições"
            ),
            Mission(
                id = 2,
                title = "Mobilidade",
                description = "Complete 10 minutos",
                category = MissionCategory.MOBILITY,
                xpReward = 50,
                validationType =
                    ValidationType.TIMER,
                targetValue = 10,
                targetUnit = "minutos"
            ),
            Mission(
                id = 3,
                title = "Leitura",
                description = "Leia durante 15 minutos",
                category = MissionCategory.MENTAL,
                xpReward = 50,
                validationType =
                    ValidationType.TIMER,
                targetValue = 15,
                targetUnit = "minutos"
            ),
            Mission(
                id = 4,
                title = "Provação do Despertar",
                description =
                    "Complete o desafio para provar sua evolução",
                category = MissionCategory.STRENGTH,
                xpReward = 600,
                validationType =
                    ValidationType.CAMERA_REPS,
                targetValue = 20,
                targetUnit = "repetições"
            )
        )

    }

}

