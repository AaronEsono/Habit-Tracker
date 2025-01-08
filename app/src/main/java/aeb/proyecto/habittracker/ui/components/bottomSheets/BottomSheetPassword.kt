package aeb.proyecto.habittracker.ui.components.bottomSheets

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.ui.components.buttons.CustomFilledButton
import aeb.proyecto.habittracker.ui.components.text.LabelMediumText
import aeb.proyecto.habittracker.ui.components.text.TitleLargeText
import aeb.proyecto.habittracker.ui.components.textField.CustomOutlinedTextField
import aeb.proyecto.habittracker.utils.Dimmens.spacing16
import aeb.proyecto.habittracker.utils.Dimmens.spacing4
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetPassword(
    onDismiss: () -> Unit = {},
    onAccept: (String) -> Unit = {},
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer
){

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val email = rememberTextFieldState()
    val wasFilled = remember { mutableStateOf(false) }

    if (email.text.isNotEmpty()) wasFilled.value = true

    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        sheetState = sheetState,
        containerColor = containerColor
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = spacing16, vertical = spacing8),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TitleLargeText(
                text = stringResource(R.string.import_habit_forgot_title_dx),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.padding(vertical = spacing8))

            LabelMediumText(
                text = stringResource(R.string.import_habit_forgot_subtitle_dx),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.padding(vertical = spacing8))

            CustomOutlinedTextField(
                rememberTextFieldState = email,
                label = R.string.import_habit_screen_email,
                isError = email.text.isEmpty() && wasFilled.value,
                modifier = Modifier.fillMaxWidth().height(70.dp),
                modifierError = Modifier.fillMaxWidth().padding(horizontal = spacing8),
                labelError = R.string.import_habit_screen_email_error,
                imeAction = ImeAction.Done,
                keyBoardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.padding(vertical = spacing8))

            CustomFilledButton(
                title = R.string.buttons_accept,
                icon = R.drawable.ic_check,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                colorIcon = MaterialTheme.colorScheme.inverseSurface,
                colorText = MaterialTheme.colorScheme.inverseSurface,
                enabled = email.text.isNotEmpty(),
                modifier = Modifier.fillMaxWidth().height(50.dp),
                onClick = {
                    onAccept(email.text.toString())

                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            onDismiss()
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.padding(vertical = spacing4))
        }
    }
}