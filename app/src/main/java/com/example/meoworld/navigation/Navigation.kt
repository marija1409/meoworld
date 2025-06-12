package com.example.meoworld.navigation

interface Navigation {

    data class NavigationState(
        val hasAccount: Boolean = false,
        val isLoading: Boolean = true,
    )
}