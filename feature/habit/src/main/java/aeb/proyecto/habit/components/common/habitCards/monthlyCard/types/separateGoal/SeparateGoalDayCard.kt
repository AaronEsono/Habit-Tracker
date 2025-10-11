package aeb.proyecto.habit.components.common.habitCards.monthlyCard.types.separateGoal

import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.LabelMediumText
import aeb.proyecto.ui.text.LabelSmallText
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import java.time.LocalDate

// Poner modo desactivado

@Composable
fun SeparateGoalDayCard(
    modifier: Modifier = Modifier,
    day: LocalDate? = LocalDate.now(),
    habitDay: HabitDay?
){

    Box(
        modifier = modifier
            .padding(horizontal = spacing6, vertical = spacing2)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
            .aspectRatio(1f)
    ){
        LabelMediumText(
            day?.dayOfMonth.toString(),
            modifier = Modifier.align(Alignment.Center)
        )
    }

}