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


/**
 * High-order Master Screen Hub for the Habit Mutation Module.
 * Acts as the centralized architectural Smart Controller that establishes lifecycle bindings with the
 * [AddHabitViewModel]. Resolves systemic hardware configurations (Orientation contexts) and handles
 * reactive data rendering across downstream stateless presentation surfaces.
 *
 * @param habitIt Unique persistent identifier mapping the active entity; passes -1L to initiate a creation loop.
 * @param navigateToHabit Asynchronous navigation callback triggered to clear the stack back to parent dashboards.
 * @param viewModel Central business logic state tracker injected smoothly via Hilt.
 */
@Composable
fun AddHabitScreen(
    habitIt:Long,
    navigateToHabit: () -> Unit,
    viewModel: AddHabitViewModel = hiltViewModel()
){

    // Audit active hardware ecosystem configurations dynamically
    val orientation = getOrientation()

    // Collect asynchronous flow states safely tracking the lifecycle checkpoints of the host activity
    val dataAddHabit = viewModel.dataAddHabit.collectAsStateWithLifecycle().value
    val uiState = viewModel.addHabitUIState.collectAsStateWithLifecycle().value

    // Trigger initial data acquisition pipeline on launch without blockades
    LaunchedEffect (Unit){
        viewModel.getData(habitIt)
    }

    // ============================================================================
    // CONTEXTUAL COMPOSITION LOCAL INJECTIONS (SYSTEM APP BAR COORDINATION)
    // ============================================================================
    ProvideAppBarTitle {
        val title = if (habitIt == -1L) stringResource(R.string.add_habit_topbar_title_add)
        else stringResource(R.string.add_habit_topbar_title_edit)

        LabelLargeText(title, fontSize = 20.sp)
    }

    ProvideAppBarActions {
        // Expose persistence committing controls exclusively upon validating state integrity guarantees
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

    // ============================================================================
    // PROGRAMMATIC LINEAR FUNCTIONAL REFERENCE BRIDGES (LAMBDA ROUTERS)
    // ============================================================================
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

    // Persistent Bottom Sheet structural event binding maps
    val onDismiss = viewModel::closeBottomSheet
    val onAccept = viewModel::onAcceptBottomSheet

    // ============================================================================
    // DYNAMIC WORKSPACE ROUTING SYSTEM BASED ON ACTIVE DEVICE ORIENTATION
    // ============================================================================
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