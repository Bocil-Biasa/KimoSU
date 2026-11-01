// Mod by Nixz • Dev
package com.kimo.nixz.ui.theme

import androidx.compose.ui.graphics.Color

val PRIMARY = Color(0xFF8AADF4)           // Catppuccin Blue
val PRIMARY_LIGHT = Color(0xFFB7BDF8)     // Catppuccin Lavender
val SECONDARY_LIGHT = Color(0xFFA6DA95)   // Catppuccin Green

val PRIMARY_DARK = Color(0xFF7DC4E4)      // Catppuccin Sky
val SECONDARY_DARK = Color(0xFFF5BDE6)    // Catppuccin Pink

val AMOLED_BLACK = Color(0xFF000000)      // Pure black for AMOLED

val DARK_PURPLE = Color(0xFF6E6CB6)       // Catppuccin Mauve (dark purple)
val DARK_GREY = Color(0xFF363A4F)         // Catppuccin Surface (dark grey)

val GREEN = Color(0xFF4CAF50)             // Green
val RED = Color(0xFFF44336)               // Red
val YELLOW = Color(0xFFFFEB3B)            // Yellow
val ORANGE = Color(0xFFFF9800)            // Orange

val PRESET_ACCENT_COLORS = listOf(
    "Pixel Blue" to Color(0xFF1A73E8),
    "KernelSU Green" to Color(0xFF2E7D32),
    "Magisk Teal" to Color(0xFF00897B),
    "Shamiko Orange" to Color(0xFFFB8C00),
    "LSposed Purple" to Color(0xFF8E24AA),
    "Zygisk Red" to Color(0xFFE53935),
    "Viper Pink" to Color(0xFFD81B60),
    "Neon Green" to Color(0xFF00E676),
    "Cyan Tech" to Color(0xFF00BCD4),
    "Amber Gold" to Color(0xFFFFB300),
    "MIUI Blue" to Color(0xFF0D84FF),
    "Slate Grey" to Color(0xFF546E7A)
)

val MIDNIGHT_NAVY_BG = Color(0xFF0A0E17)
val MIDNIGHT_NAVY_SURFACE = Color(0xFF121826)
val MIDNIGHT_NAVY_CONTAINER = Color(0xFF1B2337)

val MOCHA_BG = Color(0xFF1E1E2E)
val MOCHA_SURFACE = Color(0xFF181825)
val MOCHA_CONTAINER = Color(0xFF313244)

fun parseHexColor(hex: String?): Color? {
    if (hex.isNullOrBlank()) return null
    return try {
        val cleanHex = hex.trim().removePrefix("#").removePrefix("0x")
        val colorInt = when (cleanHex.length) {
            6 -> (0xFF000000 or cleanHex.toLong(16)).toInt()
            8 -> cleanHex.toLong(16).toInt()
            else -> return null
        }
        Color(colorInt)
    } catch (_: Exception) {
        null
    }
}

fun Color.toHexString(includeAlpha: Boolean = false): String {
    val a = (alpha * 255).toInt() and 0xFF
    val r = (red * 255).toInt() and 0xFF
    val g = (green * 255).toInt() and 0xFF
    val b = (blue * 255).toInt() and 0xFF
    return if (includeAlpha) {
        String.format("#%02X%02X%02X%02X", a, r, g, b)
    } else {
        String.format("#%02X%02X%02X", r, g, b)
    }
}