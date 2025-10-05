package org.christophertwo.qr.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import org.christophertwo.qr.core.common.formatPhoneNumber
import org.christophertwo.qr.core.common.parseEmail
import org.christophertwo.qr.core.common.parseLocation
import org.christophertwo.qr.core.common.parseSms
import org.christophertwo.qr.core.common.parseVCard
import org.christophertwo.qr.core.common.parseWiFi

private val prettyJson = Json { prettyPrint = true }

@Composable
fun JsonContentDisplay(content: String) {
    runCatching {
        val jsonElement = Json.parseToJsonElement(content)
        prettyJson.encodeToString(JsonElement.serializer(), jsonElement)
    }.onFailure {
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontFamily = FontFamily.Monospace
            ),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth()
        )
    }.onSuccess { formattedJson ->
        Text(
            text = formattedJson,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontFamily = FontFamily.Monospace
            ),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Composable
fun MarkdownContentDisplay(content: String) {

}

@Composable
fun VCardContentDisplay(content: String) {
    val vCardData = parseVCard(content)
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        vCardData.forEach { (label, value) ->
            if (value.isNotBlank()) {
                Row {
                    Text(
                        text = "$label: ",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = value,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
fun WiFiContentDisplay(content: String) {
    val wifiData = parseWiFi(content)
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        wifiData.forEach { (label, value) ->
            if (value.isNotBlank()) {
                Row {
                    Text(
                        text = "$label: ",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = value,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
fun UrlContentDisplay(content: String) {
    Text(
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            ) {
                append(content)
            }
        },
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun EmailContentDisplay(content: String) {
    val emailData = parseEmail(content)
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        emailData.forEach { (label, value) ->
            if (value.isNotBlank()) {
                Row {
                    Text(
                        text = "$label: ",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = value,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
fun PhoneContentDisplay(content: String) {
    Text(
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            ) {
                append(formatPhoneNumber(content))
            }
        },
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun SmsContentDisplay(content: String) {
    val smsData = parseSms(content)
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        smsData.forEach { (label, value) ->
            if (value.isNotBlank()) {
                Row {
                    Text(
                        text = "$label: ",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = value,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
fun LocationContentDisplay(content: String) {
    val locationData = parseLocation(content)
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        locationData.forEach { (label, value) ->
            if (value.isNotBlank()) {
                Row {
                    Text(
                        text = "$label: ",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = value,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
fun CodeContentDisplay(content: String) {
    Text(
        text = content,
        style = MaterialTheme.typography.bodyMedium.copy(
            fontFamily = FontFamily.Monospace
        ),
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun PlainTextContentDisplay(content: String) {
    Text(
        text = content,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.fillMaxWidth()
    )
}
