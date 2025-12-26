package aeb.proyecto.statistics.components.vertical.screens

import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.relations.HabitWithDailyHabit
import aeb.proyecto.statistics.components.common.card.HeaderCard
import aeb.proyecto.statistics.model.StatisticsState
import aeb.proyecto.ui.dimmens.Dimmens.spacing1
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelMediumText
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ContentVerticalStatisticsScreen(
    habits: List<Habit>,
    habitSelected: HabitWithDailyHabit
){

    Column (
        modifier = Modifier.fillMaxSize().padding(top = spacing6)
    ){

        LazyRow(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.075f).padding(horizontal = spacing8),
            horizontalArrangement = Arrangement.spacedBy(spacing8)
        ){
            items(
                habits.size,
                key = {habits[it].id}
            ){ index ->
                HeaderCard(
                    habit = habits[index],
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.padding(top = spacing2))

        HorizontalDivider(color = MaterialTheme.colorScheme.outline, thickness = spacing2)
    }

}