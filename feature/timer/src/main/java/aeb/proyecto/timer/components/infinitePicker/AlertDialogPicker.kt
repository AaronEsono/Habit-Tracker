package aeb.proyecto.timer.components.infinitePicker

import aeb.proyecto.timer.R
import aeb.proyecto.timer.components.textField.TimerTextField
import aeb.proyecto.timer.constants.TypeUnitDate
import aeb.proyecto.ui.dialog.CustomDialog
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing20
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.regexTextField.IsOnlyZeroTo59
import aeb.proyecto.ui.regexTextField.IsOnlyZeroTo99
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.LabelMediumText
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp

@Composable
fun AlertDialogPicker(
    typeList: TypeUnitDate,
    initialText: String = "00",
    onDismissRequest: () -> Unit = {},
    onAccept: (Int) -> Unit = {}
) {
    val textFieldState = rememberTextFieldState(initialText = initialText)

    LaunchedEffect (initialText){
        textFieldState.edit { replace(0,length,initialText) }
    }

    when (typeList) {
        TypeUnitDate.Minutes, TypeUnitDate.Seconds -> {
            IsOnlyZeroTo59(textFieldState)
        }

        TypeUnitDate.Hours -> {
            IsOnlyZeroTo99(textFieldState)
        }
    }

    CustomDialog(
        onDismissRequest = onDismissRequest,
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(start = spacing12, end = spacing4, bottom = spacing8, top = spacing20),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column {
                TimerTextField(
                    textFieldState = textFieldState,
                )

                LabelMediumText(
                    stringResource(typeList.label),
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(top = spacing2)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = spacing20),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {

                CustomRipple {
                    TextButton(
                        onClick = onDismissRequest,
                        shape = RoundedCornerShape(spacing12)
                    ) {
                        LabelLargeText(
                            stringResource(R.string.timer_cancel),
                            fontSize = 16.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                CustomRipple {
                    TextButton(
                        onClick = {
                            val number = textFieldState.text.toString().toIntOrNull() ?: 0
                            onAccept(number)
                            onDismissRequest()
                        },
                        shape = RoundedCornerShape(spacing12)
                    ) {
                        LabelLargeText(
                            stringResource(R.string.timer_accept),
                            fontSize = 16.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

data class DialogDataTimerScreen(
    val showDialog: Boolean = false,
    val typeUnitDate: TypeUnitDate = TypeUnitDate.Hours,
    val initialText:String = "00"
)