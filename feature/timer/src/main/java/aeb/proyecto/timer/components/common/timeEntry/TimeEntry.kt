package aeb.proyecto.timer.components.common.timeEntry

import aeb.proyecto.room.entities.relations.TimeEntryWithHabit
import aeb.proyecto.timer.R
import aeb.proyecto.timer.utils.convertToHours
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.LabelMediumText
import aeb.proyecto.ui.text.LabelSmallText
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun TimeEntry(
    modifier: Modifier = Modifier,
    timeEntry: TimeEntryWithHabit,
    lastOne: Boolean = false,
    onClickTimeEntry: (Long) -> Unit = {},
    onClickFavorite: (Long,Boolean) -> Unit = {_,_ ->},
    onClickDelete: (Long) -> Unit = {_ -> },
) {

    val clip = remember (lastOne){
        if(lastOne){
            RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)
        }else{
            RectangleShape
        }
    }

    CustomRipple {
        Row (
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(clip)
                .border(1.dp, MaterialTheme.colorScheme.outline,clip)
                .background(MaterialTheme.colorScheme.primary)
                .clickable(onClick = {onClickTimeEntry(timeEntry.timeEntry.id)})
                .padding(vertical = spacing8, horizontal = spacing12),
            verticalAlignment = Alignment.CenterVertically
        ){
            Column (
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Image(
                    painter = painterResource(
                        when(timeEntry.timeEntry.typeTimer){
                            0 -> {R.drawable.im_stopwatch}
                            1 -> {R.drawable.im_timer}
                            else -> {R.drawable.im_interval}
                        }
                    ),
                    contentDescription = "image time Entry",
                    modifier = Modifier.size(25.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                )
            }

            // T0do el texto
            Column (
                modifier = Modifier.weight(1f)
                    .padding(start = spacing12),
                verticalArrangement = Arrangement.Top
            ){
                LabelMediumText(
                    stringResource(
                        when(timeEntry.timeEntry.typeTimer){
                            0 -> {R.string.timer_segmented_button_stopwatch}
                            1 -> {R.string.timer_segmented_button_timer}
                            else -> {R.string.timer_segmented_button_interval}
                        }
                    )
                )

                when(timeEntry.timeEntry.typeTimer){
                    0 -> Unit
                    1,2 -> {
                        LabelSmallText(
                            stringResource(
                                R.string.timer_entry_time,
                                convertToHours(timeEntry.timeEntry.time ?: 0)
                            )
                        )
                    }
                    else -> Unit
                }

                when(timeEntry.timeEntry.typeTimer){
                    2 -> {
                        LabelSmallText(
                            stringResource(
                                R.string.timer_entry_interval,
                                timeEntry.timeEntry.intervals ?: 1,
                                convertToHours(timeEntry.timeEntry.restTime ?: 0)
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    else -> Unit
                }

                timeEntry.habit?.let { habit ->
                    LabelSmallText(
                        stringResource(
                            R.string.timer_entry_habit,
                            habit.name
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.wrapContentHeight()
            ){
                Crossfade(targetState = timeEntry.timeEntry.favourite, animationSpec = tween(500)) { favorite ->
                    if (favorite) {
                        Image(
                            painterResource(R.drawable.im_favorite),
                            contentDescription = null,
                            modifier = Modifier.size(25.dp)
                                .clickable (
                                    indication = null,
                                    interactionSource = null
                                ){
                                    onClickFavorite(
                                        timeEntry.timeEntry.id,
                                        false
                                    )
                                })
                    } else {
                        Image(
                            painterResource(R.drawable.im_no_favorite),
                            contentDescription = null,
                            modifier = Modifier.size(25.dp)
                                .clickable (
                                    indication = null,
                                    interactionSource = null
                                ){
                                    onClickFavorite(
                                        timeEntry.timeEntry.id,
                                        true
                                    )
                                },
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface))
                    }
                }
            }

            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(start = spacing8).wrapContentHeight(),
            ){
                Icon(
                    Icons.Filled.Clear,
                    contentDescription = "",
                    modifier = Modifier.size(30.dp)
                        .clickable (
                            indication = null,
                            interactionSource = null
                        ){
                            onClickDelete(timeEntry.timeEntry.id)
                        }
                )
            }
        }
    }

}