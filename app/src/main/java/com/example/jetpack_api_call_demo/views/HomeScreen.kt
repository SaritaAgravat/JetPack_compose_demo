package com.example.jetpack_api_call_demo.views

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jetpack_demo.model.MessageData
import com.jetpack_demo.ui.theme.Jetpack_api_call_demoTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.jetpack_demo.base_api.Resource
import com.jetpack_demo.model.request.ClientListRequest
import com.jetpack_demo.model.response.ClientListResponse.ClientListData
import com.jetpack_demo.model.response.ClientListResponse.ClientListResponse
import com.jetpack_demo.util.rememberSvgPainter
import com.jetpack_demo.viewModel.GetClientListResponseViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.jetpack_api_call_demo.app_ui.BottomNavScreen
import com.example.jetpack_api_call_demo.viewModel.AddClientDataViewmodel
import com.example.jetpack_api_call_demo.views.AddClientScreen
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.jetpack_api_call_demo.views.HelloWorldScreen
import com.jetpack_demo.ExtendedExample


@Composable
fun HomeScreen(
    viewModel: GetClientListResponseViewModel,
    navController: NavController,
    isDarkMode: Boolean
) {
    val context = LocalContext.current

    val message1 = MessageData(author = "JetPack", body = "Android compose data")
    val message2 = MessageData(author = "John", body = "Hi there!")

    val state = viewModel.dataState.observeAsState(Resource.Loading).value

    LaunchedEffect(Unit) {
        val req = ClientListRequest(limit = 10, offset = 0, search = "")
        viewModel.setStateEvent(
            GetClientListResponseViewModel.GetClientListResponseViewModelStateEvent.GetClientListEvent,
            req
        )
    }
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
            ) // Set your background color here
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // ... your existing items
            item {
                MessageCard(name = message1)
                Spacer(modifier = Modifier.height(18.dp))
            }

            item {
                ArtistCardModifiers(
                    name = message2,
                    onClick = {
                        Toast.makeText(context, "Artist card clicked!", Toast.LENGTH_SHORT).show()
                    },
                    navController = navController
                )
                Spacer(modifier = Modifier.height(18.dp))
            }

            when (state) {
                is Resource.Loading -> item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is Resource.Error -> item {
                    val msg = (state as Resource.Error).message ?: "Unknown error"
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Error: $msg", color = Color.Red)
                    }
                }

                is Resource.Success -> {
                    val clients = (state as Resource.Success<ClientListResponse>).data.data?.clientList ?: emptyList()
                    items(clients) { client ->
                        ClientRow(client!!)
                        Divider()
                    }
                }

                else -> {}
            }
        }
    }
}


@Composable
fun ArtistCardModifiers(
    name: MessageData,
    onClick: () -> Unit,navController: NavController
) {
    val padding = 16.dp
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(padding)
            .fillMaxWidth()
    ) {

        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp.dp
        Spacer(Modifier.size(padding))
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp), modifier = Modifier.fillMaxWidth()
        ) {     Image(
            painter = rememberSvgPainter("ic_daily_app_logo"),
            contentDescription = "Contact profile picture",
            modifier = Modifier
                .width(screenWidth)   // fixed width = device width
                .height(120.dp)  // height = width for circle
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
        ) }
        Spacer(Modifier.size(padding))
        ExtendedExample(onClick = {
            navController.navigate("clients")
        }, modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}

@Composable
fun MessageCard(name : MessageData){
    Row( modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End) {

        Box{
            Image(
                painter = rememberSvgPainter("ic_daily_app_logo"), // Use the util function
                contentDescription = "Contact profile picture",
                modifier = Modifier.size(100.dp).clip(CircleShape).border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)

            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd) // position in bottom-right
                    .offset(x = (-4).dp, y = (-4).dp) // optional offset inward
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surface, // Background color
                    shadowElevation = 2.dp, // Optional elevation
                    modifier = Modifier.size(24.dp) // Size of the circle
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(4.dp) // Center the icon inside the circle
                    )
                }
            }

        }



        Spacer(modifier = Modifier.width(18.dp))

        Column {
            Text(
                text = name.author,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(4.dp))

            Surface(shape = MaterialTheme.shapes.medium, shadowElevation = 1.dp) {
                Text(
                    text = name.body,
                    modifier = Modifier.padding(all = 4.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }

}


@Composable
fun ClientRow(client: ClientListData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Use Coil for loading network images; fall back to a placeholder if empty
        val imageUrl = client.image!!.ifBlank { null }
        AsyncImage(
            model = imageUrl,
            placeholder = rememberSvgPainter("ic_daily_app_logo"),
            error = rememberSvgPainter("ic_daily_app_logo"),
            contentDescription = "${client.userName} profile",
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )


        Spacer(Modifier.width(18.dp))

        Column {
            Surface(
                shape = MaterialTheme.shapes.medium,
                shadowElevation = 1.dp
            ) {
                Text(
                    text = client.userName!!,
                    modifier = Modifier.padding(4.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
