package com.example.meoworld.core.compose

import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ViewMoreImagesButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .absolutePadding(left = 10.dp, right = 0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorScheme.secondary,
            contentColor = colorScheme.onPrimary
        )
    ) {
        Text("View More Images")
    }
}
