package org.christophertwo.qr.presentation.theme

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
val DarkYellowPrimary = Color(0xFFD4AF37)
val DarkOnPrimary = Color(0xFF403000)
private val DarkYellowPrimaryContainer = Color(0xFF5C4400)
private val DarkOnYellowPrimaryContainer = Color(0xFFF9E08A)
private val DarkInversePrimary = Color(0xFF785A00)
val DarkYellowSecondary = Color(0xFFCDC58E)
private val DarkOnSecondary = Color(0xFF36311E)
private val DarkYellowSecondaryContainer = Color(0xFF4D4833)
private val DarkOnYellowSecondaryContainer = Color(0xFFEAE1A9)
private val DarkYellowTertiary = Color(0xFFADCFAB)
private val DarkOnTertiary = Color(0xFF1B361D)
private val DarkYellowTertiaryContainer = Color(0xFF314D33)
private val DarkOnYellowTertiaryContainer = Color(0xFFC8EBC6)
private val DarkError = Color(0xFFFFB4AB)
private val DarkOnError = Color(0xFF690005)
private val DarkErrorContainer = Color(0xFF93000A)
private val DarkOnErrorContainer = Color(0xFFFFDAD6)
private val DarkBackground = Color(0xFF1E1B16)
private val DarkOnBackground = Color(0xFFEAE1D9)
private val DarkSurface = Color(0xFF1E1B16)
private val DarkOnSurface = Color(0xFFEAE1D9)
private val DarkSurfaceVariant = Color(0xFF4D4639)
private val DarkOnSurfaceVariant = Color(0xFFD1C5B4)
private val DarkInverseSurface = Color(0xFFEAE1D9)
private val DarkInverseOnSurface = Color(0xFF1E1B16)
private val DarkOutline = Color(0xFF999080)
private val DarkOutlineVariant = Color(0xFF4D4639)
private val DarkScrim = Color(0x99000000) // 60% black
private val DarkSurfaceTint = DarkYellowPrimary
private val DarkSurfaceBright = Color(0xFF3A3831)
private val DarkSurfaceDim = Color(0xFF16130F)
private val DarkSurfaceContainerLowest = Color(0xFF16130F)
private val DarkSurfaceContainerLow = Color(0xFF1E1B16)
private val DarkSurfaceContainer = Color(0xFF221F1A)
private val DarkSurfaceContainerHigh = Color(0xFF2D2A24)
private val DarkSurfaceContainerHighest = Color(0xFF38352F)


// --- Definiciones de Colores para el Tema Amarillo (Light) ---
private val LightYellowPrimary = Color(0xFF785A00)
private val LightOnPrimary = Color(0xFFFFFFFF)
private val LightYellowPrimaryContainer = Color(0xFFF9E08A)
private val LightOnYellowPrimaryContainer = Color(0xFF251A00)
private val LightInversePrimary = Color(0xFFD4AF37)
private val LightYellowSecondary = Color(0xFF686043)
private val LightOnSecondary = Color(0xFFFFFFFF)
private val LightYellowSecondaryContainer = Color(0xFFEFE5BF)
private val LightOnYellowSecondaryContainer = Color(0xFF221C06)
private val LightYellowTertiary = Color(0xFF48674A)
private val LightOnTertiary = Color(0xFFFFFFFF)
private val LightYellowTertiaryContainer = Color(0xFFC8EBC6)
private val LightOnYellowTertiaryContainer = Color(0xFF04210C)
private val LightError = Color(0xFFBA1A1A)
private val LightOnError = Color(0xFFFFFFFF)
private val LightErrorContainer = Color(0xFFFFDAD6)
private val LightOnErrorContainer = Color(0xFF410002)
private val LightBackground = Color(0xFFFFFDF7)
private val LightOnBackground = Color(0xFF1E1B16)
private val LightSurface = Color(0xFFFFFDF7)
private val LightOnSurface = Color(0xFF1E1B16)
private val LightSurfaceVariant = Color(0xFFECE1CF)
private val LightOnSurfaceVariant = Color(0xFF4D4639)
private val LightInverseSurface = Color(0xFF33302A)
private val LightInverseOnSurface = Color(0xFFF8F0E7)
private val LightOutline = Color(0xFF7F7667)
private val LightOutlineVariant = Color(0xFFD1C5B4)
private val LightScrim = Color(0x99000000) // 60% black
private val LightSurfaceTint = LightYellowPrimary
private val LightSurfaceBright = Color(0xFFFFFDF7)
private val LightSurfaceDim = Color(0xFFE0D9D1)
private val LightSurfaceContainerLowest = Color(0xFFFFFFFF)
private val LightSurfaceContainerLow = Color(0xFFF8F2EC)
private val LightSurfaceContainer = Color(0xFFF2ECE6)
private val LightSurfaceContainerHigh = Color(0xFFECE7E1)
private val LightSurfaceContainerHighest = Color(0xFFE6E1DC)


private val DarkColorScheme = darkColorScheme(
    primary = DarkYellowPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkYellowPrimaryContainer,
    onPrimaryContainer = DarkOnYellowPrimaryContainer,
    inversePrimary = DarkInversePrimary,
    secondary = DarkYellowSecondary,
    onSecondary = DarkOnSecondary,
    secondaryContainer = DarkYellowSecondaryContainer,
    onSecondaryContainer = DarkOnYellowSecondaryContainer,
    tertiary = DarkYellowTertiary,
    onTertiary = DarkOnTertiary,
    tertiaryContainer = DarkYellowTertiaryContainer,
    onTertiaryContainer = DarkOnYellowTertiaryContainer,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    surfaceTint = DarkSurfaceTint,
    inverseSurface = DarkInverseSurface,
    inverseOnSurface = DarkInverseOnSurface,
    error = DarkError,
    onError = DarkOnError,
    errorContainer = DarkErrorContainer,
    onErrorContainer = DarkOnErrorContainer,
    outline = DarkOutline,
    outlineVariant = DarkOutlineVariant,
    scrim = DarkScrim,
    surfaceBright = DarkSurfaceBright,
    surfaceContainer = DarkSurfaceContainer,
    surfaceContainerHigh = DarkSurfaceContainerHigh,
    surfaceContainerHighest = DarkSurfaceContainerHighest,
    surfaceContainerLow = DarkSurfaceContainerLow,
    surfaceContainerLowest = DarkSurfaceContainerLowest,
    surfaceDim = DarkSurfaceDim,
)

private val LightColorScheme = lightColorScheme(
    primary = LightYellowPrimary,
    onPrimary = LightOnPrimary,
    primaryContainer = LightYellowPrimaryContainer,
    onPrimaryContainer = LightOnYellowPrimaryContainer,
    inversePrimary = LightInversePrimary,
    secondary = LightYellowSecondary,
    onSecondary = LightOnSecondary,
    secondaryContainer = LightYellowSecondaryContainer,
    onSecondaryContainer = LightOnYellowSecondaryContainer,
    tertiary = LightYellowTertiary,
    onTertiary = LightOnTertiary,
    tertiaryContainer = LightYellowTertiaryContainer,
    onTertiaryContainer = LightOnYellowTertiaryContainer,
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
    surfaceTint = LightSurfaceTint,
    inverseSurface = LightInverseSurface,
    inverseOnSurface = LightInverseOnSurface,
    error = LightError,
    onError = LightOnError,
    errorContainer = LightErrorContainer,
    onErrorContainer = LightOnErrorContainer,
    outline = LightOutline,
    outlineVariant = LightOutlineVariant,
    scrim = LightScrim,
    surfaceBright = LightSurfaceBright,
    surfaceContainer = LightSurfaceContainer,
    surfaceContainerHigh = LightSurfaceContainerHigh,
    surfaceContainerHighest = LightSurfaceContainerHighest,
    surfaceContainerLow = LightSurfaceContainerLow,
    surfaceContainerLowest = LightSurfaceContainerLowest,
    surfaceDim = LightSurfaceDim,
)

@Composable
fun QrTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context).copy(
                primary = DarkYellowPrimary,
                onPrimary = DarkOnPrimary,
                primaryContainer = DarkYellowPrimaryContainer,
                onPrimaryContainer = DarkOnYellowPrimaryContainer,
                inversePrimary = DarkInversePrimary,
                secondary = DarkYellowSecondary,
                onSecondary = DarkOnSecondary,
                secondaryContainer = DarkYellowSecondaryContainer,
                onSecondaryContainer = DarkOnYellowSecondaryContainer,
                tertiary = DarkYellowTertiary,
                onTertiary = DarkOnTertiary,
                tertiaryContainer = DarkYellowTertiaryContainer,
                onTertiaryContainer = DarkOnYellowTertiaryContainer,
                background = DarkBackground,
                onBackground = DarkOnBackground,
                surface = DarkSurface,
                onSurface = DarkOnSurface,
                surfaceVariant = DarkSurfaceVariant,
                onSurfaceVariant = DarkOnSurfaceVariant,
                surfaceTint = DarkSurfaceTint,
                inverseSurface = DarkInverseSurface,
                inverseOnSurface = DarkInverseOnSurface,
                error = DarkError,
                onError = DarkOnError,
                errorContainer = DarkErrorContainer,
                onErrorContainer = DarkOnErrorContainer,
                outline = DarkOutline,
                outlineVariant = DarkOutlineVariant,
                scrim = DarkScrim,
                surfaceBright = DarkSurfaceBright,
                surfaceContainer = DarkSurfaceContainer,
                surfaceContainerHigh = DarkSurfaceContainerHigh,
                surfaceContainerHighest = DarkSurfaceContainerHighest,
                surfaceContainerLow = DarkSurfaceContainerLow,
                surfaceContainerLowest = DarkSurfaceContainerLowest,
                surfaceDim = DarkSurfaceDim,
            ) else dynamicLightColorScheme(context).copy(
                primary = LightYellowPrimary,
                onPrimary = LightOnPrimary,
                primaryContainer = LightYellowPrimaryContainer,
                onPrimaryContainer = LightOnYellowPrimaryContainer,
                inversePrimary = LightInversePrimary,
                secondary = LightYellowSecondary,
                onSecondary = LightOnSecondary,
                secondaryContainer = LightYellowSecondaryContainer,
                onSecondaryContainer = LightOnYellowSecondaryContainer,
                tertiary = LightYellowTertiary,
                onTertiary = LightOnTertiary,
                tertiaryContainer = LightYellowTertiaryContainer,
                onTertiaryContainer = LightOnYellowTertiaryContainer,
                background = LightBackground,
                onBackground = LightOnBackground,
                surface = LightSurface,
                onSurface = LightOnSurface,
                surfaceVariant = LightSurfaceVariant,
                onSurfaceVariant = LightOnSurfaceVariant,
                surfaceTint = LightSurfaceTint,
                inverseSurface = LightInverseSurface,
                inverseOnSurface = LightInverseOnSurface,
                error = LightError,
                onError = LightOnError,
                errorContainer = LightErrorContainer,
                onErrorContainer = LightOnErrorContainer,
                outline = LightOutline,
                outlineVariant = LightOutlineVariant,
                scrim = LightScrim,
                surfaceBright = LightSurfaceBright,
                surfaceContainer = LightSurfaceContainer,
                surfaceContainerHigh = LightSurfaceContainerHigh,
                surfaceContainerHighest = LightSurfaceContainerHighest,
                surfaceContainerLow = LightSurfaceContainerLow,
                surfaceContainerLowest = LightSurfaceContainerLowest,
                surfaceDim = LightSurfaceDim,
            )
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
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