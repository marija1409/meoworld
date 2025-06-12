package com.example.meoworld.segments.cats.gallery

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meoworld.core.mappers.asImageUiModel
import com.example.meoworld.segments.cats.list.CatList.ListError
import com.example.meoworld.segments.cats.repo.CatsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor (
    savedStateHandle: SavedStateHandle,
    private val repository: CatsRepo
): ViewModel() {

    private val breedId = savedStateHandle.get<String>("breedId")!!

    private val _state = MutableStateFlow(GalleryState(breedId = breedId))
    val state = _state.asStateFlow()

    private fun setState(reducer: GalleryState.() -> GalleryState) = _state.getAndUpdate(reducer)

    init {
        observeImages()
    }

    private fun observeImages() {
        viewModelScope.launch {
            repository.observeImagesForBreed(breedId)
                .distinctUntilChanged()
                .collect {
                    setState { copy(images = it.map { it.asImageUiModel() }) }
                }
        }
    }


}