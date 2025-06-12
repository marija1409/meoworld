package com.example.meoworld.features.leaderboard.screen

import com.example.meoworld.features.leaderboard.uiModel.LeaderboardUIModel


interface Leaderboard {

    data class LeaderboardState(
        val leaderboardItems: List<LeaderboardUIModel> = emptyList(),
        val fetching: Boolean = false,
        val error: ListError? = null,
    ) {
        sealed class ListError {
            data class ListUpdateFailed(val cause: Throwable? = null) : ListError()
        }
    }
}