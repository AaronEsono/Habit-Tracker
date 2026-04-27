package aeb.proyecto.statistics.components.common.donutChart

import aeb.proyecto.statistics.R
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import com.patrykandpatrick.vico.core.common.copyColor

enum class PieChartState{
    COMPLETED,
    UNCOMPLETED,
    NOT_DONE
}

data class PieChartData(
    val percentage: Int,
    val value:Float,
    val state: PieChartState = PieChartState.NOT_DONE,
    val habitColor:Int = 0,
)

@Composable
fun PieChartData.getColor(): Color{

    val color = when(state){
        PieChartState.NOT_DONE -> MaterialTheme.colorScheme.secondaryContainer
        PieChartState.COMPLETED -> Color(habitColor)
        PieChartState.UNCOMPLETED -> Color(habitColor).copy(alpha = 0.5f)
    }

    return color
}

@Composable
fun PieChartData.getTitle(): String{

    val title = when(state){
        PieChartState.NOT_DONE -> stringResource(R.string.statistics_title_not_done)
        PieChartState.COMPLETED -> stringResource(R.string.statistics_title_completed)
        PieChartState.UNCOMPLETED -> stringResource(R.string.statistics_title_uncompleted)
    }

    return title
}