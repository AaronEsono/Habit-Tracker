package aeb.proyecto.habit

import aeb.proyecto.habit.components.loading.HabitLoading
import aeb.proyecto.habit.components.screen.NoHabitScreen
import aeb.proyecto.habit.components.screen.PagerElementScreen
import aeb.proyecto.habit.model.PagerElement
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.topbar.providers.ProvideAppBarActions
import aeb.proyecto.ui.topbar.providers.ProvideAppBarTitle
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

//Iconos de flaticon y svgRepo

@Composable
fun HabitScreen(
    viewModel: HabitViewModel = hiltViewModel(),
    navigateToAddHabit: (Long) -> Unit
){

    val pagerElements = viewModel.availableTypes.collectAsStateWithLifecycle().value
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

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

    HabitScreen(
        uiState = uiState,
        availableTypes = pagerElements
    )
}

@Composable
internal fun HabitScreen(
    uiState: HabitUIState,
    availableTypes: List<PagerElement>
) {
    when (uiState) {
        HabitUIState.Error -> Unit
        HabitUIState.Loading -> {
            HabitLoading()
        }
        HabitUIState.Success -> {
            if (availableTypes.isEmpty()) {
                NoHabitScreen()
            } else {
                PagerElementScreen(
                    uiState = uiState,
                    pagerElements = availableTypes
                )
            }
        }
    }
}