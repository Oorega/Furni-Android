package com.example.furni.data.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.furni.data.entity.Order
import com.example.furni.data.entity.User

data class UserWithOrders(
    @Embedded val user: User,

    @Relation(
        parentColumn = "userId",
        entityColumn = "userId"
    )
    val orders: List<Order>
)
