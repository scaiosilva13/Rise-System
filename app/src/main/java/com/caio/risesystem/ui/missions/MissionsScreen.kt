package com.caio.risesystem.ui.missions

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

@Composable
fun MissionsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF020711))
            .padding(20.dp)
    ) {
        Text(
            text = "MISSÕES",
            color = Color.White,
            fontSize = 29.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 3.sp
        )

        Text(
            text = "Complete suas ordens diárias",
            color = Color(0xFF8D96A8),
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "3 missões ativas",
            color = Color(0xFF2AF5F5),
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "A lista completa e o controle das missões serão implementados na próxima etapa.",
            color = Color(0xFF8D96A8),
            fontSize = 15.sp
        )
    }
}

