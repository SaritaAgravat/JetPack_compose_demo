package com.example.jetpack_api_call_demo.views

import android.app.DatePickerDialog
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import java.time.LocalDate
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import java.util.Locale
import android.content.Context
import android.widget.Toast
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.core.net.toFile
import androidx.navigation.compose.rememberNavController
import com.example.jetpack_api_call_demo.model.request.AddClientRequest
import com.example.jetpack_api_call_demo.viewModel.AddClientDataViewmodel
import com.jetpack_demo.base_api.Resource
import com.jetpack_demo.viewModel.GetClientListResponseViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import java.io.File
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun AddClientScreen(
    viewModel: AddClientDataViewmodel ,
    onSubmitSuccess: () -> Unit = {},
    isDarkMode: Boolean
) {
    val context = LocalContext.current
    val submissionState by viewModel.addUpdateClientDataState.observeAsState()

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var name by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var issueDate by remember { mutableStateOf<LocalDate?>(null) }
    var collectionDate by remember { mutableStateOf<LocalDate?>(null) }
    val navController = rememberNavController() // make sure it's passed or accessible

    // Handle success/error after state changes
    LaunchedEffect(submissionState) {
        when (submissionState) {
            is Resource.Success -> {
                Toast.makeText(context, "Client added/updated successfully", Toast.LENGTH_SHORT).show()
                delay(500) // Optional: wait for the Toast to show
                onSubmitSuccess() // optional callback
                navController.popBackStack() // navigate back
            }
            is Resource.Error -> {
                Toast.makeText(context, (submissionState as Resource.Error).message ?: "Error", Toast.LENGTH_LONG).show()
            }
            else -> Unit
        }
    }

    fun validateAndSubmit() {
        val imageFile = imageUri?.toFile(context)
        val valid = validateFields(
            context,
            name,
            mobile,
            amount,
            issueDate,
            collectionDate,
            false
        )
        if (valid) {
            val request = AddClientRequest(
                clientId = 0, // or actual ID
                userName = name,
                mobile = mobile,
                amount = amount,
                issueDate = issueDate.toString(),
                collectionDate = collectionDate.toString(), image = null
            )
            viewModel.addUpdateClientApiDataState(
                AddClientDataViewmodel.AddClientDataViewmodelState.addClientDataViewmodelState,
                request,
                imageFile
            )
        }
    }

    val gradientColors = if (isDarkMode) {
        listOf(Color.Black, Color.Black)
    } else {
        listOf(
            Color(0xFFC3E7FB), // bg_light_blue_shadow
            Color(0xFFF7F7F7)  // bg_light_white_shadow
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        ProfileImagePicker(imageUri = imageUri, onImageSelected = { imageUri = it })

        InputField("Name", name, { name = it })
        InputField(
            label = "Mobile Number",
            value = mobile,
            onValueChange = {
                if (it.length <= 10 && it.all { char -> char.isDigit() }) {
                    mobile = it
                }
            },
            keyboardType = KeyboardType.Phone
        )
        InputField("Amount", amount, { amount = it }, keyboardType = KeyboardType.Number)

        CustomDatePickerField("Select issue date", issueDate, { issueDate = it }, isPastOnly = true)
        CustomDatePickerField("Select collection date", collectionDate, { collectionDate = it })

        SubmitButton {
            validateAndSubmit()
        }

        if (submissionState is Resource.Loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }

}


fun Uri.toFile(context: Context): File? {
    val inputStream = context.contentResolver.openInputStream(this) ?: return null
    val file = File.createTempFile("upload_", ".jpg", context.cacheDir)
    file.outputStream().use { outputStream ->
        inputStream.copyTo(outputStream)
    }
    return file
}


@Composable
fun ProfileImagePicker(
    imageUri: Uri?,
    onImageSelected: (Uri) -> Unit
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let { onImageSelected(it) }
    }

    Box(
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .background(Color.Gray)
            .clickable { launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
        contentAlignment = Alignment.Center
    ) {
        if (imageUri != null) {
            AsyncImage(
                model = imageUri,
                contentDescription = "Profile Image",
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Default Profile",
                tint = Color.White,
                modifier = Modifier.size(60.dp)
            )
        }
    }
}


@Composable
fun InputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )
}




@Composable
fun DatePickerField(
    label: String,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = selectedDate?.year ?: calendar.get(Calendar.YEAR)
    val month = selectedDate?.monthValue?.minus(1) ?: calendar.get(Calendar.MONTH)
    val day = selectedDate?.dayOfMonth ?: calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = remember {
        DatePickerDialog(context, { _, y, m, d ->
            val pickedDate = LocalDate.of(y, m + 1, d)
            onDateSelected(pickedDate)
        }, year, month, day)
    }

    OutlinedTextField(
        value = selectedDate?.format(DateTimeFormatter.ofPattern("dd MMM, yyyy")) ?: "",
        onValueChange = {},
        label = { Text(label) },
        readOnly = true,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                datePickerDialog.show()
            }
    )
}


fun formatDisplayDate(date: LocalDate): String {
    return date.format(DateTimeFormatter.ofPattern("dd MMM, yyyy", Locale.getDefault()))
}

fun formatApiDate(date: LocalDate): String {
    return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault()))
}

@Composable
fun CustomDatePickerField(
    label: String,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    isFutureOnly: Boolean = false,
    isPastOnly: Boolean = false,
) {
    val context = LocalContext.current
    val displayDate = selectedDate?.let { formatDisplayDate(it) } ?: ""

    OutlinedTextField(
        value = displayDate,
        onValueChange = {},
        readOnly = true,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                showDatePickerDialog(context, selectedDate ?: LocalDate.now(), onDateSelected, isFutureOnly, isPastOnly)
            },
        enabled = true,
        singleLine = true
    )
}
fun showDatePickerDialog(
    context: Context,
    initialDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    isFutureOnly: Boolean = false,
    isPastOnly: Boolean = false
) {
    val now = Calendar.getInstance()
    val year = initialDate.year
    val month = initialDate.monthValue - 1 // Month is 0-based
    val day = initialDate.dayOfMonth

    val datePicker = DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            val pickedDate = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay)
            onDateSelected(pickedDate)
        },
        year, month, day
    )

    if (isFutureOnly) {
        datePicker.datePicker.minDate = now.timeInMillis
        datePicker.datePicker.maxDate = now.apply { add(Calendar.YEAR, 10) }.timeInMillis
    } else if (isPastOnly) {
        datePicker.datePicker.maxDate = now.timeInMillis
        datePicker.datePicker.minDate = Calendar.getInstance().apply {
            set(1900, 0, 1)
        }.timeInMillis
    }

    datePicker.show()
}

@Composable
fun DateSelectionScreen() {
    var issueDate by remember { mutableStateOf<LocalDate?>(null) }
    var collectionDate by remember { mutableStateOf<LocalDate?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {
        CustomDatePickerField(
            label = "Select issue date",
            selectedDate = issueDate,
            onDateSelected = { issueDate = it },
            isPastOnly = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomDatePickerField(
            label = "Select collection date",
            selectedDate = collectionDate,
            onDateSelected = { collectionDate = it },
            isFutureOnly = true
        )
    }
}

fun validateFields(
    context: Context,
    name: String,
    mobile: String,
    amount: String,
    issueDate: LocalDate?,
    collectionDate: LocalDate?,
    isFromEdit: Boolean
): Boolean {
    if (name.isBlank()) {
        Toast.makeText(context, "Please enter name", Toast.LENGTH_SHORT).show()
        return false
    }
    if (name.length < 2) {
        Toast.makeText(context, "Name too short", Toast.LENGTH_SHORT).show()
        return false
    }
    if (mobile.isBlank()) {
        Toast.makeText(context, "Please enter mobile number", Toast.LENGTH_SHORT).show()
        return false
    }
    if (mobile.length < 10) {
        Toast.makeText(context, "Mobile number is invalid", Toast.LENGTH_SHORT).show()
        return false
    }

    if (!isFromEdit) {
        if (amount.isBlank() || amount.toDoubleOrNull() == null || amount.toDouble() <= 0) {
            Toast.makeText(context, "Please enter valid amount", Toast.LENGTH_SHORT).show()
            return false
        }
        if (issueDate == null) {
            Toast.makeText(context, "Please select issue date", Toast.LENGTH_SHORT).show()
            return false
        }
        if (issueDate.isAfter(LocalDate.now())) {
            Toast.makeText(context, "Issue date cannot be in future", Toast.LENGTH_SHORT).show()
            return false
        }
        if (collectionDate == null) {
            Toast.makeText(context, "Please select collection date", Toast.LENGTH_SHORT).show()
            return false
        }
        if (collectionDate.isBefore(issueDate)) {
            Toast.makeText(context, "Collection date must be after issue date", Toast.LENGTH_SHORT).show()
            return false
        }
    }

    return true
}


@Composable
fun SubmitButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Text("Submit")
    }
}
