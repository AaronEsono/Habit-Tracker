package aeb.proyecto.habittracker.components

import aeb.proyecto.ui.controllerProvider.LocalNavController
import aeb.proyecto.ui.topbar.TopBarViewModel
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarHabit() {
    val navController = LocalNavController.current
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    navBackStackEntry?.let { entry ->
        val viewModel: TopBarViewModel = viewModel(
            viewModelStoreOwner = entry,
            initializer = { TopBarViewModel() },
        )

        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            title = viewModel.title,
            actions = viewModel.actions,
            navigationIcon = viewModel.navigationIcon
        )
    }
}