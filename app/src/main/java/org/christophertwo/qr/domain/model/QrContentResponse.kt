package org.christophertwo.qr.domain.model

import com.google.gson.annotations.SerializedName

/**
 * Representa la estructura completa del JSON devuelto por la IA.
 */
data class QrContentResponse(
    @SerializedName("contenido")
    val contenido: Contenido,

    @SerializedName("nivel_de_correccion")
    val nivelDeCorreccion: String,

    @SerializedName("colores")
    val colores: Colores,

    @SerializedName("estilo")
    val estilo: String
)

/**
 * Contenedor para los datos principales del QR, incluyendo su tipo y la data estructurada.
 */
data class Contenido(
    @SerializedName("tipo")
    val tipo: String,

    // Se usa 'Any' porque la estructura de 'data' es dinámica.
    // Se debe castear posteriormente según el valor de 'tipo'.
    @SerializedName("data")
    val data: Any
)

/**
 * Define los colores de fondo y principal del QR.
 */
data class Colores(
    @SerializedName("fondo")
    val fondo: String,

    @SerializedName("principal")
    val principal: Principal
)

/**
 * Define el color principal, que puede ser un color sólido o un gradiente.
 */
data class Principal(
    @SerializedName("tipo")
    val tipo: String,

    @SerializedName("valores")
    val valores: List<String>
)
