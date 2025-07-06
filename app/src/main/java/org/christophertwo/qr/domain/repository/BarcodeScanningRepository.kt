package org.christophertwo.qr.domain.repository

import androidx.camera.core.ImageProxy
import kotlinx.coroutines.flow.Flow
import org.christophertwo.qr.domain.model.BarcodeModel

/**
 * Interfaz para el repositorio de escaneo de códigos de barras.
 * Define el contrato para la funcionalidad de escaneo de códigos de barras,
 * permitiendo la abstracción de la implementación concreta.
 */
interface BarcodeScanningRepository {
    /**
     * Procesa una imagen de CameraX para escanear códigos de barras.
     * Utiliza ML Kit para detectar códigos QR en la imagen proporcionada.
     *
     * @param imageProxy La imagen a procesar, encapsulada en un objeto ImageProxy.
     *                   Es importante cerrar este ImageProxy después de su uso para liberar recursos.
     * @return Un Flow que emite un [Result] que contiene una lista de [BarcodeModel].
     *         En caso de éxito, el Result contendrá la lista de códigos de barras encontrados.
     *         En caso de error (por ejemplo, si mediaImage no está disponible o durante el procesamiento),
     *         el Result contendrá una excepción.
     *         El Flow se completa después de emitir el resultado.
     *         Se asegura de cerrar el ImageProxy y el escáner de ML Kit al finalizar.
     */
    fun scanBarcode(imageProxy: ImageProxy): Flow<Result<List<BarcodeModel>>>
}

