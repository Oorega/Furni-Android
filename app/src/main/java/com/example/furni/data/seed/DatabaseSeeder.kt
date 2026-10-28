package com.example.furni.data.seed

import android.util.Log
import com.example.furni.data.database.FurniDatabase
import com.example.furni.data.entity.Category
import com.example.furni.data.entity.Product
import com.example.furni.data.entity.Review
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Inserts sample categories, products and reviews, but only when the database has no
 * categories and no products yet. Safe to call on every launch.
 * imageUrl is the path inside app/src/main/assets, e.g. "product/fancy_sofa.jpg".
 *
 * Debug logging is included: filter Logcat by the tag "FurniSeed".
 */
object DatabaseSeeder {

    private const val TAG = "FurniSeed"
    private val mutex = Mutex()

    private data class SeedProduct(
        val category: String,
        val name: String,
        val description: String,
        val price: Double,
        val quantity: Int,
        val image: String
    )

    private val categories = listOf(
        "Living Room" to "Sofas, chairs and everything for the living room.",
        "Dining Room" to "Tables and seating for family meals.",
        "Bedroom" to "Beds, lighting and bedroom essentials.",
        "Outdoors" to "Garden and patio furniture built for the outdoors.",
        "Kids & Study" to "Desks and furniture for kids and studying."
    )

    private val products = listOf(
        SeedProduct("Living Room", "Corner Couch", "A spacious L-shaped corner couch with deep cushions, perfect for family movie nights.", 1199.0, 3, "product/corner_couch.jpg"),
        SeedProduct("Living Room", "Fancy Sofa", "An elegant sofa with a tufted back and soft upholstery that adds style to any room.", 999.0, 4, "product/fancy_sofa.jpg"),
        SeedProduct("Living Room", "Grey Couch", "A modern three-seater grey couch with a clean design and supportive cushions.", 849.0, 6, "product/grey_couch.jpg"),
        SeedProduct("Living Room", "Fabric Chair", "A comfortable armchair with soft fabric upholstery and sturdy wooden legs.", 249.0, 12, "product/fabric_chair.jpg"),
        SeedProduct("Living Room", "Rocking Chair", "A classic wooden rocking chair with a smooth, relaxing motion.", 279.0, 6, "product/rocking_chair.jpg"),
        SeedProduct("Dining Room", "Dining Room Table", "A large dining table that comfortably seats six, with a durable finish.", 749.0, 5, "product/dining_room_table.jpg"),
        SeedProduct("Dining Room", "Wooden Table", "A simple solid wood table that works in the kitchen, dining room or hallway.", 329.0, 9, "product/wooden_table.jpg"),
        SeedProduct("Bedroom", "Bedside Lamp", "A warm-light bedside lamp with a fabric shade and a stable base.", 59.0, 20, "product/bedside_lamp.jpg"),
        SeedProduct("Outdoors", "Outdoor Set", "A weather-resistant outdoor seating set for your garden or patio.", 599.0, 4, "product/outdoor_set.jpg"),
        SeedProduct("Outdoors", "Fabric Hammock", "A soft fabric hammock for relaxing outside, easy to hang and fold.", 129.0, 0, "product/fabric_hammock.jpg"),
        SeedProduct("Kids & Study", "Kindergarten Desks", "A set of child-sized desks designed for classrooms and play rooms.", 399.0, 7, "product/kindergarten_desks.jpg"),
        SeedProduct("Kids & Study", "Pink Studying Desk", "A bright pink desk with a roomy surface for homework and study time.", 219.0, 10, "product/pink_studying_desk.jpg")
    )

    // Sample reviews so the rating UI has something to show. userId is null (no user attached).
    private val reviews = listOf(
        Triple("Fancy Sofa", 5, "Very comfortable and looks great in our living room."),
        Triple("Fancy Sofa", 4, "Good quality. Delivery took a little longer than expected."),
        Triple("Corner Couch", 4, "Plenty of space for everyone, easy to assemble."),
        Triple("Rocking Chair", 5, "Smooth and sturdy. My favorite spot in the house.")
    )

    suspend fun seedIfEmpty(db: FurniDatabase) {
        mutex.withLock {
            try {
                Log.d(TAG, "seedIfEmpty called")
                val existingCategories = db.categoryDao().getAllCategories().first()
                val productCount = db.productDao().getAllProducts().first().size
                Log.d(
                    TAG,
                    "found ${existingCategories.size} categories ${existingCategories.map { it.name }} and $productCount products"
                )
                if (productCount > 0) {
                    Log.d(TAG, "products already exist, skipping")
                    return@withLock
                }

                // Reuse categories that already exist (e.g. left over from an earlier partial seed).
                val categoryIds = HashMap<String, Long>()
                for ((name, description) in categories) {
                    val existingId = db.categoryDao().getCategoryByName(name)?.categoryId
                    categoryIds[name] = existingId ?: db.categoryDao().insertCategory(
                        Category(categoryId = null, name = name, description = description)
                    )
                }

                val productIds = HashMap<String, Long>()
                for (p in products) {
                    productIds[p.name] = db.productDao().insertProduct(
                        Product(
                            productId = null,
                            categoryId = categoryIds[p.category],
                            name = p.name,
                            description = p.description,
                            price = p.price,
                            quantity = p.quantity,
                            imageUrl = p.image
                        )
                    )
                }

                for ((productName, rating, comment) in reviews) {
                    db.reviewDao().insertReview(
                        Review(
                            reviewId = null,
                            userId = null,
                            productId = productIds[productName],
                            rating = rating,
                            comment = comment
                        )
                    )
                }
                Log.d(TAG, "seeding finished: ${categoryIds.size} categories, ${productIds.size} products")
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "seeding failed", e)
            }
        }
    }
}
