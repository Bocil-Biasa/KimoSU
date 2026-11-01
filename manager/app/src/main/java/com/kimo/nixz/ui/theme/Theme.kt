// Mod by Nixz • Dev
package com.kimo.nixz.ui.theme

import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

val LocalAppStyling = compositionLocalOf { "Default" }
val LocalCardCornerRadius = compositionLocalOf { 16 }

private val DarkColorScheme = darkColorScheme(
    primary = PRIMARY,
    secondary = PRIMARY_DARK,
    tertiary = SECONDARY_DARK
)

private val LightColorScheme = lightColorScheme(
    primary = PRIMARY,
    secondary = PRIMARY_LIGHT,
    tertiary = SECONDARY_LIGHT
)

private val MiuixDarkColorScheme = darkColorScheme(
    primary = Color(0xFF0D84FF),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF143864),
    onPrimaryContainer = Color(0xFFD0E4FF),
    secondary = Color(0xFF1E6CEB),
    tertiary = Color(0xFF64D2FF),
    background = Color(0xFF111317),
    surface = Color(0xFF1C2028),
    surfaceVariant = Color(0xFF282D39),
    surfaceContainer = Color(0xFF1C2028),
    surfaceContainerLow = Color(0xFF161920),
    surfaceContainerLowest = Color(0xFF101217),
    surfaceContainerHigh = Color(0xFF232833),
    surfaceContainerHighest = Color(0xFF2B3240),
    outline = Color(0xFF383F52),
    outlineVariant = Color(0xFF232835)
)

private val MiuixLightColorScheme = lightColorScheme(
    primary = Color(0xFF007AFF),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE2EFFF),
    onPrimaryContainer = Color(0xFF004494),
    secondary = Color(0xFF0D84FF),
    tertiary = Color(0xFF5AC8FA),
    background = Color(0xFFF2F4F8),
    surface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFE5E9F2),
    surfaceContainer = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFF8FAFD),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerHigh = Color(0xFFEDF1F7),
    surfaceContainerHighest = Color(0xFFE3E8F0),
    outline = Color(0xFFCFD6E4),
    outlineVariant = Color(0xFFE2E7F0)
)

private val MiuixShapes = Shapes(
    extraSmall = RoundedCornerShape(10.dp),
    small = RoundedCornerShape(16.dp),
    medium = RoundedCornerShape(26.dp),
    large = RoundedCornerShape(32.dp),
    extraLarge = RoundedCornerShape(38.dp)
)

fun Color.blend(other: Color, ratio: Float): Color {
    val inverse = 1f - ratio
    return Color(
        red = red * inverse + other.red * ratio,
        green = green * inverse + other.green * ratio,
        blue = blue * inverse + other.blue * ratio,
        alpha = alpha
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("DEPRECATION")
@Composable
fun KernelSUTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    amoledMode: Boolean = false,
    styling: String = "Default",
    darkVariant: String = "Default",
    cornerRadius: Int = 16,
    accentColor: Color? = null,
    rippleColor: Color? = null,
    content: @Composable () -> Unit
) {
    val isMiuix = styling == "MiuiX"

    val baseColorScheme = when {
        isMiuix -> {
            if (darkTheme) MiuixDarkColorScheme else MiuixLightColorScheme
        }
        darkTheme -> {
            when {
                amoledMode || darkVariant == "Amoled" -> {
                    val base = if (dynamicColor && accentColor == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        dynamicDarkColorScheme(LocalContext.current)
                    } else {
                        DarkColorScheme
                    }
                    base.copy(
                        background = AMOLED_BLACK,
                        surface = AMOLED_BLACK,
                        surfaceVariant = DARK_GREY.blend(AMOLED_BLACK, 0.8f),
                        surfaceContainer = AMOLED_BLACK,
                        surfaceContainerLow = AMOLED_BLACK,
                        surfaceContainerLowest = AMOLED_BLACK,
                        surfaceContainerHigh = Color(0xFF111111),
                        surfaceContainerHighest = Color(0xFF181818)
                    )
                }
                darkVariant == "Midnight" -> {
                    DarkColorScheme.copy(
                        background = MIDNIGHT_NAVY_BG,
                        surface = MIDNIGHT_NAVY_SURFACE,
                        surfaceVariant = MIDNIGHT_NAVY_CONTAINER,
                        surfaceContainer = MIDNIGHT_NAVY_SURFACE,
                        surfaceContainerHigh = Color(0xFF1A2438),
                        surfaceContainerHighest = Color(0xFF222F4A)
                    )
                }
                darkVariant == "Mocha" -> {
                    DarkColorScheme.copy(
                        background = MOCHA_BG,
                        surface = MOCHA_SURFACE,
                        surfaceVariant = MOCHA_CONTAINER,
                        surfaceContainer = MOCHA_SURFACE,
                        surfaceContainerHigh = Color(0xFF24273A),
                        surfaceContainerHighest = Color(0xFF363A4F)
                    )
                }
                dynamicColor && accentColor == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                    dynamicDarkColorScheme(LocalContext.current)
                }
                else -> DarkColorScheme
            }
        }
        else -> {
            if (dynamicColor && accentColor == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                dynamicLightColorScheme(LocalContext.current)
            } else {
                LightColorScheme
            }
        }
    }

    val colorScheme = if (accentColor != null) {
        baseColorScheme.copy(
            primary = accentColor,
            primaryContainer = accentColor.copy(alpha = if (darkTheme) 0.35f else 0.18f),
            onPrimaryContainer = if (darkTheme) Color.White else Color.Black,
            outline = accentColor.copy(alpha = 0.5f)
        )
    } else {
        baseColorScheme
    }

    val computedShapes = if (isMiuix) {
        MiuixShapes
    } else {
        Shapes(
            extraSmall = RoundedCornerShape((cornerRadius / 2).coerceAtLeast(4).dp),
            small = RoundedCornerShape((cornerRadius * 3 / 4).coerceAtLeast(8).dp),
            medium = RoundedCornerShape(cornerRadius.dp),
            large = RoundedCornerShape((cornerRadius * 1.5).toInt().dp),
            extraLarge = RoundedCornerShape((cornerRadius * 2).dp)
        )
    }

    SystemBarStyle(
        darkMode = darkTheme
    )

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = computedShapes,
        typography = Typography,
        content = {
            CompositionLocalProvider(
                LocalAppStyling provides styling,
                LocalCardCornerRadius provides if (isMiuix) 26 else cornerRadius
            ) {
                if (rippleColor != null) {
                    CompositionLocalProvider(
                        LocalRippleConfiguration provides RippleConfiguration(color = rippleColor)
                    ) {
                        content()
                    }
                } else {
                    content()
                }
            }
        }
    )
}

@Composable
private fun SystemBarStyle(
    darkMode: Boolean,
    statusBarScrim: Color = Color.Transparent,
    navigationBarScrim: Color = Color.Transparent,
) {
    val context = LocalContext.current
    val activity = context as ComponentActivity

    SideEffect {
        activity.enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                statusBarScrim.toArgb(),
                statusBarScrim.toArgb(),
            ) { darkMode },
            navigationBarStyle = when {
                darkMode -> SystemBarStyle.dark(
                    navigationBarScrim.toArgb()
                )

                else -> SystemBarStyle.light(
                    navigationBarScrim.toArgb(),
                    navigationBarScrim.toArgb(),
                )
            }
        )
    }
}
