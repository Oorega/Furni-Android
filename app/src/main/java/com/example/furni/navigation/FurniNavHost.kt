package com.example.furni.navigation

import android.content.Context
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.furni.data.database.FurniDatabase
import com.example.furni.ui.auth.AuthViewModel
import com.example.furni.ui.auth.LoginScreen
import com.example.furni.ui.auth.RegisterScreen
import com.example.furni.ui.cart.CartScreen
import com.example.furni.ui.home.HomeScreen
import com.example.furni.ui.orders.OrdersScreen
import com.example.furni.ui.products.ProductsScreen
import com.example.furni.ui.products.ProductDetailsScreen
import com.example.furni.ui.profile.EditProfileScreen
import com.example.furni.ui.profile.ProfileScreen
import com.example.furni.ui.profile.ProfileViewModel

@Composable
fun FurniNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onCategoryClick = { categoryId -> navController.navigate(Screen.Products.createRoute(categoryId)) },
                onProductClick = { id -> navController.navigate(Screen.ProductDetails.createRoute(id)) },
                onSearchClick = { navController.navigate(Screen.Products.route) },
                onShopNowClick = { navController.navigate(Screen.Products.route) }
            )
        }

        composable(Screen.Login.route) {
            val context = LocalContext.current
            val database = FurniDatabase.getInstance(context)
            val viewModel: AuthViewModel = viewModel(
                factory = viewModelFactory { initializer { AuthViewModel(database, context) } }
            )

            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onRegisterClick = { navController.navigate(Screen.Register.route) }
            )
        }

        composable(Screen.Register.route) {
            val context = LocalContext.current
            val database = FurniDatabase.getInstance(context)
            val viewModel: AuthViewModel = viewModel(
                factory = viewModelFactory { initializer { AuthViewModel(database, context) } }
            )

            RegisterScreen(
                viewModel = viewModel,
                onRegisterSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onLoginClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Profile.route) {
            val context = LocalContext.current
            val database = FurniDatabase.getInstance(context)
            val profileViewModel: ProfileViewModel = viewModel(
                factory = viewModelFactory { initializer { ProfileViewModel(database, context) } }
            )

            ProfileScreen(
                viewModel = profileViewModel,
                onLogoutClick = {
                    context.getSharedPreferences("furni_prefs", Context.MODE_PRIVATE).edit().clear().apply()
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
                },
                onEditProfileClick = {
                    navController.navigate("edit_profile")
                }
            )
        }

        composable("edit_profile") {
            val context = LocalContext.current
            val database = FurniDatabase.getInstance(context)
            // Reuse the same ViewModel class, but instantiate it for this screen
            val profileViewModel: ProfileViewModel = viewModel(
                factory = viewModelFactory { initializer { ProfileViewModel(database, context) } }
            )

            EditProfileScreen(
                viewModel = profileViewModel,
                onBackClick = { navController.popBackStack() },
                onAccountDeleted = {
                    // Navigate back to Login and clear the whole backstack
                    navController.navigate(Screen.Login.route) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.Products.ROUTE_PATTERN,
            arguments = listOf(
                navArgument(Screen.Products.CATEGORY_ARG) {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            ProductsScreen(onProductClick = { id -> navController.navigate(Screen.ProductDetails.createRoute(id)) })
        }

        composable(Screen.Cart.route) {
            CartScreen(onCheckoutClick = { navController.navigate(Screen.Checkout.route) })
        }

        composable(Screen.Orders.route) {
            OrdersScreen()
        }

        composable(
            Screen.ProductDetails.route,
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { entry ->
            val id = entry.arguments?.getInt("productId") ?: return@composable
            ProductDetailsScreen(
                productId = id.toLong(),
                onBack = { navController.popBackStack() },
                onAddToCart = { _, _ -> /* TODO */ }
            )
        }

        composable(Screen.Checkout.route) { Text("Checkout - Member 5") }
        composable(Screen.OrderConfirmation.route) { Text("Confirmation - Member 5") }
    }
}