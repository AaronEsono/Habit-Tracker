package aeb.proyecto.habit.components.vertical.components.bottomSheet.configureHabit

import aeb.proyecto.habit.components.common.bottomSheet.configureHabit.card.CardDayConfigureHabit
import aeb.proyecto.habit.components.common.bottomSheet.configureHabit.screen.IncompleteDay
import aeb.proyecto.habit.components.common.bottomSheet.configureHabit.screen.RestartDay
import aeb.proyecto.habit.model.TypeBottomSheet
import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.ui.bottomsheet.CustomBottomSheet
import aeb.proyecto.ui.date.utils.getTextToday
import aeb.proyecto.ui.dimmens.Dimmens.spacing10
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelLargeText
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerticalConfigureHabitBottomSheet(
    habitWithDay: HabitWithDay,
    onDismiss: (typeBottomSheet: TypeBottomSheet) -> Unit = {},
    onRestart:(id:Long,date: LocalDate) -> Unit,
    onClickTimer: (Triple<Long,String, BigDecimal>) -> Unit,
    onClick:(id:Long, date: LocalDate, goalDone: BigDecimal) -> Unit
){

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    val isFinished = remember { habitWithDay.habit.goal
        .minus(habitWithDay.day.goalDone)
        .setScale(3, RoundingMode.HALF_UP)
        .stripTrailingZeros() ?: BigDecimal.ZERO}


    CustomBottomSheet (
        sheetState = sheetState,
        onDismiss = { onDismiss(TypeBottomSheet.ConfigureHabit(enabled = false))}
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

                CardDayConfigureHabit {
                    LabelLargeText(
                        getTextToday(habitWithDay.day.date)
                    )
                }

                CardDayConfigureHabit (
                    modifier = Modifier.padding(start = spacing8)
                ){
                    Icon(
                        habitWithDay.habit.icon,
                        contentDescription = "edit habit day icon title",
                        tint = Color(habitWithDay.habit.color),
                        modifier = Modifier.size(15.dp)
                    )

                    LabelLargeText(
                        habitWithDay.habit.name,
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
                                onDismiss(TypeBottomSheet.ConfigureHabit(enabled = false))
                            }
                        }
                )
            }


            when{
                isFinished <= BigDecimal.ZERO -> {
                    RestartDay(
                        habitWithDay = habitWithDay,
                        coroutineScope = coroutineScope,
                        sheetState = sheetState,
                        onRestart = onRestart,
                        onDismiss = {onDismiss(TypeBottomSheet.ConfigureHabit(enabled = false))}
                    )
                }
                else -> {
                    IncompleteDay(
                        habitWithDay = habitWithDay,
                        onClickTimer = { (id,date,leftTimes) ->
                            coroutineScope.launch {
                                onClickTimer(Triple(id,date,leftTimes))
                                sheetState.hide()
                                onDismiss(TypeBottomSheet.ConfigureHabit(enabled = false))
                            }
                        },
                        onRestart = { id, date ->
                            coroutineScope.launch {
                                onRestart(id,date)
                                sheetState.hide()
                                onDismiss(TypeBottomSheet.ConfigureHabit(enabled = false))
                            }
                        },
                        onClick = { id, date, goalDone ->
                            coroutineScope.launch {
                                onClick(id,date,goalDone)
                                sheetState.hide()
                                onDismiss(TypeBottomSheet.ConfigureHabit(enabled = false))
                            }
                        }
                    )
                }
            }
        }
    }

}