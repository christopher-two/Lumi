package org.christophertwo.qr.presentation.components

import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Un Composable que muestra una vista previa de la cámara y proporciona ImageProxy para el análisis.
 *
 * Este Composable encapsula la lógica para configurar y mostrar una vista previa de la cámara
 * utilizando CameraX y Jetpack Compose. También configura un analizador de imágenes para
 * procesar los frames de la cámara.
 *
 * @param modifier Modificador de Compose para personalizar la apariencia y el comportamiento del Composable.
 * @param cameraSelector Selector para elegir la cámara a utilizar (por ejemplo, frontal o trasera).
 *                       Por defecto, utiliza la cámara trasera.
 * @param onImageProxy Callback que se invoca con cada [ImageProxy] disponible del análisis de la cámara.
 *                     El consumidor es responsable de cerrar el [ImageProxy] después de su uso.
 *
 * Importante: Este Composable asume que los permisos de cámara necesarios ya han sido otorgados.
 * En una aplicación real, asegúrate de manejar la solicitud y el estado de los permisos
 * antes de intentar utilizar este Composable. Deberías integrar la lógica de permisos
 * antes de invocar este Composable o condicionalmente en función del estado de los permisos.
 */
@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA,
    onImageProxy: (ImageProxy) -> Unit
) {
    // Obtiene el LifecycleOwner actual desde la composición local.
    // Es necesario para vincular el ciclo de vida de la cámara al ciclo de vida del Composable.
    val lifecycleOwner = LocalLifecycleOwner.current

    // Crea y recuerda un ExecutorService de un solo hilo para el análisis de imágenes.
    // 'remember' asegura que el executor persista a través de las recomposiciones.
    val cameraExecutor: ExecutorService = remember { Executors.newSingleThreadExecutor() }

    // DisposableEffect se utiliza para manejar la creación y liberación de recursos
    // que deben limpiarse cuando el Composable deja la composición.
    DisposableEffect(Unit) { // El efecto se ejecuta una vez cuando el Composable entra en la composición.
        onDispose {
            // Cuando el Composable se elimina, se apaga el cameraExecutor.
            // Esto es crucial para liberar recursos y evitar fugas.
            cameraExecutor.shutdown()
        }
    }

    // AndroidView se utiliza para incrustar una vista de Android tradicional (PreviewView)
    // dentro de un layout de Jetpack Compose.
    AndroidView(
        modifier = modifier, // Aplica el modificador pasado al Composable.
        factory = { ctx ->
            // El bloque 'factory' se ejecuta una vez para crear la instancia de la vista.
            // Crea una instancia de PreviewView, que es la vista que mostrará la salida de la cámara.
            val previewView = PreviewView(ctx)

            // Obtiene una instancia futura del ProcessCameraProvider.
            // ProcessCameraProvider se utiliza para vincular el ciclo de vida de la cámara
            // al LifecycleOwner de la aplicación.
            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

            // Agrega un listener a cameraProviderFuture.
            // Este listener se ejecutará cuando el ProcessCameraProvider esté disponible.
            cameraProviderFuture.addListener({
                // Obtiene la instancia de ProcessCameraProvider desde el futuro.
                val cameraProvider = cameraProviderFuture.get()

                // Configura el caso de uso de Preview.
                // Preview se utiliza para mostrar la vista previa de la cámara.
                val preview = Preview.Builder().build().also {
                    // Vincula el SurfaceProvider de PreviewView al caso de uso de Preview.
                    // Esto conecta la salida de la cámara con la vista que la muestra.
                    it.surfaceProvider = previewView.surfaceProvider
                }

                // Configura el caso de uso de ImageAnalysis.
                // ImageAnalysis se utiliza para acceder a los frames de la cámara para su procesamiento (por ejemplo, escaneo de códigos QR).
                val imageAnalysis = ImageAnalysis.Builder()
                    // Establece la estrategia de contrapresión.
                    // STRATEGY_KEEP_ONLY_LATEST asegura que solo se procese la última imagen disponible,
                    // descartando las anteriores si el analizador es más lento que la tasa de frames.
                    // Esto es útil para aplicaciones en tiempo real.
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        // Establece el analizador para el caso de uso de ImageAnalysis.
                        // El análisis se ejecutará en el 'cameraExecutor' (hilo separado).
                        // 'onImageProxy' es la función lambda que se llamará con cada frame.
                        it.setAnalyzer(cameraExecutor, onImageProxy)
                    }

                try {
                    // Antes de vincular nuevos casos de uso, desvincula todos los casos de uso anteriores
                    // del cameraProvider. Esto asegura una configuración limpia.
                    cameraProvider.unbindAll()

                    // Vincula los casos de uso (Preview y ImageAnalysis) al ciclo de vida.
                    // La cámara ahora comenzará a funcionar y se gestionará automáticamente
                    // según el ciclo de vida proporcionado por 'lifecycleOwner'.
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner, // El LifecycleOwner al que se vinculará la cámara.
                        cameraSelector, // El selector de cámara (frontal, trasera).
                        preview,        // El caso de uso de Preview.
                        imageAnalysis   // El caso de uso de ImageAnalysis.
                    )
                } catch (e: Exception) {
                    // Captura cualquier excepción que pueda ocurrir durante la vinculación
                    // o configuración de la cámara.
                    Log.e("CameraPreview", "Error al iniciar la cámara", e)
                    // Considera un manejo de errores más robusto para producción,
                    // como mostrar un mensaje al usuario o reintentar.
                }
                // El listener se ejecuta en el hilo principal de Android.
            }, ContextCompat.getMainExecutor(ctx))

            // Devuelve la PreviewView creada para que AndroidView la muestre.
            previewView
        }
    )
}
