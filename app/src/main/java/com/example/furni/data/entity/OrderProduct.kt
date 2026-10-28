package com.example.furni.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(tableName = "order_products",
    foreignKeys = [
        ForeignKey(
            entity = Order::class,
            parentColumns = ["orderId"],
            childColumns = ["orderId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Product::class,
            parentColumns = ["productId"],
            childColumns = ["productId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("orderId"), Index("productId")]
)
data class OrderProduct (
    @PrimaryKey(autoGenerate = true) val orderProductId: Long? = null,
    val orderId: Long?,
    val productId: Long?,
    val productName: String,
    val unitPrice: Double,
    val quantity: Int

)
