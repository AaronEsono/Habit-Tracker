package aeb.proyecto.statistics.components.common.card

import aeb.proyecto.room.entities.Habit
import aeb.proyecto.ui.dimmens.Dimmens.spacing1
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.BodySmallText
import aeb.proyecto.ui.text.LabelSmallText
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Renders a circular card representing a habit in the statistics header.
 * The card changes its border and background color based on the [selected] state,
 * providing visual feedback for navigation between habits.
 *
 * @param modifier Applied to the column container.
 * @param habit The habit model to display.
 * @param selected Whether this habit is currently the active one.
 * @param onClickCard Callback triggered when the card is clicked, returning the habit ID.
 */
@Composable
fun HeaderCard(
    modifier: Modifier = Modifier,
    habit: Habit,
    selected: Boolean = true,
    onClickCard: (id:Long) -> Unit
){

    val background = if (!selected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.onPrimaryContainer
    }

    val border = if (!selected) {
        MaterialTheme.colorScheme.scrim
    } else {
        MaterialTheme.colorScheme.tertiaryContainer
    }

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ){
        Box(
            modifier = modifier
                .clip(CircleShape)
                .aspectRatio(1f)
                .border(spacing1, border, CircleShape)
                .background(background)
                .semantics {
                    this.selected = selected
                }
                .clickable{
                    onClickCard(habit.id)
                }
                .testTag("statistics_header_card_${habit.id}")
        ){

            Icon(
                imageVector = habit.icon,
                contentDescription = "Icon header",
                modifier = Modifier
                    .fillMaxSize(0.75f)
                    .align(Alignment.Center),
                tint = Color(habit.color)
            )

        }

        LabelSmallText(
            text = habit.name,
            fontSize = 10.sp
        )
    }
}