package com.example.jetpack_api_call_demo.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.jetpack_api_call_demo.app_ui.AppText
import com.example.jetpack_api_call_demo.app_ui.BarData
import com.example.jetpack_api_call_demo.app_ui.BarGroup
import com.example.jetpack_api_call_demo.app_ui.CustomBarChart
import com.example.jetpack_api_call_demo.app_ui.DynamicColumnChart
//import com.example.jetpack_api_call_demo.app_ui.DynamicColumnChart
import com.jetpack_demo.model.response.ClientListResponse.ClientListData
import com.jetpack_demo.util.rememberSvgPainter
import ir.ehsannarmani.compose_charts.models.Bars

@Composable
fun HelloWorldScreen(  isDarkMode: Boolean) {
    val balanceTextColor  = Color(0xFFD283FF)

    val gradientColors = if (isDarkMode) {
        listOf(Color.Black, Color.Black)
    } else {
        listOf(
//            Color(0xFFC3E7FB), // bg_light_blue_shadow
//            Color(0xFFF7F7F7)  // bg_light_white_shadow

            Color(0x000000), // bg_light_blue_shadow
            Color(0x000000)  // bg_light_white_shadow
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0x000000), Color(0x000000))
                )
            )
    ) {
        // Make column scrollable
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // <-- this enables scrolling
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppText(
                    text = "OVERVIEW",
                    showCurrencySymbol = false,
                    currencySymbol = "$",
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                AppText(
                    text = "THIS MONTHS",
                    showCurrencySymbol = false,
                    currencySymbol = "$",
                    color = balanceTextColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            OverViewUiData()
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppText(
                    text = "People Summery",
                    showCurrencySymbol = false,
                    currencySymbol = "$",
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                AppText(
                    text = "View All",
                    showCurrencySymbol = false,
                    currencySymbol = "$",
                    color = balanceTextColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            PeopleSummaryList()
            Spacer(modifier = Modifier.height(16.dp))

//            ChartScreen()
//            Spacer(modifier = Modifier.height(16.dp))
            customBarChart()
        }
    }
}

@Composable
fun OverViewUiData() {
    val lightGrayColor = Color(0xFFE9ECEF)
    val dividerHorizontal  = Color(0xFFCAC4D0)
    val balanceTextColor  = Color(0xFFD283FF)
    Card(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = lightGrayColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp)
        ) {
            // Row 1
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .padding(start = 5.dp, end = 10.dp, top = 8.dp, bottom = 8.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    overViewDataRow("Given", "ic_upload_icon", "599.00")
                }

                // Vertical Divider that matches Row content height
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                        .background(Color.LightGray)
                )

                Box(modifier = Modifier.weight(1f)) {
                    overViewDataRow("Taken", "ic_download_icon", "599.00")
                }
            }


            // 🔽 Divider between Row 1 and Row 2
//            HorizontalDivider(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 0.dp),
//                thickness = 1.dp,
//                color = dividerHorizontal
//            )
            Box(modifier = Modifier.fillMaxWidth()) {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = dividerHorizontal
                )
            }
            // Row 2
            Row( modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(start = 5.dp, end = 10.dp, top = 8.dp, bottom = 8.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    overViewDataRow("Income","ic_income_icon","599.00")
                }
                // Vertical Divider that matches Row content height
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                        .background(Color.LightGray)
                )
                Box(modifier = Modifier.weight(1f)) {
                    overViewDataRow("Expense","ic_export_icon","599.00")
                }
            }

            Box(modifier = Modifier.fillMaxWidth()) {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = dividerHorizontal
                )
            }

            Row(  modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(start = 5.dp, end = 10.dp, top = 12.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.Center, // 🔹 This centers children horizontally
                verticalAlignment = Alignment.CenterVertically // 🔹 (optional) vertically center within row
            ) {
                AppText(
                    text = "Balance : ",
                    showCurrencySymbol = false,
                    currencySymbol = "$",
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                AppText(
                    text = "278.00",
                    showCurrencySymbol = true,
                    currencySymbol = "$",
                    color =balanceTextColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

@Composable
fun GridItem(text: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .border(1.dp, Color.Gray)
            .padding(16.dp), // Adjust padding as needed
        contentAlignment = Alignment.Center
    ) {
        Text(text)
    }
}

@Composable
fun overViewDataRow(text: String, imageName : String, valueOfText: String,) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val colorGreen = Color(0xFF10B981)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {


        Image(
            painter = rememberSvgPainter(imageName),
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
                text = text,
                showCurrencySymbol = false,
                currencySymbol = "$",
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            AppText(
                text = valueOfText,
                showCurrencySymbol = true,
                currencySymbol = "$",
                color =colorGreen,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@Composable
fun PeopleSummaryList() {
    val lightGrayColor = Color(0xFFE9ECEF)

    val people = listOf(
        PersonSummary("ic_user_place_holder", "James Gunn", "Owes you \$500.00"),
        PersonSummary("ic_user_place_holder", "Daniel Hunt", "Owes you -\$500.00"),
        PersonSummary("ic_user_place_holder", "Mark Rumario", "Owes you \$500.00")
    )

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
                    peopleSummaryRowData(person)
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
fun peopleSummaryRowData(data : PersonSummary){
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

@Composable
fun SimpleCard() {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "Card Title",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "This is the content inside the card. You can put text, images, buttons, etc.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}


data class PersonSummary(
    val imageUrl: String,
    val name: String,
    val amount: String
)

@Composable
fun ChartScreen() {
    val data = listOf(
        Bars(
            label = "Jan",
            values = listOf(
                Bars.Data(value = 40.0, label = "Linux", color = SolidColor(Color.Blue)),
                Bars.Data(value = 60.0, label = "Windows", color = SolidColor(Color.Red))
            )
        ),
        Bars(
            label = "Feb",
            values = listOf(
                Bars.Data(value = 30.0, label = "Linux", color = SolidColor(Color.Blue)),
                Bars.Data(value = 10.0, label = "Windows", color = SolidColor(Color.Red))
            )
        ),
        Bars(
            label = "March",
            values = listOf(
                Bars.Data(value = 30.0, label = "Linux", color = SolidColor(Color.Blue)),
                Bars.Data(value = 95.0, label = "Windows", color = SolidColor(Color.Red))
            )
        ),
        Bars(
            label = "April",
            values = listOf(
                Bars.Data(value = 10.0, label = "Linux", color = SolidColor(Color.Blue)),
                Bars.Data(value = 60.0, label = "Windows", color = SolidColor(Color.Red))
            )
        ),
        Bars(
            label = "May",
            values = listOf(
                Bars.Data(value = 30.0, label = "Linux", color = SolidColor(Color.Blue)),
                Bars.Data(value = 15.0, label = "Windows", color = SolidColor(Color.Red))
            )
        )

    )
    DynamicColumnChart(data = data)

}

@Composable
fun customBarChart(){

    val chartData = listOf(
        BarGroup(
            label = "Jan",
            bars = listOf(
                BarData("Linux", 40.0f, Color.Blue),
                BarData("Windows", 60.0f, Color.Red)
            )
        ),
        BarGroup(
            label = "Feb",
            bars = listOf(
                BarData("Linux", 30.0f, Color.Blue),
                BarData("Windows", 10.0f, Color.Red)
            )
        ),
        BarGroup(
            label = "March",
            bars = listOf(
                BarData("Linux", 30.0f, Color.Blue),
                BarData("Windows", 95.0f, Color.Red)
            )
        )
        ,
        BarGroup(
            label = "April",
            bars = listOf(
                BarData("Linux", 10.0f, Color.Blue),
                BarData("Windows", 60.0f, Color.Red)
            )
        )
        ,
        BarGroup(
            label = "May",
            bars = listOf(
                BarData("Linux", 30.0f, Color.Blue),
                BarData("Windows", 15.0f, Color.Red)
            )
        )
    )
    CustomBarChart(data = chartData, maxValue = 100.0f, minValue = 0.0f)
}
