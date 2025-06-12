package com.example.meoworld.features.cats.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meoworld.core.mappers.asBreedUiModel
import com.example.meoworld.features.cats.repo.CatsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.collections.filter
import kotlin.collections.map
import kotlin.text.startsWith

@HiltViewModel
class CatListViewModel @Inject constructor(
    private val repo: CatsRepo
) : ViewModel() {

    private val _state = MutableStateFlow(CatList.CatListState())

    val state = _state.asStateFlow()

    private fun setState(reducer: CatList.CatListState.() -> CatList.CatListState) = _state.update(reducer)

    private val events = MutableSharedFlow<CatList.CatListUIEvent>()
    fun setEvent(event: CatList.CatListUIEvent) = viewModelScope.launch { events.emit(event) }

    init {
        observeEvents()
        observeBreeds()
        fetchBreedsIfEmpty()
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect {
                when (it) {
                    is CatList.CatListUIEvent.ClearSearch -> setState { copy(query = "") }
                    is CatList.CatListUIEvent.SearchQueryChanged -> {
                        filterBreedList(query = it.query)
                    }

                }
            }
        }
    }

    private fun observeBreeds() {
        viewModelScope.launch {
            repo.observeBreeds()
                .collect { breeds ->
                    val uiBreeds = breeds.map { it.asBreedUiModel() }
                    setState { copy(breeds = uiBreeds, filteredBreeds = uiBreeds) }
                }
        }
    }

    private fun fetchBreedsIfEmpty() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            try {
                withContext(Dispatchers.IO) {
                    if (repo.getBreeds().isEmpty()) {
                        repo.fetchBreeds()
                    }
                }
            } catch (error: Exception) {
                setState { copy(error = CatList.ListError.ListUpdateFailed(error)) }
                Log.e("BreedListViewModel", "Error", error)
            } finally {
                setState { copy(loading = false) }
            }
        }
    }



    private fun filterBreedList(query: String) {
        val filteredBreeds = _state.value.breeds.filter { breed ->
            breed.name.startsWith(query, ignoreCase = true)
        }
        setState { copy(filteredBreeds = filteredBreeds, query = query) }

    }

}