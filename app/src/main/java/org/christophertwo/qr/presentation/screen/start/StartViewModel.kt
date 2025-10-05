package org.christophertwo.qr.presentation.screen.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class StartViewModel : ViewModel() {

    private val _state = MutableStateFlow(StartState())
    val state = _state
        .onStart { }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = StartState()
        )

    fun onAction(action: StartAction) {
        when (action) {
            else -> TODO("Handle actions")
        }
    }

}