package org.christophertwo.qr.core

import kotlinx.serialization.json.Json

fun detectEnhancedContentType(content: String): EnhancedContentType {
    return when {
        content.startsWith("BEGIN:VCARD") -> EnhancedContentType.CONTACT
        content.startsWith("WIFI:") -> EnhancedContentType.WIFI
        content.startsWith("mailto:") -> EnhancedContentType.EMAIL
        content.startsWith("tel:") || content.startsWith("phone:") -> EnhancedContentType.PHONE
        content.startsWith("sms:") || content.startsWith("smsto:") -> EnhancedContentType.SMS
        content.startsWith("geo:") || content.contains("maps.google.com") -> EnhancedContentType.LOCATION
        isValidUrl(content) -> EnhancedContentType.URL
        isValidJson(content) -> EnhancedContentType.JSON
        isMarkdown(content) -> EnhancedContentType.MARKDOWN
        isCode(content) -> EnhancedContentType.CODE
        else -> EnhancedContentType.PLAIN_TEXT
    }
}

fun isValidJson(content: String): Boolean {
    return try {
        Json.parseToJsonElement(content)
        true
    } catch (e: Exception) {
        false
    }
}

fun isMarkdown(content: String): Boolean {
    return content.contains("#") || content.contains("**") || content.contains("*") ||
            content.contains("```") || content.contains("`") || content.contains("- ") ||
            content.contains("1. ") || content.contains("[") && content.contains("](")
}

fun isCode(content: String): Boolean {
    val codeKeywords = listOf(
        "function", "class", "import", "export", "var", "let", "const",
        "def", "print", "return", "if", "else", "for", "while", "{", "}", ";"
    )
    return codeKeywords.any { content.contains(it, ignoreCase = true) }
}

fun isValidUrl(text: String): Boolean {
    return text.startsWith("http://") ||
            text.startsWith("https://") ||
            text.startsWith("www.") ||
            (text.contains(".") && text.contains("/") && !text.contains(" "))
}
