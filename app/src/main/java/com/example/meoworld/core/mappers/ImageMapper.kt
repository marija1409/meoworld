package com.example.meoworld.core.mappers

import com.example.meoworld.data.api.models.ImageApiModel
import com.example.meoworld.data.database.entities.ImageDbModel
import com.example.meoworld.segments.cats.uiModel.ImageUiModel
import kotlinx.serialization.InternalSerializationApi

@OptIn(InternalSerializationApi::class)
fun ImageApiModel.asImageDbModel(breedId: String): ImageDbModel {
    return ImageDbModel(
        id = id,
        breedId = breedId,
        url = url,
        width = width,
        height = height
    )
}

fun ImageDbModel.asImageUiModel(): ImageUiModel {
    return ImageUiModel(
        id = id,
        url = url,
        width = width,
        height = height
    )
}