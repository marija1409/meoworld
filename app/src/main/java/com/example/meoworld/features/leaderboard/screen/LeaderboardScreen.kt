package com.example.meoworld.features.leaderboard.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import com.example.meoworld.core.compose.ErrorMessage
import com.example.meoworld.core.compose.LeaderboardList
import com.example.meoworld.core.compose.LoadingIndicator
import kotlinx.coroutines.launch

fun NavGraphBuilder.leaderboardScreen(
    route: String,
    navController: NavController,
) = composable(route) {
    val viewModel: LeaderboardViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    LeaderboardScreen(
        state = state,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(
    state: Leaderboard.LeaderboardState,
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Leaderboard",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.primaryContainer,
                    scrolledContainerColor = colorScheme.primaryContainer,
                )
            )
        },
        floatingActionButton = {
            SmallFloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(0)
                    }
                }
            ) {
                Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "Scroll to Top"
                    )
            }
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when {
                    state.fetching -> {
                        LoadingIndicator(modifier = Modifier.align(Alignment.Center))
                    }

                    state.error != null -> {
                        val errorMessage = when (state.error) {
                            is Leaderboard.LeaderboardState.ListError.ListUpdateFailed ->
                                "Failed to load. Please try again later. Error message: ${state.error.cause?.message}."
                        }
                        ErrorMessage(errorMessage)
                    }

                    else -> {
                        LeaderboardList(items = state.leaderboardItems, listState = listState)
                    }
                }
            }
        }
    )
}



