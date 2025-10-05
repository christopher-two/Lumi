package org.christophertwo.qr.data.auth.api

// Define posibles resultados de la autenticación externa
sealed class ExternalAuthResult {
    data class Success(val idToken: String) : ExternalAuthResult()
    data class Error(val message: String) : ExternalAuthResult()
    object Cancelled : ExternalAuthResult()
}