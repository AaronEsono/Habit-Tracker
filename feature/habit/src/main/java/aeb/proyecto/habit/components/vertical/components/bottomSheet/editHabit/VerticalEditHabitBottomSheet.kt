package aeb.proyecto.habit.components.vertical.components.bottomSheet.editHabit

import aeb.proyecto.habit.components.common.bottomSheet.editHabit.card.DailyHabitCard
import aeb.proyecto.habit.components.common.bottomSheet.editHabit.state.EditHabitState
import aeb.proyecto.habit.components.common.loading.HabitLoading
import aeb.proyecto.habit.model.TypeBottomSheet
import aeb.proyecto.room.model.classes.TypeHabit
import aeb.proyecto.ui.bottomsheet.CustomBottomSheet
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelLargeText
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerticalEditHabitBottomSheet(
    idHabit:Long,
    verticalEditHabitVM: VerticalEditHabitVM = hiltViewModel(),
    onDismiss: (typeBottomSheet: TypeBottomSheet) -> Unit = {}
){

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        verticalEditHabitVM.getIdHabit(idHabit)
    }

    val bottomSheetState = verticalEditHabitVM.bottomSheetState.collectAsStateWithLifecycle().value

    //TODO NO VA AL EL SUCCESS, ARREGLAR

    CustomBottomSheet (
        sheetState = sheetState,
        onDismiss = { onDismiss(TypeBottomSheet.EditHabit()) },
    ){
        when(bottomSheetState){
            is EditHabitState.Error -> Unit
            EditHabitState.Loading -> {
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(vertical = spacing8).height(200.dp)
                ){
                    HabitLoading()
                }
            }
            is EditHabitState.Success -> {
                Column {
                    when(bottomSheetState.habit.typeHabit){
                        TypeHabit.Daily -> {
                            DailyHabitCard(
                                habit = bottomSheetState.habit,
                                onDismissBottomSheet = {  }
                            )
                        }
                        is TypeHabit.Monthly -> TODO()
                        is TypeHabit.Recurring -> TODO()
                        is TypeHabit.Weekly -> TODO()
                    }
                }
            }
        }
    }
}