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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// Static sample data, for layout only.
private data class ProductUi(val id: Int, val name: String, val price: Double, val category: String)

private val sampleProducts = listOf(
    ProductUi(1, "Modern Sofa", 899.0, "Living Room"),
    ProductUi(2, "Coffee Table", 199.0, "Living Room"),
    ProductUi(3, "Oak Bed Frame", 649.0, "Bedroom"),
    ProductUi(4, "Wardrobe", 549.0, "Bedroom"),
    ProductUi(5, "Garden Lounge Set", 499.0, "Outdoors"),
    ProductUi(6, "Patio Table", 299.0, "Outdoors")
)

private val categoryNames = listOf("All", "Living Room", "Bedroom", "Outdoors")

@Composable
fun ProductsScreen(onProductClick: (Int) -> Unit) {
    var query by remember { mutableStateOf("") }
    var selected by remember { mutableStateOf("All") }

    Column(Modifier.fillMaxSize().statusBarsPadding().padding(horizontal = 16.dp)) {
        Spacer(Modifier.height(16.dp))
        Text("Shop", style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = query, onValueChange = { query = it },
            placeholder = { Text("Search furniture...") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            singleLine = true, modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(categoryNames) { name ->
                FilterChip(selected = selected == name, onClick = { selected = name },
                    label = { Text(name) })
            }
        }
        Spacer(Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp)) {
            items(sampleProducts, key = { it.id }) { p ->
                Card(Modifier.fillMaxWidth().clickable { onProductClick(p.id) }) {
                    Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            modifier = Modifier.size(80.dp),
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(8.dp)
                        ) {}
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(p.name, fontWeight = FontWeight.SemiBold)
                            Text(p.category, style = MaterialTheme.typography.bodySmall)
                            Text("$${p.price}", color = MaterialTheme.colorScheme.secondary)
                        }
                    }
                }
            }
        }
    }
}
