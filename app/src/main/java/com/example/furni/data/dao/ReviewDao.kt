package com.example.furni.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.furni.data.entity.Review
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewDao {

    @Insert
    suspend fun insertReview(review: Review): Long

    @Update
    suspend fun updateReview(review: Review): Int

    @Delete
    suspend fun deleteReview(review: Review): Int

    @Query("""
        DELETE FROM reviews
        WHERE productId = :productId
    """)
    suspend fun deleteReviewsByProductId(productId: Long): Int


    @Query("SELECT * FROM reviews ORDER BY reviewId DESC")
    fun getAllReviews(): Flow<List<Review>>

    @Query("SELECT * FROM reviews WHERE reviewId = :reviewId")
    suspend fun getReviewById(reviewId: Long): Review?

    @Query("""
        SELECT * FROM reviews
        WHERE productId = :productId
        ORDER BY reviewId DESC
    """)
    fun getReviewsByProductId(productId: Long): Flow<List<Review>>

    @Query("""
        SELECT * FROM reviews
        WHERE userId = :userId
        ORDER BY reviewId DESC
    """)
    fun getReviewsByUserId(userId: Long): Flow<List<Review>>


    @Query("""
        SELECT AVG(rating) FROM reviews
        WHERE productId = :productId
    """)
    fun getAverageRatingForProduct(productId: Long): Flow<Double?>

    @Query("""
        SELECT COUNT(*) FROM reviews
        WHERE productId = :productId
    """)
    fun getReviewCountForProduct(productId: Long): Flow<Int>

}
