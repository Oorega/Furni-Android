package com.example.furni.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.furni.data.entity.OrderProduct
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderProductDao {

    @Insert
    suspend fun insertOrderProduct(orderProduct: OrderProduct): Long

    @Insert
    suspend fun insertMultipleOrderProducts(orderProducts: List<OrderProduct>): List<Long>

    @Update
    suspend fun updateOrderProduct(orderProduct: OrderProduct): Int

    @Delete
    suspend fun deleteOrderProduct(orderProduct: OrderProduct): Int

    @Query("""
        DELETE FROM order_products
        WHERE orderId = :orderId
    """)
    suspend fun deleteOrderProductsByOrderId(orderId: Long): Int


    @Query("SELECT * FROM order_products")
    fun getAllOrderProducts(): Flow<List<OrderProduct>>

    @Query("SELECT * FROM order_products WHERE orderProductId = :orderProductId")
    suspend fun getOrderProductById(orderProductId: Long): OrderProduct?

    @Query("SELECT * FROM order_products WHERE orderId = :orderId")
    fun getOrderProductsByOrderId(orderId: Long): Flow<List<OrderProduct>>

    @Query("SELECT * FROM order_products WHERE productId = :productId")
    fun getOrderProductsByProductId(productId: Long): Flow<List<OrderProduct>>


}
