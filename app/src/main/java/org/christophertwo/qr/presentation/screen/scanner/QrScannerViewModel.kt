package org.christophertwo.qr.presentation.screen.scanner

import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.christophertwo.qr.domain.usecase.ScanBarcodeUseCase

class QrScannerViewModel(
    private val scanBarcodeUseCase: ScanBarcodeUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(QrScannerState())
    val state = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = QrScannerState()
        )

    fun onAction(action: QrScannerAction) {
        when (action) {
            is QrScannerAction.ScanBarcode -> processImage(action.imageProxy)
            // Añade este caso para el reinicio
            is QrScannerAction.ResetScanner -> resetScanner()
        }
    }

    private fun resetScanner() {
        _state.update {
            it.copy(
                barcodes = emptyList(),
                error = null
            )
        }
    }

    fun processImage(imageProxy: ImageProxy) {
        // Si ya tenemos un resultado, no procesamos nuevas imágenes para "pausar" el escáner.
        if (_state.value.barcodes.isNotEmpty()) {
            imageProxy.close()
            return
        }

        viewModelScope.launch {
            scanBarcodeUseCase(imageProxy)
                .onStart {
                    // Muestra el indicador de carga
                    _state.update { it.copy(isLoading = true) }
                }
                .catch { e ->
                    // En caso de un error inesperado en el Flow, oculta el indicador y muestra el error
                    _state.update { it.copy(isLoading = false, error = e.message) }
                }
                .collect { result ->
                    result.onSuccess { barcodes ->
                        // Solo actualizamos el estado si encontramos un código de barras.
                        // Esto evita que un frame vacío borre un resultado válido.
                        if (barcodes.isNotEmpty()) {
                            _state.update {
                                it.copy(isLoading = false, barcodes = barcodes, error = null)
                            }
                        } else {
                            // Si no se encontraron códigos, simplemente dejamos de cargar.
                            _state.update { it.copy(isLoading = false) }
                        }
                    }.onFailure { e ->
                        // Si falla, oculta el indicador y muestra el error.
                        _state.update {
                            it.copy(isLoading = false, error = e.message)
                        }
                    }
                }
        }
    }
}
