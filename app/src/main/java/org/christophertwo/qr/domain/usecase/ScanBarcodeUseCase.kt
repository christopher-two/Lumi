package org.christophertwo.qr.domain.usecase

import androidx.camera.core.ImageProxy
import org.christophertwo.qr.data.repository.BarcodeScanningRepository

/**
 * Caso de uso para escanear códigos de barras a partir de un ImageProxy.
 *
 * Este caso de uso toma un [ImageProxy] como entrada, que generalmente proviene de CameraX,
 * y utiliza el [BarcodeScanningRepository] inyectado para realizar el escaneo del código de barras.
 *
 * @property repository El repositorio encargado de la lógica de escaneo de códigos de barras.
 */
class ScanBarcodeUseCase(
    private val repository: BarcodeScanningRepository
) {
    /**
     * Ejecuta el caso de uso para escanear un código de barras.
     *
     * @param imageProxy La imagen a procesar, encapsulada en un objeto [ImageProxy].
     *                   Es importante que el consumidor de este caso de uso maneje el cierre
     *                   del [ImageProxy] después de que el escaneo haya concluido o en caso de error,
     *                   ya que este caso de uso delega esa responsabilidad al [repository].
     * @return Un Flow que emite un [Result] que contiene una lista de [BarcodeModel].
     *         En caso de éxito, el Result contendrá la lista de códigos de barras encontrados.
     *         En caso de error, el Result contendrá una excepción.
     */
    operator fun invoke(imageProxy: ImageProxy) = repository.scanBarcode(imageProxy)
}
