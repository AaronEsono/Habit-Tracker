package aeb.proyecto.statistics.components.vertical.screens

import aeb.proyecto.room.entities.relations.HabitWithDailyHabit
import aeb.proyecto.statistics.components.common.header.HeaderTitle
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun VerticalHabitSelectedScreen(
    habitSelected: HabitWithDailyHabit
){
    Column (
        modifier = Modifier.fillMaxSize()
            .padding(top = spacing6)
    ){

        HeaderTitle(
            habit = habitSelected.habit,
            modifier = Modifier.fillMaxHeight(0.07f)
        )

    }
}