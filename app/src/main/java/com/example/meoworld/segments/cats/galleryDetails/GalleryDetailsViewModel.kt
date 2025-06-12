package com.example.meoworld.segments.cats.galleryDetails

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.meoworld.core.mappers.asImageUiModel
import com.example.meoworld.segments.cats.galleryDetails.GalleryDetails.BreedGalleryState
import com.example.meoworld.segments.cats.repo.CatsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.getAndUpdate
import javax.inject.Inject

@HiltViewModel
class GalleryDetailsViewModel @Inject constructor (
    savedStateHandle: SavedStateHandle,
    private val repository: CatsRepo
) : ViewModel() {

    private val breedId = savedStateHandle.get<String>("breedId")!!
    private val currentImage = savedStateHandle.get<String>("currentImage")!!

    private val _state = MutableStateFlow(BreedGalleryState())
    val state = _state.asStateFlow()

    private fun setState(reducer: BreedGalleryState.() -> BreedGalleryState) = _state.getAndUpdate(reducer)

    init {
        fetchImages()
    }

    private fun fetchImages() {
        viewModelScope.launch {
            setState { copy(loading = true) }

            try {
                val breedImages = repository.getImagesForBreed(breedId).map { it.asImageUiModel() }
                val currentIndex = breedImages.indexOfFirst { it.id == currentImage }

                setState { copy(
                    images = breedImages,
                    currentIndex = currentIndex
                ) }

            } catch (error: Exception) {
                Log.e("CATAPULT", "Failed to fetch images for breed $breedId, error: ${error.message}")
            }

            setState { copy(loading = false) }
        }
    }
}