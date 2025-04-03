package aeb.proyecto.ui.topbar.providers

import aeb.proyecto.ui.topbar.TopBarViewModel
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry

@Composable
fun ProvideAppBarActions(actions: @Composable RowScope.() -> Unit) {

    val viewModelStoreOwner = LocalViewModelStoreOwner.current
    (viewModelStoreOwner as? NavBackStackEntry)?.let { owner ->
        val viewModel: TopBarViewModel = viewModel(
            viewModelStoreOwner = owner,
            initializer = { TopBarViewModel() },
        )
        LaunchedEffect(actions) {
            viewModel.actions = actions
        }
    }

}