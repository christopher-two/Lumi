package org.christophertwo.qr.presentation.screen.generator

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.graphics.createBitmap
import androidx.core.graphics.toColorInt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.christophertwo.qr.data.repository.GeminiRepository
import org.christophertwo.qr.domain.model.QrContentResponse
import java.io.OutputStream
import kotlin.math.sqrt

class QrGeneratorViewModel(
    private val geminiRepository: GeminiRepository,
    private val appContext: Context
) : ViewModel() {

    private val _state = MutableStateFlow(QrGeneratorState())
    val state = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = QrGeneratorState()
        )

    fun onAction(action: QrGeneratorAction) {
        when (action) {
            is QrGeneratorAction.GenerateQrFromPrompt -> {
                generateQr(action.prompt)
            }

            is QrGeneratorAction.DownloadQr -> {
                downloadQr(action.bitmap)
            }
        }
    }

    private fun generateQr(prompt: String) {
        _state.update {
            it.copy(
                isLoading = true,
                error = null,
                finalQrString = "",
                qrResponse = null
            )
        }

        viewModelScope.launch {
            geminiRepository.getStructuredQrContent(prompt)
                .onEach { result ->
                    Log.d("QrGeneratorViewModel", "Received result: $result")

                    result
                        .onSuccess { response ->
                            val finalString = processQrData(response)
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    qrResponse = response,
                                    finalQrString = finalString,
                                    error = null
                                )
                            }
                        }
                        .onFailure { error ->
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    error = error.message ?: "Ocurrió un error desconocido",
                                    finalQrString = "",
                                    qrResponse = null
                                )
                            }
                        }
                }.launchIn(viewModelScope)
        }
    }

    /**
     * Procesa la respuesta de la IA y la convierte en el string final para el QR.
     */
    private fun processQrData(response: QrContentResponse): String {
        return when (response.contenido.tipo) {
            "texto_plano" -> response.contenido.data.jsonPrimitive.content

            "vcard" -> {
                val dataObj = response.contenido.data.jsonObject
                val nombre = dataObj["nombre"]?.jsonPrimitive?.contentOrNull ?: ""
                val apellido = dataObj["apellido"]?.jsonPrimitive?.contentOrNull ?: ""
                val telefono = dataObj["telefono"]?.jsonPrimitive?.contentOrNull ?: ""
                val email = dataObj["email"]?.jsonPrimitive?.contentOrNull ?: ""
                val organizacion = dataObj["organizacion"]?.jsonPrimitive?.contentOrNull ?: ""
                val url = dataObj["url"]?.jsonPrimitive?.contentOrNull ?: ""

                buildString {
                    append("BEGIN:VCARD\n")
                    append("VERSION:3.0\n")
                    append("N:$apellido;$nombre\n")
                    append("FN:$nombre $apellido\n")
                    if (telefono.isNotEmpty()) append("TEL;TYPE=CELL:$telefono\n")
                    if (email.isNotEmpty()) append("EMAIL:$email\n")
                    if (organizacion.isNotEmpty()) append("ORG:$organizacion\n")
                    if (url.isNotEmpty()) append("URL:$url\n")
                    append("END:VCARD")
                }
            }

            "wifi" -> {
                val dataObj = response.contenido.data.jsonObject
                val ssid = dataObj["ssid"]?.jsonPrimitive?.content ?: ""
                val password = dataObj["password"]?.jsonPrimitive?.contentOrNull ?: ""
                val security = dataObj["tipo_seguridad"]?.jsonPrimitive?.contentOrNull ?: "WPA"
                "WIFI:T:$security;S:$ssid;P:$password;;"
            }

            "geolocalizacion" -> {
                val dataObj = response.contenido.data.jsonObject
                val latitud = dataObj["latitud"]?.jsonPrimitive?.content ?: ""
                val longitud = dataObj["longitud"]?.jsonPrimitive?.content ?: ""
                "geo:$latitud,$longitud"
            }

            "evento_calendario" -> {
                val dataObj = response.contenido.data.jsonObject
                val titulo = dataObj["titulo"]?.jsonPrimitive?.contentOrNull ?: ""
                val ubicacion = dataObj["ubicacion"]?.jsonPrimitive?.contentOrNull ?: ""
                val descripcion = dataObj["descripcion"]?.jsonPrimitive?.contentOrNull ?: ""
                val fechaInicio = dataObj["fecha_inicio"]?.jsonPrimitive?.contentOrNull ?: ""
                val fechaFin = dataObj["fecha_fin"]?.jsonPrimitive?.contentOrNull ?: ""

                buildString {
                    append("BEGIN:VEVENT\n")
                    append("SUMMARY:$titulo\n")
                    if (ubicacion.isNotEmpty()) append("LOCATION:$ubicacion\n")
                    if (descripcion.isNotEmpty()) append("DESCRIPTION:$descripcion\n")
                    if (fechaInicio.isNotEmpty()) append("DTSTART:$fechaInicio\n")
                    if (fechaFin.isNotEmpty()) append("DTEND:$fechaFin\n")
                    append("END:VEVENT")
                }
            }

            "email" -> {
                val dataObj = response.contenido.data.jsonObject
                val destinatario = dataObj["destinatario"]?.jsonPrimitive?.contentOrNull ?: ""
                val asunto = dataObj["asunto"]?.jsonPrimitive?.contentOrNull ?: ""
                val cuerpo = dataObj["cuerpo"]?.jsonPrimitive?.contentOrNull ?: ""
                "mailto:$destinatario?subject=$asunto&body=$cuerpo"
            }

            "sms" -> {
                val dataObj = response.contenido.data.jsonObject
                val numero = dataObj["numero"]?.jsonPrimitive?.contentOrNull ?: ""
                val mensaje = dataObj["mensaje"]?.jsonPrimitive?.contentOrNull ?: ""
                "smsto:$numero:$mensaje"
            }

            "json", "markdown", "codigo" -> response.contenido.data.toString()

            else -> response.contenido.data.toString()
        }
    }

    private fun downloadQr(bitmap: Bitmap) {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    // Crear bitmap con fondo blanco y texto
                    val qrResponse = _state.value.qrResponse
                    val processedBitmap = createBitmapWithBranding(bitmap, qrResponse)
                    saveImageToGallery(processedBitmap)
                }

                _state.update {
                    it.copy(
                        downloadSuccess = if (result) "QR guardado exitosamente" else null,
                        error = if (!result) "Error al guardar el QR" else null
                    )
                }

                // Limpiar mensaje después de 3 segundos
                kotlinx.coroutines.delay(3000)
                _state.update {
                    it.copy(downloadSuccess = null, error = null)
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        error = "Error al guardar: ${e.message}",
                        downloadSuccess = null
                    )
                }
            }
        }
    }

    /**
     * Calcula la luminancia de un color usando la fórmula de luminancia relativa.
     * Valores más altos = colores más claros
     */
    private fun calculateLuminance(color: Int): Double {
        val r = Color.red(color) / 255.0
        val g = Color.green(color) / 255.0
        val b = Color.blue(color) / 255.0

        // Aplicar corrección gamma
        val rLinear = if (r <= 0.03928) r / 12.92 else Math.pow((r + 0.055) / 1.055, 2.4)
        val gLinear = if (g <= 0.03928) g / 12.92 else Math.pow((g + 0.055) / 1.055, 2.4)
        val bLinear = if (b <= 0.03928) b / 12.92 else Math.pow((b + 0.055) / 1.055, 2.4)

        // Calcular luminancia relativa
        return 0.2126 * rLinear + 0.7152 * gLinear + 0.0722 * bLinear
    }

    /**
     * Calcula el contraste entre dos colores según WCAG
     * Retorna un valor entre 1 y 21
     */
    private fun calculateContrast(color1: Int, color2: Int): Double {
        val lum1 = calculateLuminance(color1)
        val lum2 = calculateLuminance(color2)

        val lighter = maxOf(lum1, lum2)
        val darker = minOf(lum1, lum2)

        return (lighter + 0.05) / (darker + 0.05)
    }

    /**
     * Determina el mejor color de fondo basándose en el color principal del QR
     */
    private fun determineBestBackgroundColor(qrResponse: QrContentResponse?): Int {
        if (qrResponse == null) {
            return Color.WHITE // Fondo blanco por defecto
        }

        try {
            // Obtener el color principal del QR
            val principalColor = qrResponse.colores?.principal?.valores?.firstOrNull()

            if (principalColor != null) {
                val qrColor = principalColor.toColorInt()

                // Calcular contraste con blanco y negro
                val contrastWithWhite = calculateContrast(qrColor, Color.WHITE)
                val contrastWithBlack = calculateContrast(qrColor, Color.BLACK)

                // Elegir el fondo que proporcione mayor contraste
                // Un contraste mínimo recomendado es 4.5:1 para texto normal
                return if (contrastWithWhite > contrastWithBlack) {
                    Color.WHITE
                } else {
                    Color.BLACK
                }
            }
        } catch (e: Exception) {
            Log.e("QrGeneratorViewModel", "Error al determinar color de fondo: ${e.message}")
        }

        // Por defecto, usar blanco
        return Color.WHITE
    }

    /**
     * Determina el mejor color de texto basándose en el color de fondo
     */
    private fun determineTextColor(backgroundColor: Int): Int {
        return if (backgroundColor == Color.WHITE) {
            "#666666".toColorInt() // Texto gris oscuro para fondo blanco
        } else {
            "#CCCCCC".toColorInt() // Texto gris claro para fondo negro
        }
    }

    private fun createBitmapWithBranding(qrBitmap: Bitmap, qrResponse: QrContentResponse?): Bitmap {
        // Dimensiones del QR original
        val qrWidth = qrBitmap.width
        val qrHeight = qrBitmap.height

        // Espacio adicional para el padding y el texto
        val padding = 40 // Padding alrededor del QR
        val textHeight = 40 // Altura para el texto "Generado por Lumi"

        // Crear un nuevo bitmap con espacio adicional
        val finalWidth = qrWidth + (padding * 2)
        val finalHeight = qrHeight + (padding * 2) + textHeight

        val finalBitmap = createBitmap(finalWidth, finalHeight)
        val canvas = Canvas(finalBitmap)

        // Determinar el mejor color de fondo según el color del QR
        val backgroundColor = determineBestBackgroundColor(qrResponse)
        val textColor = determineTextColor(backgroundColor)

        // Dibujar fondo con el color determinado
        canvas.drawColor(backgroundColor)

        // Dibujar el QR en el centro con padding
        canvas.drawBitmap(qrBitmap, padding.toFloat(), padding.toFloat(), null)

        // Configurar el paint para el texto
        val textPaint = Paint().apply {
            color = textColor
            textSize = 20f
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
            typeface = android.graphics.Typeface.create(
                android.graphics.Typeface.DEFAULT,
                android.graphics.Typeface.NORMAL
            )
        }

        // Calcular posición del texto (centrado horizontalmente, debajo del QR)
        val textX = finalWidth / 2f
        val textY = qrHeight + (padding * 2) + (textHeight / 2f) + 15f

        // Dibujar el texto
        canvas.drawText("Generado por Lumi", textX, textY, textPaint)

        return finalBitmap
    }

    private fun saveImageToGallery(bitmap: Bitmap): Boolean {
        val filename = "QR_${System.currentTimeMillis()}.png"
        var fos: OutputStream? = null

        return try {
            // Para Android 10 y superior
            val resolver = appContext.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_PICTURES + "/LumiQR"
                )
            }

            val imageUri =
                resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            fos = imageUri?.let { resolver.openOutputStream(it) }

            fos?.use {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
            }
            true
        } catch (e: Exception) {
            Log.e("QrGeneratorViewModel", "Error saving image: ${e.message}", e)
            false
        } finally {
            fos?.close()
        }
    }
}