package org.christophertwo.qr.ui.screen.generator

sealed interface QrGeneratorAction {
    data class GenerateQrFromPrompt(val prompt: String) : QrGeneratorAction
}
