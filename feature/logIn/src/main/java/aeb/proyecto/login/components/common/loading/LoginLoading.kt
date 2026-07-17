package aeb.proyecto.login.components.common.loading

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
 * Full-screen loading overlay designed to intercept user interactions during
 * asynchronous authentication operations.
 * * Provides a semi-transparent surface container with a central indeterminate
 * progress indicator, ensuring the user is aware of the background task state.
 */
@Composable
fun LoginLoading(){

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.8f))
            .zIndex(1f)
            .testTag("Login overlay")
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