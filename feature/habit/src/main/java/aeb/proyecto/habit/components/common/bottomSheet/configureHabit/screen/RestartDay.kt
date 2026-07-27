package aeb.proyecto.habit.components.common.bottomSheet.configureHabit.screen

import aeb.proyecto.habit.R
import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.ui.constants.getContrastColor
import aeb.proyecto.ui.dimmens.Dimmens.spacing10
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing14
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * Tactical confirmation overlay for habit progress reset operations.
 * Coordinates the visual dismissal of the bottom sheet with the asynchronous
 * data-purge transactional event.
 *
 * @param modifier Structural Modifier ecosystem parameters.
 * @param habitWithDay Data model carrying the target habit entity and the operational date context.
 * @param coroutineScope Host scope for managing asynchronous dismiss/restart lifecycle events.
 * @param onDismiss Callback to handle the component dismissal flow.
 * @param sheetState Reference to the parent BottomSheet host state for transactional UI control.
 * @param onRestart Callback action triggering the database purge for a specific habit ID and date.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestartDay(
    modifier: Modifier = Modifier,
    habitWithDay: HabitWithDay,
    coroutineScope: CoroutineScope,
    onDismiss: () -> Unit = {},
    sheetState: SheetState,
    onRestart:(id:Long,date: LocalDate) -> Unit,
){

    // Confirmation header
    LabelLargeText(
        stringResource(R.string.habit_restart_day),
        modifier = Modifier
            .padding(
                bottom = spacing4,
                top = spacing14
            ),
        fontSize = 16.sp
    )

    // Transactional action row
    Row (
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = spacing12, top = spacing10),
        verticalAlignment = Alignment.CenterVertically
    ){

        // Cancel/Dismiss action
        CustomRipple {
            OutlinedButton(
                modifier = Modifier
                    .weight(1f)
                    .testTag("habit_cancel_button_restart_day"),
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

        // Confirm reset action with dynamic color branding
        CustomRipple {
            Button(
                modifier = Modifier
                    .padding(start = spacing10)
                    .weight(1f)
                    .testTag("habit_confirm_button_restart_day"),
                onClick = {
                    coroutineScope.launch {
                        coroutineScope.launch {
                            onRestart(habitWithDay.habit.id,habitWithDay.day.date)
                            sheetState.hide()
                            onDismiss()
                        }
                    }
                },
                shape = RoundedCornerShape(spacing8),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(habitWithDay.habit.color)
                )
            ) {
                LabelLargeText(
                    stringResource(R.string.habit_accept),
                    color = getContrastColor(Color(habitWithDay.habit.color))
                )
            }
        }
    }
}