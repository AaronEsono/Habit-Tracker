package aeb.proyecto.statistics.components.common.donutChart

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt

const val TOTAL_ANGLE = 360.0f
val STROKE_SIZE_UNSELECTED = 40.dp
val STROKE_SIZE_SELECTED = 60.dp

/**
 * Calculate the gap width between the arcs based on [gapPercentage]. The percentage is applied
 * to the average count to determine the width in pixels.
 */
fun DonutChartDataCollection.calculateGap(gapPercentage: Float): Float {
    if (this.items.isEmpty()) return 0f

    return (this.totalAmount / this.items.size) * gapPercentage
}

/**
 * Returns the total data points including the individual gap widths indicated by the
 * [gapPercentage].
 */
fun DonutChartDataCollection.getTotalAmountWithGapIncluded(gapPercentage: Float): Float {
    val gap = this.calculateGap(gapPercentage)
    return this.totalAmount + (this.items.size * gap)
}

/**
 * Calculate the sweep angle of an arc including the gap as well. The gap is derived based
 * on [gapPercentage].
 */
fun DonutChartDataCollection.calculateGapAngle(gapPercentage: Float): Float {
    val gap = this.calculateGap(gapPercentage)
    val totalAmountWithGap = this.getTotalAmountWithGapIncluded(gapPercentage)

    return (gap / totalAmountWithGap) * TOTAL_ANGLE
}

/**
 * Returns the sweep angle of a given point in the [DonutChartDataCollection]. This calculations
 * takes the gap between arcs into the account.
 */
fun DonutChartDataCollection.findSweepAngle(
    index: Int,
    gapPercentage: Float
): Float {
    val amount = items[index].amount
    val gap = this.calculateGap(gapPercentage)
    val totalWithGap = getTotalAmountWithGapIncluded(gapPercentage)
    val gapAngle = this.calculateGapAngle(gapPercentage)
    return ((((amount + gap) / totalWithGap) * TOTAL_ANGLE)) - gapAngle
}

fun findTappedAngleIndex(
    tapOffset: Offset,
    canvasSize: Size,
    strokeWidthPx: Float,
    anglesList: List<DrawingAngles>
): Int {
    val centerX = canvasSize.width / 2
    val centerY = canvasSize.height / 2

    // 1. Calcular distancia desde el centro (Pitágoras)
    val dx = tapOffset.x - centerX
    val dy = tapOffset.y - centerY
    val distanceToCenter = sqrt(dx * dx + dy * dy)

    // 2. Verificar si el toque está dentro del grosor del Donut
    val innerRadius = (canvasSize.width / 2) - strokeWidthPx
    val outerRadius = canvasSize.width / 2

    if (distanceToCenter !in innerRadius..outerRadius) return -1

    // 3. Calcular el ángulo del toque en grados (0 a 360)
    var tapAngle = Math.toDegrees(atan2(dy.toDouble(), dx.toDouble())).toFloat()

    // atan2 devuelve de -180 a 180, normalizamos a 0-360
    if (tapAngle < 0) tapAngle += 360f

    // 4. Buscar en qué rango de anglesList cae
    return anglesList.indexOfFirst { range ->
        // Caso simple: el ángulo está entre inicio y fin
        if (range.start <= tapAngle && tapAngle <= (range.start + range.end)) {
            true
        } else if (range.start + range.end > 360f) {
            // Caso borde: cuando el arco cruza la barrera de los 360 grados
            val wrappedEnd = (range.start + range.end) % 360f
            tapAngle >= range.start || tapAngle <= wrappedEnd
        } else {
            false
        }
    }
}