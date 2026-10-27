package com.example.furni.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(tableName = "categories", indices = [Index(value = ["name"], unique = true)])
data class Category (
    @PrimaryKey(autoGenerate = true) val categoryId: Long? = 0,
    val name: String,
    val description: String
)