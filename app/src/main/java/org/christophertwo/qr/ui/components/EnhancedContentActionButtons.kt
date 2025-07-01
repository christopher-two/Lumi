package org.christophertwo.qr.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.christophertwo.qr.R
import org.christophertwo.qr.utils.EnhancedContentType

@Composable
fun EnhancedContentActionButtons(
    content: String,
    contentType: EnhancedContentType,
    onCopy: () -> Unit,
    onNavigateToUrl: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Botón copiar (siempre presente)
        OutlinedButton(
            onClick = onCopy,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                painter = painterResource(R.drawable.content_copy_24dp_e3e3e3_fill0_wght400_grad0_opsz24),
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Copiar")
        }

        // Botones específicos por tipo de contenido
        when (contentType) {
            EnhancedContentType.URL -> {
                Button(
                    onClick = { onNavigateToUrl(content) },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.call_made_24dp_e3e3e3_fill0_wght400_grad0_opsz24),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Abrir")
                }
            }

            EnhancedContentType.EMAIL -> {
                Button(
                    onClick = { onNavigateToUrl(content) },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.outline_mail_24),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Enviar")
                }
            }

            EnhancedContentType.PHONE -> {
                Button(
                    onClick = { onNavigateToUrl("tel:$content") },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.outline_phone_enabled_24),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Llamar")
                }
            }

            EnhancedContentType.SMS -> {
                Button(
                    onClick = { onNavigateToUrl(content) },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.outline_sms_24),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Enviar")
                }
            }

            EnhancedContentType.LOCATION -> {
                Button(
                    onClick = { onNavigateToUrl(content) },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.outline_location_on_24),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ver mapa")
                }
            }

            else -> {
                // Para otros tipos, solo mostrar el botón copiar
            }
        }
    }
}
