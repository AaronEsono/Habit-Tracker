package aeb.proyecto.addhabit

import aeb.proyecto.addhabit.components.horizontal.HorizontalAddHabitScreen
import aeb.proyecto.addhabit.components.vertical.VerticalAddHabitScreen
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.navigationIcon.NavigationIcon
import aeb.proyecto.ui.orientation.Orientation
import aeb.proyecto.ui.orientation.getOrientation
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.topbar.providers.ProvideAppBarActions
import aeb.proyecto.ui.topbar.providers.ProvideAppBarNavigationIcon
import aeb.proyecto.ui.topbar.providers.ProvideAppBarTitle
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

//Habits entre dias personalizables
// Exportar schema

@Composable
fun AddHabitScreen(
    habitIt:Long,
    navigateToHabit: () -> Unit,
    viewModel: AddHabitViewModel = hiltViewModel()
){

    val orientation = getOrientation()

    val dataAddHabit = viewModel.dataAddHabit.collectAsStateWithLifecycle().value
    val uiState = viewModel.addHabitUIState.collectAsStateWithLifecycle().value

    LaunchedEffect (Unit){
        viewModel.getData(habitIt)
    }

    ProvideAppBarTitle {
        val title = if (habitIt == -1L) stringResource(R.string.add_habit_topbar_title_add)
        else stringResource(R.string.add_habit_topbar_title_edit)

        LabelLargeText(title, fontSize = 20.sp)
    }

    ProvideAppBarActions {
        if(uiState == AddHabitUIState.Success){
            TextButton(
                onClick = viewModel::saveData
            ) {
                LabelLargeText(
                    stringResource(R.string.add_habit_save),
                    modifier = Modifier.padding(end = spacing6),
                    fontSize = 18.sp
                )
            }
        }
    }

    ProvideAppBarNavigationIcon {
        NavigationIcon()
    }

    //Variables pantalla
    val onClickCard = viewModel::onClickCard
    val onClickGridOption = viewModel::onClickGridOption
    val onClickDialog = viewModel::setDialog
    val onDismissDialog = viewModel::closeDialog
    val onClickTypeHabit = viewModel::onClickTypeHabit
    val onClickWeekly = viewModel::onClickWeekly
    val onMonthNumberSelected = viewModel::monthNumberSelected
    val onDateSelected = viewModel::onClickDate
    val onPickUnit = viewModel::onPickUnit
    val onClickTypeNotification = viewModel::onClickTypeNotification
    val onTimeSelected = viewModel::onTimeSelected
    val onClickDeleteNotification = viewModel::onClickDeleteNotification
    val onCheckedWeeklyChange = viewModel::onCheckedWeeklyChange
    val onCheckedMonthlyChange = viewModel::onCheckedMonthlyChange
    val onClickTypeNotificationResult = viewModel::onClickTypeNotificationResult
    val onClickEditNotification = viewModel::onEditNotification

    //Variables bottomSheet
    val onDismiss = viewModel::closeBottomSheet
    val onAccept = viewModel::onAcceptBottomSheet

    when(orientation){
        Orientation.Portrait -> {
            VerticalAddHabitScreen(
                dataAddHabit = dataAddHabit,
                uiState = uiState,
                navigateToHabit = navigateToHabit,
                onClickCard = onClickCard,
                onClickGridOption = onClickGridOption,
                onClickDialog = onClickDialog,
                onDismissDialog = onDismissDialog,
                onClickTypeHabit = onClickTypeHabit,
                onClickWeekly = onClickWeekly,
                onMonthNumberSelected = onMonthNumberSelected,
                onDateSelected = onDateSelected,
                onPickUnit = onPickUnit,
                onClickTypeNotification = onClickTypeNotification,
                onTimeSelected = onTimeSelected,
                onCheckedWeeklyChange = onCheckedWeeklyChange,
                onCheckedMonthlyChange = onCheckedMonthlyChange,
                onClickDeleteNotification = onClickDeleteNotification,
                onClickTypeNotificationResult = onClickTypeNotificationResult,
                onClickEditNotification = onClickEditNotification,
                onDismiss = onDismiss,
                onAccept = onAccept
            )
        }
        Orientation.Landscape -> {
            HorizontalAddHabitScreen(
                dataAddHabit = dataAddHabit,
                uiState = uiState,
                navigateToHabit = navigateToHabit,
                onClickCard = onClickCard,
                onClickGridOption = onClickGridOption,
                onClickDialog = onClickDialog,
                onDismissDialog = onDismissDialog,
                onClickTypeHabit = onClickTypeHabit,
                onClickWeekly = onClickWeekly,
                onMonthNumberSelected = onMonthNumberSelected,
                onDateSelected = onDateSelected,
                onPickUnit = onPickUnit,
                onClickTypeNotification = onClickTypeNotification,
                onTimeSelected = onTimeSelected,
                onCheckedWeeklyChange = onCheckedWeeklyChange,
                onCheckedMonthlyChange = onCheckedMonthlyChange,
                onClickDeleteNotification = onClickDeleteNotification,
                onClickTypeNotificationResult = onClickTypeNotificationResult,
                onClickEditNotification = onClickEditNotification,
                onDismiss = onDismiss,
                onAccept = onAccept
            )
        }
    }
}