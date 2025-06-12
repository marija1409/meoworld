package com.example.meoworld.core.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ErrorMessage(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = message, color = colorScheme.error)
    }
}
