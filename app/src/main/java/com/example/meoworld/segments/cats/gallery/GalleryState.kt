package com.example.meoworld.segments.cats.gallery

import com.example.meoworld.segments.cats.list.CatList
import com.example.meoworld.segments.cats.uiModel.ImageUiModel

data class GalleryState (
    val breedId: String,
    val fetching: Boolean = false,
    val error: CatList.ListError? = null,

    val images: List<ImageUiModel> = emptyList()
) {
    sealed class ListError {
        data class ListUpdateFailed(val cause: Throwable? = null) : ListError()
    }
}