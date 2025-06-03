package com.example.jetpack_api_call_demo.app_ui


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.EmptyBuildDrawCacheParams.size
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.input.KeyboardType.Companion.Text
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.ehsannarmani.compose_charts.ColumnChart
import ir.ehsannarmani.compose_charts.models.BarProperties
import ir.ehsannarmani.compose_charts.models.Bars
import ir.ehsannarmani.compose_charts.models.DrawStyle
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalDensity
import ir.ehsannarmani.compose_charts.models.LabelProperties
import androidx.compose.ui.graphics.PathEffect


@Composable
fun DynamicColumnChart(
    data: List<Bars>,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(220.dp), // Increased height slightly to make room for labels
    barThickness: Int = 15,
    spacing: Int = 0,
    drawStyle: DrawStyle? = null
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Bottom
    ) {
        // Chart itself
        ColumnChart(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), // Fill most space, leave space below for labels
            data = data,
            maxValue = 100.0,
            barProperties = BarProperties(
                thickness = barThickness.dp,
                spacing = spacing.dp,
                style = drawStyle ?: DrawStyle.Fill
            ),
            minValue = 0.0,
            labelProperties = LabelProperties(true),

        )


    }
}



@Composable
fun CustomBarChart(
    data: List<BarGroup>,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(200.dp),
    steps: Int = 4, // Y-axis grid lines
    xSteps: Int = 4, // X-axis grid lines
    minValue: Float? = null,
    maxValue: Float? = null
) {
    val labelColor = Color.Gray
    val axisLineColor = Color.LightGray
    val yAxisWidth = 40.dp
    val bottomLabelHeight = 30.dp
    val horizontalPadding =0.dp  // <-- New horizontal padding after Y axis

    val density = LocalDensity.current
    val yAxisPx = with(density) { yAxisWidth.toPx() }
    val bottomLabelPx = with(density) { bottomLabelHeight.toPx() }
    val horizontalPaddingPx = with(density) { horizontalPadding.toPx() }

    val actualMax = maxValue ?: data.flatMap { it.bars }.maxOfOrNull { it.value } ?: 100f
    val actualMin = minValue ?: data.flatMap { it.bars }.minOfOrNull { it.value } ?: 0f
    val valueRange = actualMax - actualMin



    BoxWithConstraints(modifier) {
        val canvasWidth = constraints.maxWidth.toFloat() - yAxisPx - horizontalPaddingPx
        val canvasHeight = constraints.maxHeight.toFloat() - bottomLabelPx

        val barGroupWidth = canvasWidth / data.size.toFloat()
        val barWidth = (barGroupWidth / (data.first().bars.size + 1).toFloat()) - 10
        val dashedEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = yAxisWidth, bottom = bottomLabelHeight)
        ) {
            // Draw Y-axis line
            drawLine(
                color = axisLineColor,
                start = Offset(0f, 0f),
                end = Offset(0f, canvasHeight),
                strokeWidth = 2f
            )

            // Draw Y-axis grid lines and labels
            for (i in 0..steps) {
                val value = actualMin + (valueRange * (i / steps.toFloat()))
                val y = canvasHeight - ((value - actualMin) / valueRange) * canvasHeight


                val x = i * (canvasWidth / xSteps)
                drawLine(
                    color = axisLineColor,
                    start = Offset(x, 0f),
                    end = Offset(x, canvasHeight),
                    strokeWidth = 1.dp.toPx(),
                    pathEffect = dashedEffect // Dotted effect
                )

                // ✅ Compose drawLine with dashed effect
                drawLine(
                    color = axisLineColor,
                    start = Offset(0f, y),
                    end = Offset(canvasWidth, y),
                    strokeWidth = 1.dp.toPx(),
                    pathEffect = dashedEffect
                )

                drawLine(
                    color = axisLineColor,
                    start = Offset(0f, canvasHeight),
                    end = Offset(canvasWidth, canvasHeight),
                    strokeWidth = 1f, // Solid, thicker line
                )

                // ✅ Text must still use nativeCanvas
                drawContext.canvas.nativeCanvas.drawText(
                    value.toInt().toString(),
                    -16f,
                    y + 8f,
                    android.graphics.Paint().apply {
                        color = android.graphics.Color.DKGRAY
                        textSize = 26f
                        textAlign = android.graphics.Paint.Align.RIGHT
                        isAntiAlias = true
                    }
                )
//                drawLine(
//                    color = axisLineColor,
//                    start = Offset(0f, y),
//                    end = Offset(canvasWidth + horizontalPaddingPx, y), // extend line under bars
//                    strokeWidth = 1f
//                )

                drawContext.canvas.nativeCanvas.drawText(
                    value.toInt().toString(),
                    -16f,
                    y + 8f,
                    android.graphics.Paint().apply {
                        color = android.graphics.Color.DKGRAY
                        textSize = 26f
                        textAlign = android.graphics.Paint.Align.RIGHT
                        isAntiAlias = true
                    }
                )
            }

            // Draw bars, starting after horizontalPaddingPx to leave space after Y-axis
            data.forEachIndexed { groupIndex, group ->
                group.bars.forEachIndexed { barIndex, bar ->
                    val normalizedValue = (bar.value - actualMin) / valueRange
                    val barHeight = normalizedValue * canvasHeight
                    val x = horizontalPaddingPx + groupIndex * barGroupWidth + (barIndex + 1) * barWidth
                    val y = canvasHeight - barHeight

                    drawRect(
                        color = bar.color,
                        topLeft = Offset(x, y),
                        size = Size(barWidth, barHeight)
                    )
                }
            }

            // X-axis numeric labels (e.g. 0, 25, 50, 75, 100)
            val xMax = 100f
            val xStepSize = canvasWidth / xSteps.toFloat()
            val xValueStep = xMax / xSteps.toFloat()

            for (i in 0..xSteps) {
                val x = horizontalPaddingPx + i * xStepSize
                val label = (i * xValueStep).toInt().toString()

                drawContext.canvas.nativeCanvas.drawText(
                    label,
                    x,
                    canvasHeight + 20f,
                    android.graphics.Paint().apply {
                        color = android.graphics.Color.DKGRAY
                        textSize = 26f
                        textAlign = android.graphics.Paint.Align.CENTER
                        isAntiAlias = true
                    }
                )
            }
        }

        // X-axis group labels below bars
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = yAxisWidth + horizontalPadding)
                .align(Alignment.BottomStart),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val labelWidthDp = with(LocalDensity.current) { barGroupWidth.toDp() }
            data.forEach { group ->
                Text(
                    text = group.label,
                    modifier = Modifier.width(labelWidthDp),
                    color = labelColor,
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp
                )
            }
        }
    }
}



//@Composable
//fun CustomBarChart(
//    data: List<BarGroup>,
//    modifier: Modifier = Modifier
//        .fillMaxWidth()
//        .height(200.dp),
//    steps: Int = 4, // Grid steps on Y-axis
//
//) {
//    val labelColor = Color.Gray
//    val axisLineColor = Color.LightGray
//    val yAxisWidth = 40.dp
//    val bottomLabelHeight = 30.dp
//
//    val density = LocalDensity.current
//    val yAxisPx = with(density) { yAxisWidth.toPx() }
//    val bottomLabelPx = with(density) { bottomLabelHeight.toPx() }
//
//    var maxValue = data.flatMap { it.bars }.maxOfOrNull { it.value } ?: 100f
//
//    BoxWithConstraints(modifier) {
//        val canvasWidth = constraints.maxWidth.toFloat() - yAxisPx
//        val canvasHeight = constraints.maxHeight.toFloat() - bottomLabelPx
//
//        val barGroupWidth = canvasWidth / data.size.toFloat()
//        val barWidth = barGroupWidth / (data.first().bars.size + 1).toFloat()
//
//        Canvas(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(start = yAxisWidth, bottom = bottomLabelHeight)
//
//        ) {
//            // Y-Axis lines and labels
//            // Y-Axis lines and labels
//            for (i in 0..steps) {
//                val value = maxValue * (i / steps.toFloat())
//                val y = canvasHeight - (value / maxValue) * canvasHeight
//
//                // Line
//                drawLine(
//                    color = axisLineColor,
//                    start = Offset(0f, y),
//                    end = Offset(canvasWidth, y),
//                    strokeWidth = 1f
//                )
//
//                // Y-axis label (offset -8 for padding)
//                drawContext.canvas.nativeCanvas.drawText(
//                    value.toInt().toString(),
//                    -16f, // further left so it's outside the drawing area
//                    y + 8f, // baseline alignment
//                    android.graphics.Paint().apply {
//                        color = android.graphics.Color.DKGRAY
//                        textSize = 26f
//                        textAlign = android.graphics.Paint.Align.RIGHT
//                        isAntiAlias = true
//                    }
//                )
//            }
//
//            // Draw bars
//            data.forEachIndexed { groupIndex, group ->
//                group.bars.forEachIndexed { barIndex, bar ->
//                    val barHeight = (bar.value / maxValue) * canvasHeight
//                    val x = groupIndex * barGroupWidth + (barIndex + 1) * barWidth
//                    val y = canvasHeight - barHeight
//
//                    drawRect(
//                        color = bar.color,
//                        topLeft = Offset(x, y),
//                        size = Size(barWidth, barHeight)
//                    )
//                }
//            }
//        }
//
//        // X-axis labels below the bars
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(start = yAxisWidth)
//                .align(Alignment.BottomStart),
//            horizontalArrangement = Arrangement.SpaceEvenly
//        ) {
//            val labelWidthDp = with(LocalDensity.current) { barGroupWidth.toDp() }
//
//
//            data.forEach { group ->
//                Text(
//                    text = group.label,
//                    modifier = Modifier.width(labelWidthDp),
//                    color = labelColor,
//                    textAlign = TextAlign.Center,
//                    fontSize = 12.sp
//                )
//            }
//        }
//    }
//}




data class BarGroup(
    val label: String,
    val bars: List<BarData>
)

data class BarData(
    val label: String,
    val value: Float,
    val color: Color
)

