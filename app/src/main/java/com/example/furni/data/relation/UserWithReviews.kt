package com.example.furni.data.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.furni.data.entity.Review
import com.example.furni.data.entity.User

data class UserWithReviews(
    @Embedded val user: User,

    @Relation(
        parentColumn = "userId",
        entityColumn = "userId"
    )
    val reviews: List<Review>
)
