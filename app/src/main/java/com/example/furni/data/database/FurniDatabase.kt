package com.example.furni.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.furni.data.dao.CategoryDao
import com.example.furni.data.dao.OrderDao
import com.example.furni.data.dao.OrderProductDao
import com.example.furni.data.dao.ProductDao
import com.example.furni.data.dao.ReviewDao
import com.example.furni.data.dao.UserDao
import com.example.furni.data.entity.Category
import com.example.furni.data.entity.Order
import com.example.furni.data.entity.OrderProduct
import com.example.furni.data.entity.Product
import com.example.furni.data.entity.Review
import com.example.furni.data.entity.User


@Database(entities =
    [Category::class, Product::class, User::class, Order::class, OrderProduct::class, Review::class],
    version = 1, exportSchema = true)
abstract class FurniDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun categoryDao(): CategoryDao
    abstract fun productDao(): ProductDao
    abstract fun orderDao(): OrderDao
    abstract fun orderProductDao(): OrderProductDao
    abstract fun reviewDao(): ReviewDao

    companion object {
        @Volatile
        private var INSTANCE: FurniDatabase? = null

        fun getInstance(context: Context): FurniDatabase {
            return INSTANCE ?: synchronized(FurniDatabase::class.java) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    FurniDatabase::class.java,
                    "furni_database"
                ).build().also { INSTANCE = it }
            }
        }
    }

}