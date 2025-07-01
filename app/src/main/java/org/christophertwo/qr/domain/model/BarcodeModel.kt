package org.christophertwo.qr.domain.model

data class BarcodeModel(
    val rawValue: String,
    val format: String,
    val type: String
)