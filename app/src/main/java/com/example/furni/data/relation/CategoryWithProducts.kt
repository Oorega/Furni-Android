package com.example.furni.data.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.furni.data.entity.Category
import com.example.furni.data.entity.Product

data class CategoryWithProducts(
    @Embedded val category: Category,

    @Relation(
        parentColumn = "categoryId",
        entityColumn = "categoryId"
    )
    val products: List<Product>
)
