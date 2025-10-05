package org.christophertwo.qr.presentation.screen.scanner

import androidx.compose.runtime.*
import org.christophertwo.qr.domain.model.BarcodeModel

@Immutable
data class QrScannerState(
    val isLoading: Boolean = false,
    val barcodes: List<BarcodeModel> = emptyList(),
    val error: String? = null
)