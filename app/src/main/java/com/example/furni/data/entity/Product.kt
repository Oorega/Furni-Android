package com.example.furni.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(tableName = "products",
    foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = ["categoryId"],
        childColumns = ["categoryId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("categoryId")]
)
data class Product (
    @PrimaryKey(autoGenerate = true) val productId: Long? = 0,
    val categoryId: Long?,
    val name: String,
    val description: String,
    val price: Double,
    val quantity: Int,
    val imageUrl: String? = null
)
