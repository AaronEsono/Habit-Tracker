package aeb.proyecto.habittracker.ui.components.dailyHabit

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.data.entities.Habit
import aeb.proyecto.habittracker.ui.components.text.LabelLargeText
import aeb.proyecto.habittracker.ui.components.text.LabelSmallText
import aeb.proyecto.habittracker.ui.theme.textColors
import aeb.proyecto.habittracker.utils.Dimmens.spacing12
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun CardDailyHabitHeader(
    habit: Habit,
    isInDialog: Boolean,
    times: State<Int>,
    unit: State<Int?>,
    targetProgress: Float,
    icon: State<ImageVector>,
    onClick: (Long) -> Unit,
) {
    val dimens = remember { if (isInDialog) 55.dp else 45.dp }
    val dimensIcon = remember { if (isInDialog) 35.dp else 30.dp }

    val colorIcons = remember { Color(habit.color).copy(alpha = 0.1f) }


    val animatedColor by animateColorAsState(
        targetValue = if (targetProgress == 1f) colorIcons.copy(alpha = 0.6f) else colorIcons.copy(alpha = 0.1f),
        animationSpec = tween(
            durationMillis = 1000, // Duración de la animación
            easing = FastOutSlowInEasing // Curva de animación
        ), label = ""
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = spacing8)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = colorIcons,
            ),
            modifier = Modifier
                .size(dimens)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = iconByName(habit.icon),
                    contentDescription = "Add",
                    tint = textColors,
                    modifier = Modifier.size(dimensIcon)
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .height(dimens)
                .padding(horizontal = spacing8),
            verticalArrangement = Arrangement.Center
        ) {
            LabelLargeText(habit.name, color = textColors)

            habit.description?.let {
                LabelSmallText(
                    habit.description!!,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = textColors
                )
            }
        }

        Column(
            modifier = Modifier
                .height(dimens)
                .padding(horizontal = spacing8), verticalArrangement = Arrangement.Center
        ) {
            LabelSmallText(
                text = "${times.value}/${habit.times}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color(habit.color)
            )
            LabelSmallText(
                text = stringResource(unit.value ?: R.string.add_habit_unit_pl_1),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color(habit.color)
            )
        }

        Card(
            colors = CardDefaults.cardColors(
                containerColor = animatedColor,
            ),
            modifier = Modifier
                .size(dimens)
                .clip(RoundedCornerShape(spacing12))
                .clickable() {
                    onClick(habit.id)
                }
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = icon.value,
                    contentDescription = "",
                    modifier = Modifier.size(dimensIcon)
                )
            }
        }
    }
}