package org.christophertwo.qr.data.auth.impl.google

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.IntentSenderRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await
import org.christophertwo.qr.data.auth.api.ExternalAuthResult
import org.christophertwo.qr.data.auth.api.GoogleAuthManager

/**
 * Implementación de [org.override.lyra.data.auth.api.plataform.GoogleAuthManager] que utiliza el SDK de Google Sign-In tradicional.
 * Esta clase maneja el flujo de autenticación con Google, incluyendo la obtención del Intent
 * de inicio de sesión, el procesamiento del resultado y el cierre de sesión.
 *
 * @property context El contexto de la aplicación, necesario para inicializar [GoogleSignInClient].
 */
class GoogleAuthManagerImpl(
    private val context: Context
) : GoogleAuthManager {
    companion object {
        private const val TAG = "GoogleAuthManager"
        private const val WEB_ID =
            "836573887013-j21vsa7laq7av1g2g3vde4f3m0nr5pko.apps.googleusercontent.com"
    }

    /**
     * Cliente de Google Sign-In, inicializado de forma diferida.
     *
     * Este cliente se configura con las opciones necesarias para solicitar
     * el token de ID (necesario para la autenticación con Firebase) y el correo electrónico del usuario.
     *
     * La inicialización es perezosa (`by lazy`) para que el cliente solo se cree
     * cuando se acceda por primera vez, optimizando el rendimiento.
     *
     * Utiliza [GoogleSignInOptions.DEFAULT_SIGN_IN] como base y luego añade:
     * - `requestIdToken(WEB_ID)`: Solicita el token de ID, que se usará para autenticar
     *   al usuario con Firebase. [WEB_ID] es el ID de cliente web de tu proyecto.
     * - `requestEmail()`: Solicita el permiso para acceder a la dirección de correo electrónico
     *   del usuario.
     *
     * Luego, se obtiene el [GoogleSignInClient] usando el [context] de la aplicación y las
     * opciones configuradas.
     */
    private val googleSignInClient: GoogleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(WEB_ID)
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }

    /**
     * Obtiene el Intent necesario para iniciar el flujo de inicio de sesión tradicional de Google.
     */
    override fun getSignInIntent(): Intent {
        return googleSignInClient.signInIntent
    }

    /**
     * Procesa el resultado del Intent de inicio de sesión de Google y
     * se autentica con Firebase.
     *
     * @param intent El [Intent] recibido en el callback del ActivityResultLauncher.
     * @return Result.success con el [AuthResult] de Firebase.
     * @return Result.failure con la [ApiException] si hubo un error.
     */
    override suspend fun handleSignInResultWithFirebase(intent: Intent?): Result<AuthResult> {
        return try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
            val account = task.getResult(ApiException::class.java)!! // Lanza ApiException si falla
            val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
            val authResult =
                Firebase.auth.signInWithCredential(credential).await()
            Result.success(authResult)
        } catch (e: ApiException) {
            Log.e(TAG, "Error al iniciar sesión con Google", e)
            Result.failure(e)
        }
    }

    /**
     * Cierra la sesión del usuario de Google.
     * Es importante llamar a esto cuando el usuario cierra sesión en tu app
     * para desvincular la cuenta de Google.
     */
    override suspend fun signOut() {
        try {
            googleSignInClient.signOut().await()
            googleSignInClient.revokeAccess().await()
        } catch (e: Exception) {
            Log.e(TAG, "Error al cerrar sesión de Google", e)
        }
    }

    override suspend fun beginSignIn(): Result<IntentSenderRequest> {
        TODO("Not yet implemented")
    }

    override fun handleSignInResult(intent: Intent?): ExternalAuthResult {
        TODO("Not yet implemented")
    }
}