package aeb.proyecto.habittracker.ui.screens.importHabit.importComponents

import aeb.proyecto.habittracker.data.model.state.ImportState
import aeb.proyecto.habittracker.ui.components.bottomSheets.BottomSheetGeneral
import aeb.proyecto.habittracker.utils.ColorsTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign

@Composable
fun ImportHabitComponents(uiState: ImportState, onDismiss: () -> Unit, onAccept: () -> Unit = {}) {

    if(uiState.showGeneralDx){
        BottomSheetGeneral(
            title = uiState.titleDx,
            subtitle = uiState.subtitleDx,
            color = ColorsTheme.terciaryColorApp,
            textAlign = TextAlign.Center,
            titleAccept = uiState.titleButton,
            onDismiss = {onDismiss()},
            onAccept = { onAccept() }
        )
    }
}