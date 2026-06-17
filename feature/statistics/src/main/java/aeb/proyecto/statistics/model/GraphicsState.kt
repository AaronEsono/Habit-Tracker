package aeb.proyecto.statistics.model

import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModel

/**
 * Represents the state of a chart for a given habit.
 *
 * @property model The [CartesianChartModel] containing the data points to be rendered.
 * @property color The color resource ID associated with the habit's theme.
 */
data class GraphicsState(
    val model: CartesianChartModel? = null,
    val color: Int = 0
)