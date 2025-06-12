package com.example.meoworld.core.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.meoworld.segments.leaderboard.uiModel.LeaderboardUIModel

@Composable
fun LeaderboardListItem(model: LeaderboardUIModel, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "#${model.ranking}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.width(40.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(text = model.nickname, style = MaterialTheme.typography.titleMedium)
                Text(text = "Score: %.2f".format(model.result), style = MaterialTheme.typography.bodyMedium)
            }
            Text(
                text = "Games: ${model.totalGamesPlayed}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}