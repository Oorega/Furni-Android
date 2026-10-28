package com.example.furni.ui.products

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.furni.data.entity.Product
import com.example.furni.data.entity.Review

@Composable
fun ProductDetailsScreen(
    productId: Long,
    onBack: () -> Unit,
    // Wire this to Member 3's cart code in the NavHost once it exists.
    onAddToCart: (productId: Long, quantity: Int) -> Unit,
    viewModel: ProductDetailsViewModel = viewModel(factory = ProductDetailsViewModel.factory(productId))
) {
    val state by viewModel.state.collectAsState()
    val reviews by viewModel.reviews.collectAsState()
    val averageRating by viewModel.averageRating.collectAsState()
    val reviewCount by viewModel.reviewCount.collectAsState()

    Column(Modifier.fillMaxSize().statusBarsPadding()) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 4.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text("Details", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
        }

        val product = state.product
        when {
            state.loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }

            product == null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Product not found.")
            }

            else -> DetailsContent(
                product = product,
                categoryName = state.categoryName,
                reviews = reviews,
                averageRating = averageRating,
                reviewCount = reviewCount,
                onAddToCart = onAddToCart,
                onSubmitReview = viewModel::submitReview
            )
        }
    }
}

@Composable
private fun DetailsContent(
    product: Product,
    categoryName: String?,
    reviews: List<Review>,
    averageRating: Double?,
    reviewCount: Int,
    onAddToCart: (Long, Int) -> Unit,
    onSubmitReview: (Int, String) -> Unit
) {
    val inStock = product.quantity > 0
    var quantity by remember(product.productId) { mutableIntStateOf(1) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            ProductImage(
                imageUrl = product.imageUrl,
                modifier = Modifier.fillMaxWidth().height(220.dp).clip(RoundedCornerShape(12.dp)),
                targetWidthPx = 1080
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(product.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                if (categoryName != null) {
                    Text(categoryName, style = MaterialTheme.typography.bodyMedium)
                }
                Text(
                    "$" + "%.2f".format(product.price),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    if (inStock) "In stock: ${product.quantity}" else "Out of stock",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (inStock) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.error
                )
            }
        }

        item { Text(product.description, style = MaterialTheme.typography.bodyLarge) }

        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { if (quantity > 1) quantity-- }, enabled = inStock && quantity > 1) {
                    Icon(Icons.Filled.Remove, contentDescription = "Decrease quantity")
                }
                Text("$quantity", style = MaterialTheme.typography.titleMedium)
                IconButton(
                    onClick = { if (quantity < product.quantity) quantity++ },
                    enabled = inStock && quantity < product.quantity
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Increase quantity")
                }
                Spacer(Modifier.width(12.dp))
                Button(
                    onClick = { product.productId?.let { onAddToCart(it, quantity) } },
                    enabled = inStock && product.productId != null,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Add to cart")
                }
            }
        }

        item { HorizontalDivider() }

        item {
            Text(
                if (averageRating != null && reviewCount > 0)
                    "Reviews: ★ ${"%.1f".format(averageRating)} ($reviewCount)"
                else
                    "Reviews: no reviews yet",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }

        item { ReviewForm(onSubmit = onSubmitReview) }

        items(reviews, key = { it.reviewId ?: it.hashCode().toLong() }) { review ->
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    val stars = review.rating.coerceIn(1, 5)
                    Text(
                        "★".repeat(stars) + "☆".repeat(5 - stars),
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(review.comment)
                }
            }
        }
    }
}

@Composable
private fun ReviewForm(onSubmit: (Int, String) -> Unit) {
    var rating by remember { mutableIntStateOf(0) }
    var comment by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Write a review", style = MaterialTheme.typography.titleSmall)
        Row {
            for (star in 1..5) {
                IconButton(onClick = { rating = star }) {
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = "$star stars",
                        tint = if (star <= rating) MaterialTheme.colorScheme.secondary
                        else MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }
        }
        OutlinedTextField(
            value = comment, onValueChange = { comment = it },
            placeholder = { Text("Share your thoughts...") },
            modifier = Modifier.fillMaxWidth(), minLines = 2
        )
        Button(
            onClick = {
                onSubmit(rating, comment)
                rating = 0
                comment = ""
            },
            enabled = rating > 0 && comment.isNotBlank()
        ) {
            Text("Submit review")
        }
    }
}
