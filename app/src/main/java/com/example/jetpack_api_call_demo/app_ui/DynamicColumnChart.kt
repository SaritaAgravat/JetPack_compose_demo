//package com.example.jetpack_api_call_demo.app_ui
//
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.drawscope.DrawStyle
//import androidx.compose.ui.unit.dp
//import ir.ehsannarmani.compose_charts.charts.ColumnChart
//import ir.ehsannarmani.compose_charts.data.Bars
//import ir.ehsannarmani.compose_charts.properties.BarProperties
//
//@Composable
//fun DynamicColumnChart(
//    data: List<Bars>,
//    modifier: Modifier = Modifier
//        .fillMaxWidth()
//        .height(200.dp)
//        .padding(horizontal = 16.dp),
//    barThickness: Int = 15,
//    spacing: Int = 4,
//    drawStyle: DrawStyle? = null
//) {
//    ColumnChart(
//        modifier = modifier,
//        data = data,
//        barProperties = BarProperties(
//            thickness = barThickness.dp,
//            spacing = spacing.dp,
//            style = drawStyle
//        )
//    )
//}
