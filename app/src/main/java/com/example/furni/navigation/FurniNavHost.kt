package com.example.furni.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.furni.ui.auth.LoginScreen
import com.example.furni.ui.auth.RegisterScreen
import com.example.furni.ui.cart.CartScreen
import com.example.furni.ui.home.HomeScreen
import com.example.furni.ui.orders.OrdersScreen
import com.example.furni.ui.products.ProductsScreen
import com.example.furni.ui.profile.ProfileScreen
import androidx.navigation.NavGraph.Companion.findStartDestination

@Composable
fun FurniNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
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

        composable(Screen.Login.route) {
            LoginScreen(
                onLoginClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onRegisterClick = { navController.navigate(Screen.Register.route) }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterClick = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onLoginClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Products.route) {
            ProductsScreen(
                onProductClick = { id -> navController.navigate(Screen.ProductDetails.createRoute(id)) }
            )
        }

        composable(Screen.Cart.route) {
            CartScreen(
                onCheckoutClick = { navController.navigate(Screen.Checkout.route) }
            )
        }

        composable(Screen.Orders.route) {
            OrdersScreen()
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onLogoutClick = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                },
                onOrdersClick = {
                    navController.navigate(Screen.Orders.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        // ---- Placeholders: replace with real screens as members finish ----
        composable(
            Screen.ProductDetails.route,
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { entry ->
            val id = entry.arguments?.getInt("productId")
            Text("Product Details $id - Member 2")
        }
        composable(Screen.Checkout.route) { Text("Checkout - Member 5") }
        composable(Screen.OrderConfirmation.route) { Text("Confirmation - Member 5") }
    }
}