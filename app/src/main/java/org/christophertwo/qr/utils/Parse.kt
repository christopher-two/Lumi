package org.christophertwo.qr.utils

import java.net.URLDecoder

fun formatPhoneNumber(phone: String): String {
    val cleanPhone = phone.replace(Regex("[^+\\d]"), "")
    return when {
        cleanPhone.startsWith("+52") -> {
            val number = cleanPhone.substringAfter("+52")
            if (number.length == 10) {
                "+52 ${number.substring(0, 3)} ${number.substring(3, 6)} ${number.substring(6)}"
            } else cleanPhone
        }

        cleanPhone.length == 10 -> {
            "${cleanPhone.substring(0, 3)} ${cleanPhone.substring(3, 6)} ${cleanPhone.substring(6)}"
        }

        else -> cleanPhone
    }
}

fun parseEmail(content: String): List<Pair<String, String>> {
    val data = mutableListOf<Pair<String, String>>()

    if (content.startsWith("mailto:")) {
        val emailPart = content.substringAfter("mailto:")
        val parts = emailPart.split("?")

        // El destinatario generalmente no necesita decodificación, pero no hace daño
        val to = URLDecoder.decode(parts[0], "UTF-8")
        data.add("Para" to to)

        if (parts.size > 1) {
            val params = parts[1].split("&")
            for (param in params) {
                val keyValue = param.split("=")
                if (keyValue.size == 2) {
                    val key = keyValue[0]
                    // Decodificar el valor usando UTF-8
                    val value = URLDecoder.decode(keyValue[1], "UTF-8")

                    when (key) {
                        "subject" -> data.add("Asunto" to value)
                        "body" -> data.add("Mensaje" to value)
                        "cc" -> data.add("CC" to value)
                        "bcc" -> data.add("BCC" to value)
                    }
                }
            }
        }
    }

    return data
}

fun parseLocation(content: String): List<Pair<String, String>> {
    val data = mutableListOf<Pair<String, String>>()

    when {
        content.startsWith("geo:") -> {
            val coords = content.substringAfter("geo:")
            val parts = coords.split(",")
            if (parts.size >= 2) {
                data.add("Latitud" to parts[0])
                data.add("Longitud" to parts[1])
            }
        }

        content.contains("maps.google.com") -> {
            data.add("URL" to content)
        }
    }

    return data
}

fun parseSms(content: String): List<Pair<String, String>> {
    val data = mutableListOf<Pair<String, String>>()

    if (content.startsWith("sms:") || content.startsWith("smsto:")) {
        val smsPart = content.substringAfter(":")
        val parts = smsPart.split("?")

        data.add("Número" to parts[0])

        if (parts.size > 1) {
            val params = parts[1].split("&")
            for (param in params) {
                val keyValue = param.split("=")
                if (keyValue.size == 2 && keyValue[0] == "body") {
                    data.add("Mensaje" to keyValue[1])
                }
            }
        }
    }

    return data
}

fun parseVCard(content: String): List<Pair<String, String>> {
    val data = mutableListOf<Pair<String, String>>()
    val lines = content.lines()

    for (line in lines) {
        if (line.isBlank() || line.startsWith("BEGIN:") || line.startsWith("END:")) {
            continue
        }

        val parts = line.split(":", limit = 2)
        if (parts.size == 2) {
            val key = parts[0]
            val value = parts[1]

            when {
                key.startsWith("FN") -> data.add("Nombre" to value)
                key.startsWith("ORG") -> data.add("Organización" to value)
                key.startsWith("TEL") -> data.add("Teléfono" to value)
                key.startsWith("EMAIL") -> data.add("Email" to value)
                key.startsWith("URL") -> data.add("URL" to value)
                key.startsWith("ADR") -> {
                    // Limpieza simple para direcciones
                    val cleanAddress = value.replace(";", " ").trim()
                    data.add("Dirección" to cleanAddress)
                }
            }
        }
    }
    return data
}

fun parseWiFi(content: String): List<Pair<String, String>> {
    val data = mutableListOf<Pair<String, String>>()

    // Formato: WIFI:T:WPA;S:MyNetwork;P:MyPassword;H:false;
    val parts = content.substringAfter("WIFI:").split(";")

    for (part in parts) {
        when {
            part.startsWith("T:") -> data.add("Tipo" to part.substringAfter("T:"))
            part.startsWith("S:") -> data.add("Nombre de red" to part.substringAfter("S:"))
            part.startsWith("P:") -> data.add("Contraseña" to part.substringAfter("P:"))
            part.startsWith("H:") -> {
                val hidden = if (part.substringAfter("H:") == "true") "Sí" else "No"
                data.add("Red oculta" to hidden)
            }
        }
    }

    return data
}