package com.example.jetpack_api_call_demo.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun HelloWorldScreen(  isDarkMode: Boolean) {

    val gradientColors = if (isDarkMode) {
        listOf(Color.Black, Color.Black)
    } else {
        listOf(
            Color(0xFFC3E7FB), // bg_light_blue_shadow
            Color(0xFFF7F7F7)  // bg_light_white_shadow
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFC3E7FB), Color(0xFFF7F7F7))
                )
            ) //
    ) {
        Text(text = "Hello World!", style = MaterialTheme.typography.headlineMedium)
    }
}
