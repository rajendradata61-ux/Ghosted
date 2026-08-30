package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val GameColorScheme = darkColorScheme(
    primary = NeonLime,
    onPrimary = Color(0xFF00381B),
    primaryContainer = Color(0xFF00522B),
    onPrimaryContainer = NeonLime,

    secondary = ElectricPurple,
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = ElectricPurpleDark,
    onSecondaryContainer = Color(0xFFEEDBFF),

    tertiary = ElectricCyan,
    onTertiary = Color(0xFF00363D),
    tertiaryContainer = Color(0xFF004F58),
    onTertiaryContainer = ElectricCyan,

    error = HotCoral,
    onError = Color(0xFF37000B),
    errorContainer = HotCoralDark,
    onErrorContainer = Color(0xFFFFDAD6),

    background = DarkBg,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = DarkCardBorder,
    outlineVariant = DarkPurpleBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Enforce our custom bold party game visual theme
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = GameColorScheme,
        typography = Typography,
        content = content
    )
}
