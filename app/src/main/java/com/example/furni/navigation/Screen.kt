package com.example.furni.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Products : Screen("products") {
        const val CATEGORY_ARG = "categoryId"
        const val ROUTE_PATTERN = "products?categoryId={categoryId}"
        fun createRoute(categoryId: Long) = "products?categoryId=$categoryId"
    }
    object ProductDetails : Screen("product/{productId}") {
        fun createRoute(productId: Int) = "product/$productId"
    }
    object Cart : Screen("cart")
    object Checkout : Screen("checkout")
    object OrderConfirmation : Screen("order_confirmation")
    object Orders : Screen("orders")
    object Profile : Screen("profile")
}