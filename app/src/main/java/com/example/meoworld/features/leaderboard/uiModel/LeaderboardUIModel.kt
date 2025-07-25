package com.example.meoworld.features.leaderboard.uiModel

data class LeaderboardUIModel(
    val ranking: Int = -1,
    val nickname: String = "",
    val result: Double = 0.0,
    val totalGamesPlayed: Int = 0,
)