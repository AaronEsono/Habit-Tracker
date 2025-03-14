package aeb.proyecto.login.navigation

import aeb.proyecto.login.LoginScreen
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object Login

fun NavController.navigateToLogin(optionsBuilder: NavOptionsBuilder.() -> Unit = {}){
    navigate(Login,optionsBuilder)
}

fun NavGraphBuilder.loginScreen(onSaveNavigate: () -> Unit) {
    composable<Login> {
        LoginScreen()
    }
}