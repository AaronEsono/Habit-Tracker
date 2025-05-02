package aeb.proyecto.timer.components.screens

import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.timer.R
import aeb.proyecto.timer.TimerSelectedState
import aeb.proyecto.timer.components.timerPicker.TimerPicker
import aeb.proyecto.ui.constants.getContrastColor
import aeb.proyecto.ui.date.utils.getTextToday
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing20
import aeb.proyecto.ui.dimmens.Dimmens.spacing32
import aeb.proyecto.ui.dimmens.Dimmens.spacing36
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing40
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.LabelMediumText
import aeb.proyecto.ui.text.TitleLargeText
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RelojScreen(
    habitWithDay: HabitWithDay,
    timerSelected: TimerSelectedState,
    timeLeft: String,
    onHourChange: (String) -> Unit = {},
    onMinuteChange: (String) -> Unit = {},
    onSecondChange: (String) -> Unit = {}
){
    Column {

        Row (
            verticalAlignment = Alignment.CenterVertically
        ){
            ElevatedCard(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceTint
                ),
                shape = RoundedCornerShape(spacing8),
                modifier = Modifier
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.outline,
                        RoundedCornerShape(spacing8)
                    )
            ) {
                LabelLargeText(getTextToday(habitWithDay.day.date),
                    modifier = Modifier.padding(horizontal = spacing8, vertical = spacing4))
            }

            ElevatedCard(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceTint
                ),
                shape = RoundedCornerShape(spacing8),
                modifier = Modifier
                    .padding(start = spacing8)
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.outline,
                        RoundedCornerShape(spacing8)
                    )
            ) {
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = spacing8, vertical = spacing4)
                ){

                    Icon(
                        habitWithDay.habit.icon,
                        contentDescription = "timer icon habit",
                        tint = Color(habitWithDay.habit.color),
                        modifier = Modifier.size(20.dp)
                    )

                    LabelLargeText(habitWithDay.habit.name,
                        modifier = Modifier.padding(start = spacing4))
                }
            }
        }

        ElevatedCard(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceTint
            ),
            shape = RoundedCornerShape(spacing8),
            modifier = Modifier
                .padding(top = spacing8)
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.outline,
                    RoundedCornerShape(spacing8)
                )
        ) {
            LabelLargeText(stringResource(R.string.timer_left,timeLeft),
                modifier = Modifier.padding(horizontal = spacing8, vertical = spacing4))
        }


        TimerPicker(
            modifier = Modifier.padding(top = spacing36),
            timerSelected = timerSelected,
            onHourChange = onHourChange,
            onMinuteChange = onMinuteChange,
            onSecondChange = onSecondChange
        )


        Row (
            modifier = Modifier.fillMaxWidth().padding(top = spacing40),
            horizontalArrangement = Arrangement.Center
        ){

            CustomRipple {
                Button(
                    onClick = {  },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(habitWithDay.habit.color)
                    ),
                    shape = RoundedCornerShape(spacing12),
                ) {
                    TitleLargeText(
                        stringResource(R.string.timer_start),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = getContrastColor(Color(habitWithDay.habit.color)),
                        modifier = Modifier.padding(horizontal = spacing12, vertical = spacing4)
                    )
                }
            }
        }
    }
}