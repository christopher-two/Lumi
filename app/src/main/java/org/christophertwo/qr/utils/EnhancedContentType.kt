package org.christophertwo.qr.utils

import org.christophertwo.qr.R

enum class EnhancedContentType(
    val displayName: String,
    val iconRes: Int
) {
    URL("URL", R.drawable.outline_link_24),
    EMAIL("Email", R.drawable.outline_mail_24),
    PHONE("Teléfono", R.drawable.outline_phone_enabled_24),
    SMS("SMS", R.drawable.outline_sms_24),
    WIFI("WiFi", R.drawable.outline_wifi_password_24),
    CONTACT("Contacto", R.drawable.outline_contacts_24),
    LOCATION("Ubicación", R.drawable.outline_location_on_24),
    JSON("JSON", R.drawable.outline_file_json_24),
    MARKDOWN("Markdown", R.drawable.outline_markdown_24),
    CODE("Código", R.drawable.outline_code_blocks_24),
    PLAIN_TEXT("Texto", R.drawable.outline_docs_24)
}
