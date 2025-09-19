package aeb.proyecto.timer.components.common.bottomSheet.pickHabit.components

import aeb.proyecto.room.entities.Habit
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelLargeText
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HabitCard(
    modifier: Modifier = Modifier,
    habit:Habit,
    selected:Boolean,
    onClickHabit:()->Unit = {}
){

    Card(
        modifier = modifier.fillMaxWidth().padding(bottom = spacing8),
        colors = CardDefaults.elevatedCardColors(
            containerColor = if(selected) MaterialTheme.colorScheme.scrim
            else MaterialTheme.colorScheme.primary
        ),
        onClick = onClickHabit
    ) {
        Row (
            modifier = Modifier.padding(horizontal = spacing12, vertical = spacing8),
            verticalAlignment = Alignment.CenterVertically
        ){

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(spacing8))
                    .background(Color(habit.color).copy(alpha = 0.75f),
                        RoundedCornerShape(spacing8)),
                contentAlignment = Alignment.Center
            ){
                Icon(
                    habit.icon,
                    contentDescription = "habit icon pick habit",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(spacing8).fillMaxSize(1f)
                )
            }

            LabelLargeText(
                text = habit.name,
                modifier = Modifier.padding(start = spacing12, end = spacing16),
                maxLines = 1,
                fontSize = 16.sp,
                overflow = TextOverflow.Ellipsis,
                color = if(selected) MaterialTheme.colorScheme.inverseOnSurface
                else MaterialTheme.colorScheme.onSurface
            )

            if(selected){
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    Icons.Filled.Check,
                    contentDescription = "habit icon pick habit",
                    tint = MaterialTheme.colorScheme.inverseOnSurface,
                    modifier = Modifier.padding(end = spacing8)
                        .size(30.dp)
                )
            }
        }
    }

}