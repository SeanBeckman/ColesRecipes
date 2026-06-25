package com.example.colesrecipes.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = ColesRed,
    secondary = ColesLightGrey,
    tertiary = ColesDarkRed,
    background = ColesDarkGrey,
    surface = ColesDarkGrey,
    onPrimary = ColesWhite,
    onSecondary = ColesDarkGrey,
    onTertiary = ColesWhite,
    onBackground = ColesWhite,
    onSurface = ColesWhite
)

private val LightColorScheme = lightColorScheme(
    primary = ColesRed,
    secondary = ColesDarkGrey,
    tertiary = ColesLightGrey,
    background = ColesWhite,
    surface = ColesWhite,
    onPrimary = ColesWhite,
    onSecondary = ColesWhite,
    onTertiary = ColesDarkGrey,
    onBackground = ColesDarkGrey,
    onSurface = ColesDarkGrey
)

@Composable
fun ColesRecipesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    // Setting to false by default to ensure the Coles brand colors are used
    dynamicColor: Boolean = false,
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
        typography = Typography,
        content = content
    )
}
