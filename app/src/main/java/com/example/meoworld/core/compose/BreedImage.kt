package com.example.meoworld.core.compose

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun BreedImage(imageUrl: String?, name: String) {
    Box(
    modifier = Modifier
    .fillMaxWidth()
    .height(280.dp)
    .padding(16.dp)
    ) {
        imageUrl?.let {
            AsyncImage(
                model = it,
                contentDescription = "$name image",
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .border(
                        2.dp,
                        colorScheme.secondary,
                        RoundedCornerShape(12.dp)
                    ),
                contentScale = ContentScale.Fit
            )
        }
    }
}