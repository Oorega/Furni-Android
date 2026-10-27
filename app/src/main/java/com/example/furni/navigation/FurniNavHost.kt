package com.example.furni.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.furni.ui.home.HomeScreen

@Composable
fun FurniNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route, // Member 6 will change this to Login later
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onCategoryClick = { navController.navigate(Screen.Products.route) },
                onProductClick = { id -> navController.navigate(Screen.ProductDetails.createRoute(id)) },
                onSearchClick = { navController.navigate(Screen.Products.route) },
                onShopNowClick = { navController.navigate(Screen.Products.route) }
            )
        }

        // ---- Placeholders: replace with real screens as members finish ----
        composable(Screen.Products.route) { Text("Products - Member 2") }
        composable(
            Screen.ProductDetails.route,
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { entry ->
            val id = entry.arguments?.getInt("productId")
            Text("Product Details $id - Member 2")
        }
        composable(Screen.Cart.route) { Text("Cart - Member 3") }
        composable(Screen.Checkout.route) { Text("Checkout - Member 5") }
        composable(Screen.OrderConfirmation.route) { Text("Confirmation - Member 5") }
        composable(Screen.Orders.route) { Text("Orders - Member 5") }
        composable(Screen.Login.route) { Text("Login - Member 6") }
        composable(Screen.Register.route) { Text("Register - Member 6") }
        composable(Screen.Profile.route) { Text("Profile - Member 6") }
    }
}