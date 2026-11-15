package com.example.furni.ui.checkout

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.furni.ui.cart.CartViewModel
import com.example.furni.ui.orders.AmountRow
import com.example.furni.ui.orders.formatMoney

@Composable
fun CheckoutScreen(
    cartViewModel: CartViewModel,
    onBack: () -> Unit,
    onOrderPlaced: (Long) -> Unit,
    viewModel: CheckoutViewModel = viewModel()
) {
    val cartItems by cartViewModel.cartItems.collectAsState()
    val state by viewModel.uiState.collectAsState()

    // rememberSaveable keeps typed values when the screen rotates
    var name by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var street by rememberSaveable { mutableStateOf("") }
    var city by rememberSaveable { mutableStateOf("") }
    var region by rememberSaveable { mutableStateOf("") }
    var postal by rememberSaveable { mutableStateOf("") }
    var submitted by rememberSaveable { mutableStateOf(false) }

    val phoneClean = phone.filter { it.isDigit() || it == '+' }
    val nameError = if (name.isBlank()) "Full name is required" else null
    val phoneError =
        if (!Regex("^\\+?[0-9]{8,15}$").matches(phoneClean)) "Enter a valid phone number" else null
    val streetError = if (street.isBlank()) "Street address is required" else null
    val cityError = if (city.isBlank()) "City is required" else null
    val regionError = if (region.isBlank()) "Region is required" else null
    val postalError =
        if (!Regex("^[A-Za-z0-9 -]{3,10}$").matches(postal.trim())) "Enter a valid postal code" else null
    val formValid = listOf(nameError, phoneError, streetError, cityError, regionError, postalError)
        .all { it == null }

    // When the order is saved: leave this screen, then empty the cart
    LaunchedEffect(state.orderId) {
        val id = state.orderId
        if (id != null) {
            onOrderPlaced(id)
            cartViewModel.clearCart()
        }
    }

    val subtotal = cartItems.sumOf { it.product.price * it.quantity }
    val total = subtotal + CheckoutViewModel.SHIPPING_FEE

    Column(Modifier.fillMaxSize().statusBarsPadding()) {

        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text(
                "Checkout",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        if (cartItems.isEmpty() && state.orderId == null) {
            Box(
                Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text("Your cart is empty", style = MaterialTheme.typography.titleLarge)
            }
        } else {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            "Order summary",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))
                        cartItems.forEach { item ->
                            AmountRow(
                                "${item.product.name} x${item.quantity}",
                                formatMoney(item.product.price * item.quantity)
                            )
                        }
                        HorizontalDivider(Modifier.padding(vertical = 8.dp))
                        AmountRow("Subtotal", formatMoney(subtotal))
                        AmountRow("Shipping", formatMoney(CheckoutViewModel.SHIPPING_FEE))
                        AmountRow("Total", formatMoney(total), bold = true)
                    }
                }

                Text(
                    "Shipping details",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                FormField("Full name", name, { name = it }, if (submitted) nameError else null)
                FormField(
                    "Phone number", phone, { phone = it },
                    if (submitted) phoneError else null, KeyboardType.Phone
                )
                FormField("Street address", street, { street = it }, if (submitted) streetError else null)
                FormField("City", city, { city = it }, if (submitted) cityError else null)
                FormField("Region / Governorate", region, { region = it }, if (submitted) regionError else null)
                FormField(
                    "Postal code", postal, { postal = it },
                    if (submitted) postalError else null
                )

                Text(
                    "Payment: Cash on delivery",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(Modifier.height(8.dp))
            }
            Surface(tonalElevation = 3.dp, shadowElevation = 8.dp) {
                Column(Modifier.fillMaxWidth().padding(16.dp)) {
                    if (state.error != null) {
                        Text(
                            state.error!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(Modifier.height(8.dp))
                    }

                    Button(
                        onClick = {
                            submitted = true
                            if (formValid) {
                                val address = listOf(street, city, region, postal)
                                    .joinToString(", ") { it.trim() }
                                viewModel.placeOrder(
                                    items = cartItems,
                                    recipientName = name.trim(),
                                    recipientPhone = phone.trim(),
                                    shippingAddress = address
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !state.isPlacing
                    ) {
                        if (state.isPlacing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Place order - ${formatMoney(total)}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    error: String?,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        isError = error != null,
        supportingText = { if (error != null) Text(error) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier.fillMaxWidth()
    )
}