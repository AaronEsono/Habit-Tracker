package aeb.proyecto.habit.components.common.button

import aeb.proyecto.habit.R
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.topbar.providers.ProvideAppBarActions
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

/**
 * A reusable icon component intended for use within an App Bar, providing
 * access to date-selection functionality.
 *
 * This component wraps the calendar icon with necessary container styling
 * ([ProvideAppBarActions]) and handles the click event to trigger the
 * display of a BottomSheet.
 *
 * @param onBottomSheetSelected Callback function invoked when the icon is clicked,
 * signaling the intent to open the date-selection BottomSheet.
 */
@Composable
fun BarActionIcon(
    onBottomSheetSelected: () -> Unit = {},
){

    ProvideAppBarActions {
        Icon(
            painter = painterResource(R.drawable.ic_find_date),
            contentDescription = "calendar icon",
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(end = spacing6)
                .size(25.dp)
                .clickable {onBottomSheetSelected()}
        )
    }

}

