package org.christophertwo.qr.domain.repository

import androidx.camera.core.ImageProxy
import kotlinx.coroutines.flow.Flow
import org.christophertwo.qr.domain.model.BarcodeModel

interface BarcodeScanningRepository {
    /**
     * Procesa una imagen de CameraX para escanear códigos de barras.
     * @param imageProxy La imagen a procesar.
     * @return Un Flow que emite una lista de códigos de barras encontrados.
     */
    fun scanBarcode(imageProxy: ImageProxy): Flow<Result<List<BarcodeModel>>>
}
