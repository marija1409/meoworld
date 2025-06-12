package com.example.meoworld.networking.di

import com.example.meoworld.data.api.CatsApi
import com.example.meoworld.data.api.LeaderboardApi
import com.example.meoworld.networking.urls.CatApiUrl
import com.example.meoworld.networking.urls.LeaderboardApiUrl
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor {
                val updatedRequest = it.request().newBuilder()
                    .addHeader("CustomHeader", "CustomValue")
                    .addHeader("x-api-key", "live_Ouzr56q1jEfpak0yBCxLNfOWKkj2QsIauzeYpasboh04Nb96LQIbGsgIVHuX5Me8")
                    .build()
                it.proceed(updatedRequest)
            }
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()



    @Provides
    @Singleton
    @Named("CatApiRetrofit")
    fun provideCatApiRetrofit(okHttpClient: OkHttpClient, @CatApiUrl baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    @Named("LeaderboardApiRetrofit")
    fun provideLeaderboardApiRetrofit(okHttpClient: OkHttpClient, @LeaderboardApiUrl baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }



    @Provides
    @Singleton
    fun provideBreedsRetrofit(@Named("CatApiRetrofit") retrofit: Retrofit): CatsApi = retrofit.create()

    @Provides
    @Singleton
    fun provideLeaderboardRetrofit(@Named("LeaderboardApiRetrofit") retrofit: Retrofit): LeaderboardApi = retrofit.create()


}
