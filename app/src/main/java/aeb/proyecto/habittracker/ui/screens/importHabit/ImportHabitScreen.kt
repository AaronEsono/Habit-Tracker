package aeb.proyecto.habittracker.ui.screens.importHabit

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.ui.screens.importHabit.importComponents.ImportHabitComponents
import aeb.proyecto.habittracker.ui.screens.importHabit.importComponents.LoginScreenImportHabit
import aeb.proyecto.habittracker.utils.AuthenticationManager
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun ImportHabitScreen(importHabitViewModel: ImportHabitViewModel = hiltViewModel(), navigateToSave:() -> Unit) {

    val context = LocalContext.current
    val coroutine = rememberCoroutineScope()
    val uiState = importHabitViewModel.uiState.collectAsState().value
    val authentication = remember { AuthenticationManager(context) }

    LoginScreenImportHabit(
        importHabitViewModel = importHabitViewModel,

        signInGoogle = {
            importHabitViewModel.setLoading()

            authentication.signInWithGoogle().onEach {
                importHabitViewModel.handleSignInGoogle(it){
                    navigateToSave()
                }
            }.launchIn(coroutine)
        },

        signIn = { email, password, saveCredentials ->
            importHabitViewModel.setLoading()

            authentication.signInWithEmail(email, password).onEach { response ->
                importHabitViewModel.handleSignIn(response,email,password,saveCredentials){
                    navigateToSave()
                }
            }.launchIn(coroutine)
        },

        signUp = { email, password ->
            importHabitViewModel.setLoading()

            authentication.createAccountWithEmail(email, password).onEach { response ->
                importHabitViewModel.handleSignUp(response)
            }.launchIn(coroutine)
        },

        resetPassword = {
            importHabitViewModel.openPasswordDx()
        }
    )

    fun closeGeneralDx(){
        importHabitViewModel.closeGeneralDx()
        importHabitViewModel.setLogin()
    }

    ImportHabitComponents(uiState, onDismiss = {
        closeGeneralDx()
    },
        onAcceptGeneral = {
        if (uiState.subtitleDx == R.string.import_habit_create_account) {
            closeGeneralDx()
        } else {
            importHabitViewModel.setLoading()

            authentication.resendEmail().onEach {
                importHabitViewModel.handleResendEmail(it) {
                    Toast.makeText(context, R.string.import_habit_toast, Toast.LENGTH_SHORT).show()
                }
            }.launchIn(coroutine)
        }
    },
        onAcceptPassword = { email ->
            importHabitViewModel.setLoading()

            authentication.forgotPassword(email).onEach {
                importHabitViewModel.handleForgotPassword(it) {
                    Toast.makeText(context, R.string.import_habit_toast, Toast.LENGTH_SHORT).show()
                }
            }.launchIn(coroutine)
        },
        onDismissPassword = {
            importHabitViewModel.closePasswordDx()
        }
    )
}