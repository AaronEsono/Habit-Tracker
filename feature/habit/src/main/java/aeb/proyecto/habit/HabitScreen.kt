package aeb.proyecto.habit

import aeb.proyecto.habit.components.loading.HabitLoading
import aeb.proyecto.habit.components.screen.NoHabitScreen
import aeb.proyecto.habit.components.screen.PagerElementScreen
import aeb.proyecto.habit.model.PagerElement
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.topbar.providers.ProvideAppBarActions
import aeb.proyecto.ui.topbar.providers.ProvideAppBarTitle
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
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
import java.time.LocalDate

//Iconos de flaticon y svgRepo

@Composable
fun HabitScreen(
    viewModel: HabitViewModel = hiltViewModel(),
    navigateToAddHabit: (Long) -> Unit
){

    val typeUIState = viewModel.typeUIState.collectAsStateWithLifecycle().value
    val selectedType = viewModel.selectedType.collectAsStateWithLifecycle().value
    val habitUIState = viewModel.habitUIState.collectAsStateWithLifecycle().value
    val dateSelected = viewModel.dateSelected.collectAsStateWithLifecycle().value

    ProvideAppBarTitle {
        LabelLargeText(stringResource(R.string.habit_topbar), fontSize = 20.sp)
    }

    HabitScreen(
        typeUIState = typeUIState,
        habitsUIState = habitUIState,
        pagerSelected = selectedType,
        dateSelected = dateSelected,
        navigateToAddHabit = navigateToAddHabit,
        onClickTab = viewModel::onClickTab
    )
}

@Composable
internal fun HabitScreen(
    typeUIState: TypeUIState,
    habitsUIState : HabitsUIState,
    pagerSelected: SelectedTypeState,
    dateSelected: LocalDate = LocalDate.now(),
    navigateToAddHabit: (Long) -> Unit = {},
    onClickTab: (PagerElement) -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize()){
        when (typeUIState) {
            TypeUIState.Error -> Unit
            TypeUIState.Loading -> {
                HabitLoading()
            }
            is TypeUIState.Success -> {
                if (typeUIState.availableTypes.isEmpty()) {
                    NoHabitScreen()
                } else {
                    PagerElementScreen(
                        pagerElements = typeUIState.availableTypes,
                        habitsUIState = habitsUIState,
                        pagerSelected = pagerSelected,
                        onClickTab = onClickTab
                    )
                }
            }
        }

        CustomRipple {
            FloatingActionButton(
                onClick = { navigateToAddHabit(-1) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(spacing16)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Floating action button")
            }
        }
    }
}