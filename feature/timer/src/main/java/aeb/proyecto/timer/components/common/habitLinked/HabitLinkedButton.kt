package aeb.proyecto.timer.components.common.habitLinked

import aeb.proyecto.timer.components.common.habitLinked.states.LinkedHabit
import aeb.proyecto.timer.components.common.habitLinked.states.NoLinkedHabit
import aeb.proyecto.timer.model.HabitLinkedState
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
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
    onClickCross:()->Unit = {}
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
                border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline),
        ){

            Row (
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ){
                when(linkedState){
                    is HabitLinkedState.Data -> {
                        LinkedHabit(
                            linkedState = linkedState,
                            onClickCross = onClickCross
                        )
                    }
                    HabitLinkedState.NoData -> {
                        NoLinkedHabit(
                            modifier = Modifier.padding(start = spacing12)
                        )
                    }
                }
            }
        }
    }
}