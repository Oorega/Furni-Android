package com.example.furni.ui.checkout

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.withTransaction
import com.example.furni.data.database.FurniDatabase
import com.example.furni.data.entity.Order
import com.example.furni.data.entity.OrderProduct
import com.example.furni.ui.cart.CartItem
import com.example.furni.util.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CheckoutUiState(
    val isPlacing: Boolean = false,
    val error: String? = null,
    val orderId: Long? = null
)

class CheckoutViewModel(application: Application) : AndroidViewModel(application) {
    private val db = FurniDatabase.getInstance(application)

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState

    fun placeOrder(
        items: List<CartItem>,
        recipientName: String,
        recipientPhone: String,
        shippingAddress: String
    ) {
        if (_uiState.value.isPlacing) return

        val userId = SessionManager(getApplication<Application>()).getUserId()
        if (userId == null) {
            _uiState.update { it.copy(error = "Please log in again to place an order") }
            return
        }
        if (items.isEmpty()) {
            _uiState.update { it.copy(error = "Your cart is empty") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isPlacing = true, error = null) }

            try {
                val subTotal = items.sumOf { it.product.price * it.quantity }

                val newOrderId = db.withTransaction {
                    val orderId = db.orderDao().insertOrder(
                        Order(
                            userId = userId,
                            recipientName = recipientName,
                            recipientPhone = recipientPhone,
                            shippingAddress = shippingAddress,
                            subTotal = subTotal,
                            shippingFee = SHIPPING_FEE,
                            totalAmount = subTotal + SHIPPING_FEE
                        )
                    )

                    db.orderProductDao().insertMultipleOrderProducts(
                        items.map {
                            OrderProduct(
                                orderId = orderId,
                                productId = it.product.productId,
                                productName = it.product.name,
                                unitPrice = it.product.price,
                                quantity = it.quantity
                            )
                        }
                    )

                    items.forEach { item ->
                        val updatedRows = db.productDao()
                            .decreaseStock(item.product.productId!!, item.quantity)
                        if (updatedRows == 0) {
                            throw IllegalStateException(
                                "Not enough stock for ${item.product.name}"
                            )
                        }
                    }

                    orderId
                }

                _uiState.update { it.copy(isPlacing = false, orderId = newOrderId) }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isPlacing = false, error = e.message ?: "Could not place the order")
                }
            }
        }
    }
    companion object {
        const val SHIPPING_FEE = 25.0
    }
}