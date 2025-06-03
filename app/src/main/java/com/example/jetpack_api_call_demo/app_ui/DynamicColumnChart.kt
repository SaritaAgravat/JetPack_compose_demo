package com.example.jetpack_api_call_demo.app_ui


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.EmptyBuildDrawCacheParams.size
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ir.ehsannarmani.compose_charts.ColumnChart
import ir.ehsannarmani.compose_charts.models.BarProperties
import ir.ehsannarmani.compose_charts.models.Bars
import ir.ehsannarmani.compose_charts.models.DrawStyle


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
//            style = drawStyle ?: DrawStyle.Fill
//        )
//    )
//}

@Composable
fun CustomBarChart(
    data: List<BarGroup>,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(200.dp),
    maxValue: Float = data.flatMap { it.bars }.maxOfOrNull { it.value } ?: 100f
) {
    Canvas(modifier = modifier.padding(16.dp)) {
        val barWidth = size.width / (data.size * 3)
        val spacing = barWidth

        data.forEachIndexed { groupIndex, barGroup ->
            barGroup.bars.forEachIndexed { barIndex, barData ->
                val barHeight = (barData.value / maxValue) * size.height
                val left = (groupIndex * 3 + barIndex) * barWidth
                val top = size.height - barHeight

                drawRect(
                    color = barData.color,
                    topLeft = Offset(left, top),
                    size = Size(barWidth, barHeight)
                )
            }
        }
    }
}


data class BarGroup(
    val label: String,
    val bars: List<BarData>
)

data class BarData(
    val label: String,
    val value: Float,
    val color: Color
)

