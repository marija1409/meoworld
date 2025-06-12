package com.example.meoworld.data.api.models

import kotlinx.serialization.Serializable

@Serializable
data class CatsApiModel(
    val id: String,
    val name: String,
    val temperament: String,
    val alt_names: String = "",
    val origin: String,
    val description: String,
    val life_span: String,
    val adaptability: Int,
    val affection_level: Int,
    val child_friendly: Int,
    val dog_friendly: Int,
    val energy_level: Int,
    val grooming: Int,
    val health_issues: Int,
    val intelligence: Int,
    val shedding_level: Int,
    val social_needs: Int,
    val stranger_friendly: Int,
    val vocalisation: Int,
    val rare: Int,
    val wikipedia_url: String = "",
    val country_codes: String? = null,
    val weight: CatWeight,
    val reference_image_id: String? = null,
    val image: ImageApiModel? = null
)

@Serializable
data class ImageApiModel(
    val id: String,
    val url: String,
    val width: Int,
    val height: Int
)

@Serializable
data class CatWeight(
    val imperial: String,
    val metric: String
)

