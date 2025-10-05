package org.christophertwo.qr.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.materialkolor.DynamicMaterialTheme

@Composable
fun QrTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    DynamicMaterialTheme(
        seedColor = Color(0xFF785A00),
        isDark = darkTheme,
        content = {
            Surface(
                color = colorScheme.background,
                contentColor = colorScheme.onBackground,
                content = { content() },
                modifier = Modifier.fillMaxSize()
            )
        }
    )
}