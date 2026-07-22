package com.caio.risesystem.ui.today

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreTime
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.caio.risesystem.domain.model.DailyTask
import com.caio.risesystem.domain.model.DailyTaskStatus
import com.caio.risesystem.domain.model.DailyTaskType

@Composable
fun TodayScreen(
    viewModel: TodayViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF020711))
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "HOJE",
            color = Color(0xFF2AF5F5),
            fontSize = 32.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 2.sp
        )
        
        Text(
            text = uiState.dateDisplay,
            color = Color(0xFF8D96A8),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(24.dp))

        AdherenceCard(
            percentage = uiState.percentage,
            completed = uiState.completedCount,
            total = uiState.totalCount,
            classification = uiState.classification
        )

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 24.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(uiState.tasks, key = { it.id }) { task ->
                TaskItem(
                    task = task,
                    onStatusChange = { newStatus ->
                        viewModel.updateTaskStatus(task.id, newStatus)
                    }
                )
            }
        }
    }
}

@Composable
fun AdherenceCard(
    percentage: Int,
    completed: Int,
    total: Int,
    classification: String
) {
    Surface(
        color = Color(0xFF071823),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Adesão Diária",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = classification,
                        color = when {
                            percentage >= 90 -> Color(0xFF2ADFA6)
                            percentage >= 70 -> Color(0xFFFFC857)
                            else -> Color(0xFFFF5252)
                        },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        progress = { percentage / 100f },
                        modifier = Modifier.size(60.dp),
                        color = Color(0xFF2AF5F5),
                        strokeWidth = 6.dp,
                        trackColor = Color(0xFF1E2A37)
                    )
                    Text(
                        text = "$percentage%",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            LinearProgressIndicator(
                progress = { if (total > 0) completed.toFloat() / total else 0f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape),
                color = Color(0xFF2AF5F5),
                trackColor = Color(0xFF1E2A37)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "$completed de $total tarefas concluídas",
                color = Color(0xFF8D96A8),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun TaskItem(
    task: DailyTask,
    onStatusChange: (DailyTaskStatus) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Surface(
        color = Color(0xFF030A14),
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showMenu = true }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = when (task.status) {
                            DailyTaskStatus.COMPLETED -> Color(0xFF2ADFA6).copy(alpha = 0.2f)
                            DailyTaskStatus.NOT_COMPLETED -> Color(0xFFFF5252).copy(alpha = 0.2f)
                            else -> Color(0xFF1E2A37)
                        },
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (task.status) {
                        DailyTaskStatus.COMPLETED -> Icons.Default.Check
                        DailyTaskStatus.NOT_COMPLETED -> Icons.Default.Close
                        else -> Icons.Default.MoreTime
                    },
                    contentDescription = null,
                    tint = when (task.status) {
                        DailyTaskStatus.COMPLETED -> Color(0xFF2ADFA6)
                        DailyTaskStatus.NOT_COMPLETED -> Color(0xFFFF5252)
                        else -> Color(0xFF667080)
                    },
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = task.suggestedTime,
                        color = Color(0xFF8D96A8),
                        fontSize = 13.sp
                    )
                    Text(
                        text = " • ",
                        color = Color(0xFF8D96A8),
                        fontSize = 13.sp
                    )
                    Text(
                        text = task.type.toReadableString(),
                        color = Color(0xFF2AF5F5),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            StatusSelector(
                currentStatus = task.status,
                onStatusChange = onStatusChange
            )
        }
    }
}

@Composable
fun StatusSelector(
    currentStatus: DailyTaskStatus,
    onStatusChange: (DailyTaskStatus) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        StatusButton(
            icon = Icons.Default.MoreTime,
            isSelected = currentStatus == DailyTaskStatus.PENDING,
            color = Color(0xFF667080),
            onClick = { onStatusChange(DailyTaskStatus.PENDING) }
        )
        StatusButton(
            icon = Icons.Default.Check,
            isSelected = currentStatus == DailyTaskStatus.COMPLETED,
            color = Color(0xFF2ADFA6),
            onClick = { onStatusChange(DailyTaskStatus.COMPLETED) }
        )
        StatusButton(
            icon = Icons.Default.Close,
            isSelected = currentStatus == DailyTaskStatus.NOT_COMPLETED,
            color = Color(0xFFFF5252),
            onClick = { onStatusChange(DailyTaskStatus.NOT_COMPLETED) }
        )
    }
}

@Composable
fun StatusButton(
    icon: ImageVector,
    isSelected: Boolean,
    color: Color,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(32.dp)
            .background(
                color = if (isSelected) color.copy(alpha = 0.2f) else Color.Transparent,
                shape = CircleShape
            )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) color else color.copy(alpha = 0.4f),
            modifier = Modifier.size(18.dp)
        )
    }
}
