package aeb.proyecto.ui.topbar.providers

import aeb.proyecto.ui.topbar.TopBarViewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry

/**
 * High-performance state injection node designed to push title layouts into the host top app bar infrastructure.
 * Safely extracts the active [NavBackStackEntry] contextual scope to bind mutations to the current
 * navigation destination frame, completely neutralizing cross-screen state leakage.
 *
 * Utilizes a [LaunchedEffect] boundary layer to stream updates cleanly outside drawing runtime cycles.
 *
 * @param title The declarative composable UI slot code containing the graphical heading representation.
 */
@Composable
fun ProvideAppBarTitle(title: @Composable () -> Unit) {

    // Resolve the nearby state warehouse owner safely from the tree environment
    val viewModelStoreOwner = LocalViewModelStoreOwner.current

    // Establish a defensive smart-cast boundary ensuring alignment with the active routing stack entry
    (viewModelStoreOwner as? NavBackStackEntry)?.let { owner ->
        // Retrieve or instantiate the centralized toolbar coordinator inside this navigation frame scope
        val viewModel: TopBarViewModel = viewModel(
            viewModelStoreOwner = owner,
            initializer = { TopBarViewModel() },
        )

        // Stream text/layout mutations reactively into the view model state slot whenever the closure updates
        LaunchedEffect(title) {
            viewModel.title = title
        }
    }

}