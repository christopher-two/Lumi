package org.christophertwo.qr.presentation.screen.start

import androidx.compose.runtime.*
import androidx.compose.runtime.Immutable
import com.google.android.gms.auth.api.signin.GoogleSignInClient

@Immutable
data class StartState(
    val isLoading: Boolean = false,
    val theme: Boolean = false,
    val isLoggedIn: Boolean = false,
    val error: String? = null,
    val googleSignInClient: GoogleSignInClient? = null
)