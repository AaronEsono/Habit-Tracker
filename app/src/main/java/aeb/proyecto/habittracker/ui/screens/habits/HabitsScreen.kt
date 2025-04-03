package aeb.proyecto.habittracker.ui.screens.habits

import aeb.proyecto.addhabit.navigation.navigateToAddHabit
import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.ui.components.dailyHabit.CardDailyHabit
import aeb.proyecto.habittracker.ui.components.text.LabelMediumText
import aeb.proyecto.habittracker.ui.screens.habits.habitsComponents.HabitScreenStates
import aeb.proyecto.habittracker.utils.AppState
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import aeb.proyecto.ui.topbar.providers.ProvideAppBarActions
import aeb.proyecto.ui.topbar.providers.ProvideAppBarTitle
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay

@Composable
fun HabitsScreen(
    habitsViewModel: HabitsViewModel = hiltViewModel(),
    navController: NavHostController,
    onEditClick: (Long) -> Unit = {}
) {

    ProvideAppBarTitle {
        aeb.proyecto.ui.text.LabelMediumText("Hábitos")
    }

    ProvideAppBarActions {
        Icon(Icons.Filled.Face, contentDescription = "",
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.clickable {
                navController.navigateToAddHabit(-1)
            })
    }

    val uiState = habitsViewModel.uiState.collectAsState().value

    val shouldBlur = remember(uiState.showDialog) { uiState.showDialog }
    
}