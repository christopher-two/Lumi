package org.christophertwo.qr.ui.screen.generator

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.alexzhirkevich.qrose.options.QrBallShape
import io.github.alexzhirkevich.qrose.options.QrBrush
import io.github.alexzhirkevich.qrose.options.QrFrameShape
import io.github.alexzhirkevich.qrose.options.QrPixelShape
import io.github.alexzhirkevich.qrose.options.brush
import io.github.alexzhirkevich.qrose.options.circle
import io.github.alexzhirkevich.qrose.options.roundCorners
import io.github.alexzhirkevich.qrose.options.solid
import io.github.alexzhirkevich.qrose.options.square
import io.github.alexzhirkevich.qrose.rememberQrCodePainter
import org.christophertwo.qr.domain.model.QrContentResponse
import org.christophertwo.qr.ui.components.AnimatedShimmerTextCustom
import org.christophertwo.qr.ui.components.ModernBackground
import org.christophertwo.qr.ui.theme.QrTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun QrGeneratorRoot(
    viewModel: QrGeneratorViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    QrGeneratorScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun QrGeneratorScreen(
    state: QrGeneratorState,
    onAction: (QrGeneratorAction) -> Unit,
) {
    ModernBackground()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = colorScheme.background.copy(alpha = 0f),
        contentColor = colorScheme.onBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // Contenedor del QR y mensajes
            Box(
                modifier = Modifier.size(300.dp),
                contentAlignment = Alignment.Center
            ) {
                Crossfade(
                    targetState = state,
                    label = "QrContentCrossfade"
                ) { currentState ->
                    when {
                        currentState.isLoading -> {
                            CircularProgressIndicator(modifier = Modifier.size(64.dp))
                        }

                        currentState.error != null -> {
                            Text(
                                text = "Error: ${currentState.error}",
                                color = colorScheme.error,
                                textAlign = TextAlign.Center
                            )
                        }

                        currentState.finalQrString.isNotBlank() -> {
                            CardQr(
                                finalQrString = currentState.finalQrString,
                                response = currentState.qrResponse
                            )
                        }

                        else -> {
                            AnimatedShimmerTextCustom(
                                text = "Qué generaremos hoy?",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 30.sp,
                                    textAlign = TextAlign.Center
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Input del usuario
            DataGeneratorMinimal(
                isLoading = state.isLoading,
                onAction = onAction
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun CardQr(
    finalQrString: String,
    response: QrContentResponse?
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = response?.colores?.fondo?.toColor()
                    ?: colorScheme.surfaceContainerHighest.copy(alpha = 0.5f),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        QrCode(
            content = finalQrString,
            response = response,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun QrCode(
    content: String,
    response: QrContentResponse?,
    modifier: Modifier = Modifier
) {
    val onSurface = colorScheme.onSurface
    // Determina el pincel (color sólido o gradiente)
    val brush = remember(response) {
        val principal = response?.colores?.principal
        when (principal?.tipo) {
            "gradiente" -> QrBrush.brush {
                Brush.linearGradient(
                    principal.valores.map { it.toColor() }
                )
            }

            else -> QrBrush.solid(
                principal?.valores?.firstOrNull()?.toColor() ?: onSurface
            )
        }
    }

    // Determina la forma de los píxeles
    val pixelShape = remember(response) {
        when (response?.estilo) {
            "redondeado" -> QrPixelShape.roundCorners()
            "puntos" -> QrPixelShape.circle()
            else -> QrPixelShape.square()
        }
    }

    val qrcodePainter: Painter = rememberQrCodePainter(content) {
        shapes {
            ball = QrBallShape.circle()
            darkPixel = pixelShape
            frame = QrFrameShape.roundCorners(.25f)
        }
        colors {
            dark = brush
        }
    }
    Image(
        painter = qrcodePainter,
        contentDescription = "Generated QR Code",
        modifier = modifier
    )
}

@Composable
fun DataGeneratorMinimal(
    isLoading: Boolean,
    onAction: (QrGeneratorAction) -> Unit
) {
    var textFieldValue by remember { mutableStateOf(TextFieldValue("")) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(28.dp),
        color = colorScheme.surfaceContainerHighest,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
                .animateContentSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BasicTextField(
                value = textFieldValue,
                onValueChange = { textFieldValue = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = colorScheme.onSurface
                ),
                decorationBox = { innerTextField ->
                    if (textFieldValue.text.isEmpty()) {
                        Text(
                            text = "Un QR para mi web, con puntos azules...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = colorScheme.onSurfaceVariant
                        )
                    }
                    innerTextField()
                },
                maxLines = 4,
                enabled = !isLoading
            )

            IconButton(
                onClick = {
                    if (textFieldValue.text.isNotBlank()) {
                        onAction(QrGeneratorAction.GenerateQrFromPrompt(textFieldValue.text))
                    }
                },
                enabled = textFieldValue.text.isNotBlank() && !isLoading,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Create,
                    contentDescription = "Generar QR",
                    tint = if (textFieldValue.text.isNotBlank() && !isLoading) {
                        colorScheme.primary
                    } else {
                        colorScheme.onSurfaceVariant
                    },
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

/**
 * Función de extensión para convertir un string hexadecimal a un objeto Color de Compose.
 * Incluye manejo de errores por si el formato no es válido.
 */
fun String.toColor(): Color {
    return try {
        Color(this.toColorInt())
    } catch (e: Exception) {
        // Devuelve un color por defecto si el formato es incorrecto
        Color.Black
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun QrGeneratorRootPreview() {
    QrTheme {
        QrGeneratorScreen(
            state = QrGeneratorState(finalQrString = "https://google.com"),
            onAction = {}
        )
    }
}
