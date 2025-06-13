package com.example.meoworld.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.catapult.segments.quiz.start_screen.quizStartScreen
import com.example.meoworld.features.cats.details.breedDetails
import com.example.meoworld.navigation.bottomNav.BottomNavItem
import com.example.meoworld.navigation.bottomNav.BottomNavigationBar
import com.example.meoworld.features.cats.gallery.gallery
import com.example.meoworld.features.cats.galleryDetails.galleryDetails
import com.example.meoworld.features.cats.list.cats
import com.example.meoworld.features.leaderboard.screen.leaderboardScreen
import com.example.meoworld.features.quiz.question_screen.QuizQuestionViewModel
import com.example.meoworld.features.quiz.question_screen.quizQuestionScreen
import com.example.meoworld.features.user.edit.profileEditScreen
import com.example.meoworld.features.user.profile.profileScreen
import com.example.meoworld.features.user.register.registerScreen
import com.example.meoworld.features.welcome.welcomeScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val viewModel = hiltViewModel<NavigationViewModel>()
    val state by viewModel.state.collectAsState()

    val quizViewModel = hiltViewModel<QuizQuestionViewModel>()
    val quizState by quizViewModel.state.collectAsState()



    val startDestination = when {
        state.isLoading -> "welcome"
        state.hasAccount -> "breeds"
        else -> "register"
    }

    val bottomBarScreens = BottomNavItem.items.map { it.route }

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    val hideBottomBarRoutes = listOf("welcome", "register")
    val showBottomBar = currentRoute != null && currentRoute !in hideBottomBarRoutes


    Scaffold(
        bottomBar = {
                if (showBottomBar){
                    BottomNavigationBar(
                        navController = navController,
                        isQuizRunning = quizState.isQuizRunning,
                    )
                }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(padding),
        ) {

            welcomeScreen(
                route = "welcome"
            )

            registerScreen(
                route = "register",
                navController = navController
            )

            cats(
                route = "breeds",
                onBreedClick = {
                    navController.navigate(route = "breeds/$it")
                }
            )

            breedDetails(
                route = "breeds/{Id}",
                navController = navController,
                onClose = {
                    navController.navigateUp()
                },

                )

            gallery (
                route = "breed/images/{breedId}",
                arguments = listOf(
                    navArgument("breedId") {
                        nullable = false
                        type = NavType.StringType
                    },
                ),
                navController = navController,
                onBack = { navController.navigateUp() }
            )

            galleryDetails(
                route = "breed/images/{breedId}?currentImage={currentImage}",
                arguments = listOf(
                    navArgument(name = "breedId") {
                        nullable = false
                        type = NavType.StringType
                    },
                    navArgument(name = "currentImage") {
                        nullable = false
                        type = NavType.StringType
                    }
                ),
                onBack = { navController.navigateUp() }
            )

        quizStartScreen(
            route = "quiz/start",
            navController = navController,
        )

        quizQuestionScreen(
            route = "quiz",
            navController = navController,
        )

        leaderboardScreen(
            route = "leaderboard",
            navController = navController
        )

        profileScreen(
            route = "profile",
            navController = navController,
        )

        profileEditScreen(
            route = "profile/edit",
            navController = navController,
        )
        }
    }

}