package org.christophertwo.qr.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * Representa la estructura completa del JSON devuelto por la IA.
 */
@Serializable
data class QrContentResponse(
    @SerialName("contenido")
    val contenido: Contenido,

    @SerialName("nivel_de_correccion")
    val nivelDeCorreccion: String = "Q",

    @SerialName("colores")
    val colores: Colores,

    @SerialName("estilo")
    val estilo: String = "cuadrados"
)

/**
 * Contenedor para los datos principales del QR, incluyendo su tipo y la data estructurada.
 */
@Serializable
data class Contenido(
    @SerialName("tipo")
    val tipo: String,

    // Se usa JsonElement porque la estructura de 'data' es dinámica.
    @SerialName("data")
    val data: JsonElement
)

/**
 * Define los colores de fondo y principal del QR.
 */
@Serializable
data class Colores(
    @SerialName("fondo")
    val fondo: String = "#FFFFFF",

    @SerialName("principal")
    val principal: Principal
)

/**
 * Define el color principal, que puede ser un color sólido o un gradiente.
 */
@Serializable
data class Principal(
    @SerialName("tipo")
    val tipo: String = "solido",

    @SerialName("valores")
    val valores: List<String> = listOf("#000000")
)
