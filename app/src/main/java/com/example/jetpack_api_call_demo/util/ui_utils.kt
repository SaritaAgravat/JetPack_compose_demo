package com.jetpack_demo.util
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest

@Composable
fun rememberSvgPainter(resourceName: String): androidx.compose.ui.graphics.painter.Painter {
    val context = LocalContext.current
    return rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data("android.resource://${context.packageName}/raw/$resourceName")
            .decoderFactory(SvgDecoder.Factory())
            .build()
    )
}
