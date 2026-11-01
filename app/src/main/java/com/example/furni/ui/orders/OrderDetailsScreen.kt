package com.example.furni.ui.orders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun OrderDetailsScreen(
    orderId: Long,
    onBack: () -> Unit,
    viewModel: OrderDetailsViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val intents = remember { OrderIntents(context) }

    LaunchedEffect(orderId) { viewModel.load(orderId) }

    Column(Modifier.fillMaxWidth().statusBarsPadding()) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text(
                "Order #${orderId}",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        val details = state.details

        when {
            state.isLoading -> Box(
                Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }

            details == null -> Box(
                Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) { Text("Order not found", style = MaterialTheme.typography.titleLarge) }

            else -> Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(details.order.orderDate, style = MaterialTheme.typography.bodyMedium)
                    StatusChip(details.order.status)
                }

                OrderInfoSection(details)

                Button(
                    onClick = { intents.shareOrder(details) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Filled.Share, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Share order")
                }

                OutlinedButton(
                    onClick = { intents.contactSupport(details.order) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Filled.Email, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Contact support")
                }
            }
        }
    }
}