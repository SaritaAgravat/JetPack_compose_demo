package com.example.jetpack_api_call_demo.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text


@Composable
fun AddPeopleScreen(  isDarkMode: Boolean, onSubmit: (String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        InputField("Name", name, { name = it })

        InputField("Amount", amount, { amount = it }, keyboardType = KeyboardType.Number)

        SubmitButton {
            if (name.isNotBlank() && amount.isNotBlank()) {
                onSubmit(name, amount)
            }
        }
    }
}