package aeb.proyecto.habit.components.timeRange.components

import aeb.proyecto.ui.date.utils.getAvr
import aeb.proyecto.ui.dimmens.Dimmens.spacing1
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing20
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelMediumText
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@Composable
fun DayCard(
    modifier:Modifier = Modifier,
    date:LocalDate,
    isSelected:Boolean = false,
    onClick: (LocalDate) -> Unit = {}
){

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(spacing12))
            .boxBackgroundSelected(isSelected)
            .padding(top = spacing4)
            .clickable { onClick(date) }
    ) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LabelMediumText(stringResource(getAvr(date.dayOfWeek)), color = textSelected(isSelected))

            Box(
                modifier = Modifier
                    .padding(top = spacing2)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = spacing8, topEnd = spacing8))
                    .backgroundTextSelected(isSelected),
                contentAlignment = Alignment.Center
            ) {
                LabelMediumText(
                    date.dayOfMonth.toString(),
                    modifier = Modifier.align(Alignment.Center).padding(top = spacing6,bottom = spacing8),
                    color = textSelected(isSelected)
                )
            }
        }
    }
}

@Composable
fun Modifier.boxBackgroundSelected(
    isSelected: Boolean
): Modifier {
    val targetColor = if (isSelected) {
        MaterialTheme.colorScheme.onSurface
    } else {
        MaterialTheme.colorScheme.primary
    }
    return this.background(animatorSelected(targetColor = targetColor, label = "BoxBackgroundAnimation"))
}

@Composable
fun Modifier.backgroundTextSelected(
    isSelected: Boolean
): Modifier {
    val targetColor = if (isSelected) {
        MaterialTheme.colorScheme.onBackground
    } else {
        MaterialTheme.colorScheme.secondaryContainer
    }
    return this.background(animatorSelected(targetColor = targetColor, label = "BoxBackgroundAnimation"))
}


@Composable
fun textSelected(
    isSelected: Boolean
): Color {
    val targetColor = if (isSelected) {
        MaterialTheme.colorScheme.inverseOnSurface
    } else {
        MaterialTheme.colorScheme.onSurface
    }
    return animatorSelected(targetColor = targetColor, label = "TextColorAnimation")
}

@Composable
fun animatorSelected(
    targetColor:Color,
    label:String
):Color{
    return animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = label
    ).value
}