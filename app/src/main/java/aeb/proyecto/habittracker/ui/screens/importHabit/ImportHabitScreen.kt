package aeb.proyecto.habittracker.ui.screens.importHabit

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.ui.screens.importHabit.importComponents.ImportHabitComponents
import aeb.proyecto.habittracker.ui.screens.importHabit.importComponents.LoginScreenImportHabit
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ImportHabitScreen(importHabitViewModel: ImportHabitViewModel = hiltViewModel(), navigateToSave:() -> Unit) {

    val context = LocalContext.current
    val uiState = importHabitViewModel.uiState.collectAsState().value

    LoginScreenImportHabit(
        importHabitViewModel = importHabitViewModel,

        signInGoogle = {
            importHabitViewModel.signInGoogle {
                navigateToSave()
            }
        },
        signIn = { email, password, saveCredentials ->
            importHabitViewModel.signIn(email, password, saveCredentials) {
                navigateToSave()
            }
        },
        signUp = { email, password ->
            importHabitViewModel.signUp(email, password)
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
            importHabitViewModel.resendEmail(){
                Toast.makeText(context, R.string.import_habit_resend_email, Toast.LENGTH_SHORT).show()
            }
        }
    },
        onAcceptPassword = { email ->
            importHabitViewModel.forgotPassword(email){
                Toast.makeText(context, R.string.import_habit_forgot_password, Toast.LENGTH_SHORT).show()
            }
        },
        onDismissPassword = {
            importHabitViewModel.closePasswordDx()
        }
    )
}