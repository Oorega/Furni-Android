package com.example.furni.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.furni.ui.products.ProductImage

// Picks an icon for a category by name; unknown categories get a generic chair icon.
private fun categoryIcon(name: String): ImageVector = when {
    name.contains("living", ignoreCase = true) -> Icons.Filled.Weekend
    name.contains("dining", ignoreCase = true) -> Icons.Filled.Restaurant
    name.contains("bed", ignoreCase = true) -> Icons.Filled.Bed
    name.contains("outdoor", ignoreCase = true) -> Icons.Filled.Deck
    name.contains("kid", ignoreCase = true) || name.contains("study", ignoreCase = true) -> Icons.Filled.School
    else -> Icons.Filled.Chair
}

@Composable
fun HomeScreen(
    // Called with the tapped category's id so the Shop screen can open pre-filtered.
    onCategoryClick: (Long) -> Unit,
    onProductClick: (Int) -> Unit,
    onSearchClick: () -> Unit,
    onShopNowClick: () -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val categories by viewModel.categories.collectAsState()
    val featured by viewModel.featuredProducts.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Text("Furni", style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Text("Find furniture you'll love", style = MaterialTheme.typography.bodyMedium)
        }

        // Search bar (tapping it opens the Products screen)
        item {
            OutlinedCard(
                modifier = Modifier.fillMaxWidth().clickable { onSearchClick() },
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Search, contentDescription = null)
                    Spacer(Modifier.width(12.dp))
                    Text("Search furniture...")
                }
            }
        }

        // Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(Modifier.padding(24.dp)) {
                    Text("New Season Collection", style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = onShopNowClick,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                    ) { Text("Shop Now") }
                }
            }
        }

        // Categories (from the database)
        if (categories.isNotEmpty()) {
            item { Text("Categories", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(categories, key = { it.categoryId ?: it.name.hashCode().toLong() }) { cat ->
                        Card(
                            modifier = Modifier.clickable { cat.categoryId?.let(onCategoryClick) },
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                        ) {
                            Column(
                                Modifier.padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(categoryIcon(cat.name), contentDescription = cat.name, modifier = Modifier.size(32.dp))
                                Spacer(Modifier.height(8.dp))
                                Text(cat.name)
                            }
                        }
                    }
                }
            }
        }

        // Featured products (from the database)
        if (featured.isNotEmpty()) {
            item { Text("Featured", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(featured, key = { it.productId ?: it.name.hashCode().toLong() }) { p ->
                        Card(
                            modifier = Modifier.width(160.dp).clickable {
                                p.productId?.let { onProductClick(it.toInt()) }
                            }
                        ) {
                            Column(Modifier.padding(12.dp)) {
                                ProductImage(
                                    imageUrl = p.imageUrl,
                                    modifier = Modifier.fillMaxWidth().height(100.dp).clip(RoundedCornerShape(8.dp)),
                                    targetWidthPx = 400
                                )
                                Spacer(Modifier.height(8.dp))
                                Text(p.name, fontWeight = FontWeight.SemiBold)
                                Text("$" + "%.2f".format(p.price), color = MaterialTheme.colorScheme.secondary)
                            }
                        }
                    }
                }
            }
        }
    }
}
