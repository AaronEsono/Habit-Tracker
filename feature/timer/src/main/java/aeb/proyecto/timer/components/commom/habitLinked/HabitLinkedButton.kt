package aeb.proyecto.timer.components.commom.habitLinked

import aeb.proyecto.timer.components.commom.habitLinked.states.LinkedHabit
import aeb.proyecto.timer.components.commom.habitLinked.states.NoLinkedHabit
import aeb.proyecto.timer.model.HabitLinkedState
import aeb.proyecto.ui.dimmens.Dimmens
import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.dimmens.Dimmens.spacing24
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.ripple.CustomRipple
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HabitLinkedButton(
    modifier: Modifier = Modifier,
    linkedState: HabitLinkedState,
    onClickHabitLinkedButton:() -> Unit,
){
    CustomRipple {
            Card(
                modifier = modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(spacing8),
                onClick = onClickHabitLinkedButton,
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                elevation = CardDefaults.elevatedCardElevation(
                    defaultElevation = spacing4
                )
        ){

            Row (
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ){
                when(linkedState){
                    is HabitLinkedState.Data -> {
                        LinkedHabit()
                    }
                    HabitLinkedState.NoData -> {
                        NoLinkedHabit(
                            modifier = Modifier.padding(start = spacing24)
                        )
                    }
                }
            }
        }
    }
}