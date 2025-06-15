package com.example.meoworld.features.user

import android.util.Log
import com.example.meoworld.data.database.AppDatabase
import com.example.meoworld.data.database.entities.ResultDbModel
import com.example.meoworld.data.datastore.UserData
import com.example.meoworld.data.datastore.UserStore
import com.example.meoworld.features.leaderboard.LeaderboardRepo
import kotlinx.serialization.InternalSerializationApi
import javax.inject.Inject

class UserRepo @Inject constructor(
    private val lbRepository: LeaderboardRepo,
    private val database: AppDatabase,
    private val store: UserStore
){

    @OptIn(InternalSerializationApi::class)
    suspend fun getUserData() = store.getUserData()

    @OptIn(InternalSerializationApi::class)
    suspend fun getBestGlobalRank(): Pair<Int, Double> {
        if (database.leaderboardDao().getAll().isEmpty())
            lbRepository.fetchLeaderboard()
        val nickname = store.getUserData().nickname
        return database.leaderboardDao().getBestGlobalRank(nickname)
    }

    suspend fun registerUser(userData: UserData) {
        Log.d("USER REPO", "User repository - registering user: $userData")
        store.setData(userData)
    }

    suspend fun clearUserData() {
        store.deleteUser()
    }

    suspend fun getUserResults(): List<ResultDbModel> {
        val nickname = store.getUserData().nickname
        return database.resultDao().getUserResults(nickname)
    }


}