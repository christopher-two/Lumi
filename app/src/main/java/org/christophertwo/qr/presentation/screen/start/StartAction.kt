package org.christophertwo.qr.presentation.screen.start

import androidx.activity.result.ActivityResult

sealed interface StartAction {
    data object ThemeOnChange : StartAction
    data class LoginWithGoogle(val result: ActivityResult) : StartAction
}