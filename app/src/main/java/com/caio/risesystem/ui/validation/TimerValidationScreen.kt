package com.caio.risesystem.ui.validation

import android.os.SystemClock
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.caio.risesystem.domain.model.Mission
import kotlinx.coroutines.delay
import kotlin.math.max

private const val TIMER_TEST_MODE = true

@Composable
fun TimerValidationScreen(
    mission: Mission,
    onMissionStarted: () -> Unit,
    onMissionCompleted: () -> Unit,
    onCancel: () -> Unit
) {
    /*
     * Durante o desenvolvimento:
     * 10 minutos ou 15 minutos serão convertidos
     * temporariamente em 10 segundos.
     *
     * Depois do teste, alteraremos
     * TIMER_TEST_MODE para false.
     */
    val totalSeconds = remember(mission.id) {
        if (TIMER_TEST_MODE) {
            10L
        } else {
            mission.targetValue.toLong() * 60L
        }
    }

    var isRunning by rememberSaveable {
        mutableStateOf(false)
    }

    var isFinished by rememberSaveable {
        mutableStateOf(false)
    }

    var startedAt by rememberSaveable {
        mutableLongStateOf(0L)
    }

    var secondsRemaining by rememberSaveable {
        mutableLongStateOf(totalSeconds)
    }

    LaunchedEffect(
        isRunning,
        startedAt,
        totalSeconds
    ) {
        if (!isRunning || isFinished) {
            return@LaunchedEffect
        }

        while (isRunning && !isFinished) {
            val elapsedMilliseconds =
                SystemClock.elapsedRealtime() - startedAt

            val elapsedSeconds =
                elapsedMilliseconds / 1_000L

            secondsRemaining = max(
                0L,
                totalSeconds - elapsedSeconds
            )

            if (secondsRemaining <= 0L) {
                isRunning = false
                isFinished = true
                onMissionCompleted()
                break
            }

            delay(250L)
        }
    }

    val progress = if (totalSeconds > 0L) {
        1f -
                (
                        secondsRemaining.toFloat() /
                                totalSeconds.toFloat()
                        )
    } else {
        1f
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF020711))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "VALIDAÇÃO EM ANDAMENTO",
            color = Color(0xFF2AF5F5),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(26.dp)
        )

        Text(
            text = mission.title,
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Text(
            text = if (TIMER_TEST_MODE) {
                "MODO DE TESTE: 10 segundos"
            } else {
                mission.description
            },
            color = Color(0xFF8D96A8),
            fontSize = 15.sp,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(40.dp)
        )

        TimerCard(
            secondsRemaining = secondsRemaining,
            progress = progress,
            isRunning = isRunning,
            isFinished = isFinished
        )

        Spacer(
            modifier = Modifier.height(26.dp)
        )

        Text(
            text = when {
                isFinished ->
                    "MISSÃO VALIDADA"

                isRunning ->
                    "Permaneça na atividade até o fim."

                else ->
                    "O tempo ainda não começou."
            },
            color = when {
                isFinished ->
                    Color(0xFF28D992)

                isRunning ->
                    Color.White

                else ->
                    Color(0xFF8D96A8)
            },
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.weight(1f)
        )

        if (!isRunning && !isFinished) {
            Button(
                onClick = {
                    onMissionStarted()

                    startedAt =
                        SystemClock.elapsedRealtime()

                    secondsRemaining =
                        totalSeconds

                    isRunning = true
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2AF5F5),
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = "INICIAR CRONÔMETRO",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "CANCELAR",
                    color = Color.White
                )
            }
        }

        if (isRunning) {
            Text(
                text = "Não feche esta tela.",
                color = Color(0xFFFFC857),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }

        if (isFinished) {
            Button(
                onClick = onCancel,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF28D992),
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = "VOLTAR ÀS MISSÕES",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun TimerCard(
    secondsRemaining: Long,
    progress: Float,
    isRunning: Boolean,
    isFinished: Boolean
) {
    val minutes =
        secondsRemaining / 60L

    val seconds =
        secondsRemaining % 60L

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF071823)
        ),
        shape = RoundedCornerShape(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(26.dp),
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {
            Text(
                text = when {
                    isFinished -> "CONCLUÍDO"
                    isRunning -> "TEMPO RESTANTE"
                    else -> "PREPARADO"
                },
                color = Color(0xFF8D96A8),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = String.format(
                    "%02d:%02d",
                    minutes,
                    seconds
                ),
                color = if (isFinished) {
                    Color(0xFF28D992)
                } else {
                    Color(0xFF2AF5F5)
                },
                fontSize = 52.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(22.dp)
            )

            LinearProgressIndicator(
                progress = {
                    progress.coerceIn(
                        0f,
                        1f
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp),
                color = if (isFinished) {
                    Color(0xFF28D992)
                } else {
                    Color(0xFF2AF5F5)
                },
                trackColor = Color(0xFF1B2A34)
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.Center
            ) {
                Text(
                    text = when {
                        isFinished ->
                            "XP liberado"

                        isRunning ->
                            "Validação ativa"

                        else ->
                            "Aguardando início"
                    },
                    color = Color(0xFF8D96A8),
                    fontSize = 14.sp
                )
            }
        }
    }
}

