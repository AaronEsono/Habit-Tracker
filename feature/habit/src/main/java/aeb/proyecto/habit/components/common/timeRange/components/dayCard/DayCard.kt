package aeb.proyecto.habit.components.common.timeRange.components.dayCard

import aeb.proyecto.ui.date.utils.getAvr
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelMediumText
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import java.time.LocalDate

/**
 * A selectable card component representing a single day in a date range picker.
 *
 * It provides visual feedback through smooth color transitions when selected or
 * unselected, using custom modifier extensions to handle background and text state.
 *
 * @param modifier Modifier for external constraints.
 * @param date The [LocalDate] represented by this card.
 * @param isSelected Whether the current card is the active selection.
 * @param onClick Callback when the card is clicked, returning the date and selection status.
 */
@Composable
fun DayCard(
    modifier:Modifier = Modifier,
    date:LocalDate,
    isSelected:Boolean = false,
    onClick: (LocalDate, Boolean) -> Unit = {_,_ -> }
){

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(spacing12))
            .boxBackgroundSelected(isSelected)
            .padding(top = spacing4)
            .clickable { onClick(date,false) }
    ) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Day of week label (e.g., "Mon")
            LabelMediumText(stringResource(getAvr(date.dayOfWeek)), color = textSelected(isSelected))

            // Date number box (e.g., "09")
            Box(
                modifier = Modifier
                    .padding(top = spacing2)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = spacing8, topEnd = spacing8))
                    .backgroundTextSelected(isSelected),
                contentAlignment = Alignment.Center
            ) {
                LabelMediumText(
                    date.dayOfMonth.toString(),
                    modifier = Modifier.align(Alignment.Center).padding(top = spacing6,bottom = spacing8),
                    color = textSelected(isSelected)
                )
            }
        }
    }
}

// --- Modifiers & Utility Animations ---

/** Animates the background color of the container based on selection state. */
@Composable
fun Modifier.boxBackgroundSelected(
    isSelected: Boolean
): Modifier {
    val targetColor = if (isSelected) {
        MaterialTheme.colorScheme.onSurface
    } else {
        MaterialTheme.colorScheme.primary
    }
    return this.background(animatorSelected(targetColor = targetColor, label = "BoxBackgroundAnimation"))
}

/** Animates the background color of the day number area based on selection state. */
@Composable
fun Modifier.backgroundTextSelected(
    isSelected: Boolean
): Modifier {
    val targetColor = if (isSelected) {
        MaterialTheme.colorScheme.onBackground
    } else {
        MaterialTheme.colorScheme.secondaryContainer
    }
    return this.background(animatorSelected(targetColor = targetColor, label = "BoxBackgroundAnimation"))
}

/** Returns an animated color for text based on selection state. */
@Composable
fun textSelected(
    isSelected: Boolean
): Color {
    val targetColor = if (isSelected) {
        MaterialTheme.colorScheme.inverseOnSurface
    } else {
        MaterialTheme.colorScheme.onSurface
    }
    return animatorSelected(targetColor = targetColor, label = "TextColorAnimation")
}

/** Helper function to wrap the color animation logic. */
@Composable
fun animatorSelected(
    targetColor:Color,
    label:String
):Color{
    return animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = label
    ).value
}