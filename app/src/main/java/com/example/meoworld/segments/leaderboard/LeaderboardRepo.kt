package com.example.meoworld.segments.leaderboard

import com.example.meoworld.data.api.LeaderboardApi
import com.example.meoworld.data.api.models.LeaderboardApiModel
import com.example.meoworld.data.api.models.SubmitResultRequest
import com.example.meoworld.data.database.AppDatabase
import com.example.meoworld.data.database.entities.LBItemDbModel
import com.example.meoworld.data.datastore.UserStore
import com.example.meoworld.segments.leaderboard.uiModel.LeaderboardUIModel
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

    /**
     * We should submit the result to the API.
     * And change the last entry in the leaderboard table (published -> true)
     */
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

        // Retrieve the last entry in the leaderboard table
        val lastEntry = database.resultDao().getLastEntry()

        lastEntry.let {
            it.published = true
            database.resultDao().update(it)
        }
    }

    /** Returns Flow which holds all leaderboard items.
     * The items are sorted by result in descending order.
     * Does not have a categoryId since this app will support only 1 category of the quiz.
     */
    fun observeLeaderboard(): Flow<List<LBItemDbModel>> {
        return database.leaderboardDao().observeAll()
    }
}

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