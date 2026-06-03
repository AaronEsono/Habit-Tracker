package aeb.proyecto.ui.constants

import androidx.compose.ui.graphics.Color
import kotlin.math.pow

/**
 * Immutable collection of predefined hexadecimal [Color] palettes.
 * Engineered to feed color-picker selection grids while interacting seamlessly
 * with accessibility luminance compliance checks.
 */
val listColors = listOf(
    Color(0xFFFF5733), // Rojo medio
    Color(0xFFFF6F3C), // Rojo anaranjado
    Color(0xFFFF7043), // Naranja medio
    Color(0xFFFF8C42), // Naranja cálido
    Color(0xFFFF9E5D), // Naranja suave
    Color(0xFFFFB74D), // Naranja pastel
    Color(0xFFFFCC33), // Amarillo dorado
    Color(0xFFFFD54F), // Amarillo mostaza
    Color(0xFFFFEB3B), // Amarillo brillante
    Color(0xFF9E9D24), // Amarillo verdoso
    Color(0xFFFBC02D), // Amarillo cálido
    Color(0xFFF9A825), // Amarillo anaranjado
    Color(0xFFF57F17), // Amarillo intenso
    Color(0xFF81C784), // Verde pastel
    Color(0xFF66BB6A), // Verde suave
    Color(0xFF4CAF50), // Verde medio
    Color(0xFF388E3C), // Verde oscuro medio
    Color(0xFF2C6F3A), // Verde oliva
    Color(0xFF1B5E20), // Verde bosque
    Color(0xFF64B5F6), // Azul claro
    Color(0xFF42A5F5), // Azul celeste
    Color(0xFF2196F3), // Azul fuerte
    Color(0xFF1E88E5), // Azul medio
    Color(0xFF1976D2), // Azul clásico
    Color(0xFF1565C0), // Azul intenso
    Color(0xFF0D47A1), // Azul muy oscuro
    Color(0xFF80DEEA), // Azul claro pastel
    Color(0xFF4DD0E1), // Azul agua
    Color(0xFF26C6DA), // Azul turquesa
    Color(0xFF00BCD4), // Azul vibrante
    Color(0xFF00ACC1), // Azul fuerte
    Color(0xFF0097A7), // Azul azulado
    Color(0xFF006064), // Azul marino
    Color(0xFF81D4FA), // Azul suave
    Color(0xFF29B6F6), // Azul brillante
    Color(0xFF039BE5), // Azul claro brillante
    Color(0xFF0288D1), // Azul profundo
    Color(0xFF0277BD), // Azul medio oscuro
    Color(0xFF01579B), // Azul oscuro
    Color(0xFFC5CAE9), // Morado suave
    Color(0xFF9FA8DA), // Morado pastel
    Color(0xFF7986CB), // Azul morado
    Color(0xFF5C6BC0), // Azul lavanda
    Color(0xFF3F51B5), // Azul zafiro
    Color(0xFF3949AB), // Azul real
    Color(0xFF303F9F), // Azul medianoche
    Color(0xFF283593), // Azul añil
    Color(0xFF1A237E), // Azul oscuro intenso
    Color(0xFF9C27B0), // Morado brillante
    Color(0xFF8E24AA), // Morado intenso
    Color(0xFF7B1FA2), // Morado medio
    Color(0xFF6A1B9A), // Morado oscuro
    Color(0xFF4A148C), // Morado fuerte
    Color(0xFF8E44AD), // Púrpura cálido
    Color(0xFF9B59B6), // Lavanda suave
    Color(0xFFD1C4E9), // Morado pastel claro
    Color(0xFFB39DDB), // Lila pastel
    Color(0xFF8E7CC3), // Lila medio
    Color(0xFF7E57C2), // Violeta suave
    Color(0xFF673AB7), // Violeta medio
    Color(0xFF5E35B1), // Violeta profundo
    Color(0xFF512DA8), // Violeta oscuro
    Color(0xFF4527A0), // Violeta cálido
    Color(0xFF311B92), // Violeta casi azul
    Color(0xFF8D6E63), // Marrón claro
    Color(0xFF795548), // Marrón medio
    Color(0xFF6D4C41), // Marrón cálido
    Color(0xFF5D4037), // Marrón oscuro
    Color(0xFF4E342E), // Marrón muy oscuro
    Color(0xFF3E2723), // Marrón profundo
    Color(0xFF3E4A89), // Gris azulado
    Color(0xFF607D8B), // Gris medio
    Color(0xFF455A64), // Gris con tono verde
    Color(0xFF546E7A), // Gris más oscuro
    Color(0xFF37474F), // Gris oscuro
    Color(0xFF263238)  // Gris casi negro
)

/**
 * Evaluates the relative luminance of a target [Color] to dynamically compute a high-contrast
 * text or icon foreground color (either White or Black).
 * Strictly complies with the W3C Web Content Accessibility Guidelines (WCAG 2.1) Nivel AA
 * standard, enforcing a minimum contrast ratio threshold of 4.5:1.
 *
 * @param color The background canvas color to inspect for luminance convergence.
 * @return A solid [Color.White] or [Color.Black] token guaranteed to maximize legibility.
 */
fun getContrastColor(color: Color): Color {
    val luminance = color.calculateLuminance()

    // Calculate the mathematical contrast ratio threshold strictly pitted against reference White (1.0)
    val contrastWithWhite = (1.0 + 0.05) / (luminance + 0.05)

    // Enforce the rigid 4.5 WCAG accessibility boundary guard
    return if (contrastWithWhite >= 4.5) Color.White else Color.Black
}

/**
 * Computes the relative luminance of a solid color converted to the linearized sRGB color space.
 * Mitigates human optical variance vectors by applying non-linear gamma corrections followed by
 * CIE standard spectral coefficients (21.26% Red, 71.52% Green, 7.22% Blue).
 *
 * @return A [Double] scalar value ranging precisely from 0.0 (absolute dark) to 1.0 (absolute light).
 */
fun Color.calculateLuminance(): Double {
    val r = red
    val g = green
    val b = blue

    // Apply piecewise linearization curves to de-convert sRGB gamma channels back to true physical light vectors
    val gammaCorrectedR = if (r <= 0.03928) r / 12.92 else ((r + 0.055) / 1.055).pow(2.4)
    val gammaCorrectedG = if (g <= 0.03928) g / 12.92 else ((g + 0.055) / 1.055).pow(2.4)
    val gammaCorrectedB = if (b <= 0.03928) b / 12.92 else ((b + 0.055) / 1.055).pow(2.4)

// Merge components utilizing standardized weight coefficients mapping human macular photopic responses
    return 0.2126 * gammaCorrectedR + 0.7152 * gammaCorrectedG + 0.0722 * gammaCorrectedB
}