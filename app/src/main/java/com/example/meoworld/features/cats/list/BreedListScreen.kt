package com.example.meoworld.features.cats.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.meoworld.core.compose.LoadingIndicator
import kotlin.text.isNotEmpty
import androidx.compose.runtime.getValue
import com.example.meoworld.core.compose.BreedCard
import com.example.meoworld.core.compose.BreedSearchField
import com.example.meoworld.core.compose.ErrorMessage

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.cats(
    route: String,
    onBreedClick: (String) -> Unit,
) = composable(
    route = route
) {

    val breedListViewModel: CatListViewModel = hiltViewModel()


    val state by breedListViewModel.state.collectAsState()


    BreedListScreen(
        state = state,
        eventPublisher = {
            breedListViewModel.setEvent(it)
        },
        onBreedClick = onBreedClick,
    )
}

@ExperimentalMaterial3Api
@Composable
fun BreedListScreen(
    state: CatList.CatListState,
    eventPublisher: (uiEvent: CatList.CatListUIEvent) -> Unit,
    onBreedClick: (String) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = "Breeds",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            })
        },
        content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {

                BreedSearchField(
                    query = state.query,
                    onQueryChange = { eventPublisher(CatList.CatListUIEvent.SearchQueryChanged(it)) },
                    onClear = { eventPublisher(CatList.CatListUIEvent.ClearSearch) }
                )

                when {
                    state.error != null -> {
                            val errorMessage = when (state.error) {
                                is CatList.ListError.ListUpdateFailed ->
                                    "Failed to load. Please try again later. Error message: ${state.error.cause?.message}."
                            }
                        ErrorMessage(errorMessage)
                    }

                    state.loading -> {
                        LoadingIndicator()
                    }

                    else -> {
                        val breedsToShow = if (state.query.isNotEmpty()) state.filteredBreeds else state.breeds
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            items(
                                items = breedsToShow,
                                key = { it.id }
                            ) { breed ->
                                BreedCard(breed = breed, onClick = { onBreedClick(breed.id) })
                            }
                        }
                    }
                }
            }
        }
    )
}
