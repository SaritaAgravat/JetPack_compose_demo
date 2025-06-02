package com.example.jetpack_api_call_demo.app_ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.material3.Text


@Composable
fun AppText(
    text: String,
    color: Color = MaterialTheme.colorScheme.onBackground,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    textAlign: TextAlign? = null,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    modifier: Modifier = Modifier,
    showCurrencySymbol: Boolean = false,
    currencySymbol: String = "₹"
) {
    val displayText = if (showCurrencySymbol) "$currencySymbol$text" else text

    Text(
        text = displayText,
        color = color,
        fontSize = fontSize,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        textAlign = textAlign,
        style = style,
        modifier = modifier
    )
}
