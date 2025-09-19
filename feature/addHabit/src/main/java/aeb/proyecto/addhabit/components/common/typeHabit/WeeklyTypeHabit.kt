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

val numberOfDaysWeek = listOf(1,2,3,4,5,6,7)

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

fun numberSelected(number:Int, selected:Int):Boolean{
    return number == selected
}