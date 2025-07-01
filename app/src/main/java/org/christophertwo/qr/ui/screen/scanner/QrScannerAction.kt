package org.christophertwo.qr.ui.screen.scanner

import androidx.camera.core.ImageProxy

sealed interface QrScannerAction {
    data class ScanBarcode(val imageProxy: ImageProxy) : QrScannerAction
    object ResetScanner : QrScannerAction
}