package com.note.coffee.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColorScheme(
    primary = LightCoffee,
    onPrimary = White,
    inversePrimary = White,
    secondary = BlackCoffee,
    onSecondary = White,
    tertiary = Coffee,
    onTertiary = White,
    surface = White,
    onSurface = Black,
    background = White,
    onBackground = Black,
    outline = Black,
    onSurfaceVariant = Black,
)

private val LightColorPalette = lightColorScheme(
    primary = LightCoffee,
    onPrimary = White,
    inversePrimary = White,
    secondary = BlackCoffee,
    onSecondary = White,
    tertiary = Coffee,
    onTertiary = White,
    surface = White,
    onSurface = Black,
    background = White,
    onBackground = Black,
    outline = Black,
    onSurfaceVariant = Black,
)

@Composable
fun CoffingNoteTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}