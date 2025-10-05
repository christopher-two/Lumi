package org.christophertwo.qr.presentation.screen.scanner

import android.Manifest
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import org.christophertwo.qr.presentation.components.CameraPreview
import org.christophertwo.qr.presentation.components.ManagerCamPermissions
import org.christophertwo.qr.presentation.components.StateContentQr
import org.koin.androidx.compose.koinViewModel

@Composable
fun QrScannerRoot(
    viewModel: QrScannerViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    QrScannerScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun QrScannerScreen(
    state: QrScannerState,
    onAction: (QrScannerAction) -> Unit,
) {
    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)
    ManagerCamPermissions(cameraPermissionState)

    FlowColumn(
        modifier = Modifier.fillMaxSize(),
        itemHorizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (cameraPermissionState.status.isGranted) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Cámara de fondo con blur
                CameraPreview(
                    modifier = Modifier.fillMaxSize(),
                    onImageProxy = { imageProxy -> onAction(QrScannerAction.ScanBarcode(imageProxy)) }
                )

                // Overlay semitransparente con área transparente para QR
                QrScannerOverlay(
                    modifier = Modifier.fillMaxSize()
                )
            }
        } else {
            Text(
                "Se necesita permiso de cámara para continuar.",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
    StateContentQr(
        state = state,
        onAction = onAction
    )
}

@Composable
private fun QrScannerOverlay(
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    // Calculamos el tamaño del área de escaneo
    val scanAreaSize = with(density) { 250.dp.toPx() }

    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        // Calculamos la posición del área de escaneo (centrado)
        val scanAreaLeft = (canvasWidth - scanAreaSize) / 2
        val scanAreaTop = (canvasHeight - scanAreaSize) / 2

        // Dibujamos el overlay oscuro con transparencia, excepto en el área de escaneo
        // Parte superior
        drawRect(
            color = Color.Black.copy(alpha = 0.5f),
            topLeft = Offset(0f, 0f),
            size = Size(canvasWidth, scanAreaTop)
        )

        // Parte inferior
        drawRect(
            color = Color.Black.copy(alpha = 0.5f),
            topLeft = Offset(0f, scanAreaTop + scanAreaSize),
            size = Size(canvasWidth, canvasHeight - (scanAreaTop + scanAreaSize))
        )

        // Parte izquierda
        drawRect(
            color = Color.Black.copy(alpha = 0.5f),
            topLeft = Offset(0f, scanAreaTop),
            size = Size(scanAreaLeft, scanAreaSize)
        )

        // Parte derecha
        drawRect(
            color = Color.Black.copy(alpha = 0.5f),
            topLeft = Offset(scanAreaLeft + scanAreaSize, scanAreaTop),
            size = Size(canvasWidth - (scanAreaLeft + scanAreaSize), scanAreaSize)
        )

        // Dibujamos el borde del área de escaneo
        drawRect(
            color = Color.White,
            topLeft = Offset(scanAreaLeft - 2, scanAreaTop - 2),
            size = Size(scanAreaSize + 4, scanAreaSize + 4),
            style = Stroke(width = 3f)
        )

        // Dibujamos las esquinas del área de escaneo
        drawScannerCorners(
            topLeft = Offset(scanAreaLeft, scanAreaTop),
            size = Size(scanAreaSize, scanAreaSize)
        )
    }
}
private fun DrawScope.drawScannerCorners(
    topLeft: Offset,
    size: Size,
    cornerLength: Float = 30f,
    cornerWidth: Float = 6f
) {
    val color = Color.White

    // Esquina superior izquierda
    drawLine(
        color = color,
        start = topLeft,
        end = Offset(topLeft.x + cornerLength, topLeft.y),
        strokeWidth = cornerWidth
    )
    drawLine(
        color = color,
        start = topLeft,
        end = Offset(topLeft.x, topLeft.y + cornerLength),
        strokeWidth = cornerWidth
    )

    // Esquina superior derecha
    val topRight = Offset(topLeft.x + size.width, topLeft.y)
    drawLine(
        color = color,
        start = topRight,
        end = Offset(topRight.x - cornerLength, topRight.y),
        strokeWidth = cornerWidth
    )
    drawLine(
        color = color,
        start = topRight,
        end = Offset(topRight.x, topRight.y + cornerLength),
        strokeWidth = cornerWidth
    )

    // Esquina inferior izquierda
    val bottomLeft = Offset(topLeft.x, topLeft.y + size.height)
    drawLine(
        color = color,
        start = bottomLeft,
        end = Offset(bottomLeft.x + cornerLength, bottomLeft.y),
        strokeWidth = cornerWidth
    )
    drawLine(
        color = color,
        start = bottomLeft,
        end = Offset(bottomLeft.x, bottomLeft.y - cornerLength),
        strokeWidth = cornerWidth
    )

    // Esquina inferior derecha
    val bottomRight = Offset(topLeft.x + size.width, topLeft.y + size.height)
    drawLine(
        color = color,
        start = bottomRight,
        end = Offset(bottomRight.x - cornerLength, bottomRight.y),
        strokeWidth = cornerWidth
    )
    drawLine(
        color = color,
        start = bottomRight,
        end = Offset(bottomRight.x, bottomRight.y - cornerLength),
        strokeWidth = cornerWidth
    )
}