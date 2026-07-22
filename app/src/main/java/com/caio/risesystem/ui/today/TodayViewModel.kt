package com.caio.risesystem.ui.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.caio.risesystem.domain.adherence.DailyAdherenceCalculator
import com.caio.risesystem.domain.model.DailyTaskStatus
import com.caio.risesystem.domain.repository.DailyTaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class TodayViewModel(
    private val repository: DailyTaskRepository
) : ViewModel() {

    private val currentDate = LocalDate.now()
    private val dateIso = currentDate.toString() // YYYY-MM-DD

    private val _uiState = MutableStateFlow(TodayUiState(
        dateDisplay = currentDate.format(
            DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM", Locale("pt", "BR"))
                .withLocale(Locale("pt", "BR"))
        ).replaceFirstChar { it.uppercase() }
    ))
    
    val uiState: StateFlow<TodayUiState> = repository.observeTasksByDate(dateIso)
        .map { tasks ->
            val adherence = DailyAdherenceCalculator.calculate(tasks)
            _uiState.value.copy(
                tasks = tasks,
                completedCount = adherence.completedCount,
                totalCount = adherence.totalCount,
                percentage = adherence.percentage,
                classification = adherence.classification
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = _uiState.value
        )

    init {
        viewModelScope.launch {
            repository.initializeTasksForDate(dateIso)
        }
    }

    fun updateTaskStatus(id: Long, status: DailyTaskStatus) {
        viewModelScope.launch {
            repository.updateTaskStatus(id, status)
        }
    }
}

class TodayViewModelFactory(
    private val repository: DailyTaskRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodayViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TodayViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
