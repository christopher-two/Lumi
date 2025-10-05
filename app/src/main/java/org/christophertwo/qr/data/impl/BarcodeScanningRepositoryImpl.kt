package org.christophertwo.qr.data.impl

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.christophertwo.qr.domain.model.BarcodeModel
import org.christophertwo.qr.data.repository.BarcodeScanningRepository

/**
 * Implementación de [BarcodeScanningRepository] que utiliza ML Kit para escanear códigos de barras.
 *
 * Esta clase se encarga de configurar el escáner de códigos de barras, procesar las imágenes
 * y transformar los resultados en un formato utilizable por el dominio de la aplicación.
 */
class BarcodeScanningRepositoryImpl : BarcodeScanningRepository {

    // Configura las opciones del escáner de códigos de barras.
    // En este caso, solo se buscan códigos QR.
    private val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
        .build()

    // Obtiene una instancia del escáner de códigos de barras de ML Kit con las opciones configuradas.
    private val scanner = BarcodeScanning.getClient(options)

    /**
     * Procesa una imagen de CameraX para escanear códigos de barras.
     */
    @OptIn(ExperimentalGetImage::class)
    override fun scanBarcode(imageProxy: ImageProxy): Flow<Result<List<BarcodeModel>>> =
        // Utiliza callbackFlow para convertir las callbacks de ML Kit en un Flow de Kotlin Coroutines.
        callbackFlow {
            // Lanza una corutina para realizar el procesamiento de la imagen de forma asíncrona.
            launch {
                try {
                    val mediaImage = imageProxy.image
                    if (mediaImage != null) {
                        val image = InputImage.fromMediaImage(
                            mediaImage,
                            imageProxy.imageInfo.rotationDegrees
                        )

                        val result = scanner.process(image).await()

                        val barcodeModels = result.map { barcode ->
                            BarcodeModel(
                                rawValue = barcode.rawValue ?: "",
                                format = barcode.format.toString(),
                                type = barcode.valueType.toString()
                            )
                        }
                        trySend(Result.success(barcodeModels))
                    } else {
                        trySend(Result.failure(Exception("MediaImage no disponible")))
                    }
                } catch (e: Exception) {
                    trySend(Result.failure(e))
                } finally {
                    imageProxy.close()
                }
            }
            awaitClose { scanner.close() }
        }
}
