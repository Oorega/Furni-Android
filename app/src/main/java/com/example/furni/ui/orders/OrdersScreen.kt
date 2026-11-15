package com.example.furni.ui.orders

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun OrdersScreen(
    onOrderClick: (Long) -> Unit,
    viewModel: OrdersViewModel = viewModel()
) {
    val orders by viewModel.orders.collectAsState()

    LaunchedEffect(Unit) { viewModel.refreshUser() }

    Column(Modifier.fillMaxSize().statusBarsPadding()) {
        Text(
            "My Orders",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp)
        )

        if (orders.isEmpty()) {
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text("You have no orders yet", style = MaterialTheme.typography.titleLarge)
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(orders, key = { it.order.orderId ?: 0L }) { summary ->
                    val order = summary.order
                    Card(
                        Modifier
                            .fillMaxWidth()
                            .clickable { order.orderId?.let(onOrderClick) }
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Order #${order.orderId}", fontWeight = FontWeight.Bold)
                                StatusChip(order.status)
                            }
                            Spacer(Modifier.height(4.dp))
                            Text(order.orderDate, style = MaterialTheme.typography.bodySmall)
                            Spacer(Modifier.height(12.dp))
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("${summary.itemCount} item(s)")
                                Text(
                                    formatMoney(order.totalAmount),
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}