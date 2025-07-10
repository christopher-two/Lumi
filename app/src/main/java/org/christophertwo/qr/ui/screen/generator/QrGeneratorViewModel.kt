package org.christophertwo.qr.ui.screen.generator

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.christophertwo.qr.domain.model.QrContentResponse
import org.christophertwo.qr.domain.repository.GeminiRepository

class QrGeneratorViewModel(
    private val geminiRepository: GeminiRepository,
    private val gson: Gson // Gson para procesar el JSON
) : ViewModel() {

    private val _state = MutableStateFlow(QrGeneratorState())
    val state = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = QrGeneratorState()
        )

    fun onAction(action: QrGeneratorAction) {
        when (action) {
            is QrGeneratorAction.GenerateQrFromPrompt -> {
                generateQr(action.prompt)
            }
        }
    }

    private fun generateQr(prompt: String) {
        // Establecer el estado de loading ANTES de hacer la petición
        _state.update {
            it.copy(
                isLoading = true,
                error = null,
                finalQrString = "",
                qrResponse = null
            )
        }

        viewModelScope.launch {
            geminiRepository.getStructuredQrContent(prompt)
                .onEach { result ->
                    Log.d("QrGeneratorViewModel", "Received result: $result")

                    result
                        .onSuccess { response ->
                            val finalString = processQrData(response)
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    qrResponse = response,
                                    finalQrString = finalString,
                                    error = null
                                )
                            }
                        }
                        .onFailure { error ->
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    error = error.message ?: "Ocurrió un error desconocido",
                                    finalQrString = "",
                                    qrResponse = null
                                )
                            }
                        }
                }.launchIn(viewModelScope)
        }
    }

    /**
     * Procesa la respuesta de la IA y la convierte en el string final para el QR.
     */
    private fun processQrData(response: QrContentResponse): String {
        return when (response.contenido.tipo) {
            "texto_plano" -> response.contenido.data as String
            "vcard" -> {
                val dataMap = response.contenido.data as Map<*, *>
                val nombre = dataMap["nombre"] ?: ""
                val apellido = dataMap["apellido"] ?: ""
                val telefono = dataMap["telefono"] ?: ""
                val email = dataMap["email"] ?: ""
                "BEGIN:VCARD\nVERSION:3.0\nN:$apellido;$nombre\nFN:$nombre $apellido\nTEL;TYPE=CELL:$telefono\nEMAIL:$email\nEND:VCARD"
            }

            "wifi" -> {
                val dataMap = response.contenido.data as Map<*, *>
                val ssid = dataMap["ssid"]
                val password = dataMap["password"]
                val security = dataMap["tipo_seguridad"] ?: "WPA"
                "WIFI:T:$security;S:$ssid;P:$password;;"
            }

            "json" -> gson.toJson(response.contenido.data)
            else -> response.contenido.data.toString()
        }
    }
}