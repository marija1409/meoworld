package com.example.meoworld.segments.leaderboard.screen

import com.example.meoworld.segments.leaderboard.uiModel.LeaderboardUIModel


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