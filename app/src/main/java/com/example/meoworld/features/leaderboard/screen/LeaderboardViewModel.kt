package com.example.meoworld.features.leaderboard.screen

import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meoworld.core.mappers.asLBItemUiModel
import com.example.meoworld.features.leaderboard.LeaderboardRepo
import com.example.meoworld.features.leaderboard.screen.Leaderboard.LeaderboardState
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val repository: LeaderboardRepo
): ViewModel() {

    private val _state = MutableStateFlow(LeaderboardState())
    val state = _state.asStateFlow()

    private fun setState(reducer: LeaderboardState.() -> LeaderboardState) = _state.update(reducer)

    init {
        observeLeaderboard()
        fetchLeaderboard()
    }

    private fun observeLeaderboard() {
        viewModelScope.launch {
            repository.observeLeaderboard().collect {
                setState { copy(
                    leaderboardItems = it.map { it.asLBItemUiModel() }
                )
                }
            }
        }
    }

    private fun fetchLeaderboard() {
        viewModelScope.launch {
            setState { copy(fetching = true) }
            try {
                repository.fetchLeaderboard()
            }  catch (error: IOException) {
                setState { copy(error = LeaderboardState.ListError.ListUpdateFailed(cause = error)) }
            } finally {
                setState { copy(fetching = false) }
            }
        }
    }
}