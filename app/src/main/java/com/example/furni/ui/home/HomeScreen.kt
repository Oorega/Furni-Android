
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// Temporary UI-only data. Real products come from Member 2/4 via Room later.
private data class HomeCategory(val name: String, val icon: ImageVector)
private data class FeaturedProduct(val id: Int, val name: String, val price: Double)

private val categories = listOf(
    HomeCategory("Living Room", Icons.Filled.Weekend),
    HomeCategory("Bedroom", Icons.Filled.Bed),
    HomeCategory("Outdoors", Icons.Filled.Deck)
)

private val featured = listOf(
    FeaturedProduct(1, "Modern Sofa", 899.0),
    FeaturedProduct(2, "Oak Bed Frame", 649.0),
    FeaturedProduct(3, "Garden Lounge Set", 499.0),
    FeaturedProduct(4, "Coffee Table", 199.0)
)

@Composable
fun HomeScreen(
    onCategoryClick: (String) -> Unit,
    onProductClick: (Int) -> Unit,
    onSearchClick: () -> Unit,
    onShopNowClick: () -> Unit
) {
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

        // Categories
        item { Text("Categories", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(categories) { cat ->
                    Card(
                        modifier = Modifier.clickable { onCategoryClick(cat.name) },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Column(
                            Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(cat.icon, contentDescription = cat.name, modifier = Modifier.size(32.dp))
                            Spacer(Modifier.height(8.dp))
                            Text(cat.name)
                        }
                    }
                }
            }
        }

        // Featured products
        item { Text("Featured", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(featured) { p ->
                    Card(
                        modifier = Modifier.width(160.dp).clickable { onProductClick(p.id) }
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            Surface(
                                modifier = Modifier.fillMaxWidth().height(100.dp),
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(8.dp)
                            ) {}  // image placeholder
                            Spacer(Modifier.height(8.dp))
                            Text(p.name, fontWeight = FontWeight.SemiBold)
                            Text("$${p.price}", color = MaterialTheme.colorScheme.secondary)
                        }
                    }
                }
            }
        }
    }
}
