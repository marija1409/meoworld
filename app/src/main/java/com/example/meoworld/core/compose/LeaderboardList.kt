package com.example.meoworld.core.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.meoworld.segments.leaderboard.uiModel.LeaderboardUIModel

@Composable
fun LeaderboardList(items: List<LeaderboardUIModel>, listState: androidx.compose.foundation.lazy.LazyListState) {
    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        itemsIndexed(items) { index, item ->
            LeaderboardListItem(
                model = item.copy(ranking = index + 1),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
        }
    }
}