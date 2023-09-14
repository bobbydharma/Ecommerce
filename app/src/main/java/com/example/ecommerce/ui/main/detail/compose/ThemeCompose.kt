package com.example.ecommerce.ui.main.detail.compose

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


data class MyColors(
    val background: Color = Color.White,
    val surface: Color = Color.White
)

@Composable
fun ThemeCompose(
    background: Color,
    surface: Color,
    content: @Composable () -> Unit
){
     MaterialTheme(

        typography = MaterialTheme.typography,
        shapes = MaterialTheme.shapes,
        content = content,
        colorScheme = MaterialTheme.colorScheme.copy(
            background = background,
            surface = surface
        )
    )
}