package com.example.furni.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.furni.data.relation.OrderWithOrderProducts
import com.example.furni.data.entity.Order
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {

    @Insert
    suspend fun insertOrder(order: Order): Long

    @Update
    suspend fun updateOrder(order: Order): Int

    @Delete
    suspend fun deleteOrder(order: Order): Int


    @Query("SELECT * FROM orders ORDER BY orderDate DESC")
    fun getAllOrders(): Flow<List<Order>>

    @Query("SELECT * FROM orders WHERE orderId = :orderId")
    suspend fun getOrderById(orderId: Long): Order?

    @Query("""
        SELECT * FROM orders
        WHERE userId = :userId
        ORDER BY orderDate DESC
    """)
    fun getOrdersByUserId(userId: Long): Flow<List<Order>>

    @Query("""
        SELECT * FROM orders
        WHERE status = :status
        ORDER BY orderDate DESC
    """)
    fun getOrdersByStatus(status: String): Flow<List<Order>>


    @Query("""
        UPDATE orders
        SET status = :status
        WHERE orderId = :orderId
    """)
    suspend fun updateOrderStatus(
        orderId: Long,
        status: String
    ): Int


    @Transaction
    @Query("SELECT * FROM orders WHERE orderId = :orderId")
    suspend fun getOrderWithOrderProducts(orderId: Long): OrderWithOrderProducts?


}
