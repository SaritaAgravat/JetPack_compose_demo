package com.jetpack_demo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
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
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.jetpack_api_call_demo.views.HelloWorldScreen
import com.example.jetpack_api_call_demo.views.HomeScreen
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.sp
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: GetClientListResponseViewModel by viewModels()
    private val addClientViewModel: AddClientDataViewmodel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApp(
                viewModel = viewModel,
                addClientViewModel = addClientViewModel
            )

        }
    }
}

@Composable
fun MyApp(
    viewModel: GetClientListResponseViewModel,
    addClientViewModel: AddClientDataViewmodel
) {
    val navController = rememberNavController()
    val screens = listOf(
        Screen("Home", "home", Icons.Filled.Home, Icons.Outlined.Home),
        Screen("Clients", "clients", Icons.Filled.Person, Icons.Outlined.Person),
        Screen("Settings", "settings", Icons.Filled.Settings, Icons.Outlined.Settings)
    )

    Scaffold(
        bottomBar = {
            BottomNavAnimation(
                screens = screens,
                navController = navController
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") { HomeScreen(viewModel, navController, false) }
            composable("clients") { AddClientScreen(addClientViewModel, {}, false) }
            composable("settings") { HelloWorldScreen(false) }
        }
    }
}

@Composable
fun BottomNavAnimation(
    screens: List<Screen>,
    navController: NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val targetRoute = navBackStackEntry?.destination?.route ?: screens.first().route

    var currentSelectedRoute by remember { mutableStateOf(targetRoute) }
    var isAnimatingChange by remember { mutableStateOf(false) }

    LaunchedEffect(targetRoute) {
        if (targetRoute != currentSelectedRoute && !isAnimatingChange) {
            isAnimatingChange = true
            // Animate deselect first by keeping currentSelectedRoute as is (unselected in UI)
            // Wait for your animation duration (say 300ms)
          //  delay(1000)
            // Then update selected tab to targetRoute
            currentSelectedRoute = targetRoute
            isAnimatingChange = false
        }
    }

    Box(
        Modifier
            .shadow(5.dp)
            .background(Color.Magenta)
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 16.dp,
                bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
            )
            .height(64.dp)
    ) {
        Row(
            Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            screens.forEach { screen ->
                val isSelected = screen.route == currentSelectedRoute
                val interactionSource = remember { MutableInteractionSource() }

                Box(
                    modifier = Modifier
                        .weight(if (isSelected) 1.5f else 1f)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            enabled = !isAnimatingChange // disable clicks during animation
                        ) {
                            if (screen.route != targetRoute && !isAnimatingChange) {
                                navController.navigate(screen.route) {
                                    launchSingleTop = true
                                    restoreState = true
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    BottomNavItem(
                        screen = screen,
                        isSelected = isSelected
                    )
                }
            }
        }
    }
}

@Composable
private fun BottomNavItem(
    screen: Screen,
    isSelected: Boolean
) {
    // Smooth fade animation
    val animatedAlpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0.5f,
        animationSpec = tween(
            durationMillis = 180,
            easing = LinearOutSlowInEasing
        )
    )

    // Smooth size animation
    val animatedIconSize by animateDpAsState(
        targetValue = if (isSelected) 26.dp else 20.dp,
        animationSpec = tween(
            durationMillis = 300,
            easing = FastOutSlowInEasing
        )
    )

    Row(
        modifier = Modifier
            .height(36.dp) // Fixed height for consistent vertical alignment
            .shadow(
                elevation = if (isSelected) 15.dp else 0.dp,
                shape = RoundedCornerShape(20.dp)
            )
            .background(
                color = Color.LightGray,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 10.dp), // Uniform padding for consistency
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // Flip Icon
        FlipIcon(
            modifier = Modifier
                .alpha(animatedAlpha)
                .size(animatedIconSize),
            isActive = isSelected,
            activeIcon = screen.activeIcon,
            inactiveIcon = screen.inactiveIcon,
            contentDescription = ""
        )

        // Animated visibility of the label
        AnimatedVisibility(
            visible = isSelected,
            enter = fadeIn(animationSpec = tween(300)),
            exit = fadeOut(animationSpec = tween(300))
        ) {
            Text(
                text = screen.title,
                modifier = Modifier.padding(start = 8.dp),
                maxLines = 1
            )
        }
    }
}

@Composable
fun FlipIcon(
    modifier: Modifier = Modifier,
    isActive: Boolean,
    activeIcon: ImageVector,
    inactiveIcon: ImageVector,
    contentDescription: String,
) {
    val animationRotation by animateFloatAsState(
        targetValue = if (isActive) 180f else 0f,
        animationSpec = spring(
            stiffness = Spring.StiffnessLow,
            dampingRatio = Spring.DampingRatioMediumBouncy
        )
    )
    Box(
        modifier = modifier
            .graphicsLayer { rotationY = animationRotation },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            rememberVectorPainter(image = if (animationRotation > 90f) activeIcon else inactiveIcon),
            contentDescription = contentDescription,
        )
    }
}
data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun BottomNavyBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("Home", Icons.Default.Home, "home"),
        BottomNavItem("Clients", Icons.Default.Person, "clients"),
        BottomNavItem("Settings", Icons.Default.Settings, "settings")
    )

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    NavigationBar(
        tonalElevation = 8.dp,
        containerColor = Color.White
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route

            val animatedScale by animateFloatAsState(targetValue = if (selected) 1.2f else 1f)
            val animatedColor by animateColorAsState(targetValue = if (selected) Color(0xFF3F51B5) else Color.Gray)

            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        launchSingleTop = true
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        restoreState = true
                    }
                },
                icon = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            modifier = Modifier.scale(animatedScale),
                            tint = animatedColor
                        )
                        AnimatedVisibility(visible = selected) {
                            Text(
                                text = item.label,
                                fontSize = 12.sp,
                                color = animatedColor,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }
                },
                label = { }, // Don't use default label
                alwaysShowLabel = false, // Important to control our own visibility
                colors = NavigationBarItemDefaults.colors(
                    // Blue oval appears only for selected
                    indicatorColor = Color(0xFF3F51B5),
                    // Icon/text color for selected/unselected
                    selectedIconColor = Color.Red, // We'll use our own tint above
                    unselectedIconColor = Color.Transparent
                )
            )
        }
    }
}
data class Screen(
    val title: String,
    val route: String,
    val activeIcon: ImageVector,
    val inactiveIcon: ImageVector
)
@Composable
fun displayLayout(viewModel: GetClientListResponseViewModel,navController: NavController) {
    val context = LocalContext.current

    val message1 = MessageData(author = "JetPack", body = "Android compose data")
    val message2 = MessageData(author = "John", body = "Hi there!")

    val state = viewModel.dataState.observeAsState(Resource.Loading).value

    // Trigger API
    LaunchedEffect(Unit) {
        val req = ClientListRequest(limit = 10, offset = 0, search = "")
        viewModel.setStateEvent(
            GetClientListResponseViewModel.GetClientListResponseViewModelStateEvent.GetClientListEvent,
            req
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            MessageCard(name = message1)
            Spacer(modifier = Modifier.height(18.dp))
        }

        item {
            ArtistCardModifiers(
                name = message2,
                onClick = {
                    Toast.makeText(context, "Artist card clicked!", Toast.LENGTH_SHORT).show()
                },navController
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
            navController.navigate("add_client_screen")
        }, modifier = Modifier.align(Alignment.CenterHorizontally))
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

        // optional edit-overlay, if you still need it
//        Box(
//            modifier = Modifier
//                .size(60.dp)
//                .offset(x = 46.dp, y = 46.dp) // bottom-right of the 60×60 image
//        ) {
//            Surface(
//                shape = CircleShape,
//                color = MaterialTheme.colorScheme.surface,
//                shadowElevation = 2.dp,
//                modifier = Modifier.size(24.dp)
//            ) {
//                Icon(
//                    imageVector = Icons.Filled.Edit,
//                    contentDescription = "Edit",
//                    tint = MaterialTheme.colorScheme.primary,
//                    modifier = Modifier.padding(4.dp)
//                )
//            }
//        }

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

@Composable
fun ExtendedExample(onClick: () -> Unit,modifier: Modifier = Modifier) {
    ExtendedFloatingActionButton(
        onClick = { onClick() },
        icon = { Icon(Icons.Filled.Edit, "Extended floating action button.") },
        text = { Text(text = "Add client") },  modifier = modifier
    )
}

@Preview
@Composable
fun PreviewDisplayLayout() {
    Jetpack_api_call_demoTheme {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .verticalScroll(rememberScrollState())
//                .padding(16.dp)
//        ) {
//            MessageCard(name = MessageData(author = "JetPack", body = "Android compose data"))
//            Spacer(modifier = Modifier.height(18.dp)) // Use height here in vertical column
//            ArtistCardModifiers(
//                name = MessageData(author = "John", body = "Hi there!"),
//                onClick = {}
//            )
//        }
        //displayLayout(viewModel)
    }
}
//@Composable
//fun MyApp(
//    viewModel: GetClientListResponseViewModel,
//    addClientViewModel: AddClientDataViewmodel
//) {
//    val navController = rememberNavController()
//
//    Scaffold(
//        bottomBar = { BottomNavyBar(navController) }
//    ) { innerPadding ->
//        NavHost(
//            navController = navController,
//            startDestination = "home",
//            modifier = Modifier.padding(innerPadding)
//        ) {
//            composable("home") { HomeScreen(viewModel,navController,false) }
//            composable("clients") { AddClientScreen(addClientViewModel,{}, false) }
//            composable("settings") { HelloWorldScreen(false) }
//        }
//    }
//
//}

@Composable
fun ClientsScreen(viewModel: GetClientListResponseViewModel) {
    val state = viewModel.dataState.observeAsState(Resource.Loading).value

    // Trigger API once
    LaunchedEffect(Unit) {
        val req = ClientListRequest(limit = 10, offset = 0, search = "") // fill in your required fields
        viewModel.setStateEvent(
            GetClientListResponseViewModel.GetClientListResponseViewModelStateEvent.GetClientListEvent,
            req
        )
    }

    when (state) {
        is Resource.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is Resource.Error -> {
            val msg = (state as Resource.Error).message ?: "Unknown error"
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Error: $msg", color = Color.Red)
            }
        }

        is Resource.Success -> {
            val clients = (state as Resource.Success<ClientListResponse>).data.data?.clientList ?: emptyList()
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize() // ✅ Constrains height
                    .padding(16.dp)
            ) {
                items(clients) { client ->
                    ClientRow(client!!)
                    Divider()
                }
            }
        }

        else -> {}
    }
}
//            val navController = rememberNavController()

//            Jetpack_api_call_demoTheme {
//                Scaffold(
//                    bottomBar = {
//                        BottomBar(navController)
//                    }
//                ) { innerPadding ->
//                    NavHost(
//                        navController = navController,
//                        startDestination = "main_screen",
//                        modifier = Modifier.padding(innerPadding)
//                    ) {
//                        composable("main_screen") {
//                            displayLayout(viewModel, navController)
//                        }
//                        composable("add_client_screen") {
//                            AddClientScreen(addClientViewModel)
//                        }
//                        // Add more composable screens if needed
//                    }
//                }
//            }

//class MainActivity : ComponentActivity() {
//    // get your Hilt-injected VM
//    private val viewModel: GetClientListResponseViewModel by viewModels()
//    // get your Hilt-injected VM
//    private val addClientViewModel: AddClientDataViewmodel by viewModels()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {val navController = rememberNavController()
//
//            Jetpack_api_call_demoTheme {
//                NavHost(navController = navController, startDestination = "main_screen") {
//                    composable("main_screen") {
//                        displayLayout(viewModel, navController)
//                    }
//
//                    composable("add_client_screen") {
//                        AddClientScreen(addClientViewModel!!)
//                    }
//                }
//
//            }
//        }
//    }
//}
//fun PreviewMessageCard(){
//    Jetpack_demoTheme {
//        MessageCard(name = MessageData(author = "JetPack", body = "Android compose data"))
//    }
//
//}

//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    Jetpack_demoTheme {
//        Greeting("Android")
//    }
//}
//@Composable
//fun BottomBar(navController: NavHostController) {
//    val items = listOf(
//        BottomNavScreen.Home,
//        BottomNavScreen.AddClient
//    )
//    val currentBackStackEntry by navController.currentBackStackEntryAsState()
//    val currentRoute = currentBackStackEntry?.destination?.route
//
//    NavigationBar(
//        containerColor = Color.White,
//        tonalElevation = 8.dp
//    ) {
//        items.forEach { screen ->
//            NavigationBarItem(
//                icon = { Icon(screen.icon, contentDescription = screen.label) },
//                label = { Text(screen.label) },
//                selected = currentRoute == screen.route,
//                onClick = {
//                    if (currentRoute != screen.route) {
//                        navController.navigate(screen.route) {
//                            popUpTo(navController.graph.startDestinationId) { saveState = true }
//                            launchSingleTop = true
//                            restoreState = true
//                        }
//                    }
//                }
//            )
//        }
//    }
//}