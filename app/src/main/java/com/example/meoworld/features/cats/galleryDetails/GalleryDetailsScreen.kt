package com.example.meoworld.features.cats.galleryDetails

import android.util.Log
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.meoworld.core.compose.ImagePreview
import com.example.meoworld.core.compose.LoadingIndicator
import com.example.meoworld.features.cats.galleryDetails.GalleryDetails.BreedGalleryState


fun NavGraphBuilder.galleryDetails(
    route: String,
    arguments: List<NamedNavArgument>,
    onBack: () -> Unit,
) = composable(
    route = route,
    arguments = arguments,
    enterTransition = { slideInVertically { it } },
    popExitTransition = { slideOutVertically { it } },
) {backStackEntry ->

    val galleryDetailsViewModel = hiltViewModel<GalleryDetailsViewModel>(backStackEntry)
    val state  by galleryDetailsViewModel.state.collectAsState()

    BreedGalleryScreen(
        state = state,
        onBack = onBack,
    )
}

@Composable
fun BreedGalleryScreen(
    state: BreedGalleryState,
    onBack: () -> Unit,
) {
    if(state.loading)
        LoadingIndicator()
    else {
        BreedGalleryScreenContent(
            state = state,
            onBack = onBack,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BreedGalleryScreenContent(
    state: BreedGalleryState,
    onBack: () -> Unit,
) {
    val pagerState = rememberPagerState(
        pageCount = { state.images.size }
    )

    LaunchedEffect(state.images, state.currentIndex) {
        if (state.images.isNotEmpty()) {
            pagerState.scrollToPage(state.currentIndex)
        }
    }

    Log.d("CATAPULT", "state.CurrentIndex: ${state.currentIndex}")
    Log.d("CATAPULT", "pageCount: ${pagerState.pageCount} currentIndex: ${pagerState.currentPage}")

    Surface(modifier = Modifier.fillMaxSize()) {
        Column {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon( imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = colorScheme.primary
                        )
                    }
                }
            )

            Box(modifier = Modifier.weight(1f)) {
                if (state.images.isNotEmpty()) {
                    HorizontalPager(
                        modifier = Modifier.fillMaxSize(),
                        pageSize = PageSize.Fill,
                        pageSpacing = 16.dp,
                        state = pagerState,
                        key = { state.images[it].id }
                    ) { pageIndex ->
                        val image = state.images[pageIndex]
                        ImagePreview(
                            modifier = Modifier,
                            image = image,
                        )
                    }
                } else {
                    Text(
                        modifier = Modifier.fillMaxSize(),
                        text = "No images.",
                    )
                }
            }
        }
    }

}

