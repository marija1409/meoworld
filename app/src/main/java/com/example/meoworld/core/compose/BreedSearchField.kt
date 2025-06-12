package com.example.meoworld.core.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp

@Composable
fun BreedSearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    onClear: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        label = { Text("Search") },
        singleLine = true,
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
        trailingIcon = {
            IconButton(onClick = {
                onClear()
                keyboardController?.hide()
                focusManager.clearFocus()
            }) {
                Icon(Icons.Filled.Close, contentDescription = "Clear Search")
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = colorScheme.surface,
            unfocusedContainerColor = colorScheme.surface,
            focusedTextColor = colorScheme.primary,
            unfocusedTextColor = colorScheme.primary,
            focusedPlaceholderColor = colorScheme.primary,
            unfocusedPlaceholderColor = colorScheme.primary,
            unfocusedTrailingIconColor = colorScheme.primary,
            focusedTrailingIconColor = colorScheme.primary,
            focusedIndicatorColor = colorScheme.primary,
            unfocusedIndicatorColor = colorScheme.secondary,
            cursorColor = colorScheme.primary,
            focusedLeadingIconColor = colorScheme.primary,
            unfocusedLeadingIconColor = colorScheme.primary,
            focusedLabelColor = colorScheme.primary,
            unfocusedLabelColor = colorScheme.onSurface.copy(alpha = 0.6f),
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}
