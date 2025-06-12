package com.example.meoworld.data.database.entities

import androidx.room.Entity

@Entity(
    tableName = "leaderboard",
    primaryKeys = ["nickname", "result", "createdAt"]
)
data class LBItemDbModel (
    val nickname: String,
    val result: Double,
    val totalGamesPlayed: Int,
    val createdAt: Long
)