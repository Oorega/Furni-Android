package com.example.furni.ui.orders

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.furni.data.database.FurniDatabase
import com.example.furni.data.entity.Order
import com.example.furni.data.entity.OrderProduct
import com.example.furni.util.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class OrderDetails(val order: Order, val items: List<OrderProduct>)

data class OrderDetailsUiState(
    val isLoading: Boolean = true,
    val details: OrderDetails? = null
)

class OrderDetailsViewModel(application: Application) : AndroidViewModel(application) {
    private val db = FurniDatabase.getInstance(application)

    private val _uiState = MutableStateFlow(OrderDetailsUiState())
    val uiState: StateFlow<OrderDetailsUiState> = _uiState

    fun load(orderId: Long) {
        viewModelScope.launch {
            _uiState.value = OrderDetailsUiState(isLoading = true)

            val order = db.orderDao().getOrderById(orderId)
            val userId = SessionManager(getApplication<Application>()).getUserId()

            if (order == null || order.userId != userId) {
                _uiState.value = OrderDetailsUiState(isLoading = false, details = null)
                return@launch
            }

            val items = db.orderProductDao().getOrderProductsByOrderId(orderId).first()
            _uiState.value = OrderDetailsUiState(isLoading = false, details = OrderDetails(order, items))
        }
    }
}