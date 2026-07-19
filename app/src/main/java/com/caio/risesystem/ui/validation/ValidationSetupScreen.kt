package com.caio.risesystem.ui.validation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.caio.risesystem.domain.model.Mission
import com.caio.risesystem.domain.model.ValidationType

@Composable
fun ValidationSetupScreen(
    mission: Mission,
    onBack: () -> Unit,
    onStartValidation: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF020711))
            .padding(24.dp)
    ) {
        Text(
            text = "PREPARAÇÃO",
            color = Color(0xFF2AF5F5),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 3.sp
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Text(
            text = mission.title,
            color = Color.White,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Text(
            text = mission.description,
            color = Color(0xFF8D96A8),
            fontSize = 16.sp
        )

        Spacer(
            modifier = Modifier.height(26.dp)
        )

        Text(
            text = "META",
            color = Color(0xFF8D96A8),
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text =
                "${mission.targetValue} " +
                        mission.targetUnit,
            color = Color(0xFF2AF5F5),
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        Text(
            text = validationInstructions(
                mission.validationType
            ),
            color = Color.White,
            fontSize = 16.sp
        )

        Spacer(
            modifier = Modifier.weight(1f)
        )

        Button(
            onClick = onStartValidation,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2AF5F5),
                contentColor = Color.Black
            )
        ) {
            Text(
                text = "COMEÇAR VALIDAÇÃO",
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "VOLTAR",
                color = Color.White
            )
        }
    }
}

private fun validationInstructions(
    validationType: ValidationType
): String {
    return when (validationType) {
        ValidationType.CAMERA_REPS ->
            "Coloque o celular em uma posição estável, " +
                    "com boa iluminação e espaço suficiente " +
                    "para que seu corpo inteiro apareça."

        ValidationType.GPS_DISTANCE ->
            "Ative a localização e vá para uma área " +
                    "aberta. A missão será validada pela " +
                    "distância registrada pelo GPS."

        ValidationType.TIMER ->
            "O cronômetro deverá permanecer ativo até " +
                    "que o tempo mínimo seja concluído."

        ValidationType.MANUAL ->
            "Esta missão ainda utiliza confirmação manual."
    }
}