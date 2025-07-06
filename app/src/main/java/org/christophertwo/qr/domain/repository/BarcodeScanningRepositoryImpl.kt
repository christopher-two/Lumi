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
     * Este método implementa la función definida en la interfaz [BarcodeScanningRepository].
     *
     * @param imageProxy La imagen a procesar, encapsulada en un objeto ImageProxy.
     *                   Es crucial cerrar este ImageProxy después de su uso para liberar recursos.
     * @return Un [Flow] que emite un [Result] que contiene una lista de [BarcodeModel].
     *         Si el escaneo es exitoso, el Result contendrá la lista de [BarcodeModel] detectados.
     *         Si ocurre un error (por ejemplo, mediaImage no está disponible o hay un problema durante el procesamiento),
     *         el Result contendrá la excepción correspondiente.
     *         El Flow se completa después de emitir el resultado.
     *         Se asegura de cerrar el ImageProxy y el escáner de ML Kit al finalizar.
     */
    @androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
    override fun scanBarcode(imageProxy: ImageProxy): Flow<Result<List<BarcodeModel>>> =
        // Utiliza callbackFlow para convertir las callbacks de ML Kit en un Flow de Kotlin Coroutines.
        callbackFlow {
            // Lanza una corutina para realizar el procesamiento de la imagen de forma asíncrona.
            launch {
                try {
                    // Obtiene la imagen en formato MediaImage desde el ImageProxy.
                    // Es necesario marcar la función con @ExperimentalGetImage para acceder a imageProxy.image.
                    val mediaImage = imageProxy.image
                    if (mediaImage != null) {
                        // Crea un objeto InputImage a partir de MediaImage y la rotación de la imagen.
                        // ML Kit necesita esta información para procesar la imagen correctamente.
                        val image = InputImage.fromMediaImage(
                            mediaImage,
                            imageProxy.imageInfo.rotationDegrees
                        )

                        // Procesa la imagen utilizando el escáner de ML Kit.
                        // .await() suspende la corutina hasta que el procesamiento esté completo.
                        val result = scanner.process(image).await()

                        // Mapea los resultados del escáner (objetos Barcode de ML Kit)
                        // a una lista de BarcodeModel, que es el modelo de datos del dominio.
                        val barcodeModels = result.map { barcode ->
                            BarcodeModel(
                                rawValue = barcode.rawValue ?: "", // Valor crudo del código de barras.
                                format = barcode.format.toString(), // Formato del código de barras (ej. QR_CODE).
                                type = barcode.valueType.toString()  // Tipo de valor del código de barras (ej. TEXT, URL).
                            )
                        }
                        // Emite el resultado exitoso con la lista de BarcodeModel.
                        trySend(Result.success(barcodeModels))
                    } else {
                        // Si mediaImage es null, emite un error indicando que la imagen no está disponible.
                        trySend(Result.failure(Exception("MediaImage no disponible")))
                    }
                } catch (e: Exception) {
                    // Si ocurre cualquier otra excepción durante el procesamiento, emite el error.
                    trySend(Result.failure(e))
                } finally {
                    // Asegura que el ImageProxy se cierre siempre, incluso si hay errores,
                    // para liberar los recursos de la cámara.
                    imageProxy.close()
                }
            }
            // Cuando el Flow se cierra o cancela, se cierra el escáner de ML Kit.
            // Esto es importante para liberar los recursos utilizados por el escáner.
            awaitClose { scanner.close() }
        }
}
