package com.example.furni.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val FurniColors = lightColorScheme(
    primary = Brown,
    onPrimary = Cream,
    secondary = Accent,
    background = Cream,
    surface = Cream,
    onBackground = Charcoal,
    onSurface = Charcoal,
    primaryContainer = BrownLight
)

@Composable
fun FurniTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = FurniColors,
        typography = Typography(),
        content = content
    )
}