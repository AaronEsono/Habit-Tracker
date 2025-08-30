package aeb.proyecto.stopwatch.overlay

import aeb.proyecto.stopwatch.manager.StopWatchStateManager
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelMediumText
import aeb.proyecto.ui.theme.HabitTrackerTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun OverlayContent(
    stateManager: StopWatchStateManager
){
    val timeElapsed = stateManager.timerString.collectAsStateWithLifecycle().value
    val isDarkTheme = isSystemInDarkTheme()

    val background = remember(isDarkTheme){
        if(isDarkTheme){ Color(0xFF1E1E1E) }
        else{ Color(0xFFFAFAFA) }
    }

    val textColor = remember(isDarkTheme){
            if(isDarkTheme){ Color(0xFFFAFAFA) }
        else{ Color(0xFF1E1E1E) }
    }

    HabitTrackerTheme(0) {
        Box(
            modifier = Modifier
                .background(background, RoundedCornerShape(spacing16))
                .padding(horizontal = spacing12, vertical = spacing8)
        ) {
            Column {
                LabelMediumText(
                    text = timeElapsed,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            }
        }
    }
}