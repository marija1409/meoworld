package com.example.meoworld.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "images",
    foreignKeys = [
        ForeignKey(
            entity = BreedDbModel::class,
            parentColumns = ["id"],
            childColumns = ["breedId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ImageDbModel (
    @PrimaryKey val id: String,
    val url: String,
    val breedId: String,
    val width: Int,
    val height: Int,
) {
    init {
        require(url.isNotBlank()) { "URL must not be blank" }
    }
}