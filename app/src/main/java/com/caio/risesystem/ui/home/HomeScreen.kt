package com.caio.risesystem.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.caio.risesystem.domain.model.Mission
import com.caio.risesystem.domain.model.MissionStatus
import com.caio.risesystem.domain.model.Player

private val BackgroundColor = Color(0xFF020711)
private val CardColor = Color(0xFF071823)
private val CyanColor = Color(0xFF2AF5F5)
private val GreenColor = Color(0xFF28D992)
private val SecondaryText = Color(0xFF8D96A8)

@Composable
fun HomeScreen(
    player: Player,
    missions: List<Mission>,
    isMissionVisible: (Mission) -> Boolean,
    onOpenValidation: (Int) -> Unit,
    onCompleteMission: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = 20.dp,
                vertical = 24.dp
            )
    ) {
        Text(
            text = "RISE SYSTEM",
            color = CyanColor,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 3.sp
        )

        Text(
            text = "Evolua. Supere seus limites.",
            color = SecondaryText,
            fontSize = 16.sp
        )

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        PlayerCard(
            player = player
        )

        Spacer(
            modifier = Modifier.height(30.dp)
        )

        Text(
            text = "MISSÕES DE HOJE",
            color = Color.White,
            fontSize = 21.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 2.sp
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        missions
            .filter { mission ->
                isMissionVisible(mission)
            }
            .forEach { mission ->
                MissionCard(
                    mission = mission,
                    onStart = {
                        onOpenValidation(mission.id)
                    },
                    onComplete = {
                        onCompleteMission(mission.id)
                    }
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )
            }

        Spacer(
            modifier = Modifier.height(24.dp)
        )
    }
}

@Composable
private fun PlayerCard(
    player: Player
) {
    val progress = if (player.xpToNextLevel > 0) {
        player.currentXp.toFloat() /
                player.xpToNextLevel.toFloat()
    } else {
        0f
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = CardColor
        ),
        shape = RoundedCornerShape(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.SpaceBetween,
                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = player.name.uppercase(),
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text =
                            "CAÇADOR • NÍVEL ${player.level}",
                        color = SecondaryText,
                        fontSize = 13.sp
                    )
                }

                Text(
                    text = "RANK ${player.rank}",
                    color = CyanColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(
                modifier = Modifier.height(22.dp)
            )

            LinearProgressIndicator(
                progress = {
                    progress.coerceIn(0f, 1f)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = CyanColor,
                trackColor = Color(0xFF1B2A34)
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text =
                    "${player.currentXp} / " +
                            "${player.xpToNextLevel} XP",
                color = SecondaryText,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun MissionCard(
    mission: Mission,
    onStart: () -> Unit,
    onComplete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = CardColor
        ),
        shape = RoundedCornerShape(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalArrangement =
                Arrangement.SpaceBetween,
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = mission.title,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = mission.description,
                    color = SecondaryText,
                    fontSize = 14.sp
                )

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                Text(
                    text = "+${mission.xpReward} XP",
                    color = CyanColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            when (mission.status) {
                MissionStatus.PENDING -> {
                    Button(
                        onClick = onStart,
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = CyanColor,
                                contentColor = Color.Black
                            ),
                        shape =
                            RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "INICIAR",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                MissionStatus.ACTIVE -> {
                    Button(
                        onClick = onComplete,
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = GreenColor,
                                contentColor = Color.Black
                            ),
                        shape =
                            RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "CONCLUIR",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                MissionStatus.COMPLETED -> {
                    Text(
                        text = "CONCLUÍDA",
                        color = GreenColor,
                        fontWeight = FontWeight.Bold
                    )
                }

                MissionStatus.FAILED -> {
                    Text(
                        text = "FALHOU",
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
