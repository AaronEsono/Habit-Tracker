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

@Composable
fun ProvideAppBarNavigationIcon(
    key:Any? = Unit,
    navigationIcon: @Composable () -> Unit) {
    val viewModelStoreOwner = LocalViewModelStoreOwner.current
    (viewModelStoreOwner as? NavBackStackEntry)?.let { owner ->
        val viewModel: TopBarViewModel = viewModel(
            viewModelStoreOwner = owner,
            initializer = { TopBarViewModel() },
        )
        LaunchedEffect(key) {
            viewModel.navigationIcon = navigationIcon
        }
    }
}