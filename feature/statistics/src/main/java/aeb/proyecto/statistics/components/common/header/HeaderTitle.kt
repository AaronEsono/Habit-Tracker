package aeb.proyecto.statistics.components.common.header

import aeb.proyecto.room.entities.Habit
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.TitleLargeText
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/**
 * Renders a descriptive header card for a habit, displaying its icon and name.
 * The component automatically scales the typography based on the available
 * container height.
 *
 * @param modifier Applied to the [BoxWithConstraints] container.
 * @param habit The habit model containing the icon, name, and theme color.
 */
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun HeaderTitle(
    modifier: Modifier = Modifier,
    habit: Habit
) {

    BoxWithConstraints(
        modifier = modifier
            .padding(horizontal = spacing4)
            .clip(RoundedCornerShape(spacing6))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(spacing6))
            .background(MaterialTheme.colorScheme.surfaceTint)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        // Calculate font size dynamically based on the component's height
        val density = LocalDensity.current
        val textSize = with(density) {
            (maxHeight * 0.45f).toSp()
        }

        // Habit Name (Centered)
        TitleLargeText(
            text = habit.name,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = textSize
        )

        // Habit Icon (Aligned to the start)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = spacing6),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .padding(vertical = spacing6)
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(spacing8))
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    imageVector = habit.icon,
                    contentDescription = "Icon header",
                    tint = Color(habit.color),
                    modifier = Modifier.align(Alignment.Center).fillMaxSize(0.8f)
                )
            }
        }
    }
}