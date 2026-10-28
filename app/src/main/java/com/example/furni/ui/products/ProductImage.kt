package com.example.furni.ui.products

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

private const val TAG = "FurniImage"

/**
 * Shows a product image stored in the app's assets folder.
 * [imageUrl] is the path inside assets, e.g. "product/fancy_sofa.jpg".
 * While loading (or if imageUrl is null / the file is missing) a plain colored box is shown.
 * Pass clipping (e.g. Modifier.clip(...)) and size through [modifier].
 *
 * Debug logging is included: filter Logcat by the tag "FurniImage".
 */
@Composable
fun ProductImage(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    targetWidthPx: Int = 600
) {
    val context = LocalContext.current
    val bitmap by produceState<ImageBitmap?>(initialValue = null, key1 = imageUrl) {
        value = if (imageUrl == null) {
            Log.w(TAG, "imageUrl is null, showing placeholder")
            null
        } else {
            withContext(Dispatchers.IO) { loadAssetBitmap(context, imageUrl, targetWidthPx) }
        }
    }

    Box(
        modifier = modifier.background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        bitmap?.let {
            Image(
                bitmap = it,
                contentDescription = null,
                contentScale = contentScale,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

// Decodes a downsampled version so big photos don't use too much memory.
private fun loadAssetBitmap(context: Context, path: String, targetWidthPx: Int): ImageBitmap? {
    return try {
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        context.assets.open(path).use { BitmapFactory.decodeStream(it, null, bounds) }

        var sample = 1
        while (bounds.outWidth / (sample * 2) >= targetWidthPx) sample *= 2

        val options = BitmapFactory.Options().apply { inSampleSize = sample }
        val bitmap = context.assets.open(path).use { BitmapFactory.decodeStream(it, null, options) }
        if (bitmap == null) {
            Log.w(TAG, "Could not decode '$path' (bounds ${bounds.outWidth}x${bounds.outHeight})")
        }
        bitmap?.asImageBitmap()
    } catch (e: Exception) {
        val folder = path.substringBeforeLast('/', "")
        val inFolder = try {
            context.assets.list(folder)?.toList()
        } catch (ignored: IOException) {
            null
        }
        val inRoot = try {
            context.assets.list("")?.toList()
        } catch (ignored: IOException) {
            null
        }
        Log.w(
            TAG,
            "Could not load asset '$path'. Files in assets/'$folder': $inFolder. Top level of assets: $inRoot",
            e
        )
        null
    }
}
