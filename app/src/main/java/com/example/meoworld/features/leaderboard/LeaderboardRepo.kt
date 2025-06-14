package com.example.meoworld.features.leaderboard

import com.example.meoworld.data.api.LeaderboardApi
import com.example.meoworld.data.api.models.LeaderboardApiModel
import com.example.meoworld.data.api.models.SubmitResultRequest
import com.example.meoworld.data.database.AppDatabase
import com.example.meoworld.data.database.entities.LBItemDbModel
import com.example.meoworld.data.datastore.UserStore
import com.example.meoworld.features.leaderboard.uiModel.LeaderboardUIModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LeaderboardRepo @Inject constructor(
    private val leaderboardApi: LeaderboardApi,
    private val database: AppDatabase,
    private val store: UserStore
){

    suspend fun fetchLeaderboard(categoryId: Int = 1) {
        withContext(Dispatchers.IO) {
            val lbItems = leaderboardApi.getLeaderboard(categoryId)
            database.leaderboardDao().deleteAll()
            database.leaderboardDao().insertAll(lbItems.map { it.asLBItemDbModel(lbItems) })
        }
    }

    suspend fun submitQuizResult(categoryId: Int = 1, result: Double) {
        withContext(Dispatchers.IO) {
            leaderboardApi.postLeaderboard(
                SubmitResultRequest(
                    nickname = store.getUserData().nickname,
                    result = result,
                    category = categoryId
                )
            )
        }

        val lastEntry = database.resultDao().getLastEntry()

        lastEntry.let {
            it.published = true
            database.resultDao().update(it)
        }
    }

    fun observeLeaderboard(): Flow<List<LBItemDbModel>> {
        return database.leaderboardDao().observeAll()
    }
}

fun LeaderboardApiModel.asLBItemDbModel(
    allResults: List<LeaderboardApiModel>
): LBItemDbModel {

    val totalGamesPlayed = allResults.count { it.nickname == this.nickname }

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