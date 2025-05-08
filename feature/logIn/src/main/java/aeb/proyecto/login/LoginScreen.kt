package aeb.proyecto.login

import aeb.proyecto.login.components.bottomSheet.LoginBottomSheet
import aeb.proyecto.login.components.button.LoginButton
import aeb.proyecto.login.components.button.LoginGoogleButton
import aeb.proyecto.login.components.loading.LoginLoading
import aeb.proyecto.login.components.textField.LoginSecureTextField
import aeb.proyecto.login.components.textField.LoginTextField
import aeb.proyecto.login.model.DataLoginBottomSheet
import aeb.proyecto.login.model.DataLoginScreen
import aeb.proyecto.ui.navigationIcon.NavigationIcon
import aeb.proyecto.ui.dimmens.Dimmens.spacing1
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing24
import aeb.proyecto.ui.dimmens.Dimmens.spacing32
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing40
import aeb.proyecto.ui.dimmens.Dimmens.spacing48
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.LabelMediumText
import aeb.proyecto.ui.text.TitleLargeText
import aeb.proyecto.ui.text.TitleSmallText
import aeb.proyecto.ui.topbar.providers.ProvideAppBarNavigationIcon
import aeb.proyecto.ui.topbar.providers.ProvideAppBarTitle
import android.util.Patterns
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun LoginScreen(
    onSaveNavigate: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
){

    val dataLoginScreen = viewModel.dataLoginScreen.collectAsStateWithLifecycle().value
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val bottomSheetState = viewModel.dataBottomSheet.collectAsStateWithLifecycle().value

    ProvideAppBarTitle {
        LabelLargeText(stringResource(R.string.login_topbar_title),fontSize = 20.sp)
    }

    ProvideAppBarNavigationIcon {
        NavigationIcon()
    }

    LaunchedEffect(Unit){
        viewModel.getSaveCredentials()
    }

    LoginScreen(
        uiState = uiState,
        dataLoginScreen = dataLoginScreen,
        onClickChecked = { viewModel.setChecked() },
        onClickLoginMode = { viewModel.setLoginMode() },
        onClickAccept = { viewModel.handleAcceptButton() },
        onClickResetPassword = { viewModel.setDataBottomSheet(DataLoginBottomSheet.FORGOT_PASSWORD) },
        onClickGoogle = { viewModel.signInGoogle() },
        toSaveScreen = onSaveNavigate
    )

    if(bottomSheetState.showBottomSheet){
        LoginBottomSheet(
            dataBottomSheet = bottomSheetState.dataBottomSheet,
            emailTextFieldState = bottomSheetState.emailSentForgotPassword,
            onDismiss = { viewModel.closeBottomSheet() },
            onAccept = { viewModel.requestAcceptBottomSheet() }
        )
    }

}

@Composable
internal fun LoginScreen(
    uiState: LoginUIState,
    dataLoginScreen: DataLoginScreen,
    onClickChecked: () -> Unit = {},
    onClickLoginMode: () -> Unit = {},
    onClickAccept: () -> Unit = {},
    onClickGoogle: () -> Unit = {},
    onClickResetPassword: () -> Unit = {},
    toSaveScreen: () -> Unit = {}
) {

    when(uiState){
        is LoginUIState.Success,LoginUIState.Error -> Unit
        is LoginUIState.Loading -> { LoginLoading() }
        is LoginUIState.Login -> {
            LaunchedEffect (Unit){
                toSaveScreen()
            }
        }
    }

    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = spacing40),
        contentAlignment = Alignment.TopCenter
    ) {
        AnimatedContent(
            targetState = dataLoginScreen.isInLoginMode
        ) { isLoginMode ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = spacing24)
                    .verticalScroll(rememberScrollState())
            ) {
                // -----------------------------------------------------
                // Variables
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

                if(isLoginMode){
                    Spacer(modifier = Modifier.padding(vertical = spacing8))

                    LabelMediumText(stringResource(R.string.login_forgot_password),
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

                Spacer(modifier = Modifier.padding(vertical = spacing8))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    LabelMediumText(accountCreated)

                    LabelMediumText(
                        signIn,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            onClickLoginMode()
                        }.testTag("Login mode")
                    )
                }
            }
        }
    }
}

fun isEmailInvalid(textFieldState: TextFieldState): Boolean {
    return !Patterns.EMAIL_ADDRESS.matcher(textFieldState.text).matches() && textFieldState.text.isNotEmpty()
}

fun isPasswordInvalid(textFieldState: TextFieldState, isLoginMode: Boolean): Boolean {
    return textFieldState.text.length < 6 && textFieldState.text.isNotEmpty() && !isLoginMode
}

fun isRememberInvalid(textFieldState: TextFieldState,passwordTextFieldState: TextFieldState): Boolean {
    return textFieldState.text.isNotEmpty() && textFieldState.text != passwordTextFieldState.text
}

fun isButtonEnabled(
    emailState: TextFieldState,
    passwordState: TextFieldState,
    rememberState: TextFieldState,
    isLoginMode: Boolean
): Boolean {
    val isEmailValid = emailState.text.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailState.text).matches()
    val isPasswordValid = passwordState.text.isNotEmpty()

    return if (isLoginMode) {
        isEmailValid && isPasswordValid
    } else {
        isEmailValid && isPasswordValid && rememberState.text == passwordState.text
    }
}