package aeb.proyecto.habittracker.ui.screens.importHabit.importComponents

import aeb.proyecto.habittracker.data.model.state.ImportState
import aeb.proyecto.habittracker.ui.components.bottomSheets.BottomSheetGeneral
import aeb.proyecto.habittracker.ui.components.bottomSheets.BottomSheetPassword
import aeb.proyecto.habittracker.utils.ColorsTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign

@Composable
fun ImportHabitComponents(
    uiState: ImportState,
    onDismiss: () -> Unit,
    onDismissPassword: () -> Unit = {},
    onAcceptGeneral: () -> Unit = {},
    onAcceptPassword: (String) -> Unit
) {

    if (uiState.showGeneralDx) {
        BottomSheetGeneral(
            title = uiState.titleDx,
            subtitle = uiState.subtitleDx,
            colorButton = ColorsTheme.terciaryColorApp,
            textAlign = TextAlign.Center,
            titleAccept = uiState.titleButton,
            onDismiss = { onDismiss() },
            onAccept = { onAcceptGeneral() }
        )
    }

    if (uiState.dxPassword) {
        BottomSheetPassword(
            onDismiss = { onDismissPassword() },
            onAccept = { onAcceptPassword(it) }
        )
    }

}