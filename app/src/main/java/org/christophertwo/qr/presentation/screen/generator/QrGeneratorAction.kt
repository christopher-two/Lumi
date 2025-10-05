package org.christophertwo.qr.presentation.screen.generator

import android.graphics.Bitmap

sealed interface QrGeneratorAction {
    data class GenerateQrFromPrompt(val prompt: String) : QrGeneratorAction
    data class DownloadQr(val bitmap: Bitmap) : QrGeneratorAction
}
