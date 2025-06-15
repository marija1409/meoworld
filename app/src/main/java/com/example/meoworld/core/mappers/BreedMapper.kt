package com.example.meoworld.core.mappers

import com.example.meoworld.data.api.models.CatsApiModel
import com.example.meoworld.data.database.entities.BreedDbModel
import com.example.meoworld.features.cats.uiModel.CatUIModel
import com.example.meoworld.features.cats.uiModel.Weight

fun CatsApiModel.asBreedDbModel(): BreedDbModel {
    return BreedDbModel(
        id = id,
        name = name?: "",
        altNames = alt_names?: "",
        description = description?: "",
        temperament = temperament?: "",
        origin = origin?: "",
        lifeSpan = life_span?: "",
        weightImperial = weight?.imperial ?: "",
        weightMetric = weight?.metric ?: "",

        adaptability = adaptability ?: 0,
        affectionLevel = affection_level ?: 0,
        childFriendly = child_friendly ?: 0,
        intelligence = intelligence,
        sheddingLevel = shedding_level ?: 0,
        strangerFriendly = stranger_friendly ?: 0,
        rare = rare ?: -1,

        wikipediaUrl = wikipedia_url,
        imageUrl = image?.url ?: ""
    )
}

fun BreedDbModel.asBreedUiModel(): CatUIModel {
    return CatUIModel(
        id = id,
        name = name,
        alternativeNames = altNames,
        description = description,
        temperaments = temperament,
        origins = origin,
        lifeSpan = lifeSpan,
        weight = Weight(
            imperial = weightImperial,
            metric = weightMetric
        ),
        adaptability = adaptability,
        affectionLevel = affectionLevel,
        childFriendly = childFriendly,
        intelligence = intelligence,
        sheddingLevel = sheddingLevel,
        rare = rare,
        wikipediaUrl = wikipediaUrl,
        image = imageUrl
    )
}