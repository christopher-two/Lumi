package org.christophertwo.qr.domain.usecase

import android.util.Log
import androidx.activity.result.ActivityResult
import com.google.android.gms.common.api.ApiException
import org.christophertwo.qr.data.auth.api.GoogleAuthManager
import org.christophertwo.qr.data.session.api.SessionRepository

class SignInWithGoogleUseCase(
    private val googleAuthManager: GoogleAuthManager,
    private val sessionRepository: SessionRepository
) {
    companion object {
        private const val TAG = "SignInWithGoogleUseCase"
    }

    suspend operator fun invoke(
        result: ActivityResult,
        onAuthComplete: () -> Unit,
        onAuthError: () -> Unit
    ) {
        // Aquí ya no llamas a GoogleSignIn directamente, sino a tu manager
        val authResultOutcome = googleAuthManager.handleSignInResultWithFirebase(result.data)
        authResultOutcome.fold(
            onSuccess = { authResult ->
                authResult.user?.let { user ->
                    sessionRepository.saveUserSession(user.uid)
                    Log.d(TAG, "User signed in: ${user.uid}")
                    onAuthComplete()
                }
            },
            onFailure = { throwable ->
                if (throwable is ApiException) {
                    Log.e(TAG, "Google sign-in failed", throwable)
                    onAuthError()
                } else {
                    Log.e(TAG, "Error during Google sign-in", throwable)
                    onAuthError()
                }
            }
        )
    }
}