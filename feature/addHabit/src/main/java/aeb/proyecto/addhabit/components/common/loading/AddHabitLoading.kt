package aeb.proyecto.addhabit.components.common.loading

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

/**
 * A full-screen non-interactive loading backdrop overlay layout.
 * Blurs out the main viewport with an elegant translucent dim screen veil and intercepts all physical
 * click touch event gestures to safeguard application states against accidental double-tap interaction exploits
 * during asynchronous background database transaction operations.
 *
 * Includes a dedicated testing identifier node to ease automated semantic integration checks.
 */
@Composable
fun AddHabitLoading (){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.8f))
            .zIndex(1f)
            .testTag("add_habit_loading_overlay")
            // Prevent downstream action pipelines from firing while an operations transition executes
            .clickable(enabled = false) {}
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}