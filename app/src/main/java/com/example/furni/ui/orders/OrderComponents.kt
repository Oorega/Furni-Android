package com.example.furni.ui.orders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

fun formatMoney(value: Double): String = "$" + "%.2f".format(value)

@Composable
fun StatusChip(status: String) {
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

@Composable
fun AmountRow(label: String, value: String, bold: Boolean = false) {
    val weight = if (bold) FontWeight.Bold else FontWeight.Normal
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontWeight = weight)
        Text(value, fontWeight = weight)
    }
}

@Composable
fun OrderInfoSection(details: OrderDetails) {
    val order = details.order
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Items", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                details.items.forEach { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(item.productName, fontWeight = FontWeight.SemiBold)
                            Text(
                                "${formatMoney(item.unitPrice)} x ${item.quantity}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Text(formatMoney(item.unitPrice * item.quantity))
                    }
                }
            }
        }

        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Shipping to", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text(order.recipientName, fontWeight = FontWeight.SemiBold)
                Text(order.recipientPhone)
                Text(order.shippingAddress)
            }
        }

        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                AmountRow("Subtotal", formatMoney(order.subTotal))
                AmountRow("Shipping", formatMoney(order.shippingFee))
                HorizontalDivider(Modifier.padding(vertical = 8.dp))
                AmountRow("Total", formatMoney(order.totalAmount), bold = true)
            }
        }
    }
}