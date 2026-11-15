package com.example.furni.ui.orders

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.furni.data.database.FurniDatabase
import com.example.furni.data.entity.Order
import com.example.furni.util.SessionManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

data class OrderSummary(val order: Order, val itemCount: Int)

@OptIn(ExperimentalCoroutinesApi::class)
class OrdersViewModel(application: Application) : AndroidViewModel(application) {

    private val db = FurniDatabase.getInstance(application)

    private val currentUserId = MutableStateFlow(SessionManager(application).getUserId())

    val orders: StateFlow<List<OrderSummary>> = currentUserId
        .flatMapLatest { userId ->
            if (userId == null) {
                flowOf(emptyList<OrderSummary>())
            } else {
                combine(
                    db.orderDao().getOrdersByUserId(userId),
                    db.orderProductDao().getAllOrderProducts()
                ) { orders, lines ->
                    val unitsByOrder = lines
                        .groupBy { it.orderId }
                        .mapValues { (_, items) -> items.sumOf { it.quantity } }
                    orders.map { OrderSummary(it, unitsByOrder[it.orderId] ?: 0) }
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // Called when the screen opens, so a different user who logged in sees their own orders
    fun refreshUser() {
        currentUserId.value = SessionManager(getApplication<Application>()).getUserId()
    }
}