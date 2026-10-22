package com.example.furni.data.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.furni.data.entity.OrderProduct
import com.example.furni.data.entity.Product

data class ProductWithOrderProducts(
    @Embedded val product: Product,

    @Relation(
        parentColumn = "productId",
        entityColumn = "productId"
    )
    val orderProducts: List<OrderProduct>
)
