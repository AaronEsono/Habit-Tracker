package aeb.proyecto.stopwatch.overlay.colors

import androidx.compose.ui.graphics.Color

data class OverlayColors(
    val backgroundColor: Color,
    val textColor: Color,
    val emptyBarColor: Color,
    val filledBarColor: Color,
    val pausedBarColor: Color
)


val darkOverlayTheme = OverlayColors(
    backgroundColor = Color(0xFF1E1E1E),
    textColor = Color(0xFFFAFAFA),
    emptyBarColor = Color(0xFF3A3A3A),     // gris oscuro, contraste con fondo
    filledBarColor = Color(0xFFFAFAFA),
    pausedBarColor = Color(0xFF888888)
)

val lightOverlayTheme = OverlayColors(
    backgroundColor = Color(0xFFFAFAFA),
    textColor = Color(0xFF1E1E1E),
    emptyBarColor = Color(0xFFE0E0E0),     // gris claro, contraste con fondo
    filledBarColor = Color(0xFF1E1E1E),
    pausedBarColor = Color(0xFF888888)
)