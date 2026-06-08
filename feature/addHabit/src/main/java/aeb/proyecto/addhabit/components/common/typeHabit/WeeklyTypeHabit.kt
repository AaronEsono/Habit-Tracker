package aeb.proyecto.addhabit.components.common.typeHabit

import aeb.proyecto.addhabit.R
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.LabelMediumText
import aeb.proyecto.ui.text.LabelSmallText
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ============================================================================
// WEEKLY VIEWPORT ARCHITECTURE METRICS
// ============================================================================

/**
 * Immutable registry tracking standard day iteration constraints for weekly matrix layouts.
 */
val numberOfDaysWeek = listOf(1,2,3,4,5,6,7)

/**
 * A tailored configuration layout container designed to manage weekly target behaviors.
 * Coordinates an animated workflow shifting reactively between a global weekly cumulative target switch
 * and a specific day-count button selection track.
 *
 * @param modifier Structural [Modifier] assembly to alter or extend the layout constraints.
 * @param weeklyGoal Core state flag indicating whether the strategy defaults to an open weekly cumulative ceiling.
 * @param numberSelected Currently active configuration boundary marker identifying the focused picked weekday count integer.
 * @param colorSelected The primary active design [Color] token allocated to paint structural focus indicator highlights.
 * @param contrastColor An accessible high-contrast [Color] reference targeted to tint internal text layers during active selections.
 * @param onClickWeekly Event callback lambda transmitting selected day metrics downstream.
 * @param onCheckedChange Interactive toggle action callback hub tracking global switch configuration shifts.
 */
@Composable
fun WeeklyTypeHabit(
    modifier: Modifier = Modifier,
    weeklyGoal:Boolean,
    numberSelected:Int,
    colorSelected:Color,
    contrastColor:Color,
    onClickWeekly: (Int) -> Unit = {},
    onCheckedChange: () -> Unit = {}
){

    Column (
        modifier = modifier,
    ){

        // SECTION 1: GLOBAL WEEKLY STRATEGY TOGGLE HEADER ROW
        Row (
            modifier = Modifier.fillMaxWidth().padding(vertical = spacing8),
            verticalAlignment = Alignment.CenterVertically
        ){
            Column (
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ){
                LabelLargeText(stringResource(R.string.add_habit_weekly_goal))

                LabelSmallText(stringResource(R.string.add_habit_weekly_type_label),
                    color = MaterialTheme.colorScheme.outline)
            }

            Switch(
                modifier = Modifier.padding(start = spacing8),
                checked = weeklyGoal,
                onCheckedChange = {onCheckedChange()},
                colors = SwitchDefaults.colors(
                    checkedThumbColor = colorSelected,
                    checkedBorderColor = colorSelected
                )
            )
        }

        // SECTION 2: GRANULAR TARGET HORIZONTAL MATRIX TRACK
        AnimatedVisibility(
            visible = !weeklyGoal
        ) {
            Column {
                LabelMediumText(stringResource(R.string.add_habit_weekly_type_title))

                Row (
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ){

                    numberOfDaysWeek.forEach { number ->
                        WeeklyButton(
                            number = number,
                            selected = numberSelected(number, numberSelected),
                            modifier = Modifier.weight(1f),
                            colorSelected = colorSelected,
                            contrastColor = contrastColor,
                            onClick = { onClickWeekly(number) }
                        )
                    }
                }
            }
        }
    }

}

/**
 * An atomic micro-card button component representing an isolated numerical day target within a weekly grid.
 * Shifts its color tokens dynamically between background container systems and custom accent highlight slots
 * based on its selection state.
 *
 * @param modifier Structural [Modifier] assembly to alter or extend the layout constraints.
 * @param number The core primitive integer identifier displayed as the main text label. Defaults to 1.
 * @param selected Operational flag determining whether this specific day node holds the active user focus.
 * @param colorSelected The primary active design [Color] token allocated to paint the container when selected is true.
 * @param contrastColor An accessible high-contrast [Color] reference targeted to tint text strings inside selected states.
 * @param onClick Interactive action callback lambda targeted to emit user selection events upstream.
 */
@Composable
fun WeeklyButton(
    modifier: Modifier = Modifier,
    number:Int = 1,
    selected:Boolean,
    colorSelected:Color,
    contrastColor:Color,
    onClick: () -> Unit = {}
) {
    val containerColor =
        if (selected) colorSelected else MaterialTheme.colorScheme.secondaryContainer
    val numberColor =
        if (selected) contrastColor else MaterialTheme.colorScheme.onSurface

    CustomRipple {
        ElevatedCard(
            modifier = modifier
                .padding(horizontal = spacing2)
                .height(40.dp),
            onClick = onClick,
            shape = RoundedCornerShape(spacing8),
            colors = CardDefaults.cardColors(
                containerColor = containerColor
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LabelLargeText(number.toString(), fontSize = 18.sp, color = numberColor)
            }
        }
    }
}

/**
 * Evaluates operational coordinate equivalences between individual node values and active target states.
 *
 * @param number The current baseline element integer to evaluate.
 * @param selected The active focused configuration state pointer index.
 * @return True if coordinates align perfectly, false otherwise.
 */
fun numberSelected(number:Int, selected:Int):Boolean{
    return number == selected
}