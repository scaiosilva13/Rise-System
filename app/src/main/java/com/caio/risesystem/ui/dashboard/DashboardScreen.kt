package com.caio.risesystem.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.caio.risesystem.domain.model.Player

@Composable
fun DashboardScreen(player: Player,
                    completedMissions: Int) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF020711))
            .padding(20.dp)
    ) {
        Text(
            text = "DASHBOARD",
            color = Color.White,
            fontSize = 29.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 3.sp
        )

        Text(
            text = "Acompanhe sua evolução",
            color = Color(0xFF8D96A8),
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(28.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatisticCard(
                value = player.currentStreak.toString(),
                label = "Sequência",
                modifier = Modifier.weight(1f)
            )

            StatisticCard(
                value = completedMissions.toString(),
                label = "Concluídas",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatisticCard(
                value = player.totalXp.toString(),
                label = "XP Total",
                modifier = Modifier.weight(1f)
            )

            StatisticCard(
                value = player.rank,
                label = "Rank Atual",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatisticCard(
                value = player.level.toString(),
                label = "Nível Atual",
                modifier = Modifier.weight(1f)
            )

            StatisticCard(
                value = "${player.currentXp}/${player.xpToNextLevel}",
                label = "XP do Nível",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun StatisticCard(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF071823)
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = value,
                color = Color(0xFF2AF5F5),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = label,
                color = Color(0xFF8D96A8),
                fontSize = 15.sp
            )
        }
    }
}