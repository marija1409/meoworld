package com.example.meoworld.features.cats.details

import com.example.meoworld.features.cats.uiModel.CatUIModel

interface Details {
    data class DetailsUiState(
        val breedId: String,
        val data: CatUIModel? = null,
        val image: String? = null,
        val fetching: Boolean = false,
        val error: DetailsError? = null,
    )
    {
        sealed class DetailsError {
            data class DataUpdateFailed(val cause: Throwable? = null) : DetailsError()
        }
    }
}