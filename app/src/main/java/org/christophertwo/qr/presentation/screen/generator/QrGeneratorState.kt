package org.christophertwo.qr.presentation.screen.generator

import androidx.compose.runtime.Immutable
import org.christophertwo.qr.domain.model.QrContentResponse

@Immutable
data class QrGeneratorState(
    val isLoading: Boolean = false,
    // Almacena la respuesta completa y estructurada de Gemini.
    val qrResponse: QrContentResponse? = null,
    // El string final que se codificará en el QR.
    val finalQrString: String = "",
    // Para mostrar mensajes de error en la UI.
    val error: String? = null
)