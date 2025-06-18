package aeb.proyecto.timer.components.commom.bottomSheet.pickHabit

import aeb.proyecto.timer.R
import aeb.proyecto.timer.components.commom.loading.TimerLoading
import aeb.proyecto.ui.bottomsheet.CustomBottomSheet
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.LabelMediumText
import aeb.proyecto.ui.text.TitleMediumText
import aeb.proyecto.ui.text.TitleSmallText
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickHabitBottomSheet(
    pickHabitViewModel: PickHabitViewModel = hiltViewModel(),
    onDismiss: () -> Unit = {}
){

    val pickHabitUIState = pickHabitViewModel.pickHabitUIState.collectAsStateWithLifecycle().value

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val scope = rememberCoroutineScope()

    CustomBottomSheet (
        onDismiss = onDismiss,
        sheetState = sheetState
    ){
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = spacing8, horizontal = spacing12),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            when(pickHabitUIState){
                PickHabitUIState.Error -> Unit
                PickHabitUIState.Loading -> {
                    TimerLoading()
                }
                is PickHabitUIState.Success -> {
                    when(pickHabitUIState.habits.size){
                        0 -> {
                            TitleMediumText(
                                stringResource(R.string.timer_linked_habit_no_habit),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(bottom = spacing8)
                            )
                        }
                        else -> {

                        }
                    }
                }
            }
        }
    }

}