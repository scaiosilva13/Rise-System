package com.caio.risesystem.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.caio.risesystem.domain.model.Player

@Composable
fun ProfileScreen(player: Player) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF020711))
            .padding(20.dp)
    ) {
        Text(
            text = "PERFIL",
            color = Color.White,
            fontSize = 29.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 3.sp
        )

        Spacer(modifier = Modifier.height(26.dp))

        Text(
            text = player.name,
            color = Color.White,
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Rank ${player.rank} • Nível ${player.level}",
            color = Color(0xFF2AF5F5),
            fontSize = 17.sp
        )

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Objetivo principal",
            color = Color(0xFF8D96A8),
            fontSize = 14.sp
        )

        Text(
            text = "Emagrecimento, força e desempenho",
            color = Color.White,
            fontSize = 17.sp
        )
    }
}


