package com.example.furni.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(tableName = "users", indices = [Index(value = ["email"], unique = true)])
data class User (
    @PrimaryKey(autoGenerate = true) val userId: Long? = 0,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val profilePicture: String? = null
)