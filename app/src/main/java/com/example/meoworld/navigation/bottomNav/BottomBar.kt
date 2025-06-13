package com.example.meoworld.navigation.bottomNav


import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.meoworld.core.compose.ConfirmCancelQuizDialog

@Composable
fun BottomNavigationBar(
    navController: NavController,
    isQuizRunning: Boolean,
    modifier: Modifier = Modifier,
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var pendingRoute by rememberSaveable { mutableStateOf<String?>(null) }

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        tonalElevation = 8.dp
    ) {
        BottomNavItem.items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        if (isQuizRunning && currentRoute == "quiz") {
                            pendingRoute = item.route
                            showDialog = true
                        } else {
                            navController.navigate(item.route) {
                                launchSingleTop = true
                            }
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        modifier = Modifier.size(20.dp)
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    unselectedIconColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                    selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unselectedTextColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                    indicatorColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }

    if (showDialog) {
        ConfirmCancelQuizDialog(
            onConfirm = {
                showDialog = false
                pendingRoute?.let { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                    }
                }
                pendingRoute = null
            },
            onDismiss = {
                showDialog = false
                pendingRoute = null
            }
        )
    }

}
