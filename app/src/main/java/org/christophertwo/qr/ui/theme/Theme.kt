package org.christophertwo.qr.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// --- Definiciones de Colores para el Tema Amarillo (Dark) ---
private val DarkYellowPrimary = Color(0xFFD4AF37) // Un tono dorado oscuro para el color primario
private val DarkYellowSecondary = Color(0xFFCDC58E) // Un tono gris-amarillo para el secundario
private val DarkYellowTertiary = Color(0xFFADCFAB) // Un verde pálido como terciario
private val DarkBackground = Color(0xFF1E1B16)
private val DarkOnBackground = Color(0xFFEAE1D9)
private val DarkSurface = Color(0xFF1E1B16)
private val DarkOnSurface = Color(0xFFEAE1D9)
private val DarkOnPrimary = Color(0xFF403000)
private val DarkOnSecondary = Color(0xFF36311E)
private val DarkOnTertiary = Color(0xFF1B361D)

// --- Definiciones de Colores para el Tema Amarillo (Light) ---
private val LightYellowPrimary = Color(0xFFFFD700) // Amarillo dorado brillante para el primario
private val LightYellowSecondary = Color(0xFF686043) // Un marrón amarillento para el secundario
private val LightYellowTertiary = Color(0xFF48674A) // Un verde oscuro para el terciario
private val LightBackground = Color(0xFFFFFDF7)
private val LightOnBackground = Color(0xFF1E1B16)
private val LightSurface = Color(0xFFFFFDF7)
private val LightOnSurface = Color(0xFF1E1B16)
private val LightOnPrimary = Color(0xFFFFFFFF) // Blanco sobre el primario brillante
private val LightOnSecondary = Color(0xFFFFFFFF) // Blanco sobre el secundario oscuro
private val LightOnTertiary = Color(0xFFFFFFFF) // Blanco sobre el terciario oscuro


private val DarkColorScheme = darkColorScheme(
    primary = DarkYellowPrimary,
    onPrimary = DarkOnPrimary,
    secondary = DarkYellowSecondary,
    onSecondary = DarkOnSecondary,
    tertiary = DarkYellowTertiary,
    onTertiary = DarkOnTertiary,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface
)

private val LightColorScheme = lightColorScheme(
    primary = LightYellowPrimary,
    onPrimary = LightOnPrimary,
    secondary = LightYellowSecondary,
    onSecondary = LightOnSecondary,
    tertiary = LightYellowTertiary,
    onTertiary = LightOnTertiary,
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightSurface,
    onSurface = LightOnSurface
)

@Composable
fun QrTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Asumo que tienes un archivo Typography.kt
        content = {
            Surface(
                color = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground,
                content = { content() },
                modifier = Modifier.fillMaxSize()
            )
        }
    )
}
