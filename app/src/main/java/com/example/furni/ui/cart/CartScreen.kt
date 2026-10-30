
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private fun money(value: Double): String {
    return "$" + "%.2f".format(value)
}

@Composable
fun CartScreen(
    onCheckoutClick: () -> Unit,
    cartViewModel: CartViewModel
) {
    // Observe the real cart data
    val cartItems by cartViewModel.cartItems.collectAsState()

    // Calculate prices
    val subtotal = cartItems.sumOf {
        it.product.price * it.quantity
    }

    val shipping = if (cartItems.isEmpty()) 0.0 else 25.0
    val total = subtotal + shipping

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {

        // Screen title
        Text(
            text = "My Cart",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(
                start = 16.dp,
                top = 16.dp,
                end = 16.dp,
                bottom = 8.dp
            )
        )

        // Empty cart message
        if (cartItems.isEmpty()) {

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Your cart is empty",
                    style = MaterialTheme.typography.titleLarge
                )
            }

        } else {

            // Cart products list
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(
                    horizontal = 16.dp,
                    vertical = 8.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = cartItems,
                    key = { it.product.productId ?: 0L }
                ) { item ->

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            // Product image placeholder
                            Surface(
                                modifier = Modifier.size(80.dp),
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(8.dp)
                            ) {}

                            Spacer(Modifier.width(12.dp))

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = item.product.name,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Text(
                                    text = money(item.product.price),
                                    color = MaterialTheme.colorScheme.secondary
                                )

                                // Quantity controls
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(
                                        onClick = {
                                            cartViewModel.updateQuantity(
                                                item.product.productId!!,
                                                item.quantity - 1
                                            )
                                        }
                                    ) {
                                        Icon(
                                            Icons.Filled.Remove,
                                            contentDescription = "Decrease"
                                        )
                                    }

                                    Text(
                                        text = "${item.quantity}",
                                        fontWeight = FontWeight.Bold
                                    )

                                    IconButton(
                                        onClick = {
                                            cartViewModel.updateQuantity(
                                                item.product.productId!!,
                                                item.quantity + 1
                                            )
                                        },
                                        enabled = item.quantity < item.product.quantity
                                    ) {
                                        Icon(
                                            Icons.Filled.Add,
                                            contentDescription = "Increase"
                                        )
                                    }
                                }

                                // Product total price
                                Text(
                                    text = "Item total: ${
                                        money(item.product.price * item.quantity)
                                    }",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            // Delete product
                            IconButton(
                                onClick = {
                                    cartViewModel.removeFromCart(
                                        item.product.productId!!
                                    )
                                }
                            ) {
                                Icon(
                                    Icons.Filled.Delete,
                                    contentDescription = "Remove",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }

        // Order summary
        Surface(
            tonalElevation = 3.dp,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                SummaryRow(
                    "Subtotal",
                    money(subtotal)
                )

                SummaryRow(
                    "Shipping",
                    money(shipping)
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                SummaryRow(
                    "Total",
                    money(total),
                    bold = true
                )

                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = onCheckoutClick,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = cartItems.isNotEmpty()
                ) {
                    Text("Proceed to Checkout")
                }
            }
        }
    }
}

@Composable
private fun SummaryRow(
    label: String,
    value: String,
    bold: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontWeight = if (bold) {
                FontWeight.Bold
            } else {
                FontWeight.Normal
            }
        )

        Text(
            text = value,
            fontWeight = if (bold) {
                FontWeight.Bold
            } else {
                FontWeight.Normal
            }
        )
    }
}
