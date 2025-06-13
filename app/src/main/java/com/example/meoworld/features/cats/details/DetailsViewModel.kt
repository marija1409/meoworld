package com.example.meoworld.features.cats.details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meoworld.core.mappers.asBreedUiModel
import com.example.meoworld.features.cats.details.Details.DetailsUiState
import com.example.meoworld.features.cats.repo.CatsRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject


@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repo: CatsRepo,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val breedId = savedStateHandle.get<String>("Id") ?: ""

    private val _state = MutableStateFlow(DetailsUiState(breedId))
    val state = _state.asStateFlow()

    private fun setState(reducer: DetailsUiState.() -> DetailsUiState) =
        _state.update(reducer)

    init {
        fetchCatDetails(breedId)
    }

    fun fetchCatDetails(breedId: String) {
        viewModelScope.launch {
            setState { copy(fetching = true, error = null) }

            try {
                val breedDbModel = repo.observeBreedDetails(breedId).firstOrNull()
                if (breedDbModel != null) {
                    val uiModel = breedDbModel.asBreedUiModel()
                    setState {
                        copy(
                            data = uiModel,
                            image = uiModel.image,
                            error = null
                        )
                    }
                } else {
                    setState {
                        copy(
                            data = null,
                            image = null,
                            error = DetailsUiState.DetailsError.DataUpdateFailed(
                                cause = IllegalStateException("Breed not found in DB")
                            )
                        )
                    }
                }
            } catch (error: Exception) {
                Log.e("DetailsViewModel", "Failed to fetch details", error)
                setState {
                    copy(error = DetailsUiState.DetailsError.DataUpdateFailed(error))
                }
            } finally {
                setState { copy(fetching = false) }
            }
        }
    }

}
