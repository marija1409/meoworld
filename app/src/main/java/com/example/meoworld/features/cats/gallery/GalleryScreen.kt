package com.example.meoworld.features.cats.gallery

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.composable
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.SubcomposeAsyncImage
import com.example.meoworld.features.cats.uiModel.ImageUiModel

fun NavGraphBuilder.gallery(
    route: String,
    navController: NavController,
    arguments: List<NamedNavArgument>,
    onBack: () -> Unit,
) = composable(route = route, arguments = arguments) { backStackEntry ->

    val breedId = backStackEntry.arguments?.getString("breedId") ?: ""

    val galleryViewModel = hiltViewModel<GalleryViewModel>(backStackEntry)
    val state by galleryViewModel.state.collectAsState()

    val onImageClick: (String) -> Unit = { imageId ->
        navController.navigate("breed/images/${breedId}?currentImage=$imageId")
    }

    BreedImagesScreen(
        state = state,
        onImageClick = onImageClick,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreedImagesScreen(
    state: GalleryState,
    onImageClick: (breedId: String) -> Unit,
    onBack: () -> Unit,
) {
    Surface {
        Column {
            // TOP BAR
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = colorScheme.primary
                        )
                    }
                },
                title = { Text("Images for breed") }
            )

            BoxWithConstraints(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.BottomCenter,
            ) {
                val screenWidth = this.maxWidth
                val cellSize = (screenWidth / 2) - 4.dp

                LazyVerticalStaggeredGrid(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 4.dp),
                    columns = StaggeredGridCells.Fixed(2),
                    contentPadding = PaddingValues(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    itemsIndexed(
                        items = state.images,
                        key = { index: Int, image: ImageUiModel -> image.id },
                    ) { index: Int, image: ImageUiModel ->
                        val aspectRatio = image.width.toFloat() / image.height.toFloat()
                        val imageHeightDp = cellSize / aspectRatio

                        Card(
                            modifier = Modifier
                                .width(cellSize)
                                .height(imageHeightDp)
                                .clickable { onImageClick(image.id) },
                        ) {
                            SubcomposeAsyncImage(
                                modifier = Modifier.fillMaxSize(),
                                model = image.url,
                                contentDescription = null,
                                loading = { CircularProgressIndicator() }
                            )
                        }
                    }
                }
            }
        }
    }

}