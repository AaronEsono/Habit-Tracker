package aeb.proyecto.timer.components.common.timeEntry

import aeb.proyecto.timer.R
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelLargeText
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

/**
 * Header component for the Time Entry history section.
 * Designed to provide a consistent top-rounded appearance that aligns
 * visually with subsequent [TimeEntry] items.
 *
 * @param modifier Applied to the [Row] container.
 */
@Composable
fun TimeEntryHeader (
    modifier: Modifier = Modifier
){

    Row (
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = spacing8, vertical = spacing12)
            .testTag("timer_time_entry_header"),
    ){

        LabelLargeText(
            stringResource(R.string.timer_history),
            modifier = Modifier.padding(start = spacing8)
        )

    }

}