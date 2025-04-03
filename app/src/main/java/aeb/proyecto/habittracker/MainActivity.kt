package aeb.proyecto.habittracker

import aeb.proyecto.addhabit.navigation.AddHabit
import aeb.proyecto.addhabit.navigation.navigateToAddHabit
import aeb.proyecto.habittracker.components.BottomNavigationHabit
import aeb.proyecto.habittracker.data.model.action.TopbarSetUp
import aeb.proyecto.habittracker.permissions.RequestPermissions
import aeb.proyecto.habittracker.ui.components.text.LabelSmallText
import aeb.proyecto.habittracker.ui.components.text.TitleLargeText
import aeb.proyecto.habittracker.ui.navigation.Habits
import aeb.proyecto.habittracker.ui.navigation.NavigationWrapper
import aeb.proyecto.habittracker.ui.navigation.Statistics
import aeb.proyecto.habittracker.ui.navigation.menuItems
import aeb.proyecto.login.navigation.Login
import aeb.proyecto.save.navigation.Save
import aeb.proyecto.settings.navigation.Settings
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.LabelMediumText
import aeb.proyecto.ui.theme.HabitTrackerTheme
import aeb.proyecto.ui.topbar.TopBarViewModel
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(navigationBarStyle = SystemBarStyle.dark(Color.Transparent.toArgb()))

        setContent {
            val mainViewModel: MainViewModel = hiltViewModel()
            val themeMode = mainViewModel.themeMode.collectAsState().value
            val navController = rememberNavController()

            HabitTrackerTheme(themeMode){
                RequestPermissions()
                AppContent(navController)
            }
        }
    }
}

@Composable
fun AppContent(navController: NavHostController) {
    Scaffold(
        topBar = { TopBarHabit(navController) },
        bottomBar = { BottomNavigationHabit(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            NavigationWrapper(navController = navController)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarHabit(navController: NavHostController) {

    val menuItems = remember { menuItems() }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = currentDestination?.hierarchy?.any { item ->
        menuItems.any {
            it.route::class.qualifiedName == item.route
        }
    }

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
            navigationIcon = {
                if (showBottomBar == false) {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = stringResource(R.string.topbar_description),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        )
    }

}