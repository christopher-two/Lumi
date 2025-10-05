package org.christophertwo.qr.data.impl

import android.content.Context
import com.google.ai.client.generativeai.GenerativeModel
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.christophertwo.qr.domain.model.QrContentResponse
import org.christophertwo.qr.data.repository.GeminiRepository

/**
 * Implementación de GeminiRepository que usa el SDK de Google AI.
 *
 * Esta clase es responsable de construir el prompt, llamar a la API de Gemini,
 * y parsear la respuesta JSON a un modelo de datos Kotlin.
 *
 * @param gson Una instancia de Gson para el parseo de JSON.
 */
class GeminiRepositoryImpl(
    private val gson: Gson,
    private val context: Context
) : GeminiRepository {

    override fun getStructuredQrContent(prompt: String): Flow<Result<QrContentResponse>> = flow {
        try {
            val fullPrompt = buildPrompt(prompt)
            val response = GenerativeModel(
                modelName = "gemini-2.0-flash-lite-001",
                apiKey = context.getSharedPreferences("ApiKey", Context.MODE_PRIVATE)
                    .getString("gemini_api_key", null) ?: "",
            ).generateContent(fullPrompt)

            val rawText = response.text
            if (rawText.isNullOrBlank()) {
                emit(Result.failure(Exception("La respuesta de la IA estaba vacía.")))
                return@flow
            }

            val jsonText = extractJson(rawText)
            if (jsonText.isNullOrBlank()) {
                emit(Result.failure(Exception("No se encontró un objeto JSON válido en la respuesta.")))
                return@flow
            }

            val qrContent = gson.fromJson(jsonText, QrContentResponse::class.java)
            emit(Result.success(qrContent))

        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    private fun extractJson(text: String): String? {
        val startIndex = text.indexOfFirst { it == '{' }
        val endIndex = text.indexOfLast { it == '}' }

        return if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
            text.substring(startIndex, endIndex + 1)
        } else {
            null
        }
    }

    private fun buildPrompt(userRequest: String): String {
        return """
        Eres un asistente experto en análisis de contenido y diseño de códigos QR. Tu única tarea es analizar la solicitud de un usuario y convertirla en un objeto JSON estricto y válido que una aplicación usará para generar el código QR. Debes inferir el tipo de contenido y estructurarlo correctamente.

    ### REGLAS ESTRICTAS:
    1.  **SOLO JSON:** Tu respuesta DEBE ser únicamente el objeto JSON. No incluyas explicaciones, saludos, ni texto adicional fuera del bloque JSON.
    2.  **INFERENCIA INTELIGENTE:** Analiza el texto del usuario para inferir todos los valores. Si el usuario no especifica un parámetro, utiliza los valores por defecto definidos. La inferencia más importante es la del tipo de contenido.
    3.  **ESTRUCTURA FIJA:** El JSON debe seguir siempre la siguiente estructura general.

    ### ESTRUCTURA JSON REQUERIDA:

    {
      "contenido": {
        "tipo": "string",
        "data": {}
      },
      "nivel_de_correccion": "string",
      "colores": {
        "fondo": "string",
        "principal": {
          "tipo": "string",
          "valores": []
        }
      },
      "estilo": "string"
    }

    ### EXPLICACIÓN DE LOS CAMPOS:

    * `contenido` (objeto): El corazón del QR.
        * `tipo` (string): El formato del contenido. Infiere el tipo a partir de la petición.
            * **Valores posibles:** "texto_plano", "vcard", "wifi", "geolocalizacion", "evento_calendario", "json", "markdown", "codigo", "email", "sms".
            * **Por defecto:** "texto_plano".
        * `data` (objeto/string): Los datos estructurados según el `tipo`.

    * `nivel_de_correccion` (string): Tolerancia a errores.
        * **Valores posibles:** "L", "M", "Q", "H". **Por defecto:** "Q".

    * `colores` (objeto): Colores del QR.
        * `fondo` (string): Color de fondo en hexadecimal. **Por defecto:** "#FFFFFF".
        * `principal` (objeto): Color de los módulos.
            * `tipo` (string): "solido" o "gradiente". **Por defecto:** "solido".
            * `valores` (array): Colores en hexadecimal. **Por defecto:** `["#000000"]`.

    * `estilo` (string): Forma de los módulos.
        * **Valores posibles:** "cuadrados", "redondeado", "puntos". **Por defecto:** "cuadrados".

    ---
    ### ESTRUCTURAS PARA `contenido.data` SEGÚN EL `tipo`:

    * **tipo: "texto_plano"**: `data` es un `string`.
    * **tipo: "vcard"**: `data` es un objeto para tarjetas de contacto.
    * **tipo: "wifi"**: `data` es un objeto para credenciales de red.
    * **tipo: "geolocalizacion"**: `data` es un objeto con coordenadas.
    * **tipo: "evento_calendario"**: `data` es un objeto para eventos (vEvent).
    * **tipo: "json"**: `data` es un objeto JSON válido. La IA debe construir el objeto.
    * **tipo: "markdown"**: `data` es un `string` con formato Markdown.
    * **tipo: "codigo"**: `data` es un objeto con el código fuente.
    * **tipo: "email"**: `data` es un objeto para un correo pre-rellenado.
    * **tipo: "sms"**: `data` es un objeto para un SMS pre-rellenado.

    ---

    Ahora, procesa la siguiente solicitud del usuario: &f
        $userRequest
        """.trimIndent()
    }
}

