package org.christophertwo.qr.domain.repository

import kotlinx.coroutines.flow.Flow
import org.christophertwo.qr.domain.model.QrContentResponse

/**
 * Interfaz del repositorio para interactuar con la IA de Gemini.
 * Abstrae el origen de datos para obtener el contenido estructurado para el código QR.
 */
interface GeminiRepository {

    /**
     * Toma la instrucción en lenguaje natural de un usuario y la convierte
     * en un objeto estructurado para generar un QR.
     *
     * @param prompt La solicitud del usuario (ej: "un qr para mi wifi, la red es MiRed y la clave es 1234").
     * @return Un Flow que emite un objeto Result, conteniendo el QrContentResponse parseado en caso de éxito,
     * o una excepción en caso de fallo.
     */
    fun getStructuredQrContent(prompt: String): Flow<Result<QrContentResponse>>
}
