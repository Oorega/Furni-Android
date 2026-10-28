package com.example.furni.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.furni.ui.auth.LoginScreen
import com.example.furni.ui.auth.RegisterScreen
import com.example.furni.ui.cart.CartScreen
import com.example.furni.ui.home.HomeScreen
import com.example.furni.ui.orders.OrdersScreen
import com.example.furni.ui.products.ProductsScreen
import com.example.furni.ui.products.ProductDetailsScreen
import com.example.furni.ui.profile.ProfileScreen

@Composable
fun FurniNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
        modifier = modifier
    ) {
        // Home Screen
        composable(Screen.Home.route) {
            HomeScreen(
                onCategoryClick = { categoryId ->
                    navController.navigate(
                        Screen.Products.createRoute(categoryId)
                    )
                },
                onProductClick = { id ->
                    navController.navigate(
                        Screen.ProductDetails.createRoute(id)
                    )
                },
                onSearchClick = {
                    navController.navigate(Screen.Products.route)
                },
                onShopNowClick = {
                    navController.navigate(Screen.Products.route)
                }
            )
        }

        // Login Screen
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        // Register Screen
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterClick = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onLoginClick = {
                    navController.popBackStack()
                }
            )
        }

        // Products Screen (supports an optional category ID)
        composable(
            route = Screen.Products.ROUTE_PATTERN,
            arguments = listOf(
                navArgument(Screen.Products.CATEGORY_ARG) {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            ProductsScreen(
                onProductClick = { id ->
                    navController.navigate(
                        Screen.ProductDetails.createRoute(id)
                    )
                }
            )
        }

        // Cart Screen
        composable(Screen.Cart.route) {
            CartScreen(
                onCheckoutClick = {
                    navController.navigate(Screen.Checkout.route)
                }
            )
        }

        // Orders Screen
        composable(Screen.Orders.route) {
            OrdersScreen()
        }

        // Profile Screen
        composable(Screen.Profile.route) {
            ProfileScreen(
                onLogoutClick = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                },
                onOrdersClick = {
                    navController.navigate(Screen.Orders.route) {
                        popUpTo(
                            navController.graph.findStartDestination().id
                        ) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        // Product Details Screen
        composable(
            Screen.ProductDetails.route,
            arguments = listOf(
                navArgument("productId") {
                    type = NavType.IntType
                }
            )
        ) { entry ->
            val id = entry.arguments?.getInt("productId")
                ?: return@composable

            ProductDetailsScreen(
                productId = id.toLong(),
                onBack = {
                    navController.popBackStack()
                },
                onAddToCart = { productId, quantity ->
                    // TODO: Connect to cart functionality
                }
            )
        }

        // Placeholder Screens
        composable(Screen.Checkout.route) {
            Text("Checkout - Member 5")
        }

        composable(Screen.OrderConfirmation.route) {
            Text("Confirmation - Member 5")
        }
    }
}
