package aeb.proyecto.login.components.bottomSheet

import aeb.proyecto.login.R
import aeb.proyecto.login.components.button.BottomSheetFilledButton
import aeb.proyecto.login.components.button.BottomSheetOutFilledButton
import aeb.proyecto.login.components.textField.LoginTextField
import aeb.proyecto.login.model.DataLoginBottomSheet
import aeb.proyecto.ui.bottomsheet.CustomBottomSheet
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing20
import aeb.proyecto.ui.dimmens.Dimmens.spacing3
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.TitleLargeText
import android.util.Patterns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginBottomSheet(
    dataBottomSheet: DataLoginBottomSheet,
    emailTextFieldState: TextFieldState = rememberTextFieldState(),
    onDismiss: () -> Unit = {},
    onAccept: () -> Unit = {}
){

    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    CustomBottomSheet(
        sheetState = sheetState,
        onDismiss = onDismiss
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = spacing8, end = spacing8, bottom = spacing8),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    dataBottomSheet.iconTitle,
                    contentDescription = "Icon Title",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(spacing20)
                )

                Spacer(modifier = Modifier.padding(horizontal = spacing3))

                TitleLargeText(stringResource(dataBottomSheet.title))
            }

            Spacer(modifier = Modifier.padding(vertical = spacing4))

            LabelLargeText(
                stringResource(dataBottomSheet.subtitle),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = spacing8)
            )

            when(dataBottomSheet){
                DataLoginBottomSheet.FORGOT_PASSWORD -> {
                    val focusManager = LocalFocusManager.current

                    Spacer(modifier = Modifier.padding(vertical = spacing4))

                    LoginTextField(
                        focusManager = focusManager,
                        textFieldState = emailTextFieldState,
                        label = stringResource(R.string.login_textField_email_label),
                        errorText = stringResource(R.string.login_textField_email_error),
                        imeAction = ImeAction.Done,
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        keyboardType = KeyboardType.Email,
                        modifier = Modifier.padding(vertical = spacing2)
                    )

                }
                else -> Unit
            }

            Spacer(modifier = Modifier.padding(vertical = spacing8))

            when(dataBottomSheet){
                DataLoginBottomSheet.UNVERIFIED_EMAIL,DataLoginBottomSheet.FORGOT_PASSWORD -> {
                    val title = if(dataBottomSheet == DataLoginBottomSheet.UNVERIFIED_EMAIL)
                        R.string.login_resent_email
                    else
                        R.string.login_accept

                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ){
                        BottomSheetOutFilledButton(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                coroutineScope.launch {
                                    sheetState.hide()
                                    onDismiss()
                                }
                            }
                        )

                        Spacer(modifier = Modifier.padding(horizontal = spacing4))

                        BottomSheetFilledButton(
                            modifier = Modifier.weight(1f),
                            title = title,
                            isEnabled = isButtonEnabled(dataBottomSheet,emailTextFieldState),
                            onClick = {
                                coroutineScope.launch {
                                    onAccept()
                                }
                            }
                        )
                    }
                }
                else -> {
                    BottomSheetFilledButton(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = spacing8),
                        onClick = {
                            coroutineScope.launch {
                                sheetState.hide()
                                onAccept()
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.padding(vertical = spacing4))
        }
    }
}

fun isButtonEnabled(dataLoginScreen: DataLoginBottomSheet,emailTextFieldState: TextFieldState): Boolean{
    return dataLoginScreen != DataLoginBottomSheet.FORGOT_PASSWORD || Patterns.EMAIL_ADDRESS.matcher(emailTextFieldState.text).matches()
}