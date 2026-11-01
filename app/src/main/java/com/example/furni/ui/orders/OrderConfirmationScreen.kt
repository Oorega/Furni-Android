package com.example.furni.ui.orders

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun OrderConfirmationScreen(
    orderId: Long,
    onViewOrders: () -> Unit,
    onContinueShopping: () -> Unit,
    viewModel: OrderDetailsViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val intents = remember { OrderIntents(context) }

    LaunchedEffect(orderId) { viewModel.load(orderId) }

    // Back leads to the shop, not into the emptied cart
    BackHandler(onBack = onContinueShopping)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Spacer(Modifier.height(16.dp))

        Icon(
            Icons.Filled.CheckCircle,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(72.dp)
        )
        Text(
            "Thank you for your order!",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text("Order #$orderId", style = MaterialTheme.typography.titleMedium)
        Text(
            "Your order has been placed and is being processed.",
            textAlign = TextAlign.Center
        )

        val details = state.details
        if (state.isLoading) {
            CircularProgressIndicator()
        } else if (details != null) {
            OrderInfoSection(details)

            OutlinedButton(
                onClick = { intents.shareOrder(details) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.Share, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Share order")
            }
        }

        Button(onClick = onViewOrders, modifier = Modifier.fillMaxWidth()) {
            Text("View my orders")
        }
        TextButton(onClick = onContinueShopping, modifier = Modifier.fillMaxWidth()) {
            Text("Continue shopping")
        }
    }
}