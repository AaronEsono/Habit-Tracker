package aeb.proyecto.addhabit.components.common.dialog

import aeb.proyecto.addhabit.R
import aeb.proyecto.addhabit.components.common.button.PickTypeHabitButton
import aeb.proyecto.addhabit.constants.TypeNotifications
import aeb.proyecto.room.model.classes.TypeNotification
import aeb.proyecto.ui.dialog.CustomDialog
import aeb.proyecto.ui.dimmens.Dimmens.spacing10
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.LabelLargeText
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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

/**
 * A modal system dialogue panel that serves as a specific reminder behavior configuration gateway.
 * Prompts the user to define their alarm layout rule (Daily fixed grid or Rolling cyclic sequence)
 * by mapping dynamic metadata entries onto reuse-optimized input button structures.
 *
 * @param onDismissRequest Contextual closure callback lambda fired to unmount the dialog overlay viewport layer.
 * @param onClickButton Callback action lambda carrying the targeted polymorphic [TypeNotification] domain behavior payload.
 */
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

            // Header asset arrangement layer managing close anchors and background vectors
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

            // Central prominent alert instruction label header
            LabelLargeText(
                stringResource(R.string.add_habit_dialog_notification_title),
                modifier = Modifier.padding(bottom = spacing4, top = spacing10)
                    .fillMaxWidth(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            // Scroll-defended iterative option node generator loop
            Column (
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ){
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
}