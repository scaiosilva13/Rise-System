package com.caio.risesystem.ui.today

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.caio.risesystem.domain.model.DailyTaskStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    taskId: Long,
    viewModel: TodayViewModel,
    onBack: () -> Unit
) {
    val tasks by viewModel.uiState.collectAsState()
    val task = tasks.tasks.find { it.id == taskId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("DETALHES", letterSpacing = 2.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF020711),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = Color(0xFF020711)
    ) { innerPadding ->
        if (task == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF2AF5F5))
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = task.title,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = task.suggestedTime,
                        color = Color(0xFF2AF5F5),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = " • ",
                        color = Color(0xFF8D96A8)
                    )
                    Text(
                        text = task.type.toReadableString(),
                        color = Color(0xFF8D96A8),
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // STATUS SELECTOR
                Surface(
                    color = Color(0xFF071823),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            text = "STATUS ATUAL",
                            color = Color(0xFF8D96A8),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        StatusSelector(
                            currentStatus = task.status,
                            onStatusChange = { newStatus ->
                                viewModel.updateTaskStatus(task.id, newStatus)
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // CONTENT
                if (task.details.isNotEmpty()) {
                    task.details.split("\n\n").forEach { block ->
                        val lines = block.trim().split("\n")
                        if (lines.isNotEmpty()) {
                            val header = lines[0].trim()
                            
                            Text(
                                text = header,
                                color = Color(0xFF2AF5F5),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Surface(
                                color = Color(0xFF030A14),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(Modifier.padding(16.dp)) {
                                    lines.drop(1).forEach { line ->
                                        Text(
                                            text = line.trim(),
                                            color = Color.White,
                                            fontSize = 15.sp,
                                            lineHeight = 22.sp,
                                            modifier = Modifier.padding(vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                } else {
                    Text(
                        text = "Sem instruções adicionais para esta tarefa.",
                        color = Color(0xFF8D96A8),
                        fontSize = 14.sp
                    )
                }
                
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}
