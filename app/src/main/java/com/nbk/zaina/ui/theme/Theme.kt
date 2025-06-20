package com.nbk.rise.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryColor,
    onPrimary = LuxuryWhite,
    primaryContainer = PrimaryVariant,
    onPrimaryContainer = LuxuryWhite,
    secondary = SecondaryColor,
    onSecondary = LuxuryWhite,
    secondaryContainer = SecondaryVariant,
    onSecondaryContainer = LuxuryWhite,
    tertiary = AccentColor,
    onTertiary = PrimaryColor,
    error = ErrorRed,
    onError = LuxuryWhite,
    errorContainer = ErrorRed,
    onErrorContainer = LuxuryWhite,
    background = DarkBackground,
    onBackground = DarkOnSurface,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    outline = TextMuted,
    outlineVariant = SecondaryColor,
    scrim = PrimaryVariant
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryColor,
    onPrimary = LuxuryWhite,
    primaryContainer = AccentLight,
    onPrimaryContainer = PrimaryColor,
    secondary = SecondaryColor,
    onSecondary = LuxuryWhite,
    secondaryContainer = AccentLight,
    onSecondaryContainer = PrimaryColor,
    tertiary = AccentColor,
    onTertiary = PrimaryColor,
    error = ErrorRed,
    onError = LuxuryWhite,
    errorContainer = ErrorRed,
    onErrorContainer = LuxuryWhite,
    background = LuxuryWhite,
    onBackground = TextPrimary,
    surface = SurfaceElevated,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceCard,
    onSurfaceVariant = TextSecondary,
    outline = TextMuted,
    outlineVariant = SurfaceDim,
    scrim = PrimaryVariant
)

@Composable
fun RiseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Disable dynamic color for brand consistency
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
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}