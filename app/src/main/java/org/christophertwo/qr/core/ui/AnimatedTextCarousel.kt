package org.christophertwo.qr.core.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun AnimatedTextCarousel(
    phrases: List<String>,
    modifier: Modifier = Modifier,
    intervalMillis: Long = 3000L,
    typingSpeedMs: Long = 80L,
    untypingSpeedMs: Long = 40L
) {
    var currentIndex by remember { mutableIntStateOf(0) }
    var targetProgress by remember { mutableFloatStateOf(1f) }

    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(
            durationMillis = if (targetProgress == 1f) {
                (phrases[currentIndex].length * typingSpeedMs).toInt()
            } else {
                (phrases[currentIndex].length * untypingSpeedMs).toInt()
            },
            easing = LinearEasing
        ),
        label = "typing_animation"
    )

    LaunchedEffect(currentIndex) {
        // Escribir el texto actual
        targetProgress = 1f

        // Esperar el tiempo de intervalo
        delay(intervalMillis)

        // Desesscribir el texto
        targetProgress = 0f

        // Esperar a que termine la animación de desescritura
        delay((phrases[currentIndex].length * untypingSpeedMs))

        // Cambiar al siguiente texto
        currentIndex = (currentIndex + 1) % phrases.size
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val currentPhrase = phrases[currentIndex]
        val visibleLength = (currentPhrase.length * animatedProgress).roundToInt()
        val displayText = currentPhrase.take(visibleLength)

        Text(
            text = displayText,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Medium,
                fontSize = 25.sp
            ),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp),
            minLines = 1 // Mantiene la altura constante
        )
    }
}