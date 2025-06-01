package aeb.proyecto.habittracker.components.bottomBars.bottomRail

import aeb.proyecto.habittracker.components.bottomBars.bottomRail.components.BottomRailIconResponsive
import aeb.proyecto.habittracker.components.bottomBars.bottomRail.components.LabelBottomRailResponsive
import aeb.proyecto.habittracker.navigation.menuItems
import aeb.proyecto.ui.controllerProvider.LocalNavController
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing3
import aeb.proyecto.ui.orientation.getOrientation
import aeb.proyecto.ui.text.LabelSmallText
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomRailHabit(){
    val navController = LocalNavController.current

    val menuItems = remember { menuItems() }

    val windowWidthSizeClass = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination
    val showBottomBar = currentDestination?.route in menuItems.map { it.route::class.qualifiedName }
    val orientation = getOrientation()

    AnimatedVisibility(
        visible = showBottomBar,
    ) {
        NavigationRail(
            containerColor = MaterialTheme.colorScheme.primary,
            windowInsets = WindowInsets(0, 0, 0, 0),
            modifier = Modifier.padding(horizontal = spacing3)
        ) {
            menuItems.forEach { menuItem ->
                NavigationRailItem(
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
                    icon = {BottomRailIconResponsive(menuItem.icon,windowWidthSizeClass,orientation)},
                    label = {LabelBottomRailResponsive(menuItem.title,windowWidthSizeClass,orientation)},
                    colors = NavigationRailItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.onSurface,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }
    }
}