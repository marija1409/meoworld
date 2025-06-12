package com.example.meoworld.segments.cats.galleryDetails

import com.example.meoworld.segments.cats.uiModel.ImageUiModel


interface GalleryDetails {

    data class BreedGalleryState(
        val loading: Boolean = true,
        val images: List<ImageUiModel> = emptyList(),
        val currentIndex: Int = 0,
    )
}