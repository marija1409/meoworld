package com.example.meoworld.core.compose

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import kotlin.let
import kotlin.text.isNotBlank
import kotlin.text.take
import com.example.meoworld.segments.cats.uiModel.CatUIModel

@Composable
fun BreedCard(
    breed: CatUIModel,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            breed.image.takeIf { it.isNotEmpty() }?.let { imageUrl ->
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "${breed.name} image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .border(
                            2.dp,
                            MaterialTheme.colorScheme.secondary,
                            RoundedCornerShape(12.dp)
                        )
                )
            }

            Text(
                text = breed.name,
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.padding(top = 12.dp)
            )

            if (breed.alternativeNames.isNotBlank()) {
                Text(
                    text = "Also known as: ${breed.alternativeNames}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            Text(
                text = breed.description.take(250),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            if (breed.temperaments.isNotBlank()) {
                // You can call your TemperamentChips here if you want:
                TemperamentChips(
                    temperamentString = breed.temperaments,
                    maxTemperaments = 3,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
