package aeb.proyecto.timer.components.common.habitLinked.states

import aeb.proyecto.timer.R
import aeb.proyecto.ui.text.LabelLargeText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp

/**
 * Displays a placeholder label when no habit is currently linked to the timer.
 * Used to guide the user to perform an action or inform them of the current status.
 *
 * @param modifier Applied to the [LabelLargeText] component.
 */
@Composable
fun NoLinkedHabit(
    modifier: Modifier = Modifier
){
    LabelLargeText(
        modifier = modifier,
        text = stringResource(R.string.timer_linked_habit_no_data),
        fontSize = 14.sp,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}