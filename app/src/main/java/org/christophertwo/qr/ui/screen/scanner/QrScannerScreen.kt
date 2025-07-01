package org.christophertwo.qr.ui.screen.scanner

import android.Manifest
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import org.christophertwo.qr.ui.components.CameraPreview
import org.christophertwo.qr.ui.components.ManagerCamPermissions
import org.christophertwo.qr.ui.components.StateContentQr
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

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (cameraPermissionState.status.isGranted) {
            CameraPreview(
                modifier = Modifier.weight(1f),
                onImageProxy = { imageProxy -> onAction(QrScannerAction.ScanBarcode(imageProxy)) }
            )
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