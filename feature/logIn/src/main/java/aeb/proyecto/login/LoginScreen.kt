package aeb.proyecto.login

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LoginScreen(
    onSaveNavigate: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
){
    LoginScreen()
}

@Composable
internal fun LoginScreen(){

}