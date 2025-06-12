package com.example.meoworld.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "results")
data class ResultDbModel (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nickname: String,
    val result: Double,
    val createdAt: Long,
    var published: Boolean = false
)