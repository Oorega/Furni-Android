
package com.example.furni.ui.cart

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.furni.data.database.FurniDatabase
import com.example.furni.data.entity.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class CartItem(
    val product: Product,
    val quantity: Int
    )

class CartViewModel(application: Application) : AndroidViewModel(application) {

    private val productDao =
        FurniDatabase.getInstance(application).productDao()

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    // Add a product to the cart
    fun addToCart(productId: Long, quantity: Int) {
        if (quantity <= 0) return

        viewModelScope.launch {
            val product = productDao.getProductById(productId) ?: return@launch

            if (product.quantity <= 0) return@launch

            val currentItems = _cartItems.value.toMutableList()
            val existingItem = currentItems.find {
                it.product.productId == productId
            }

            if (existingItem != null) {
                val newQuantity = minOf(
                    existingItem.quantity + quantity,
                    product.quantity
                )

                val index = currentItems.indexOf(existingItem)
                currentItems[index] = existingItem.copy(
                    quantity = newQuantity
                )
            } else {
                currentItems.add(
                    CartItem(
                        product = product,
                        quantity = minOf(quantity, product.quantity)
                    )
                )
            }

            _cartItems.value = currentItems
        }
    }

    // Update the selected quantity
    fun updateQuantity(productId: Long, newQuantity: Int) {
        val currentItems = _cartItems.value.toMutableList()
        val index = currentItems.indexOfFirst {
            it.product.productId == productId
        }

        if (index == -1) return

        if (newQuantity <= 0) {
            currentItems.removeAt(index)
        } else {
            val item = currentItems[index]
            val safeQuantity = minOf(newQuantity, item.product.quantity)

            if (safeQuantity <= 0) {
                currentItems.removeAt(index)
            } else {
                currentItems[index] = item.copy(
                    quantity = safeQuantity
                )
            }
        }

        _cartItems.value = currentItems
    }

    // Remove a product completely
    fun removeFromCart(productId: Long) {
        _cartItems.value = _cartItems.value.filter {
            it.product.productId != productId
        }
    }

    // Empty the cart
    fun clearCart() {
        _cartItems.value = emptyList()
    }
}
