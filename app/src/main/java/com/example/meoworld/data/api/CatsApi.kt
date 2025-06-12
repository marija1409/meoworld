package com.example.meoworld.data.api

import com.example.meoworld.data.api.models.CatsApiModel
import com.example.meoworld.data.api.models.ImageApiModel
import retrofit2.http.GET
import retrofit2.http.Query

interface CatsApi {
    @GET("breeds")
    suspend fun getAllBreeds(): List<CatsApiModel>

    @GET("images/search")
    suspend fun getImagesForBreed(
        @Query("breed_ids") breedId: String,
        @Query("limit") limit: Int = 20
    ): List<ImageApiModel>

}