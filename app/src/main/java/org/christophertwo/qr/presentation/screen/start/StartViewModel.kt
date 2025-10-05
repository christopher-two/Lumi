package org.christophertwo.qr.presentation.screen.start

import android.content.Context
import androidx.core.content.ContextCompat.getString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.christophertwo.qr.R
import org.christophertwo.qr.domain.usecase.SignInWithGoogleUseCase

class StartViewModel(
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(StartState())

    /**
     * Flujo de estado de la UI para la pantalla de inicio.
     * Observa este flujo en la UI para reaccionar a los cambios de estado.
     */
    val state = _state
        .onStart {
            val gso =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(context, R.string.web_id))
                    .requestEmail()
                    .build()
            val googleSignInClient = GoogleSignIn.getClient(context, gso)
            _state.update {
                it.copy(
                    googleSignInClient = googleSignInClient
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = StartState(),
        )

    /**
     * Maneja las acciones del usuario provenientes de la UI.
     *
     * @param action La acción específica a procesar.
     */
    fun onAction(action: StartAction) {
        when (action) {
            is StartAction.ThemeOnChange -> onThemeChange()
            is StartAction.LoginWithGoogle -> {
                viewModelScope.launch {
                    signInWithGoogleUseCase.invoke(
                        result = action.result,
                        onAuthComplete = {
                            _state.update { it.copy(isLoggedIn = true) }
                        },
                        onAuthError = {
                            _state.update { it.copy(error = it.error, isLoggedIn = false) }
                        }
                    )
                }
            }
        }
    }

    /**
     * Cambia el estado del tema actual (claro/oscuro).
     */
    private fun onThemeChange() {
        _state.update { it.copy(theme = !it.theme) }
    }
}