package com.example.meoworld.segments.cats.uiModel

data class CatUIModel (
    val id: String,
    val name: String,
    val alternativeNames: String,
    val description: String,
    val temperaments: String,


    val origins: String,
    val lifeSpan: String,
    val weight: Weight,
    val adaptability: Int,
    val affectionLevel: Int,
    val childFriendly: Int,
    val intelligence: Int,
    val sheddingLevel: Int,
    val rare: Int,
    val wikipediaUrl: String,


    val image: String,
)

data class Weight(
    val imperial: String,
    val metric: String
)