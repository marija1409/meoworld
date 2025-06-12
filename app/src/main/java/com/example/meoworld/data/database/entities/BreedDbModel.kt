package com.example.meoworld.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "breeds")
data class BreedDbModel(

    @PrimaryKey val id: String,
    val name: String,
    val altNames: String,
    val description: String,
    val temperament: String,
    val origin: String,
    val lifeSpan: String,
    val weightImperial: String,
    val weightMetric: String,
    val adaptability: Int,
    val affectionLevel: Int,
    val childFriendly: Int,
    val intelligence: Int,
    val sheddingLevel: Int,
    val strangerFriendly: Int,
    val rare: Int,
    val wikipediaUrl: String,
    val imageUrl: String
) {
    fun averageWeightMetric(): Double {
        val weights = weightMetric.replace(" kg", "").split(" - ").map { it.toFloat() }
        return weights.average()
    }

    fun averageWeightImperial(): Double {
        val weights = weightImperial.replace(" lb", "").split(" - ").map { it.toFloat() }
        return weights.average()
    }
}
