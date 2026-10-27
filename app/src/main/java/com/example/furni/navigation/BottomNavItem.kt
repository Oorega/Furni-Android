package com.example.furni.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val screen: Screen,
    val label: String,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(Screen.Home, "Home", Icons.Filled.Home),
    BottomNavItem(Screen.Products, "Shop", Icons.Filled.Chair),
    BottomNavItem(Screen.Cart, "Cart", Icons.Filled.ShoppingCart),
    BottomNavItem(Screen.Orders, "Orders", Icons.Filled.Receipt),
    BottomNavItem(Screen.Profile, "Profile", Icons.Filled.Person)
)