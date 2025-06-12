package com.example.meoworld.data.api

import com.example.meoworld.data.api.models.LeaderboardApiModel
import com.example.meoworld.data.api.models.SubmitResultRequest
import com.example.meoworld.data.api.models.SubmitResultResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface LeaderboardApi {
    @GET("leaderboard")
    suspend fun getLeaderboard(
        @Query("category") categoryId: Int,
    ): List<LeaderboardApiModel>

    @POST("leaderboard")
    suspend fun postLeaderboard(
        @Body leaderboard: SubmitResultRequest
    ): SubmitResultResponse
}