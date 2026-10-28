package com.example.furni.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.furni.data.relation.ProductWithOrderProducts
import com.example.furni.data.relation.ProductWithReviews
import com.example.furni.data.entity.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Insert
    suspend fun insertProduct(product: Product): Long

    @Update
    suspend fun updateProduct(product: Product): Int

    @Delete
    suspend fun deleteProduct(product: Product): Int


    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE productId = :productId")
    suspend fun getProductById(productId: Long): Product?

    @Query("SELECT * FROM products WHERE categoryId = :categoryId")
    fun getProductsByCategory(categoryId: Long): Flow<List<Product>>


    @Query("""
        SELECT * FROM products
        WHERE name LIKE '%' || :keyword || '%'
           OR description LIKE '%' || :keyword || '%'
    """)
    fun searchProducts(keyword: String): Flow<List<Product>>


    @Query("SELECT * FROM products ORDER BY name ASC")
    fun getProductsSortedByNameAsc(): Flow<List<Product>>

    @Query("SELECT * FROM products ORDER BY name DESC")
    fun getProductsSortedByNameDesc(): Flow<List<Product>>

    @Query("SELECT * FROM products ORDER BY price ASC")
    fun getProductsSortedByPriceAsc(): Flow<List<Product>>

    @Query("SELECT * FROM products ORDER BY price DESC")
    fun getProductsSortedByPriceDesc(): Flow<List<Product>>

    @Query("""
        SELECT products.*
        FROM products
        INNER JOIN categories
            ON products.categoryId = categories.categoryId
        ORDER BY categories.name ASC, products.name ASC
    """)
    fun getProductsSortedByCategory(): Flow<List<Product>>


    @Query("SELECT * FROM products WHERE quantity > 0")
    fun getProductsInStock(): Flow<List<Product>>

    @Query("""
        SELECT * FROM products
        WHERE quantity = 0
    """)
    fun getOutOfStockProducts(): Flow<List<Product>>


    @Query("""
        UPDATE products
        SET quantity = quantity - :amount
        WHERE productId = :productId
          AND :amount > 0
          AND quantity >= :amount
    """)
    suspend fun decreaseStock(productId: Long, amount: Int): Int

    @Query("""
        UPDATE products
        SET quantity = quantity + :amount
        WHERE productId = :productId
          AND :amount > 0
    """)
    suspend fun increaseStock(productId: Long, amount: Int): Int


    @Transaction
    @Query("SELECT * FROM products WHERE productId = :productId")
    suspend fun getProductWithOrderProducts(productId: Long): ProductWithOrderProducts?

    @Transaction
    @Query("SELECT * FROM products WHERE productId = :productId")
    suspend fun getProductWithReviews(productId: Long): ProductWithReviews?

}
