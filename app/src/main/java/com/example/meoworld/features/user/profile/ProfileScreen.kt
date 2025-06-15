@file:OptIn(InternalSerializationApi::class)

package com.example.meoworld.features.user.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.meoworld.features.user.profile.Profile.ProfileState
import kotlinx.serialization.InternalSerializationApi
import com.example.meoworld.core.compose.BestGlobalRank
import com.example.meoworld.core.compose.LoadingIndicator
import com.example.meoworld.core.compose.QuizHistorySegment
import com.example.meoworld.core.compose.UserInfo


fun NavGraphBuilder.profileScreen(
    route: String,
    navController: NavController
) = composable(route = route) {

    val profileViewModel = hiltViewModel<ProfileViewModel>()
    val state by profileViewModel.state.collectAsState()

    ProfileScreen(
        state = state,
        navController = navController,
        profileViewModel = profileViewModel
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    state: ProfileState,
    navController: NavController,
    profileViewModel: ProfileViewModel,
) {
        val scrollState = rememberScrollState()
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.verticalScroll(scrollState)) {
                TopAppBar(
                    title = {
                        Text(
                            text = "Profile info",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                )

                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 42.dp, vertical = 16.dp)
                ) {
                    if (state.fetchingData) {
                        LoadingIndicator()
                    } else {
                        Column {
                            UserInfo(user = state.userData)
                            BestGlobalRank(bestGlobalRank = state.bestGlobalRank)
                            QuizHistorySegment(
                                quizResults = state.quizResults,
                                maxVisibleRows = 4
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Button(
                                    onClick = {
                                        profileViewModel.logout {
                                            navController.navigate("register") {
                                                popUpTo(0) { inclusive = true }
                                            }
                                        }
                                    },
                                    modifier = Modifier.padding(top = 16.dp)
                                        .width(100.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                                ) {
                                    Text("Logout", color = Color.White)
                                }

                            }

                        }
                    }
                }
            }
        }
    }


