package com.example.furni.ui.orders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// Static sample data, for layout only. Member 5 replaces it with real orders.
private data class OrderUi(
    val id: String,
    val date: String,
    val status: String,
    val itemCount: Int,
    val total: Double
)

private val sampleOrders = listOf(
    OrderUi("#1004", "27 Oct 2026", "Processing", 3, 1772.0),
    OrderUi("#1003", "12 Oct 2026", "Delivered", 1, 649.0),
    OrderUi("#1002", "02 Sep 2026", "Delivered", 2, 798.0)
)

private fun money(value: Double) = "$" + "%.2f".format(value)

@Composable
fun OrdersScreen() {
    Column(Modifier.fillMaxSize().statusBarsPadding()) {
        Text(
            "My Orders",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp)
        )

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(sampleOrders, key = { it.id }) { order ->
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Order ${order.id}", fontWeight = FontWeight.Bold)
                            StatusChip(order.status)
                        }
                        Spacer(Modifier.height(4.dp))
                        Text(order.date, style = MaterialTheme.typography.bodySmall)
                        Spacer(Modifier.height(12.dp))
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("${order.itemCount} item(s)")
                            Text(money(order.total), fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.secondary)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusChip(status: String) {
    val color = if (status == "Delivered") MaterialTheme.colorScheme.primaryContainer
    else MaterialTheme.colorScheme.secondary.copy(alpha = 0.25f)
    Surface(color = color, shape = RoundedCornerShape(50)) {
        Text(
            status,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
}
