package com.example.furni.ui.products

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.furni.data.database.FurniDatabase
import com.example.furni.data.entity.Category
import com.example.furni.data.entity.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.example.furni.data.seed.DatabaseSeeder

class ProductsViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val db = FurniDatabase.getInstance(application)

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    // null means "All". The Home screen can open Shop with a category already selected:
    // the route argument "categoryId" arrives here through the SavedStateHandle (-1 = none).
    private val _selectedCategoryId = MutableStateFlow<Long?>(
        savedStateHandle.get<Long>("categoryId")?.takeIf { it >= 0 }
    )
    val selectedCategoryId: StateFlow<Long?> = _selectedCategoryId

    val categories: StateFlow<List<Category>> = db.categoryDao().getAllCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // Search and category filter are combined here in memory, so the DAOs stay untouched.
    val products: StateFlow<List<Product>> = combine(
        db.productDao().getAllProducts(),
        _query,
        _selectedCategoryId
    ) { all, query, categoryId ->
        val keyword = query.trim()
        all.filter { product ->
            val matchesCategory = categoryId == null || product.categoryId == categoryId
            val matchesQuery = keyword.isEmpty() ||
                product.name.contains(keyword, ignoreCase = true) ||
                product.description.contains(keyword, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init {
        // Fills the database with sample data on first run (does nothing if data exists).
        viewModelScope.launch { DatabaseSeeder.seedIfEmpty(db) }
    }

    fun onQueryChange(value: String) {
        _query.value = value
    }

    fun onCategorySelected(categoryId: Long?) {
        _selectedCategoryId.value = categoryId
    }
}
