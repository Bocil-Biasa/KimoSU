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
    "MIUI Blue" to Color(0xFF0D84FF),
    "Xiaomi Orange" to Color(0xFFFF6700),
    "Catppuccin Blue" to PRIMARY,
    "Lavender" to PRIMARY_LIGHT,
    "Emerald Green" to Color(0xFF10B981),
    "Crimson Red" to Color(0xFFEF4444),
    "Cyberpunk Purple" to Color(0xFFA855F7),
    "Sakura Pink" to Color(0xFFEC4899),
    "Neon Cyan" to Color(0xFF06B6D4),
    "Amber Gold" to Color(0xFFF59E0B),
    "Midnight Indigo" to Color(0xFF6366F1)
)

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