package aeb.proyecto.login.navigation

import aeb.proyecto.login.LoginScreen
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

/**
 * Navigation destination for the Login/Registration feature.
 * Marked as [Serializable] to support type-safe arguments in NavGraph.
 */
@Serializable
object Login

/**
 * Navigates to the Login screen.
 * * @param optionsBuilder Lambda to configure navigation options (e.g., popping the backstack).
 */
fun NavController.navigateToLogin(optionsBuilder: NavOptionsBuilder.() -> Unit = {}){
    navigate(Login,optionsBuilder)
}

/**
 * Adds the Login screen to the [NavGraphBuilder].
 *
 * @param onSaveNavigate Callback triggered when the authentication process
 * succeeds and the user needs to be redirected to the main application flow.
 */
fun NavGraphBuilder.loginScreen(onSaveNavigate: () -> Unit) {
    composable<Login> {
        LoginScreen(onSaveNavigate)
    }
}