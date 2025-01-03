package aeb.proyecto.habittracker.ui.screens.importHabit

import aeb.proyecto.habittracker.ui.screens.importHabit.importComponents.LoginScreenImportHabit
import aeb.proyecto.habittracker.utils.AuthResponse
import aeb.proyecto.habittracker.utils.AuthenticationManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun ImportHabitScreen(importHabitViewModel: ImportHabitViewModel = hiltViewModel()) {

    val context = LocalContext.current
    val coroutine = rememberCoroutineScope()
    val authentication = remember { AuthenticationManager(context) }

    LoginScreenImportHabit(
        importHabitViewModel = importHabitViewModel,
        signInGoogle = {
            importHabitViewModel.setLoading()

            authentication.signInWithGoogle().onEach {
                importHabitViewModel.handleSignInGoogle(it)
            }.launchIn(coroutine)
        },
        signIn = { email, password ->
            importHabitViewModel.setLoading()

            authentication.signInWithEmail(email, password).onEach { response ->
                importHabitViewModel.handleSignIn(response)
            }.launchIn(coroutine)
        },
        signUp = { email, password ->
            importHabitViewModel.setLoading()

            authentication.createAccountWithEmail(email, password).onEach { response ->
                importHabitViewModel.handleSignUp(response)
            }.launchIn(coroutine)
        }
    )
}