package org.christophertwo.qr.ui.components

import androidx.compose.runtime.Composable
import org.christophertwo.qr.utils.EnhancedContentType

@Composable
fun EnhancedContentDisplay(
    content: String,
    contentType: EnhancedContentType
) {
    when (contentType) {
        EnhancedContentType.JSON -> {
            JsonContentDisplay(content)
        }

        EnhancedContentType.MARKDOWN -> {
            MarkdownContentDisplay(content)
        }

        EnhancedContentType.CONTACT -> {
            VCardContentDisplay(content)
        }

        EnhancedContentType.WIFI -> {
            WiFiContentDisplay(content)
        }

        EnhancedContentType.URL -> {
            UrlContentDisplay(content)
        }

        EnhancedContentType.EMAIL -> {
            EmailContentDisplay(content)
        }

        EnhancedContentType.PHONE -> {
            PhoneContentDisplay(content)
        }

        EnhancedContentType.SMS -> {
            SmsContentDisplay(content)
        }

        EnhancedContentType.LOCATION -> {
            LocationContentDisplay(content)
        }

        EnhancedContentType.CODE -> {
            CodeContentDisplay(content)
        }

        EnhancedContentType.PLAIN_TEXT -> {
            PlainTextContentDisplay(content)
        }
    }
}
