package aeb.proyecto.ui.navigationIcon

import aeb.proyecto.ui.R
import aeb.proyecto.ui.controllerProvider.LocalNavController
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource

@Composable
fun NavigationIcon(){

    val navController = LocalNavController.current

    IconButton(onClick = {
        navController.popBackStack()
    }) {
        Icon(
            painter = painterResource(R.drawable.ic_arrow_back),
            contentDescription = "Icon navigation",
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}