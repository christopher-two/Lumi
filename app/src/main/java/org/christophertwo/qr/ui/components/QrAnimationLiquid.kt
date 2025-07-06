package org.christophertwo.qr.ui.components

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.RenderEffect
import android.graphics.Shader
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
// import androidx.compose.foundation.border // Not used
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay
import org.christophertwo.qr.ui.screen.generator.QrCode

private fun getRenderEffect(): RenderEffect {
    val blurEffect = RenderEffect
        .createBlurEffect(80f, 80f, Shader.TileMode.CLAMP) // Reduced blur, changed TileMode

    val alphaMatrix = RenderEffect.createColorFilterEffect(
        ColorMatrixColorFilter(
            ColorMatrix(
                floatArrayOf(
                    1f, 0f, 0f, 0f, 0f,
                    0f, 1f, 0f, 0f, 0f,
                    0f, 0f, 1f, 0f, 0f,
                    0f, 0f, 0f, 25f, -2500f // Adjusted alpha matrix
                )
            )
        )
    )

    return RenderEffect
        .createChainEffect(alphaMatrix, blurEffect)
}

@Composable
fun Circle(
    color: Color,
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = CircleShape,
    content: @Composable () -> Unit = {}
) {
    Box(
        modifier = modifier
            .padding(10.dp) // Keep padding for some spacing if desired
            .size(56.dp) // Adjusted scale animation
            .background(color, shape)
    ){
        content()
    }
}

@Composable
fun QrAnimationLiquid() {
    var isClick by remember { mutableStateOf(false) }
    var showQrCode by remember { mutableStateOf(false) } // Nuevo estado para la visibilidad del QR

    val boxAnimated = animateFloatAsState(
        targetValue = if (isClick) 10f else 0f,
        animationSpec = tween(
            durationMillis = 500,
            easing = LinearEasing
        ),
        label = "BoxAnimation"
    )
    val boxSizeAnimated = animateFloatAsState(
        targetValue = if (isClick) 200f else 50f,
        animationSpec = tween(
            durationMillis = 500,
            easing = LinearEasing
        ),
        label = "BoxSizeAnimation"
    )
    val boxRoundedAnimated = animateFloatAsState(
        targetValue = if (isClick) 1f else 360f, // Assuming 1f is less rounded, 360f is more circle-like for dp in RoundedCornerShape
        animationSpec = tween(
            durationMillis = 500,
            easing = LinearEasing
        ),
        label = "BoxRotationAnimation" // This seems to control corner radius
    )
    val boxBackgroundAnimated = animateColorAsState(
        targetValue = if (isClick) colorScheme.primaryContainer else colorScheme.tertiary,
        animationSpec = tween(
            durationMillis = 500,
            easing = LinearEasing
        ),
        label = "BoxBackgroundAnimation"
    )

    val qrBlurAnimated = animateFloatAsState(
        targetValue = if (isClick) 100f else 0f,
        animationSpec = tween(
            durationMillis = 1500,
            easing = LinearEasing
        ),
        label = "QrBlurAnimation"
    )

    LaunchedEffect(isClick) {
        if (isClick) {
            delay(500L) // Retraso de 400ms antes de mostrar el QR
            showQrCode = true
        } else {
            showQrCode = false // Ocultar inmediatamente si la animación se revierte
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box( // Main container for layering
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Layer 1: Blurred background elements
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        this.renderEffect = getRenderEffect().asComposeRenderEffect()
                    }
                    .align(Alignment.Center) // Center this blurred container
            ) {
                // Blurred Circle 1 (the clickable trigger)
                Circle(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.BottomCenter) // Aligns within the blurred Box
                        .clickable(onClick = { isClick = !isClick })
                )

                // Blurred animated background (representing the expanding part)
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter) // Aligns within the blurred Box
                        .offset(y = (-20).dp * boxAnimated.value)
                        .size(boxSizeAnimated.value.dp)
                        .graphicsLayer { // Apply rounded corners for background
                            shape = RoundedCornerShape(boxRoundedAnimated.value.dp)
                            clip = true
                        }
                        .background(boxBackgroundAnimated.value) // Animated background color
                        .border( // Animated border
                            width = 1.dp,
                            color = colorScheme.tertiary,
                            shape = RoundedCornerShape(boxRoundedAnimated.value.dp) // Ensure border matches shape
                        )
                )
            }

            // Layer 2: Sharp QrCode in its animated container
            if (showQrCode) { // Condición cambiada a showQrCode
                Box(
                    modifier = Modifier
                        .offset(y = (0).dp * boxAnimated.value)
                        .size(boxSizeAnimated.value.dp)
                        .graphicsLayer { // For clipping the QrCode to the animated shape
                            shape = RoundedCornerShape(boxRoundedAnimated.value.dp)
                            clip = true
                        }
                        .zIndex(10f)
                        .alpha(qrBlurAnimated.value),
                    contentAlignment = Alignment.BottomCenter // Center the QrCode within this animated Box
                ) {
                    QrCode(
                        content = "Hola Mundo",
                        modifier = Modifier.padding(10.dp) // Original padding for the QrCode
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    apiLevel = 35 // Ensure API level supports RenderEffect
)
@Composable
fun QrAnimationLiquidPreview() {
    QrAnimationLiquid()
}

