package aeb.proyecto.login

import aeb.proyecto.login.components.button.LoginButton
import aeb.proyecto.login.components.button.LoginGoogleButton
import aeb.proyecto.login.components.textField.LoginSecureTextField
import aeb.proyecto.login.components.textField.LoginTextField
import aeb.proyecto.ui.dimmens.Dimmens.spacing1
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing24
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing64
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.LabelMediumText
import aeb.proyecto.ui.text.TitleLargeText
import aeb.proyecto.ui.text.TitleMediumText
import aeb.proyecto.ui.text.TitleSmallText
import android.icu.text.CaseMap.Title
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlin.math.sin

@Composable
fun LoginScreen(
    onSaveNavigate: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
){
    LoginScreen()
}

@Composable
internal fun LoginScreen() {

    val focusManager = LocalFocusManager.current
    val (checkedState, onStateChange) = remember { mutableStateOf(true) }
    var signInMode by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        AnimatedContent(
            targetState = signInMode
        ) { isLoginMode ->

            Column(
                modifier = Modifier
                    .fillMaxSize(0.8f)
                    .padding(top = spacing24)
            ) {
                // ----------------------------------------------------- Variables
                val title = if (isLoginMode) stringResource(R.string.login_title)
                else stringResource(R.string.login_title_register)

                val subtitle = if (isLoginMode) stringResource(R.string.login_subtitle)
                else stringResource(R.string.login_subtitle_register)

                val accountCreated = if (isLoginMode) stringResource(R.string.login_no_account)
                else stringResource(R.string.login_account)

                val signIn = if (isLoginMode) stringResource(R.string.login_register)
                else stringResource(R.string.login_signIn)
                // -----------------------------------------------------

                TitleLargeText(title)

                Spacer(modifier = Modifier.padding(vertical = spacing1))

                TitleSmallText(subtitle)

                Spacer(modifier = Modifier.padding(vertical = spacing4))

                LoginTextField(
                    label = stringResource(R.string.login_textField_email_label),
                    errorText = stringResource(R.string.login_textField_email_error),
                    imeAction = ImeAction.Next,
                    focusManager = focusManager
                )

                LoginSecureTextField(
                    label = stringResource(R.string.login_textField_password_label),
                    errorText = stringResource(R.string.login_textField_password_error),
                    imeAction = if (isLoginMode) ImeAction.Done else ImeAction.Next,
                    focusManager = focusManager
                )

                if (!isLoginMode) {
                    LoginSecureTextField(
                        label = stringResource(R.string.login_textField_remember_password_label),
                        errorText = stringResource(R.string.login_textField_remember_password_error),
                        imeAction = ImeAction.Done,
                        focusManager = focusManager
                    )
                }

                if (isLoginMode) {
                    Spacer(modifier = Modifier.padding(vertical = spacing2))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier
                            .wrapContentWidth()
                            .toggleable(
                                value = checkedState,
                                onValueChange = { onStateChange(!checkedState) },
                                role = Role.Checkbox
                            )
                    ) {

                        Checkbox(
                            checked = checkedState,
                            onCheckedChange = null,
                            colors = CheckboxDefaults.colors(
                                checkedColor = MaterialTheme.colorScheme.onSurface,
                                checkmarkColor = MaterialTheme.colorScheme.inverseSurface,
                            )
                        )

                        Spacer(modifier = Modifier.padding(horizontal = spacing6))

                        TitleSmallText(stringResource(R.string.login_remember))
                    }
                }

                if(isLoginMode){
                    Spacer(modifier = Modifier.padding(vertical = spacing8))

                    LabelMediumText(stringResource(R.string.login_forgot_password),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {})
                }

                Spacer(modifier = Modifier.padding(vertical = spacing6))

                LoginButton(
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.padding(vertical = spacing6))

                if(isLoginMode){

                    LabelMediumText(
                        stringResource(R.string.login_continue),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.padding(vertical = spacing6))

                    LoginGoogleButton(
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.padding(vertical = spacing8))

                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    LabelMediumText(accountCreated)

                    LabelMediumText(
                        signIn,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            signInMode = !signInMode
                        }
                    )
                }
            }
        }
    }
}