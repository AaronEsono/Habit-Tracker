package aeb.proyecto.statistics.components.vertical.screens

import aeb.proyecto.statistics.R
import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelLargeText
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

/**
 * Renders an empty state view when no statistical data is available for
 * the selected habit. Displays an illustrative icon and a message to
 * inform the user.
 *
 * @param modifier Applied to the [Column] container.
 */
@Composable
fun NoContentVerticalStatisticsScreen(){

    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Icon(
            painter = painterResource(R.drawable.ic_no_statistics),
            contentDescription = "no content statistics",
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .size(150.dp)
                .testTag("statistics_no_content_screen")
        )

        Spacer(modifier = Modifier.padding(vertical = spacing8))

        LabelLargeText(
            stringResource(R.string.statistics_no_content),
            modifier = Modifier.padding(horizontal = spacing16)
        )
    }

}