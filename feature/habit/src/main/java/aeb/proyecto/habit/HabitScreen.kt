package aeb.proyecto.habit

import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.topbar.providers.ProvideAppBarActions
import aeb.proyecto.ui.topbar.providers.ProvideAppBarTitle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HabitScreen(
    viewModel: HabitViewModel = hiltViewModel(),
    navigateToAddHabit: (Long) -> Unit
){

    ProvideAppBarTitle {
        LabelLargeText(stringResource(R.string.habit_topbar), fontSize = 20.sp)
    }

    ProvideAppBarActions {
        IconButton(
            onClick = {navigateToAddHabit(-1L)}
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_add),
                contentDescription = "action button habit",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }

    HabitScreen()
}

@Composable
internal fun HabitScreen(){

}