package com.example.furni.ui.products

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.furni.data.database.FurniDatabase
import com.example.furni.data.entity.Product
import com.example.furni.data.entity.Review
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ProductDetailsState(
    val loading: Boolean = true,
    val product: Product? = null,
    val categoryName: String? = null
)

class ProductDetailsViewModel(
    application: Application,
    private val productId: Long
) : AndroidViewModel(application) {

    private val db = FurniDatabase.getInstance(application)

    // TODO (Member 6): replace with the logged-in user's id once auth exposes it.
    // null is valid: Review.userId is nullable and the foreign key is SET_NULL.
    private val currentUserId: Long? = null

    private val _state = MutableStateFlow(ProductDetailsState())
    val state: StateFlow<ProductDetailsState> = _state

    val reviews: StateFlow<List<Review>> = db.reviewDao().getReviewsByProductId(productId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val averageRating: StateFlow<Double?> = db.reviewDao().getAverageRatingForProduct(productId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val reviewCount: StateFlow<Int> = db.reviewDao().getReviewCountForProduct(productId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    init {
        viewModelScope.launch {
            val product = db.productDao().getProductById(productId)
            val category = product?.categoryId?.let { db.categoryDao().getCategoryById(it) }
            _state.value = ProductDetailsState(
                loading = false,
                product = product,
                categoryName = category?.name
            )
        }
    }

    fun submitReview(rating: Int, comment: String) {
        if (rating !in 1..5 || comment.isBlank()) return
        viewModelScope.launch {
            db.reviewDao().insertReview(
                Review(
                    userId = currentUserId,
                    productId = productId,
                    rating = rating,
                    comment = comment.trim()
                )
            )
        }
    }

    companion object {
        fun factory(productId: Long): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application
                ProductDetailsViewModel(app, productId)
            }
        }
    }
}
