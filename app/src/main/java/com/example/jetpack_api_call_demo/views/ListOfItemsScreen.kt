package com.example.jetpack_api_call_demo.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.jetpack_api_call_demo.app_ui.AppButton
import com.example.jetpack_api_call_demo.app_ui.AppText
import com.example.jetpack_api_call_demo.viewModel.AddPeopleViewModel
import com.jetpack_demo.util.rememberSvgPainter

@Composable
fun ListOfItemScreen(
    isDarkMode: Boolean,
    navController: NavController,
    addClientViewModel: AddPeopleViewModel
) {
    val people = addClientViewModel.peopleList

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(Color(0x000000), Color(0x000000))))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            PeopleList(people)
            AppButton(text = "Add Client") {
                navController.navigate("add_client")
            }
        }
    }
}
@Composable
fun PeopleList(people: List<PersonSummary>) {
    val lightGrayColor = Color(0xFFE9ECEF)

//    val people = listOf(
//        PersonSummary("ic_user_place_holder", "James Gunn", "Owes you \$500.00"),
//        PersonSummary("ic_user_place_holder", "Daniel Hunt", "Owes you -\$500.00"),
//        PersonSummary("ic_user_place_holder", "Mark Rumario", "Owes you \$500.00")
//    )

    Card(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = lightGrayColor)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            people.forEachIndexed { index, person ->
                // Conditional padding for top and bottom
                val modifier = Modifier
                    .then(
                        if (index == 0) Modifier.padding(top = 8.dp) else Modifier
                    )
                    .then(
                        if (index == people.lastIndex) Modifier.padding(bottom = 8.dp) else Modifier
                    )

                Box(modifier = modifier) {
                    peopleRowData(person)
                }


                if (index != people.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 8.dp),
                        thickness = 1.dp,
                        color = Color(0xFFCAC4D0)
                    )
                }
            }
        }

    }


}

@Composable
fun peopleRowData(data : PersonSummary){
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val colorGreen = Color(0xFF10B981)
    val dividerHorizontal  = Color(0xFFCAC4D0)

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberSvgPainter(data.imageUrl),
                contentDescription = "Contact profile picture",
                modifier = Modifier
                    .width(40.dp)   // fixed width = device width
                    .height(40.dp)  // height = width for circle
                    .clip(CircleShape)
            )

            Spacer(Modifier.width(8.dp))

            Column(
                modifier = Modifier.fillMaxWidth(), // fill remaining width
                horizontalAlignment = Alignment.Start // align all text to left
            ) {
                AppText(
                    text = data.name,
                    showCurrencySymbol = false,
                    currencySymbol = "$",
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                AppText(
                    text = data.amount,
                    showCurrencySymbol = false,
                    currencySymbol = "$",
                    color =colorGreen,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }

    }
}
