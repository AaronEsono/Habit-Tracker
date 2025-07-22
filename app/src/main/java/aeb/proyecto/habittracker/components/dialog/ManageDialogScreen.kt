package aeb.proyecto.habittracker.components.dialog

import aeb.proyecto.domain.usecase.main.ShowDialogState
import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.utils.convertToHours
import aeb.proyecto.ui.date.utils.getTextToday
import aeb.proyecto.ui.dialog.CustomDialog
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.LabelMediumText
import aeb.proyecto.ui.text.LabelSmallText
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ManageDialogScreen (
   state: ShowDialogState,
   onDismissRequest: () -> Unit,
   onConfirm: () -> Unit
){

    when(state){
        ShowDialogState.NoShowDialog -> Unit
        is ShowDialogState.ShowDialog -> {

            val time = remember (state.time){
                convertToHours(state.time)
            }

            CustomDialog(
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth(),
                onDismissRequest = onDismissRequest
            ) {
                Column (
                    modifier = Modifier.fillMaxWidth()
                        .padding(spacing8)
                ){

                    Image(
                        painter = painterResource(R.drawable.im_reloj),
                        contentDescription = "Image dialog manage",
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally)
                            .size(60.dp)
                    )

                    LabelLargeText(
                        stringResource(R.string.dialog_title, state.habit.habit.name),
                        modifier = Modifier.padding(top = spacing8),
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center
                    )

                    LabelMediumText(
                        stringResource(R.string.dialog_time, time),
                        modifier = Modifier.padding(top = spacing12, start = spacing12),
                    )

                    LabelMediumText(
                        stringResource(R.string.dialog_date, getTextToday(state.habit.day.date)),
                        modifier = Modifier.padding(top = spacing2, start = spacing12)
                    )


                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ){
                        TextButton(
                            onClick = onDismissRequest,
                        ) {
                            LabelLargeText(stringResource(R.string.dialog_cancel))
                        }

                        TextButton(
                            onClick = onConfirm,
                        ) {
                            LabelLargeText(stringResource(R.string.dialog_accept))
                        }
                    }
                }
            }
        }
    }
}