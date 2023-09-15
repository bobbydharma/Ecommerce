package com.example.ecommerce.ui.main.detail.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color



@Composable
fun ThemeCompose(
    content: @Composable () -> Unit
){

     val LightColorPalette = lightColorScheme(
        surface = Color.White,
        background = Color.White,
    )

     val DarkColorPalette = darkColorScheme(
        surface = Color(android.graphics.Color.parseColor("#1D1B20")),
        background = Color(android.graphics.Color.parseColor("#1D1B20")),
    )

    val darkTheme = isSystemInDarkTheme()
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

     MaterialTheme(

        typography = MaterialTheme.typography,
        shapes = MaterialTheme.shapes,
        content = content,
        colorScheme = colors
    )
}