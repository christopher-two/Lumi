package org.christophertwo.qr.data.impl

import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.Schema
import com.google.firebase.ai.type.generationConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.json.Json
import org.christophertwo.qr.data.repository.GeminiRepository
import org.christophertwo.qr.domain.model.QrContentResponse

/**
 * Implementación de GeminiRepository que usa el SDK de Firebase AI.
 *
 * Esta clase es responsable de construir el prompt, llamar a la API de Gemini,
 * y parsear la respuesta JSON a un modelo de datos Kotlin usando kotlinx.serialization.
 */
class GeminiRepositoryImpl : GeminiRepository {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    private val jsonSchema = Schema.obj(
        mapOf(
            "contenido" to Schema.obj(
                mapOf(
                    "tipo" to Schema.enumeration(
                        listOf(
                            "texto_plano",
                            "vcard",
                            "wifi",
                            "geolocalizacion",
                            "evento_calendario",
                            "json",
                            "markdown",
                            "codigo",
                            "email",
                            "sms"
                        )
                    ),
                    "data" to Schema.string()
                )
            ),
            "nivel_de_correccion" to Schema.enumeration(listOf("L", "M", "Q", "H")),
            "colores" to Schema.obj(
                mapOf(
                    "fondo" to Schema.string(),
                    "principal" to Schema.obj(
                        mapOf(
                            "tipo" to Schema.enumeration(listOf("solido", "gradiente")),
                            "valores" to Schema.array(Schema.string())
                        )
                    )
                )
            ),
            "estilo" to Schema.enumeration(listOf("cuadrados", "redondeado", "puntos"))
        )
    )

    private val model = Firebase.ai(backend = GenerativeBackend.googleAI()).generativeModel(
        modelName = "gemini-2.0-flash-exp",
        generationConfig = generationConfig {
            responseMimeType = "application/json"
            responseSchema = jsonSchema
        }
    )

    override fun getStructuredQrContent(prompt: String): Flow<Result<QrContentResponse>> = flow {
        try {
            val fullPrompt = buildPrompt(prompt)
            val response = model.generateContent(fullPrompt)

            val rawText = response.text
            if (rawText.isNullOrBlank()) {
                emit(Result.failure(Exception("La respuesta de la IA estaba vacía.")))
                return@flow
            }

            val qrContent = json.decodeFromString<QrContentResponse>(rawText)
            emit(Result.success(qrContent))

        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    private fun buildPrompt(userRequest: String): String {
        return """
Eres un asistente experto en análisis de contenido y diseño de códigos QR. Tu tarea es analizar la solicitud de un usuario y convertirla en un objeto JSON válido que una aplicación usará para generar el código QR.

### REGLAS ESTRICTAS:
1. **SOLO JSON:** Tu respuesta DEBE ser únicamente el objeto JSON. No incluyas explicaciones, saludos, ni texto adicional.
2. **INFERENCIA INTELIGENTE:** Analiza el texto del usuario para inferir todos los valores. Si el usuario no especifica un parámetro, utiliza los valores por defecto.
3. **ESTRUCTURA FIJA:** El JSON debe seguir siempre la estructura definida.

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

### CAMPOS:

* `contenido.tipo`: Formato del contenido. Valores posibles: "texto_plano", "vcard", "wifi", "geolocalizacion", "evento_calendario", "json", "markdown", "codigo", "email", "sms". Por defecto: "texto_plano".
* `contenido.data`: Datos estructurados según el tipo.
* `nivel_de_correccion`: Tolerancia a errores. Valores: "L", "M", "Q", "H". Por defecto: "Q".
* `colores.fondo`: Color de fondo en hexadecimal. Por defecto: "#FFFFFF".
* `colores.principal.tipo`: "solido" o "gradiente". Por defecto: "solido".
* `colores.principal.valores`: Array de colores en hexadecimal. Por defecto: ["#000000"].
* `estilo`: Forma de los módulos. Valores: "cuadrados", "redondeado", "puntos". Por defecto: "cuadrados".

### ESTRUCTURAS PARA `contenido.data`:

* **texto_plano**: data es un string.
* **vcard**: data es un objeto con campos: nombre, apellido, telefono, email, organizacion, url.
* **wifi**: data es un objeto con: ssid, password, tipo_seguridad (WPA/WEP/nopass).
* **geolocalizacion**: data es un objeto con: latitud, longitud.
* **evento_calendario**: data es un objeto con: titulo, ubicacion, descripcion, fecha_inicio, fecha_fin.
* **json**: data es un objeto JSON válido.
* **markdown**: data es un string con formato Markdown.
* **codigo**: data es un objeto con: lenguaje, codigo.
* **email**: data es un objeto con: destinatario, asunto, cuerpo.
* **sms**: data es un objeto con: numero, mensaje.

### SOLICITUD DEL USUARIO:
$userRequest
        """.trimIndent()
    }
}
