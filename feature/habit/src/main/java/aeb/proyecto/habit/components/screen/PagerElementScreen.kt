package aeb.proyecto.habit.components.screen

import aeb.proyecto.habit.HabitsUIState
import aeb.proyecto.habit.SelectedTypeState
import aeb.proyecto.habit.components.loading.HabitLoading
import aeb.proyecto.habit.components.screen.typeHabits.DailyHabitsScreen
import aeb.proyecto.habit.components.screen.typeHabits.MonthlyHabitsScreen
import aeb.proyecto.habit.model.PagerElement
import aeb.proyecto.ui.text.LabelLargeText
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagerElementScreen(
    pagerElements: List<PagerElement>,
    habitsUIState: HabitsUIState,
    pagerSelected: SelectedTypeState,
    dateSelected: LocalDate = LocalDate.now(),
    onClickTab: (PagerElement) -> Unit = {}
){

    val selectedTabIndex = when (pagerSelected) {
        is SelectedTypeState.Selected -> pagerSelected.pagerSelected.index
        else -> 0 // O un índice predeterminado si no está inicializado
    }

    Column (
        modifier = Modifier.fillMaxSize()
    ){

        PrimaryTabRow(selectedTabIndex = selectedTabIndex,
            indicator = {
                TabRowDefaults.PrimaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(selectedTabIndex, matchContentSize = true),
                    width = Dp.Unspecified,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            divider = {
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface)
            },
            containerColor = MaterialTheme.colorScheme.surfaceTint
        ) {
            pagerElements.forEachIndexed { index, pagerElement ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { onClickTab(pagerElement) },
                    text = {
                        LabelLargeText(
                            stringResource(pagerElement.title),
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
            }
        }

        // Content
        when (habitsUIState) {
            is HabitsUIState.Loading, is HabitsUIState.Error, is HabitsUIState.Empty -> {
                HabitLoading()
            }
            is HabitsUIState.Success -> {
                if (pagerSelected is SelectedTypeState.Selected) {
                    when (pagerSelected.pagerSelected.pagerElement) {
                        PagerElement.DAILY -> {
                            DailyHabitsScreen()
                        }
                        PagerElement.WEEKLY -> {}
                        PagerElement.MONTHLY -> {
                            MonthlyHabitsScreen()
                        }
                        PagerElement.RECURRING -> {}
                    }
                }
            }
        }

    }
}