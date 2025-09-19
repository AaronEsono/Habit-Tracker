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
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Floating action button")
        }
    }
}