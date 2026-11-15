package com.example.furni.ui.orders

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.example.furni.data.entity.Order

class OrderIntents(private val context: Context)  {
    private val supportEmail =  "support@furni.com"

    fun shareOrder(details: OrderDetails) {
        val order = details.order
        val text = buildString {
            appendLine("Furni order #${order.orderId}")
            appendLine("Date: ${order.orderDate}")
            appendLine("Status: ${order.status}")
            appendLine()
            appendLine("Items:")
            details.items.forEach {
                appendLine("- ${it.productName} x${it.quantity} (${formatMoney(it.unitPrice * it.quantity)})")
            }
            appendLine()
            appendLine("Subtotal: ${formatMoney(order.subTotal)}")
            appendLine("Shipping: ${formatMoney(order.shippingFee)}")
            appendLine("Total: ${formatMoney(order.totalAmount)}")
            appendLine()
            appendLine("Ship to: ${order.recipientName}, ${order.recipientPhone}")
            append(order.shippingAddress)
        }

        val send = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Furni order #${order.orderId}")
            putExtra(Intent.EXTRA_TEXT, text)
        }

        try {
            context.startActivity(Intent.createChooser(send, "Share order"))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "No app available to share", Toast.LENGTH_SHORT).show()
        }
    }

    fun contactSupport(order: Order) {
        val email = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(supportEmail))
            putExtra(Intent.EXTRA_SUBJECT, "Question about order #${order.orderId}")
            putExtra(Intent.EXTRA_TEXT, "Hello, I need help with order #${order.orderId}.\n\n")
        }

        try {
            context.startActivity(email)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "No email app found", Toast.LENGTH_SHORT).show()
        }
    }
}