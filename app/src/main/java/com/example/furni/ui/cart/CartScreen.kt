
package com.example.furni.ui.cart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// Static sample data, for layout only. Member 3 replaces it with real cart data.
private data class CartItemUi(val id: Int, val name: String, val price: Double, val quantity: Int)

private val sampleCart = listOf(
    CartItemUi(1, "Modern Sofa", 899.0, 1),
    CartItemUi(2, "Coffee Table", 199.0, 2),
    CartItemUi(3, "Oak Bed Frame", 649.0, 1)
)

private fun money(value: Double) = "$" + "%.2f".format(value)

@Composable
fun CartScreen(onCheckoutClick: () -> Unit) {
    val subtotal = sampleCart.sumOf { it.price * it.quantity }
    val shipping = 25.0
    val total = subtotal + shipping

    Column(Modifier.fillMaxSize().statusBarsPadding()) {
        Text(
            "My Cart",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(sampleCart, key = { it.id }) { item ->
                Card(Modifier.fillMaxWidth()) {
                    Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            modifier = Modifier.size(80.dp),
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(8.dp)
                        ) {} // image placeholder
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(item.name, fontWeight = FontWeight.SemiBold)
                            Text(money(item.price), color = MaterialTheme.colorScheme.secondary)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = {}) {
                                    Icon(Icons.Filled.Remove, contentDescription = "Decrease")
                                }
                                Text("${item.quantity}", fontWeight = FontWeight.Bold)
                                IconButton(onClick = {}) {
                                    Icon(Icons.Filled.Add, contentDescription = "Increase")
                                }
                            }
                        }
                        IconButton(onClick = {}) {
                            Icon(Icons.Filled.Delete, contentDescription = "Remove",
                                tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }

        // Order summary
        Surface(tonalElevation = 3.dp, shadowElevation = 8.dp) {
            Column(Modifier.fillMaxWidth().padding(16.dp)) {
                SummaryRow("Subtotal", money(subtotal))
                SummaryRow("Shipping", money(shipping))
                HorizontalDivider(Modifier.padding(vertical = 8.dp))
                SummaryRow("Total", money(total), bold = true)
                Spacer(Modifier.height(12.dp))
                Button(onClick = onCheckoutClick, modifier = Modifier.fillMaxWidth()) {
                    Text("Proceed to Checkout")
                }
            }
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String, bold: Boolean = false) {
    Row(Modifier.fillMaxWidth().padding(vertical = 2.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal)
        Text(value, fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal)
    }
}