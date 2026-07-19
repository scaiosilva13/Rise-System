package com.caio.risesystem.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caio.risesystem.data.repository.RiseRepository
import com.caio.risesystem.ui.state.RiseUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RiseViewModel(
    private val repository: RiseRepository
) : ViewModel() {

    val uiState: StateFlow<RiseUiState> =
        repository.uiState.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(
                stopTimeoutMillis = 5_000
            ),
            initialValue = RiseUiState()
        )

    init {
        viewModelScope.launch {
            repository.initializeDatabase()
        }
    }

    fun startMission(
        missionId: Int
    ) {
        viewModelScope.launch {
            repository.startMission(missionId)
        }
    }

    fun completeMission(
        missionId: Int
    ) {
        viewModelScope.launch {
            repository.completeMission(missionId)
        }
    }

    fun cancelMission(
        missionId: Int
    ) {
        viewModelScope.launch {
            repository.cancelMission(
                missionId
            )
        }
    }
}

