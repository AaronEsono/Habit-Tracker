package aeb.proyecto.statistics.components.common.calendar

import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.text.LabelMediumText
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import java.time.LocalDate

// Poner diferentes tipos de tonalidad dependiendo si el habito esta hecho al completo o no
// Poner la cantidad exacta, si se ha hecho, en los dias correspondientes

@Composable
fun CalendarItem(
    modifier: Modifier = Modifier,
    day: LocalDate,
    monthSelected: LocalDate = LocalDate.now(),
    habitWithDay: HabitWithDay? = null
){

    val goalDone = remember (habitWithDay){
        habitWithDay?.day?.goalDone?.toFloat() ?: 0f
    }

    // Arreglar esto, el alpha no se aplica
    val colorHabit = remember(habitWithDay) {
        val habit = habitWithDay?.habit
        val goal = habit?.goal?.toFloat() ?: 0f
        val baseColor = Color(habit?.color ?: 0)

        if (goal >= goalDone) baseColor else baseColor.copy(alpha = 0.5f)
    }

    val isInMonth = remember (day,monthSelected){
        day.month == monthSelected.month
    }

    val color = if (goalDone <= 0) MaterialTheme.colorScheme.background else colorHabit

    Box(
        modifier = modifier
            .alpha(if (isInMonth) 1f else 0.20f)
            .aspectRatio(1f)
            .clip(RoundedCornerShape(spacing12))
            .background(color),
        contentAlignment = Alignment.Center
    ){
        LabelMediumText(
            day.dayOfMonth.toString()
        )
    }

}