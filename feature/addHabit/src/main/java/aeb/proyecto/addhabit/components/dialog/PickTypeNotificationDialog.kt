package aeb.proyecto.addhabit.components.dialog

import aeb.proyecto.addhabit.R
import aeb.proyecto.addhabit.components.button.PickTypeHabitButton
import aeb.proyecto.addhabit.constants.TypeNotifications
import aeb.proyecto.room.model.classes.TypeNotification
import aeb.proyecto.ui.dialog.CustomDialog
import aeb.proyecto.ui.dimmens.Dimmens.spacing10
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.LabelLargeText
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PickTypeNotificationDialog(
    onDismissRequest: () -> Unit = {},
    onClickButton: (TypeNotification) -> Unit = {}
){

    CustomDialog(
        onDismissRequest = onDismissRequest,
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = spacing10, vertical = spacing12)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = spacing8)
            ){
                Image(
                    painter = painterResource(R.drawable.im_notification),
                    contentDescription = "icon",
                    modifier = Modifier
                        .size(80.dp)
                        .align(Alignment.Center)
                )

                CustomRipple{
                    Icon(
                        Icons.Filled.Clear,
                        contentDescription = "close button",
                        modifier = Modifier
                            .size(35.dp)
                            .align(Alignment.TopEnd)
                            .clickable { onDismissRequest() },
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            LabelLargeText(
                stringResource(R.string.add_habit_dialog_notification_title),
                modifier = Modifier.padding(bottom = spacing4, top = spacing10)
                    .fillMaxWidth(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            TypeNotifications.entries.forEach { typeNotification ->
                PickTypeHabitButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = spacing8),
                    title = stringResource(typeNotification.title),
                    subtitle = stringResource(typeNotification.label),
                    onClick = {
                        onDismissRequest()
                        onClickButton(typeNotification.type)
                    }
                )
            }
        }
    }

}