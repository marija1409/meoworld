package com.example.meoworld.segments.user.profile

import com.example.meoworld.data.database.entities.ResultDbModel
import com.example.meoworld.data.datastore.UserData
import kotlinx.serialization.InternalSerializationApi

interface Profile {

    @OptIn(InternalSerializationApi::class)
    data class ProfileState(
        var fetchingData: Boolean = true,

        val userData: UserData = UserData(),
        val bestGlobalRank: Pair<Int, Double> = Pair(-1, -1.0),
        val totalGamesPlayed: Int = 0,
        val quizResults: List<ResultDbModel> = emptyList(),
    )
}