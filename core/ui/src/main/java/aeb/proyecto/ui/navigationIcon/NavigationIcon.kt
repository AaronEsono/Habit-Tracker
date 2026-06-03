package aeb.proyecto.ui.navigationIcon

import aeb.proyecto.ui.R
import aeb.proyecto.ui.controllerProvider.LocalNavController
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource

/**
 * Reusable navigation anchor node designed to trigger backwards stack pops.
 * Dynamically resolves the active enrouting framework utilizing implicit tree-scoped [LocalNavController]
 * context injections, completely removing parameter propagation overhead across top app bar nodes.
 *
 * Automatically couples its chromatic contrast token to the targeted semantic surface color profile.
 */
@Composable
fun NavigationIcon(){

    // Extract the implicit global routing vector safely from the composition environment
    val navController = LocalNavController.current

    IconButton(onClick = {
        // Execute an atomic stack frame destruction to transition back to the prior screen
        navController.popBackStack()
    }) {
        Icon(
            painter = painterResource(R.drawable.ic_arrow_back),
            contentDescription = "Icon navigation",
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}