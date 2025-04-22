package aeb.proyecto.habit.components.bottomSheet.editHabitDay.screen

import aeb.proyecto.habit.R
import aeb.proyecto.habit.components.bottomSheet.editHabitDay.utils.isValidInput
import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.ui.constants.getContrastColor
import aeb.proyecto.ui.dimmens.Dimmens.spacing10
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing14
import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.dimmens.Dimmens.spacing20
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.LabelLargeText
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestartDay(
    habit: Habit,
    habitDay: HabitDay,
    coroutineScope: CoroutineScope,
    onDismiss: () -> Unit = {},
    sheetState: SheetState,
    onRestart:(id:Long,date: LocalDate) -> Unit,
){


    LabelLargeText(
        stringResource(R.string.habit_restart_day),
        modifier = Modifier
            .padding(
                bottom = spacing10,
                top = spacing14
            ),
        fontSize = 16.sp
    )

    /** Botones */
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = spacing12, top = spacing10),
        verticalAlignment = Alignment.CenterVertically
    ){

        CustomRipple {
            OutlinedButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    coroutineScope.launch {
                        sheetState.hide()
                        onDismiss()
                    }
                },
                shape = RoundedCornerShape(spacing8),
            ) {
                LabelLargeText(
                    stringResource(R.string.habit_cancel)
                )
            }
        }

        CustomRipple {
            Button(
                modifier = Modifier
                    .padding(start = spacing10)
                    .weight(1f),
                onClick = {
                    coroutineScope.launch {
                        coroutineScope.launch {
                            onRestart(habit.id,habitDay.date)
                            sheetState.hide()
                            onDismiss()
                        }
                    }
                },
                shape = RoundedCornerShape(spacing8),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(habit.color)
                )
            ) {
                LabelLargeText(
                    stringResource(R.string.habit_accept),
                    color = getContrastColor(Color(habit.color))
                )
            }
        }
    }
}