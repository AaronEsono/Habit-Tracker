package aeb.proyecto.addhabit.components.dialog

import aeb.proyecto.addhabit.R
import aeb.proyecto.addhabit.components.button.PickTypeHabitButton
import aeb.proyecto.addhabit.constants.TypeHabit
import aeb.proyecto.ui.dialog.CustomDialog
import aeb.proyecto.ui.dimmens.Dimmens.spacing10
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.ripple.CustomRipple
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
import androidx.compose.ui.unit.dp

@Composable
fun PickTypeHabitDialog(
    onDismissRequest: () -> Unit,
    onClickButton: (TypeHabit) -> Unit
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
                    painter = painterResource(R.drawable.im_habit),
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

            TypeHabit.entries.forEach { typeHabit ->
                PickTypeHabitButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = spacing8),
                    title = stringResource(typeHabit.title),
                    subtitle = stringResource(typeHabit.subtitle),
                    onClick = {
                        onClickButton(typeHabit)
                        onDismissRequest()
                    }
                )
            }
        }
    }

}