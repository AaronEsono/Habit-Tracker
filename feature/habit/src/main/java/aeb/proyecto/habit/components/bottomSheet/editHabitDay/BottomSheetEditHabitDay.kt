package aeb.proyecto.habit.components.bottomSheet.editHabitDay

import aeb.proyecto.habit.R
import aeb.proyecto.habit.components.bottomSheet.editHabitDay.button.ButtonEditDay
import aeb.proyecto.habit.components.bottomSheet.editHabitDay.card.CardEditDay
import aeb.proyecto.habit.components.bottomSheet.editHabitDay.screen.IncompleteDay
import aeb.proyecto.habit.components.bottomSheet.editHabitDay.screen.RestartDay
import aeb.proyecto.habit.components.bottomSheet.editHabitDay.textField.TextFieldEditHabit
import aeb.proyecto.habit.components.bottomSheet.editHabitDay.utils.halfTimesLeft
import aeb.proyecto.habit.components.bottomSheet.editHabitDay.utils.isValidInput
import aeb.proyecto.habit.components.bottomSheet.editHabitDay.utils.timesLeft
import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.room.model.classes.UnitHabit
import aeb.proyecto.room.model.classes.listTime
import aeb.proyecto.ui.bottomsheet.CustomBottomSheet
import aeb.proyecto.ui.constants.getContrastColor
import aeb.proyecto.ui.date.utils.getTextToday
import aeb.proyecto.ui.dimmens.Dimmens.spacing10
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing20
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.regexTextField.IsOnlyDigit
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.LabelLargeText
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetEditHabitDay(
    habit: Habit,
    habitDay: HabitDay,
    onDismiss: () -> Unit = {},
    onRestart:(id:Long,date:LocalDate) -> Unit,
    onClick:(id:Long,date:LocalDate,goalDone:BigDecimal) -> Unit
){

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    val isFinished = remember { habit.goal
        .minus(habitDay.goalDone)
        .setScale(3, RoundingMode.HALF_UP)
        .stripTrailingZeros() ?: BigDecimal.ZERO}

    CustomBottomSheet (
        sheetState = sheetState,
        onDismiss = onDismiss
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = spacing10),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            /**Informacion y cerrar bottomSheet*/
            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){

                CardEditDay {
                    LabelLargeText(
                        getTextToday(habitDay.date)
                    )
                }

                CardEditDay (
                    modifier = Modifier.padding(start = spacing8)
                ){
                    Icon(
                        habit.icon,
                        contentDescription = "edit habit day icon title",
                        tint = Color(habit.color),
                        modifier = Modifier.size(15.dp)
                    )

                    LabelLargeText(
                        habit.name,
                        modifier = Modifier.padding(start = spacing6),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    Icons.Filled.Clear,
                    contentDescription = "edit habit day close button",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(start = spacing8)
                        .size(35.dp)
                        .clickable {
                            coroutineScope.launch {
                                sheetState.hide()
                                onDismiss()
                            }
                        }
                )
            }


            when{
                isFinished <= BigDecimal.ZERO -> {
                    RestartDay(
                        habit = habit,
                        habitDay = habitDay,
                        coroutineScope = coroutineScope,
                        sheetState = sheetState,
                        onRestart = onRestart,
                        onDismiss = onDismiss
                    )
                }
                else -> {
                    IncompleteDay(
                        habit = habit,
                        habitDay = habitDay,
                        onRestart = { id, date ->
                            coroutineScope.launch {
                                onRestart(id,date)
                                sheetState.hide()
                                onDismiss()
                            }
                        },
                        onClick = { id, date, goalDone ->
                            coroutineScope.launch {
                                onClick(id,date,goalDone)
                                sheetState.hide()
                                onDismiss()
                            }
                        }
                    )
                }
            }
        }
    }
}
