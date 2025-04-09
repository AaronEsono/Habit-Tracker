package aeb.proyecto.statistics

import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.topbar.providers.ProvideAppBarTitle
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel = hiltViewModel()
){

    ProvideAppBarTitle {
        LabelLargeText(stringResource(R.string.topbar_habit), fontSize = 20.sp)
    }

    StatisticsScreen(
        onClick = {}
    )

}

@Composable
internal fun StatisticsScreen(
    onClick: () -> Unit
){

}