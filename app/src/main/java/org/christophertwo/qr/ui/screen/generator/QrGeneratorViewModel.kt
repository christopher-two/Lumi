package org.christophertwo.qr.ui.screen.generator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class QrGeneratorViewModel : ViewModel() {

    private val _state = MutableStateFlow(QrGeneratorState())
    val state = _state
        .onStart { }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = QrGeneratorState()
        )

    fun onAction(action: QrGeneratorAction) {
        when (action) {
            else -> TODO("Handle actions")
        }
    }

}