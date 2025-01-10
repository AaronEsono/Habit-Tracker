package aeb.proyecto.habittracker.ui.screens.saveHabit.saveHabitComponents


import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.ui.components.bottomSheets.BottomSheetGeneral
import aeb.proyecto.habittracker.ui.screens.saveHabit.UiStateSaveHabits
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun SaveHabitScreenStates(state: UiStateSaveHabits,onClick:() -> Unit, onDismiss:() -> Unit){

    if(state.showDx){
        BottomSheetGeneral(
            title = R.string.general_dx_attention,
            subtitle = state.dxInfo.subtitle,
            showCancel = state.dxInfo.showCancel,
            colorTextAccept = MaterialTheme.colorScheme.inverseOnSurface,
            colorIconAccept = MaterialTheme.colorScheme.inverseOnSurface,
            onDismiss = {onDismiss()},
            onAccept = {onClick()},
            onCancel = {onDismiss()}
        )
    }
}