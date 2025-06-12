package com.example.meowpedia.cats.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.meowpedia.cats.details.Details.DetailsUiState
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MediumTopAppBar
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.meoworld.core.compose.CharacteristicBar
import com.example.meoworld.core.compose.LoadingIndicator
import com.example.meoworld.core.compose.NoDataContent
import com.example.meoworld.core.compose.SectionChip
import com.example.meoworld.core.compose.SectionText
import com.example.meoworld.core.compose.TemperamentChips
import androidx.compose.runtime.getValue
import com.example.meoworld.core.compose.BreedImage
import com.example.meoworld.core.compose.ViewMoreImagesButton
import com.example.meoworld.core.compose.WikipediaButton


fun NavGraphBuilder.breedDetails(
    route: String,
    navController: NavController,
    onClose: () -> Unit,
) = composable(
    route = route,
) { navBackStackEntry ->
    val breedId = navBackStackEntry.arguments?.getString("Id")
        ?: throw IllegalStateException("breedId required")

    val detailsViewModel: DetailsViewModel = hiltViewModel()

    val state by detailsViewModel.state.collectAsState()

    DetailsScreen(
        state = state,
        onClose = onClose,
        onViewMoreImages = { id ->
            navController.navigate("breed/images/$id")
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DetailsScreen(
    state: DetailsUiState,
    onClose: () -> Unit,
    onViewMoreImages: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(text = state.data?.name ?: "Loading")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.primaryContainer,
                    scrolledContainerColor = colorScheme.primaryContainer,
                ),
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = colorScheme.primary
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (state.fetching) {
                    LoadingIndicator()
                } else if (state.error != null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        val errorMessage = when (state.error) {
                            is DetailsUiState.DetailsError.DataUpdateFailed ->
                                "Failed to load. Error message: ${state.error.cause?.message}."
                        }
                        Text(text = errorMessage)
                    }
                } else if (state.data != null) {
                    val data = state.data
                    val uriHandler = LocalUriHandler.current

                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .padding(bottom = 24.dp)
                    ) {

                        BreedImage(imageUrl = data.image, name = data.name)

                        ViewMoreImagesButton { onViewMoreImages(data.id) }

                        SectionText("Description", data.description)
                        SectionChip("Origin", data.origins)
                        SectionChip("Life Span", "${data.lifeSpan} years")
                        SectionChip("Weight", "${data.weight.metric} kg / ${data.weight.imperial} lbs")

                        if (data.rare > 0) {
                            SectionChip("Rarity", "Rare Breed")
                        }

                        SectionText("Temperaments")
                        if (data.temperaments.isNotBlank()) {
                            TemperamentChips(
                                temperamentString = data.temperaments,
                                maxTemperaments = null,
                                modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth()
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        CharacteristicBar(label = "Adaptability", level = data.adaptability)
                        CharacteristicBar(label = "Affection Level", level = data.affectionLevel)
                        CharacteristicBar(label = "Child Friendly", level = data.childFriendly)
                        CharacteristicBar(label = "Intelligence", level = data.intelligence)
                        CharacteristicBar(label = "Shedding", level = data.sheddingLevel)

                        Spacer(modifier = Modifier.height(24.dp))

                        WikipediaButton(data.wikipediaUrl)

                    }
                } else {
                    NoDataContent("There is no data for this cat")
                }
            }
        }
    )
}
