package com.example.furni.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(tableName = "reviews",
    foreignKeys = [
        ForeignKey(
        entity = User::class,
        parentColumns = ["userId"],
        childColumns = ["userId"],
        onDelete = ForeignKey.SET_NULL
    ),
    ForeignKey(
        entity = Product::class,
        parentColumns = ["productId"],
        childColumns = ["productId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("userId"), Index("productId")]
)
data class Review (
    @PrimaryKey(autoGenerate = true) val reviewId: Long? = null,
    val userId: Long?,
    val productId: Long?,
    val rating: Int,
    val comment: String

)