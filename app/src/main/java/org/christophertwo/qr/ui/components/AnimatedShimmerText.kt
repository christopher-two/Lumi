package org.christophertwo.qr.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.christophertwo.qr.ui.theme.QrTheme


@Composable
fun AnimatedShimmerText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.headlineMedium,
    shimmerColors: List<Color> = listOf(
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
        MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    )
) {
    val infiniteTransition = rememberInfiniteTransition()

    val shimmerTranslateAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        )
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(shimmerTranslateAnim - 200f, 0f),
        end = Offset(shimmerTranslateAnim, 0f)
    )

    Text(
        text = text,
        modifier = modifier,
        style = style.copy(
            brush = brush
        )
    )
}

// Versión con más personalización
@Composable
fun AnimatedShimmerTextCustom(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.headlineMedium,
    shimmerColors: List<Color> = listOf(
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
    ),
    animationDuration: Int = 2500,
    shimmerWidth: Float = 300f
) {
    val infiniteTransition = rememberInfiniteTransition()

    val shimmerTranslateAnim by infiniteTransition.animateFloat(
        initialValue = -shimmerWidth,
        targetValue = 1000f + shimmerWidth,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = animationDuration,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        )
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(shimmerTranslateAnim - shimmerWidth, 0f),
        end = Offset(shimmerTranslateAnim, 0f)
    )

    Text(
        text = text,
        modifier = modifier,
        style = style.copy(
            brush = brush,
            fontWeight = FontWeight.Bold
        )
    )
}

// Versión minimalista que sigue el estilo del DataGenerator
@Composable
fun ShimmerTextMinimal(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.titleLarge
) {
    val infiniteTransition = rememberInfiniteTransition()

    val shimmerTranslateAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1800,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        )
    )

    val shimmerColors = listOf(
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
        MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(shimmerTranslateAnim - 200f, 0f),
        end = Offset(shimmerTranslateAnim, 0f)
    )

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = Color.Transparent
    ) {
        Text(
            text = text,
            style = style.copy(
                brush = brush,
                fontWeight = FontWeight.Medium
            )
        )
    }
}

// Componente para títulos con efecto reflejo
@Composable
fun TitleWithShimmer(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition()

    val shimmerTranslateAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 800f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2200,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        )
    )

    val titleBrush = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        ),
        start = Offset(shimmerTranslateAnim - 150f, 0f),
        end = Offset(shimmerTranslateAnim, 0f)
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium.copy(
                brush = titleBrush,
                fontWeight = FontWeight.Bold
            )
        )

        subtitle?.let {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
@Preview(
    showBackground = true,
    showSystemUi = true
)
fun ShimmerTextPreview() {
    QrTheme(
        darkTheme = false,
        dynamicColor = false
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AnimatedShimmerTextCustom(
                    text = "Que generaremos hoy?",
                    style = MaterialTheme.typography.bodyMedium
                        .copy(
                            fontSize = 30.sp
                        )
                )
            }
        }
    }
}