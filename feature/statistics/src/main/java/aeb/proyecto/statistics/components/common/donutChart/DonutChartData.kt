package aeb.proyecto.statistics.components.common.donutChart

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class DonutChartData(
    val amount: Float,
    val color: Color,
    val titleString: Int,
)

data class DonutChartDataCollection(
    var items: List<DonutChartData>
) {
    internal var totalAmount: Float = items.sumOf { it.amount.toDouble() }.toFloat()
        private set
}

data class DrawingAngles(val start: Float, val end: Float)

class DonutChartState(
    val state: State = State.Unselected
) {
    val stroke: Dp
        get() = when (state) {
            State.Selected -> STROKE_SIZE_SELECTED
            State.Unselected -> STROKE_SIZE_UNSELECTED
        }

    enum class State {
        Selected, Unselected
    }
}