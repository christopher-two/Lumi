package org.christophertwo.qr.domain.repository

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

class BarcodeScanningRepositoryImpl : BarcodeScanningRepository {

    private val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_QR_CODE) // Especificamos que solo queremos QR
        .build()

    private val scanner = BarcodeScanning.getClient(options)

    @androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
    override fun scanBarcode(imageProxy: ImageProxy): Flow<Result<List<BarcodeModel>>> =
        callbackFlow {
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
                    imageProxy.close() // ¡Muy importante cerrar el ImageProxy!
                }
            }
            awaitClose { scanner.close() }
        }
}