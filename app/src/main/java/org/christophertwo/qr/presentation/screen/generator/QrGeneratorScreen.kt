package org.christophertwo.qr.presentation.screen.generator

import androidx.compose.animation.Crossfade
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.res.painterResource
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
import kotlinx.coroutines.launch
import org.christophertwo.qr.R
import org.christophertwo.qr.core.ui.theme.QrTheme
import org.christophertwo.qr.domain.model.QrContentResponse
import org.christophertwo.qr.presentation.components.AnimatedShimmerTextCustom
import org.christophertwo.qr.presentation.components.ModernBackground

@Composable
fun QrGeneratorRoot(
    viewModel: QrGeneratorViewModel
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
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val graphicsLayer = rememberGraphicsLayer()

    // Mostrar mensajes de éxito o error
    LaunchedEffect(state.downloadSuccess, state.error) {
        state.downloadSuccess?.let {
            snackbarHostState.showSnackbar(it)
        }
        state.error?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    ModernBackground()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = colorScheme.background.copy(alpha = 0f),
        contentColor = colorScheme.onBackground,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding()),
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
                    targetState = state.isLoading,
                    label = "QrContentCrossfade"
                ) { isLoading ->
                    when {
                        isLoading -> {
                            CircularProgressIndicator()
                        }

                        state.error != null && state.finalQrString.isBlank() -> {
                            Text(
                                text = "Error: ${state.error}",
                                color = colorScheme.error,
                                textAlign = TextAlign.Center
                            )
                        }

                        state.finalQrString.isNotBlank() -> {
                            CardQr(
                                finalQrString = state.finalQrString,
                                response = state.qrResponse,
                                graphicsLayer = graphicsLayer
                            )
                        }

                        else -> {
                            AnimatedShimmerTextCustom(
                                text = "¿Que Generamos?",
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
                hasQrCode = state.finalQrString.isNotBlank(),
                graphicsLayer = graphicsLayer,
                onAction = onAction
            )
        }
    }
}

@Composable
fun CardQr(
    finalQrString: String,
    response: QrContentResponse?,
    graphicsLayer: androidx.compose.ui.graphics.layer.GraphicsLayer
) {
    // Determinar el mejor color de fondo basándose en el color del QR
    val backgroundColor = remember(response) {
        determineBestBackgroundForScreen(response)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(24.dp)
            )
            .padding(20.dp)
            .drawWithCache {
                onDrawWithContent {
                    graphicsLayer.record {
                        this@onDrawWithContent.drawContent()
                    }
                    drawLayer(graphicsLayer)
                }
            },
        contentAlignment = Alignment.Center
    ) {
        QrCode(
            content = finalQrString,
            response = response,
            modifier = Modifier.fillMaxSize()
        )
    }
}

/**
 * Determina el mejor color de fondo para la pantalla basándose en el color principal del QR
 */
private fun determineBestBackgroundForScreen(qrResponse: QrContentResponse?): Color {
    if (qrResponse == null) {
        return Color.White.copy(alpha = 0.9f)
    }

    try {
        // Obtener el color principal del QR
        val principalColor = qrResponse.colores.principal.valores.firstOrNull()

        if (principalColor != null) {
            val qrColor = principalColor.toColorInt()

            // Calcular luminancia del color del QR
            val luminance = calculateLuminanceForScreen(qrColor)

            // Si el color es oscuro (luminancia baja), usar fondo claro
            // Si el color es claro (luminancia alta), usar fondo oscuro
            return if (luminance > 0.5) {
                // Color claro del QR -> Fondo oscuro
                Color.Black.copy(alpha = 0.85f)
            } else {
                // Color oscuro del QR -> Fondo claro
                Color.White.copy(alpha = 0.9f)
            }
        }
    } catch (e: Exception) {
        android.util.Log.e("QrGeneratorScreen", "Error al determinar color de fondo: ${e.message}")
    }

    // Por defecto, usar blanco con transparencia
    return Color.White.copy(alpha = 0.9f)
}

/**
 * Calcula la luminancia de un color (versión simplificada para Compose)
 */
private fun calculateLuminanceForScreen(color: Int): Double {
    val r = android.graphics.Color.red(color) / 255.0
    val g = android.graphics.Color.green(color) / 255.0
    val b = android.graphics.Color.blue(color) / 255.0

    // Aplicar corrección gamma
    val rLinear = if (r <= 0.03928) r / 12.92 else Math.pow((r + 0.055) / 1.055, 2.4)
    val gLinear = if (g <= 0.03928) g / 12.92 else Math.pow((g + 0.055) / 1.055, 2.4)
    val bLinear = if (b <= 0.03928) b / 12.92 else Math.pow((b + 0.055) / 1.055, 2.4)

    // Calcular luminancia relativa
    return 0.2126 * rLinear + 0.7152 * gLinear + 0.0722 * bLinear
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
    hasQrCode: Boolean,
    graphicsLayer: androidx.compose.ui.graphics.layer.GraphicsLayer,
    onAction: (QrGeneratorAction) -> Unit
) {
    var textFieldValue by remember { mutableStateOf(TextFieldValue("")) }
    val scope = rememberCoroutineScope()

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(
            topStart = 24.dp,
            topEnd = 24.dp
        ),
        color = colorScheme.surfaceContainerHighest,
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BasicTextField(
                value = textFieldValue,
                onValueChange = { textFieldValue = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = colorScheme.onSurface
                ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (textFieldValue.text.isEmpty()) {
                            Text(
                                text = "Un QR para mi web, con puntos azules...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = colorScheme.onSurfaceVariant
                            )
                        }
                        innerTextField()
                    }
                },
                maxLines = 2,
                enabled = !isLoading
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Botón de descarga
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = if (hasQrCode) {
                                colorScheme.primaryContainer
                            } else {
                                colorScheme.secondaryContainer.copy(alpha = 0.3f)
                            },
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = {
                            scope.launch {
                                try {
                                    val imageBitmap = graphicsLayer.toImageBitmap()
                                    val bitmap = imageBitmap.asAndroidBitmap()

                                    // Convertir a bitmap de software si es hardware bitmap
                                    val softwareBitmap = if (bitmap.config == android.graphics.Bitmap.Config.HARDWARE) {
                                        bitmap.copy(android.graphics.Bitmap.Config.ARGB_8888, false)
                                    } else {
                                        bitmap
                                    }

                                    onAction(QrGeneratorAction.DownloadQr(softwareBitmap))
                                } catch (e: Exception) {
                                    android.util.Log.e("QrGenerator", "Error al capturar QR: ${e.message}", e)
                                }
                            }
                        },
                        enabled = hasQrCode,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_download_24),
                            contentDescription = "Descargar QR",
                            tint = if (hasQrCode) {
                                colorScheme.onPrimaryContainer
                            } else {
                                colorScheme.onSecondaryContainer.copy(alpha = 0.3f)
                            },
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                // Botón de generar
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = if (textFieldValue.text.isNotBlank() && !isLoading) {
                                colorScheme.primaryContainer
                            } else {
                                colorScheme.secondaryContainer
                            },
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
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
                            painter = painterResource(id = R.drawable.baseline_create_24),
                            contentDescription = "Generar QR",
                            tint = if (textFieldValue.text.isNotBlank() && !isLoading) {
                                colorScheme.onPrimaryContainer
                            } else {
                                colorScheme.onSecondaryContainer
                            },
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
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