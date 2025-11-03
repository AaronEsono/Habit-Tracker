package aeb.proyecto.habit.components.vertical.components.bottomSheet.editHabit

import aeb.proyecto.habit.model.TypeBottomSheet
import aeb.proyecto.ui.bottomsheet.CustomBottomSheet
import aeb.proyecto.ui.text.LabelLargeText
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerticalEditHabitBottomSheet(
    onDismiss: (typeBottomSheet: TypeBottomSheet) -> Unit = {}
){

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    CustomBottomSheet (
        onDismiss = { onDismiss(TypeBottomSheet.EditHabit()) },
    ){
        LabelLargeText("Hola")
    }
}