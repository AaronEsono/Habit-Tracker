package aeb.proyecto.ui.topbar.providers

import aeb.proyecto.ui.topbar.TopBarViewModel
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry

/**
 * High-performance state injection node designed to push leading navigation icon layouts
 * (e.g., Back arrows or structural drawer anchors) into the host top app bar infrastructure.
 *
 * Binds the state life cycle directly to the active [NavBackStackEntry] to avoid cross-screen visual leaking.
 * Offers an explicit lifecycle [key] check to prevent unnecessary re-binding execution cycles.
 *
 * @param key The state tracking anchor parameter used to trigger or skip execution re-evaluation loops. Defaults to [Unit].
 * @param navigationIcon The declarative composable UI slot code containing the graphical leading icon representation.
 */
@Composable
fun ProvideAppBarNavigationIcon(
    key: Any? = Unit,
    navigationIcon: @Composable () -> Unit
) {
    // Resolve the nearby state warehouse owner safely from the tree environment
    val viewModelStoreOwner = LocalViewModelStoreOwner.current

    // Establish a defensive smart-cast boundary ensuring alignment with the active routing stack entry
    (viewModelStoreOwner as? NavBackStackEntry)?.let { owner ->
        // Retrieve or instantiate the centralized toolbar coordinator inside this navigation frame scope
        val viewModel: TopBarViewModel = viewModel(
            viewModelStoreOwner = owner,
            initializer = { TopBarViewModel() },
        )

        // Stream navigation asset mutations into the view model state slot under key evaluation constraints
        LaunchedEffect(key) {
            viewModel.navigationIcon = navigationIcon
        }
    }
}