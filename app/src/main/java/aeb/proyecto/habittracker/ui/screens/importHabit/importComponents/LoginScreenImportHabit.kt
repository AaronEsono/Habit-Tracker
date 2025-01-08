package aeb.proyecto.habittracker.ui.screens.importHabit.importComponents

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.ui.components.buttons.CustomFilledButton
import aeb.proyecto.habittracker.ui.components.cardGoogle.GoogleCard
import aeb.proyecto.habittracker.ui.components.checkBox.CheckBoxButton
import aeb.proyecto.habittracker.ui.components.text.LabelMediumText
import aeb.proyecto.habittracker.ui.components.text.LabelSmallText
import aeb.proyecto.habittracker.ui.components.text.TitleLargeText
import aeb.proyecto.habittracker.ui.components.textField.CustomOutlinedTextField
import aeb.proyecto.habittracker.ui.components.textField.OutlinedTextFieldPasswordLogin
import aeb.proyecto.habittracker.ui.screens.importHabit.ImportHabitViewModel
import aeb.proyecto.habittracker.utils.Dimmens.spacing16
import aeb.proyecto.habittracker.utils.Dimmens.spacing2
import aeb.proyecto.habittracker.utils.Dimmens.spacing24
import aeb.proyecto.habittracker.utils.Dimmens.spacing4
import aeb.proyecto.habittracker.utils.Dimmens.spacing6
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldLabelPosition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


@SuppressLint("UnusedContentLambdaTargetStateParameter")
@Composable
fun LoginScreenImportHabit(
    importHabitViewModel: ImportHabitViewModel,
    signInGoogle: () -> Unit = {},
    signIn: (String,String,Boolean) -> Unit,
    signUp: (String,String) -> Unit,
    resetPassword: () -> Unit
) {

    val email = rememberTextFieldState()
    val password = rememberTextFieldState()
    val rememberPassword = rememberTextFieldState()
    val (checkedState, onStateChange) = remember { mutableStateOf(true) }

    val wasFilledEmail = remember { mutableStateOf(false) }
    val wasFilledPassword = remember { mutableStateOf(false) }
    val wasFilledRememberPassword = remember { mutableStateOf(false) }
    val emailPassword = importHabitViewModel.emailPassword.collectAsState().value

    val uiState = importHabitViewModel.uiState.collectAsState().value

    if(email.text.isNotEmpty()) wasFilledEmail.value = true
    if(password.text.isNotEmpty()) wasFilledPassword.value = true
    if(rememberPassword.text.isNotEmpty()) wasFilledRememberPassword.value = true

    LaunchedEffect (uiState.isInLogin){
        email.edit { replace(0,length,"") }
        password.edit { replace(0,length,"") }
        rememberPassword.edit { replace(0,length,"") }

        wasFilledEmail.value = false
        wasFilledPassword.value = false
        wasFilledRememberPassword.value = false
    }

    LaunchedEffect(emailPassword){
        emailPassword?.let {
            email.edit { replace(0,length,it.email) }
            password.edit { replace(0,length,it.password) }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(0.8f)
                .padding(top = spacing24)
                .verticalScroll(rememberScrollState()),
        ) {

            AnimatedContent(
                targetState = uiState.isInLogin, label = "",
                transitionSpec = {
                    fadeIn() togetherWith  fadeOut()
                }
            ) {
                Column {
                    TitleLargeText(
                        stringResource(uiState.title),
                        modifier = Modifier.padding(bottom = spacing2)
                    )

                    LabelSmallText(
                        stringResource(uiState.subtitle), textAlign = TextAlign.Left,
                        modifier = Modifier.padding(bottom = spacing6)
                    )
                }
            }

            Spacer(modifier = Modifier.padding(vertical = spacing4))

            CustomOutlinedTextField(
                rememberTextFieldState = email,
                labelPosition = TextFieldLabelPosition.Attached(),
                label = R.string.import_habit_screen_email,
                labelError = R.string.import_habit_screen_email_error,
                keyBoardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth(),
                isError = email.text.isEmpty() && wasFilledEmail.value,
            )

            Spacer(modifier = Modifier.padding(vertical = spacing4))

            OutlinedTextFieldPasswordLogin(
                label = R.string.import_habit_screen_password,
                labelError = R.string.import_habit_screen_password_error,
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth(),
                modifierError = Modifier.fillMaxWidth().padding(horizontal = spacing8),
                rememberTextFieldState = password,
                imeActionPassword = if(uiState.isInLogin) ImeAction.Done else ImeAction.Next,
                isError = password.text.isEmpty() && wasFilledPassword.value,
            )

            AnimatedVisibility(
                visible = !uiState.isInLogin,
            ) {
                Column {
                    Spacer(modifier = Modifier.padding(vertical = spacing4))

                    OutlinedTextFieldPasswordLogin(
                        label = R.string.import_habit_screen_remember_password,
                        labelError = R.string.import_habit_screen_password_error,
                        modifier = Modifier
                            .height(60.dp)
                            .fillMaxWidth(),
                        modifierError = Modifier.fillMaxWidth().padding(horizontal = spacing8),
                        rememberTextFieldState = rememberPassword,
                        imeActionPassword = ImeAction.Done,
                        isError = rememberPassword.text.isEmpty() && wasFilledRememberPassword.value,
                    )

                    Spacer(modifier = Modifier.padding(vertical = spacing8))
                }
            }

            AnimatedVisibility(
                visible = uiState.isInLogin,
            ) {
                Column {
                    CheckBoxButton(
                        checkedState = checkedState,
                        onStateChange = onStateChange
                    )

                    LabelMediumText(
                        stringResource(R.string.import_habit_forgot_password),
                        modifier = Modifier.clickable { resetPassword()},
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.padding(bottom = spacing16))
                }
            }

            CustomFilledButton(
                modifier = Modifier
                    .padding(top = spacing2)
                    .height(50.dp)
                    .fillMaxWidth(),
                title = R.string.buttons_accept,
                icon = null,
                color = MaterialTheme.colorScheme.onSurface,
                colorIcon = MaterialTheme.colorScheme.inverseOnSurface,
                colorText = MaterialTheme.colorScheme.inverseOnSurface,
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = spacing8,
                ),
                enabled = if(uiState.isInLogin) password.text.isNotEmpty() && email.text.isNotEmpty()
                    else email.text.isNotEmpty() && password.text.isNotEmpty() && rememberPassword.text == password.text,
                onClick = {
                    if(uiState.isInLogin){
                        signIn(email.text.toString(), password.text.toString(), checkedState)
                    }else{
                        signUp(email.text.toString(), password.text.toString())
                    }
                }
            )

            AnimatedVisibility(
                visible = uiState.isInLogin,
            ){
                Column {
                    LabelMediumText(
                        stringResource(R.string.import_habit_screen_continue),
                        modifier = Modifier
                            .padding(vertical = spacing16)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    GoogleCard() {
                        signInGoogle()
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = spacing16),
                horizontalArrangement = Arrangement.Center
            ) {
                AnimatedContent(
                    targetState = uiState.isInLogin, label = "",
                    transitionSpec = {
                        fadeIn() togetherWith  fadeOut()
                    }
                ) {
                    Row {
                        LabelMediumText(
                            stringResource(uiState.textSignIn), modifier = Modifier.padding(end = spacing2)
                        )

                        LabelMediumText(stringResource(uiState.textSignUp), fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable {
                                if(uiState.isInLogin) importHabitViewModel.setRegister()
                                else importHabitViewModel.setLogin()
                            })
                    }
                }
            }
        }
    }
}