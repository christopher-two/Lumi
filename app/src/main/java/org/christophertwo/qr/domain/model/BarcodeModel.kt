package org.christophertwo.qr.domain.model

/**
 * Representa un código de barras escaneado.
 *
 * @property rawValue El valor crudo/texto del código de barras.
 * @property format El formato del código de barras (por ejemplo, QR_CODE, UPC_A).
 * @property type El tipo de valor contenido en el código de barras (por ejemplo, TEXT, URL, PRODUCT).
 */
data class BarcodeModel(
    val rawValue: String,
    val format: String,
    val type: String
)
