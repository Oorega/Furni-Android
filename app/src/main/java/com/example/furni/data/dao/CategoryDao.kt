package com.example.furni.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.furni.data.relation.CategoryWithProducts
import com.example.furni.data.entity.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Insert
    suspend fun insertCategory(category: Category): Long

    @Update
    suspend fun updateCategory(category: Category): Int

    @Delete
    suspend fun deleteCategory(category: Category): Int


    @Query("""
        SELECT * FROM categories
        ORDER BY name ASC
    """)
    fun getAllCategories(): Flow<List<Category>>

    @Query("""
        SELECT * FROM categories
        WHERE categoryId = :categoryId
    """)
    suspend fun getCategoryById(categoryId: Long): Category?

    @Query("""
        SELECT * FROM categories
        WHERE name = :name
    """)
    suspend fun getCategoryByName(name: String): Category?


    @Query("""
        SELECT * FROM categories
        WHERE name LIKE '%' || :keyword || '%'
           OR description LIKE '%' || :keyword || '%'
        ORDER BY name ASC
    """)
    fun searchCategories(keyword: String): Flow<List<Category>>


    @Transaction
    @Query("SELECT * FROM categories WHERE categoryId = :categoryId")
    suspend fun getCategoryWithProducts(categoryId: Long): CategoryWithProducts?

}
