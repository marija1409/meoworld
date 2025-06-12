package com.example.meoworld.core.mappers

import com.example.meoworld.data.api.models.LeaderboardApiModel
import com.example.meoworld.data.database.entities.LBItemDbModel
import com.example.meoworld.segments.leaderboard.uiModel.LeaderboardUIModel
import kotlinx.serialization.InternalSerializationApi

@OptIn(InternalSerializationApi::class)
fun LeaderboardApiModel.asLBItemDbModel(
    allResults: List<LeaderboardApiModel>
): LBItemDbModel {

    val totalGamesPlayed = allResults.count { it.nickname == this.nickname }

    // id is automatically generated
    return LBItemDbModel(
        nickname = nickname,
        result = result,
        totalGamesPlayed = totalGamesPlayed,
        createdAt = createdAt
    )
}

fun LBItemDbModel.asLBItemUiModel(): LeaderboardUIModel {
    return LeaderboardUIModel(
        nickname = nickname,
        result = result,
        totalGamesPlayed = totalGamesPlayed,
    )
}