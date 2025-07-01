package org.christophertwo.qr.domain.usecase

import androidx.camera.core.ImageProxy
import org.christophertwo.qr.domain.repository.BarcodeScanningRepository

class ScanBarcodeUseCase(
    private val repository: BarcodeScanningRepository
) {
    operator fun invoke(imageProxy: ImageProxy) = repository.scanBarcode(imageProxy)
}