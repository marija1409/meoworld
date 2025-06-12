package com.example.meoworld.core.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun BestGlobalRank(
    bestGlobalRank: Pair<Int, Double>
) {
    Text(
        text = "Best Global Rank",
        style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier.fillMaxWidth().padding(bottom = 14.dp),
    )
    Column(modifier = Modifier.padding(bottom = 22.dp)) {
        InformationRow(
            title = "Rank: ",
            value = bestGlobalRank.first.toString(),
            addUnderline = false
        )
        InformationRow(
            title = "Score: ",
            value = bestGlobalRank.second.toString(),
            addUnderline = false
        )
    }
}