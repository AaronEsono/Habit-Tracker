package aeb.proyecto.habit.components.common.button

import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.ripple.CustomRipple
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

/**
 * A floating action button component used to initiate the habit creation flow.
 *
 * This component provides a circular action button centered on the screen or
 * anchored to the UI, which triggers navigation to the "Add Habit" screen.
 *
 * @param modifier Modifier to be applied to the button layout.
 * @param navigateToAddHabit Callback function triggered when the button is clicked.
 * Accepts a [Long] representing the ID of the habit (typically -1 for new habit creation).
 */
@Composable
fun AddHabitButton(
    modifier: Modifier = Modifier,
    navigateToAddHabit: (Long) -> Unit = {}
){

    CustomRipple {
        FloatingActionButton(
            onClick = { navigateToAddHabit(-1) },
            modifier = modifier
                .padding(spacing16)
                .testTag("add_habit_button"),
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Floating action button")
        }
    }
}