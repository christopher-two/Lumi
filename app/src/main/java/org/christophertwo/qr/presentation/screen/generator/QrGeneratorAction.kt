package org.christophertwo.qr.presentation.screen.generator

sealed interface QrGeneratorAction {
    data class GenerateQrFromPrompt(val prompt: String) : QrGeneratorAction
}
