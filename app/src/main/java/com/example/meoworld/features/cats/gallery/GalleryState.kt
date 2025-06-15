package com.example.meoworld.features.cats.gallery

import com.example.meoworld.features.cats.uiModel.ImageUiModel

data class GalleryState (
    val breedId: String,
    val fetching: Boolean = false,
    val error: GalleryError? = null,

    val images: List<ImageUiModel> = emptyList()
) {
    sealed class GalleryError {
        data class GalleryFailed(val cause: Throwable? = null) : GalleryError()
    }
}