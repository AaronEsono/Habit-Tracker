package aeb.proyecto.login.components.horizontal

import aeb.proyecto.login.LoginUIState
import aeb.proyecto.login.R
import aeb.proyecto.login.components.commom.button.LoginButton
import aeb.proyecto.login.components.commom.button.LoginGoogleButton
import aeb.proyecto.login.components.commom.loading.LoginLoading
import aeb.proyecto.login.components.commom.textField.LoginSecureTextField
import aeb.proyecto.login.components.commom.textField.LoginTextField
import aeb.proyecto.login.components.horizontal.components.bottomSheet.HorizontalLoginBottomSheet
import aeb.proyecto.login.model.BottomSheetState
import aeb.proyecto.login.model.DataLoginScreen
import aeb.proyecto.login.utils.isButtonEnabled
import aeb.proyecto.login.utils.isEmailInvalid
import aeb.proyecto.login.utils.isPasswordInvalid
import aeb.proyecto.login.utils.isRememberInvalid
import aeb.proyecto.ui.dimmens.Dimmens.spacing1
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelMediumText
import aeb.proyecto.ui.text.TitleLargeText
import aeb.proyecto.ui.text.TitleSmallText
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign

@Composable
fun HorizontalLoginScreen(
    uiState: LoginUIState,
    dataLoginScreen: DataLoginScreen,
    bottomSheetState: BottomSheetState,
    onClickChecked: () -> Unit = {},
    onClickLoginMode: () -> Unit = {},
    onClickAccept: () -> Unit = {},
    onClickGoogle: () -> Unit = {},
    onClickResetPassword: () -> Unit = {},
    toSaveScreen: () -> Unit = {},
    onDismiss: () -> Unit = {},
    onAccept: () -> Unit = {}
){

    when(uiState){
        is LoginUIState.Success, LoginUIState.Error -> Unit
        is LoginUIState.Loading -> { LoginLoading() }
        is LoginUIState.Login -> {
            LaunchedEffect (Unit){
                toSaveScreen()
            }
        }
    }

    val focusManager = LocalFocusManager.current

    AnimatedContent(
        targetState = dataLoginScreen.isInLoginMode
    ) { isLoginMode ->
        // -----------------------------------------------------
        // Variables
        val title = remember (isLoginMode){
            if (isLoginMode) R.string.login_title
            else R.string.login_title_register
        }

        val subtitle = remember (isLoginMode){
            if (isLoginMode) R.string.login_subtitle
            else R.string.login_subtitle_register
        }

        val accountCreated = remember (isLoginMode){
            if (isLoginMode) R.string.login_no_account
            else R.string.login_account
        }

        val signIn = remember (isLoginMode){
            if (isLoginMode) R.string.login_register
            else R.string.login_signIn
        }
        // -----------------------------------------------------

        Row (
            modifier = Modifier
                .fillMaxSize()
                .padding(start = spacing16, end = spacing16, top = spacing4, bottom = spacing4),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){

            Column (
                modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()),
            ){
                TitleLargeText(stringResource(title))

                Spacer(modifier = Modifier.padding(vertical = spacing1))

                TitleSmallText(stringResource(subtitle))

                Spacer(modifier = Modifier.padding(vertical = spacing4))

                LoginTextField(
                    textFieldState = dataLoginScreen.emailTextFieldState,
                    isError = isEmailInvalid(dataLoginScreen.emailTextFieldState),
                    label = stringResource(R.string.login_textField_email_label),
                    errorText = stringResource(R.string.login_textField_email_error),
                    imeAction = ImeAction.Next,
                    focusManager = focusManager
                )

                LoginSecureTextField(
                    textFieldState = dataLoginScreen.passwordTextFieldState,
                    isError = isPasswordInvalid(dataLoginScreen.passwordTextFieldState,dataLoginScreen.isInLoginMode),
                    label = stringResource(R.string.login_textField_password_label),
                    errorText = stringResource(R.string.login_textField_password_error),
                    imeAction = if (isLoginMode) ImeAction.Done else ImeAction.Next,
                    focusManager = focusManager
                )

                if (!isLoginMode) {
                    LoginSecureTextField(
                        textFieldState = dataLoginScreen.rememberTextFieldState,
                        isError = isRememberInvalid(
                            dataLoginScreen.rememberTextFieldState,
                            dataLoginScreen.passwordTextFieldState
                        ),
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
                            .testTag("Checkbox row")
                            .toggleable(
                                value = dataLoginScreen.isChecked,
                                onValueChange = { onClickChecked() },
                                role = Role.Checkbox
                            )
                    ) {

                        Checkbox(
                            checked = dataLoginScreen.isChecked,
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
            }

            Spacer(modifier = Modifier.padding(horizontal = spacing12))

            Column (
                modifier = Modifier.weight(1f).padding(top = spacing8),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                if(isLoginMode){
                    Spacer(modifier = Modifier.padding(vertical = spacing8))

                    LabelMediumText(
                        stringResource(R.string.login_forgot_password),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            onClickResetPassword()
                        }.testTag("Forgot password"))
                }

                Spacer(modifier = Modifier.padding(vertical = spacing6))

                LoginButton(
                    modifier = Modifier.fillMaxWidth().testTag("login button"),
                    enabled = isButtonEnabled(
                        dataLoginScreen.emailTextFieldState,
                        dataLoginScreen.passwordTextFieldState,
                        dataLoginScreen.rememberTextFieldState,
                        isLoginMode
                    ),
                    onClick = onClickAccept
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
                        modifier = Modifier.fillMaxWidth().testTag("Google button"),
                        onClick = onClickGoogle
                    )
                }

                if(isLoginMode){
                    Spacer(modifier = Modifier.padding(vertical = spacing8))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    LabelMediumText(stringResource(accountCreated))

                    LabelMediumText(
                        stringResource(signIn),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            onClickLoginMode()
                        }.testTag("Login mode")
                    )
                }
            }
        }
    }

    if(bottomSheetState.showBottomSheet){
        HorizontalLoginBottomSheet(
            dataBottomSheet = bottomSheetState.dataBottomSheet,
            emailTextFieldState = bottomSheetState.emailSentForgotPassword,
            onDismiss = onDismiss,
            onAccept = onAccept
        )
    }

}