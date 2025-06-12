package com.example.meoworld.navigation.bottomNav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector,
) {
    object Breeds : BottomNavItem("breeds", "Breeds", Icons.Filled.Pets)
    object Quiz: BottomNavItem("quiz/start", "Quiz", Icons.Filled.Quiz)
    object Leaderboard : BottomNavItem("leaderboard", "Leaderboard", Icons.Filled.Leaderboard)
    object Profile : BottomNavItem("profile", "Profile", Icons.Filled.Person)

    companion object {
        val items = listOf(Breeds, Quiz, Leaderboard, Profile)
    }
}
