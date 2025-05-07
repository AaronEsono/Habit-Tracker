package aeb.proyecto.timer.components.bottomSheet.pickTime

import aeb.proyecto.timer.R
import aeb.proyecto.timer.components.bottomSheet.pickTime.model.TypePickState
import aeb.proyecto.timer.components.timerPicker.TimerPicker
import aeb.proyecto.timer.model.HourSelectedState
import aeb.proyecto.ui.bottomsheet.CustomBottomSheet
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing20
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.LabelMediumText
import aeb.proyecto.ui.text.TitleLargeText
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickTimeBottomSheet(
    modifier:Modifier = Modifier,
    hourSelectedState: HourSelectedState,
    label:String,
    typePickState: TypePickState,
    onIntervalHourChange: (Triple<String,String,String>,Int) -> Unit,
    onDismissRequest: () -> Unit,
){

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    var hourState by remember { mutableStateOf("00") }
    var minuteState by remember { mutableStateOf("00") }
    var secondState by remember { mutableStateOf("00") }

    CustomBottomSheet (
        modifier = modifier,
        sheetState = sheetState,
        onDismiss = onDismissRequest,
    ){

        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = spacing8, end = spacing8, bottom = spacing20),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacing8)
            ) {
                TitleLargeText(
                    label,
                    modifier = Modifier.align(Alignment.Center)
                )

                CustomRipple{
                    Icon(
                        Icons.Filled.Clear,
                        contentDescription = "close button",
                        modifier = Modifier
                            .size(35.dp)
                            .align(Alignment.TopEnd)
                            .clickable {
                                coroutineScope.launch {
                                    sheetState.hide()
                                    onDismissRequest()
                                }
                            },
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            TimerPicker(
                modifier = Modifier.padding(top = spacing20),
                timerSelected = hourSelectedState,
                colorGradient = MaterialTheme.colorScheme.primaryContainer,
                onHourChange = { hour -> hourState = hour },
                onMinuteChange = { minute -> minuteState = minute },
                onSecondChange = { second -> secondState = second }
            )


            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = spacing20, start = spacing12, end = spacing12)
            ) {

                CustomRipple {
                    OutlinedButton(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            coroutineScope.launch {
                                sheetState.hide()
                                onDismissRequest()
                            }
                        },
                        shape = RoundedCornerShape(spacing8)
                    ) {
                        LabelLargeText(stringResource(R.string.timer_cancel))
                    }
                }

                Spacer(modifier = Modifier.padding(horizontal = spacing8))

                CustomRipple {
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            coroutineScope.launch {
                                onIntervalHourChange(
                                    Triple(hourState,minuteState,secondState),
                                    typePickState.value
                                )
                                sheetState.hide()
                                onDismissRequest()
                            }
                        },
                        shape = RoundedCornerShape(spacing8),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        LabelLargeText(
                            stringResource(R.string.timer_accept),
                            color = MaterialTheme.colorScheme.inverseOnSurface
                        )
                    }
                }

            }
        }

    }

}