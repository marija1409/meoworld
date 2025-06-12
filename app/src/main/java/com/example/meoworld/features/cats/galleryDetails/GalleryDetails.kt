package com.example.meoworld.features.cats.galleryDetails

import com.example.meoworld.features.cats.uiModel.ImageUiModel


interface GalleryDetails {

    data class BreedGalleryState(
        val loading: Boolean = true,
        val images: List<ImageUiModel> = emptyList(),
        val currentIndex: Int = 0,
    )
}