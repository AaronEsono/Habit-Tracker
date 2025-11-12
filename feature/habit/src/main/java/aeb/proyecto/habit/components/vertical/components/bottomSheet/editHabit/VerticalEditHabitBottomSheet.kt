package aeb.proyecto.habit.components.vertical.components.bottomSheet.editHabit

import aeb.proyecto.habit.components.common.bottomSheet.editHabit.buttons.ButtonsRow
import aeb.proyecto.habit.components.common.bottomSheet.editHabit.card.daily.DailyHabitCard
import aeb.proyecto.habit.components.common.bottomSheet.editHabit.card.monthly.MonthlyHabitCard
import aeb.proyecto.habit.components.common.bottomSheet.editHabit.card.recurring.RecurringHabitCard
import aeb.proyecto.habit.components.common.bottomSheet.editHabit.card.weekly.WeeklyHabitCard
import aeb.proyecto.habit.components.common.bottomSheet.editHabit.state.EditHabitState
import aeb.proyecto.habit.components.common.loading.HabitLoading
import aeb.proyecto.habit.model.TypeBottomSheet
import aeb.proyecto.room.model.classes.TypeHabit
import aeb.proyecto.ui.bottomsheet.CustomBottomSheet
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerticalEditHabitBottomSheet(
    idHabit:Long,
    verticalEditHabitVM: VerticalEditHabitVM = hiltViewModel(),
    onDismiss: (typeBottomSheet: TypeBottomSheet) -> Unit = {},
    onClickEdit: (id:Long) -> Unit = {}
){

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        verticalEditHabitVM.getIdHabit(idHabit)
    }

    val bottomSheetState = verticalEditHabitVM.bottomSheetState.collectAsStateWithLifecycle().value

    CustomBottomSheet (
        sheetState = sheetState,
        onDismiss = { onDismiss(TypeBottomSheet.EditHabit()) },
    ){
        // Cabecera
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
                Column (
                    modifier = Modifier.padding(horizontal = spacing16)
                ){
                    when(bottomSheetState.habit.typeHabit){
                        TypeHabit.Daily -> {
                            if(bottomSheetState.habit.typeHabit is TypeHabit.Daily){
                                DailyHabitCard(
                                    habit = bottomSheetState.habit,
                                    onDismissBottomSheet = {
                                        coroutineScope.launch {
                                            sheetState.hide()
                                            onDismiss(TypeBottomSheet.EditHabit())
                                        }
                                    }
                                )
                            }
                        }
                        is TypeHabit.Monthly -> {
                            if(bottomSheetState.habit.typeHabit is TypeHabit.Monthly){
                                MonthlyHabitCard(
                                    habit = bottomSheetState.habit,
                                    onDismissBottomSheet = {
                                        coroutineScope.launch {
                                            sheetState.hide()
                                            onDismiss(TypeBottomSheet.EditHabit())
                                        }
                                    }
                                )
                            }
                        }
                        is TypeHabit.Recurring -> {
                            if(bottomSheetState.habit.typeHabit is TypeHabit.Recurring){
                                RecurringHabitCard(
                                    habit = bottomSheetState.habit,
                                    onDismissBottomSheet = {
                                        coroutineScope.launch {
                                            sheetState.hide()
                                            onDismiss(TypeBottomSheet.EditHabit())
                                        }
                                    }
                                )
                            }
                        }
                        is TypeHabit.Weekly -> {
                            if (bottomSheetState.habit.typeHabit is TypeHabit.Weekly){
                                WeeklyHabitCard(
                                    habit = bottomSheetState.habit,
                                    onDismissBottomSheet = {
                                        coroutineScope.launch {
                                            sheetState.hide()
                                            onDismiss(TypeBottomSheet.EditHabit())
                                        }
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.padding(vertical = spacing4))

                    //Botones
                    ButtonsRow(
                        onClickEdit = {
                            coroutineScope.launch {
                                onClickEdit(bottomSheetState.habit.id)
                                sheetState.hide()
                                onDismiss(TypeBottomSheet.EditHabit())
                            }
                        }
                    )

                }
            }
        }
    }
}