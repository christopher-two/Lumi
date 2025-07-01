package org.christophertwo.qr.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.christophertwo.qr.domain.model.BarcodeModel
import org.christophertwo.qr.ui.screen.scanner.QrScannerAction
import org.christophertwo.qr.ui.screen.scanner.QrScannerState

@Composable
fun StateContentQr(
    state: QrScannerState,
    onAction: (QrScannerAction) -> Unit,
    onNavigateToUrl: (String) -> Unit = {}
) {
    if (state.barcodes.isNotEmpty() || state.error != null) {
        Dialog(
            onDismissRequest = {
                if (state.barcodes.isNotEmpty()) onAction(QrScannerAction.ResetScanner)
            },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            ),
            content = {
                EnhancedQrResultCard(
                    state = state,
                    onAction = onAction,
                    onNavigateToUrl = onNavigateToUrl
                )
            }
        )
    }
}


@Composable
@Preview
private fun EnhancedContentPreview() {
    EnhancedQrResultCard(
        state = QrScannerState(
            barcodes = listOf(
                BarcodeModel(
                    rawValue = """{"nombre": "Juan Pérez", "edad": 30, "email": "juan@example.com"}""",
                    format = "",
                    type = ""
                )
            ),
            error = null
        ),
        onAction = {},
        onNavigateToUrl = {}
    )
}