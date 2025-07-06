package org.christophertwo.qr.ui.screen.generator

import androidx.compose.runtime.*

@Immutable
data class QrGeneratorState(
    val isLoading: Boolean = false,
    val content: String = "",
)