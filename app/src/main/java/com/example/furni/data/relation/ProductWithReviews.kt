package com.example.furni.data.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.furni.data.entity.Product
import com.example.furni.data.entity.Review

data class ProductWithReviews(
    @Embedded val product: Product,

    @Relation(
        parentColumn = "productId",
        entityColumn = "productId"
    )
    val reviews: List<Review>
)
