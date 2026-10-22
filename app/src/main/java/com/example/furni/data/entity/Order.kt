package com.example.furni.data.entity


import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.room.Index

@Entity(tableName = "orders",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["userId"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("userId")]
)
data class Order (
    @PrimaryKey(autoGenerate = true) val orderId: Long? = 0,
    val userId: Long?,
    val status : String = "Pending",
    val recipientName: String,
    val recipientPhone: String,
    val shippingAddress: String,
    val subTotal: Double,
    val shippingFee: Double = 0.0,
    val totalAmount: Double,
    val orderDate: String = SimpleDateFormat(
        "yyyy-MM-dd HH:mm:ss",
        Locale.getDefault()
        ).format(Date())
    )