package aeb.proyecto.timer

import aeb.proyecto.ui.controllerProvider.LocalNavController
import aeb.proyecto.ui.navigationIcon.NavigationIcon
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.LabelMediumText
import aeb.proyecto.ui.topbar.providers.ProvideAppBarNavigationIcon
import aeb.proyecto.ui.topbar.providers.ProvideAppBarTitle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import java.time.LocalDate

@Composable
fun TimerScreen(
    viewModel: TimerViewModel = hiltViewModel(),
    dayHabitId: Pair<Long,LocalDate>,
    navigateToHabitScreen: () -> Unit
){

    ProvideAppBarNavigationIcon {
        NavigationIcon()
    }

    ProvideAppBarTitle {
        LabelLargeText(stringResource(R.string.timer_title), fontSize = 20.sp)
    }

    LabelMediumText(dayHabitId.toString())
    TimerScreen()
}


@Composable
internal fun TimerScreen(){

}