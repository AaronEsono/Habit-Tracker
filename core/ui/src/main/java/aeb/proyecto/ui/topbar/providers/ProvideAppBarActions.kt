package aeb.proyecto.ui.topbar.providers

import aeb.proyecto.ui.topbar.TopBarViewModel
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry

/**
 * High-performance state injection node designed to push trailing action menus and command triggers
 * (e.g., Save anchors, settings buttons, or filtering dropdowns) into the host top app bar infrastructure.
 *
 * Exposes a specialized [RowScope] receiver block to ensure downstream assets align horizontally
 * in perfect compliance with Material Design 3 top bar spacing metrics.
 *
 * @param actions The declarative row-scoped composable UI slot code containing the trailing action layouts.
 */
@Composable
fun ProvideAppBarActions(actions: @Composable RowScope.() -> Unit) {

    // Resolve the nearby state warehouse owner safely from the tree environment
    val viewModelStoreOwner = LocalViewModelStoreOwner.current

    // Establish a defensive smart-cast boundary ensuring alignment with the active routing stack entry
    (viewModelStoreOwner as? NavBackStackEntry)?.let { owner ->
        // Retrieve or instantiate the centralized toolbar coordinator inside this navigation frame scope
        val viewModel: TopBarViewModel = viewModel(
            viewModelStoreOwner = owner,
            initializer = { TopBarViewModel() },
        )
        // Stream action matrix mutations reactively into the view model state slot whenever the closure updates
        LaunchedEffect(actions) {
            viewModel.actions = actions
        }
    }

}