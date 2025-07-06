package org.christophertwo.qr.ui.screen.generator

sealed interface QrGeneratorAction {
    data class UpdateContent(val content: String) : QrGeneratorAction
}