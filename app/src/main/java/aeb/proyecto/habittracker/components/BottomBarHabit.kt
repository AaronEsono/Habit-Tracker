package aeb.proyecto.habittracker.components

import aeb.proyecto.habittracker.ui.components.text.LabelSmallText
import aeb.proyecto.habittracker.ui.navigation.menuItems
import aeb.proyecto.ui.controllerProvider.LocalNavController
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationHabit() {
    val navController = LocalNavController.current

    val menuItems = remember { menuItems() }

    val currentDestination = navController.currentBackStackEntryAsState().value?.destination
    val showBottomBar = currentDestination?.route in menuItems.map { it.route::class.qualifiedName }

    if (showBottomBar) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            menuItems.forEach { menuItem ->
                NavigationBarItem(
                    selected = currentDestination?.route == menuItem.route::class.qualifiedName,
                    onClick = {
                        if(currentDestination?.route != menuItem.route::class.qualifiedName){
                            navController.navigate(menuItem.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    icon = {
                        Icon(
                            painter = painterResource(menuItem.icon),
                            contentDescription = stringResource(menuItem.label),
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = {
                        LabelSmallText(stringResource(menuItem.label))
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.onSurface,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }
    }
}