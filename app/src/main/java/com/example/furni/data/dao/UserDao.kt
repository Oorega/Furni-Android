package com.example.furni.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.furni.data.relation.UserWithOrders
import com.example.furni.data.relation.UserWithReviews
import com.example.furni.data.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert
    suspend fun insertUser(user: User): Long

    @Update
    suspend fun updateUser(user: User): Int

    @Query("""
        UPDATE users
        SET firstName = :firstName,
            lastName = :lastName,
            email = :email,
            profilePicture = :profilePicture
        WHERE userId = :userId
    """)
    suspend fun updateUserProfile(
        userId: Long,
        firstName: String,
        lastName: String,
        email: String,
        profilePicture: String?
    ): Int

    @Delete
    suspend fun deleteUser(user: User): Int


    @Query("SELECT * FROM users ORDER BY firstName ASC")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM users WHERE userId = :userId")
    suspend fun getUserById(userId: Long): User?


    @Transaction
    @Query("SELECT * FROM users WHERE userId = :userId")
    suspend fun getUserWithOrders(userId: Long): UserWithOrders?

    @Transaction
    @Query("SELECT * FROM users WHERE userId = :userId")
    suspend fun getUserWithReviews(userId: Long): UserWithReviews?
}
