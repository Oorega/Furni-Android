package com.example.furni.ui.products

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

// onProductClick keeps the Int signature the NavHost already uses (route argument is IntType).
@Composable
fun ProductsScreen(
    onProductClick: (Int) -> Unit,
    viewModel: ProductsViewModel = viewModel()
) {
    val query by viewModel.query.collectAsState()
    val selectedCategoryId by viewModel.selectedCategoryId.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val products by viewModel.products.collectAsState()

    val categoryNameById = remember(categories) {
        categories.associate { it.categoryId to it.name }
    }

    Column(Modifier.fillMaxSize().statusBarsPadding().padding(horizontal = 16.dp)) {
        Spacer(Modifier.height(16.dp))
        Text(
            "Shop", style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = query, onValueChange = viewModel::onQueryChange,
            placeholder = { Text("Search furniture...") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            singleLine = true, modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
                FilterChip(
                    selected = selectedCategoryId == null,
                    onClick = { viewModel.onCategorySelected(null) },
                    label = { Text("All") }
                )
            }
            items(categories, key = { it.categoryId ?: it.name.hashCode().toLong() }) { category ->
                FilterChip(
                    selected = selectedCategoryId == category.categoryId,
                    onClick = { viewModel.onCategorySelected(category.categoryId) },
                    label = { Text(category.name) }
                )
            }
        }
        Spacer(Modifier.height(8.dp))

        if (products.isEmpty()) {
            val filtering = query.isNotBlank() || selectedCategoryId != null
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    if (filtering) "No products match your search." else "No products available yet.",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(products, key = { it.productId ?: it.name.hashCode().toLong() }) { p ->
                    Card(Modifier.fillMaxWidth().clickable {
                        p.productId?.let { onProductClick(it.toInt()) }
                    }) {
                        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            ProductImage(
                                imageUrl = p.imageUrl,
                                modifier = Modifier.size(80.dp).clip(RoundedCornerShape(8.dp)),
                                targetWidthPx = 300
                            )
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Text(p.name, fontWeight = FontWeight.SemiBold)
                                Text(
                                    categoryNameById[p.categoryId] ?: "",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(
                                    "$" + "%.2f".format(p.price),
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                if (p.quantity <= 0) {
                                    Text(
                                        "Out of stock",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
