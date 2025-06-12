package com.example.meoworld.core.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.example.meoworld.segments.cats.uiModel.ImageUiModel

@Composable
fun ImagePreview(
    modifier: Modifier,
    image: ImageUiModel,
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        SubcomposeAsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = image.url,
            loading = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(36.dp),)
                }
            },
            contentDescription = null,
            contentScale = ContentScale.Fit,
        )
    }
}