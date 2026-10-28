package com.example.furni.data.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.furni.data.entity.Order
import com.example.furni.data.entity.OrderProduct

data class OrderWithOrderProducts (
    @Embedded val order: Order,

    @Relation(
        parentColumn = "orderId",
        entityColumn = "orderId"
    )
    val orderProducts: List<OrderProduct>

)