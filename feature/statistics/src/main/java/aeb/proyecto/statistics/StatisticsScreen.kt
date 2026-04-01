package aeb.proyecto.statistics

import aeb.proyecto.statistics.components.vertical.VerticalStatisticsScreen
import aeb.proyecto.ui.orientation.Orientation
import aeb.proyecto.ui.orientation.getOrientation
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.topbar.providers.ProvideAppBarTitle
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel = hiltViewModel()
){

    ProvideAppBarTitle {
        LabelLargeText(stringResource(R.string.topbar_habit), fontSize = 20.sp)
    }

    val orientation = getOrientation()
    val statisticsState = viewModel.statisticsState.collectAsStateWithLifecycle().value
    val yearMonth = viewModel.yearMonth.collectAsStateWithLifecycle().value
    val calendarState = viewModel.calendarUIState.collectAsStateWithLifecycle().value
    val startDayOfWeek = viewModel.dayOfWeek.collectAsStateWithLifecycle().value
    val boxUIState = viewModel.boxUIState.collectAsStateWithLifecycle().value
    val graphicsState = viewModel.graphicsState.collectAsStateWithLifecycle().value
    val hourlyGraphicsState = viewModel.hourlyGraphicsState.collectAsStateWithLifecycle().value
    val yearHourlyGraphicsSelected = viewModel.yearHourlyGraphicsSelected.collectAsStateWithLifecycle().value
    val yearGraphicsSelected = viewModel.yearGraphicsSelected.collectAsStateWithLifecycle().value


    when(orientation){
        Orientation.Portrait -> {
            VerticalStatisticsScreen(
                statisticsState = statisticsState,
                boxUIState = boxUIState,
                graphicsState = graphicsState,
                hourlyGraphicsState = hourlyGraphicsState,
                yearMonth = yearMonth,
                yearHourlyGraphicsSelected = yearHourlyGraphicsSelected,
                yearGraphicsSelected = yearGraphicsSelected,
                startDayOfWeek = startDayOfWeek,
                calendarUIState = calendarState,
                onCLickCard = viewModel::onCLickCard,
                onMonthChange = viewModel::onMonthButtonClicked,
                onYearSelected = viewModel::onYearSelected,
                onHourYearSelected = viewModel::onHourYearSelected
            )
        }
        Orientation.Landscape -> {}
    }

    // Vertical
    // 1. Linea horizontal que separe los hábitos. Se muestran en círculos con su nombre e icono. ---- HECHO
    // Abajo, el nombre del hábito ----- HECHO

    // 2. Nombre habito y descripcion abajo ----- HECHO

    // 3. Calendario con los días completados ----- HECHO

    // 4. Sistema de recuadros con los días a lo habitKit ----- HECHO

    // 5. Sistema de conteo de completados por mes, hacer detalle ----- HECHO

    // 6. Sistema de conteo de completados por hora, hacer detalle, en este hacer histórico? ----- HECHO

    // 7. Dos recuadros, en uno mostrar los completados total, y en otro la racha actual y la mejor racha
    // A lo mejor las rachas separarlos en dos

    // 8. Rueda donde se muestren: completados, a medio completar, no hecho, porcentaje

}