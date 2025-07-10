package org.christophertwo.qr.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun ModernBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        colorScheme.surfaceContainer.copy(alpha = 0.3f),
                        colorScheme.background.copy(alpha = 0.8f)
                    ),
                    radius = 1000f
                )
            )
            .blur(50.dp)
    ) {
        // Forma romboide principal (similar a la imagen)
        DiamondShape(
            color = colorScheme.primary.copy(alpha = 0.15f),
            size = 280.dp,
            offset = Offset(440f, 1600f),
            rotation = 45f
        )

        // Forma circular difusa
        CircularShape(
            color = colorScheme.tertiary.copy(alpha = 0.08f),
            size = 320.dp,
            offset = Offset(200f, -100f)
        )

        // Forma romboide secundaria
        DiamondShape(
            color = colorScheme.secondary.copy(alpha = 0.12f),
            size = 200.dp,
            offset = Offset(100f, 400f),
            rotation = 30f
        )

        // Forma rectangular con esquinas redondeadas
        RoundedRectShape(
            color = colorScheme.primary.copy(alpha = 0.06f),
            size = DpSize(240.dp, 180.dp),
            offset = Offset(-80f, 300f),
            rotation = 15f
        )

        // Círculo pequeño de acento
        CircularShape(
            color = colorScheme.primaryContainer.copy(alpha = 0.1f),
            size = 120.dp,
            offset = Offset(180f, 200f)
        )
    }
}

@Composable
fun DiamondShape(
    color: Color,
    size: Dp,
    offset: Offset = Offset(0f, 0f),
    rotation: Float = 45f,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size)
            .offset {
                IntOffset(offset.x.roundToInt(), offset.y.roundToInt())
            }
            .rotate(rotation)
            .background(
                color = color,
                shape = RoundedCornerShape(size / 8) // Esquinas ligeramente redondeadas
            )
            .blur(8.dp) // Efecto difuso
    )
}

@Composable
fun CircularShape(
    color: Color,
    size: Dp,
    offset: Offset = Offset(0f, 0f),
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size)
            .offset {
                IntOffset(offset.x.roundToInt(), offset.y.roundToInt())
            }
            .background(
                color = color,
                shape = CircleShape
            )
            .blur(12.dp) // Más difuso para círculos
    )
}

@Composable
fun RoundedRectShape(
    color: Color,
    size: DpSize,
    offset: Offset = Offset(0f, 0f),
    rotation: Float = 0f,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size.width, size.height)
            .offset {
                IntOffset(offset.x.roundToInt(), offset.y.roundToInt())
            }
            .rotate(rotation)
            .background(
                color = color,
                shape = RoundedCornerShape(size.width / 6)
            )
            .blur(6.dp)
    )
}
