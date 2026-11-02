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

    // ADDED ORDER_ID_ARG HERE
    object OrderConfirmation : Screen("order_confirmation/{orderId}") {
        const val ORDER_ID_ARG = "orderId"
        fun createRoute(orderId: Long) = "order_confirmation/$orderId"
    }

    object OrderDetails : Screen("order/{orderId}") {
        fun createRoute(orderId: Long) = "order/$orderId"
    }
    object Orders : Screen("orders")
    object Profile : Screen("profile")
}