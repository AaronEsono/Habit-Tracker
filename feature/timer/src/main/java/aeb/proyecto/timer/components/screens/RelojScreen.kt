package aeb.proyecto.timer.components.screens

import aeb.proyecto.timer.R
import aeb.proyecto.timer.TimerUiState
import aeb.proyecto.timer.components.screens.noTimer.NoTimerScreen
import aeb.proyecto.timer.components.screens.timer.TimerActiveScreen
import aeb.proyecto.timer.components.timerPicker.TimerPicker
import aeb.proyecto.timer.components.typeSegmentedScreen.IntervalSegmentedScreen
import aeb.proyecto.timer.components.typeSegmentedScreen.StopWatchSegmentedScreen
import aeb.proyecto.timer.components.typeSegmentedScreen.TimerSegmentedScreen
import aeb.proyecto.timer.constants.TypeUnitDate
import aeb.proyecto.timer.model.SegmentedButtonOptions
import aeb.proyecto.timer.model.TimerServiceUIState
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing20
import aeb.proyecto.ui.dimmens.Dimmens.spacing26
import aeb.proyecto.ui.dimmens.Dimmens.spacing30
import aeb.proyecto.ui.dimmens.Dimmens.spacing32
import aeb.proyecto.ui.dimmens.Dimmens.spacing36
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing40
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.LabelMediumText
import aeb.proyecto.ui.text.TitleLargeText
import aeb.proyecto.ui.text.TitleMediumText
import android.content.Intent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RelojScreen(
    timerUIState: TimerUiState.Success,
    timerStopWatchUIState: TimerServiceUIState,
    onHourChange:(String) -> Unit,
    onMinuteChange:(String) -> Unit,
    onSecondChange: (String) -> Unit,
    onIntervalHourChange: (Triple<String,String,String>,Int) -> Unit,
    onButtonIntervalWorkChange: (Boolean) -> Unit,
    onButtonIntervalRestChange: (Boolean) -> Unit,
    onSetChange: (Int) -> Unit,
    onTypeChange: (Int) -> Unit,
    onStartService: () -> Unit,
    onStopService: () -> Unit,
    onResumeButton: () -> Unit,
    onCancelButton: () -> Unit,
    onFinishButton: () -> Unit,
){

    AnimatedContent(
        targetState = timerStopWatchUIState,
        contentKey = { it::class }
    ) { timerStopWatchUIStateAnim ->

        when(timerStopWatchUIStateAnim){
            TimerServiceUIState.NoTimer -> {
                NoTimerScreen(
                    timerUIState = timerUIState,
                    onHourChange = onHourChange,
                    onMinuteChange = onMinuteChange,
                    onSecondChange = onSecondChange,
                    onTypeChange = onTypeChange,
                    onStartService = onStartService,
                    onSetChange = onSetChange,
                    onIntervalHourChange = onIntervalHourChange,
                    onButtonIntervalWorkChange = onButtonIntervalWorkChange,
                    onButtonIntervalRestChange = onButtonIntervalRestChange
                )
            }
            is TimerServiceUIState.TimerRunning -> {
                TimerActiveScreen(
                    timerStopWatchUIState = timerStopWatchUIStateAnim,
                    onStopService = onStopService,
                    onCancelButton = onCancelButton,
                    onResumeButton = onResumeButton,
                    onFinishButton = onFinishButton
                )
            }
        }
    }
}