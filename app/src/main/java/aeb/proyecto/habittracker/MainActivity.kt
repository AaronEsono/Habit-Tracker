package aeb.proyecto.habittracker

import aeb.proyecto.habittracker.ui.components.text.LabelSmallText
import aeb.proyecto.habittracker.ui.components.text.TitleLargeText
import aeb.proyecto.habittracker.ui.navigation.NavigationWrapper
import aeb.proyecto.habittracker.ui.navigation.listBottomBarScreens
import aeb.proyecto.habittracker.ui.theme.HabitTrackerTheme
import aeb.proyecto.habittracker.ui.theme.primaryColorApp
import aeb.proyecto.habittracker.ui.theme.secondaryColorApp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            HabitTrackerTheme {
                val navController = rememberNavController()
                val mainViewModel: MainViewModel = hiltViewModel()

                AppContent(navController)
            }
        }
    }
}

@Composable
fun AppContent(navController: NavHostController) {
    Scaffold(
        topBar = { TopBatHabit(navController) },
        bottomBar = {
            BottomNavigationHabit(navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(primaryColorApp)
        ) {
            NavigationWrapper(navController = navController)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBatHabit(navController: NavHostController) {

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = secondaryColorApp,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            TitleLargeText(
                text = stringResource(R.string.topbar_habit),
                textAlign = TextAlign.Center
            )
        }
    )
}

@Composable
fun BottomNavigationHabit(navController: NavHostController) {

    val menuItems = remember { listBottomBarScreens }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        contentColor = secondaryColorApp,
        containerColor = secondaryColorApp
    ) {
        menuItems.forEach {
            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any
                    {item -> item.route == it.route::class.qualifiedName } == true,
                onClick = {
                    navController.navigate(it.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(it.icon),
                        contentDescription = stringResource(it.label),
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    LabelSmallText(stringResource(it.label))
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Black,
                    indicatorColor = Color.White
                )
            )
        }
    }

}