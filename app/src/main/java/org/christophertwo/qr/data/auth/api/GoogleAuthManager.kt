package org.christophertwo.qr.data.auth.api

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.google.firebase.auth.AuthResult

/**
 * Contrato para manejar el flujo de autenticación con Google Identity Services (One Tap).
 * Su única responsabilidad es obtener un idToken de Google.
 * Esta interfaz vive en el módulo de la API de autenticación.
 */
interface GoogleAuthManager {
    /**
     * Inicia el proceso de inicio de sesión de Google One Tap.
     * En lugar de devolver un Flow, ahora es una función suspendida
     * que retorna directamente la petición para lanzar la UI de Google,
     * haciendo el código en el ViewModel más simple.
     *
     * @return Result.success con el [IntentSenderRequest] para lanzar la UI.
     * @return Result.failure si hubo un error al preparar la solicitud.
     */
    suspend fun beginSignIn(): Result<IntentSenderRequest>

    /**
     * Procesa el resultado del intent de inicio de sesión de Google.
     *
     * @param intent El [Intent] recibido en el callback del [ActivityResultLauncher].
     * @return Un [ExternalAuthResult] que encapsula el éxito (con token),
     * la cancelación del usuario o un error.
     */
    fun handleSignInResult(intent: Intent?): ExternalAuthResult

    /**
     * Obtiene el Intent para iniciar el proceso de inicio de sesión tradicional de Google.
     * Este método se utiliza como alternativa o fallback al flujo de One Tap,
     * permitiendo al usuario seleccionar una cuenta de Google de la manera convencional.
     *
     * @return El [Intent] configurado para iniciar el flujo de inicio de sesión de Google.
     */
    fun getSignInIntent(): Intent

    /**
     * Procesa el resultado del intent de inicio de sesión de Google y autentica con Firebase.
     * Esta función toma el intent resultante del flujo de Google Sign-In, extrae el token de ID
     * y luego lo utiliza para iniciar sesión en Firebase.
     *
     * @param intent El [Intent] recibido en el callback del [ActivityResultLauncher] después
     * de que el usuario interactúa con la UI de Google Sign-In. Puede ser nulo si el proceso
     * no se completó correctamente o fue cancelado.
     * @return Un [Result] que encapsula:
     *         - [Result.Success] con [AuthResult] si la autenticación con Firebase fue exitosa.
     *         - [Result.Failure] con una excepción si ocurrió algún error durante el proceso,
     *           ya sea al obtener el token de Google o al autenticar con Firebase.
     */
    suspend fun handleSignInResultWithFirebase(intent: Intent?): Result<AuthResult>

    /**
     * Cierra la sesión del usuario actual en Google.
     * Esta función es asíncrona y suspende la ejecución hasta que el proceso
     * de cierre de sesión se complete.
     */
    suspend fun signOut()
}