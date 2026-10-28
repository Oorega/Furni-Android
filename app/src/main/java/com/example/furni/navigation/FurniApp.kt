package com.example.furni.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*

@Composable
fun FurniApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    // A destination's route may carry optional arguments (e.g. "products?categoryId={categoryId}"),
    // so compare only the part before the "?" with the tab's route.
    fun matchesTab(route: String?, tabRoute: String) = route?.substringBefore('?') == tabRoute

    // Show the bottom bar only on the 5 main tabs (hide on login, checkout, etc.)
    val showBottomBar = bottomNavItems.any { item ->
        currentDestination?.hierarchy?.any { matchesTab(it.route, item.screen.route) } == true
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            selected = currentDestination?.hierarchy
                                ?.any { matchesTab(it.route, item.screen.route) } == true,
                            onClick = {
                                navController.navigate(item.screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        FurniNavHost(navController, Modifier.padding(innerPadding))
    }
}
