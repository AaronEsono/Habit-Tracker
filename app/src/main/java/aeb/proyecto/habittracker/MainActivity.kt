package aeb.proyecto.habittracker

import aeb.proyecto.habittracker.data.model.action.ActionIcon
import aeb.proyecto.habittracker.data.model.action.TopbarSetUp
import aeb.proyecto.habittracker.ui.components.bottomSheets.BottomSheetGeneral
import aeb.proyecto.habittracker.ui.components.loading.LoadingScreen
import aeb.proyecto.habittracker.ui.components.text.LabelSmallText
import aeb.proyecto.habittracker.ui.components.text.TitleLargeText
import aeb.proyecto.habittracker.ui.navigation.AddHabit
import aeb.proyecto.habittracker.ui.navigation.Habits
import aeb.proyecto.habittracker.ui.navigation.ImportHabit
import aeb.proyecto.habittracker.ui.navigation.NavigationWrapper
import aeb.proyecto.habittracker.ui.navigation.SaveHabit
import aeb.proyecto.habittracker.ui.navigation.Settings
import aeb.proyecto.habittracker.ui.navigation.Statistics
import aeb.proyecto.habittracker.ui.navigation.listBottomBarScreens
import aeb.proyecto.habittracker.ui.theme.HabitTrackerTheme
import aeb.proyecto.habittracker.utils.AppState
import aeb.proyecto.habittracker.utils.Constans.permissions
import aeb.proyecto.habittracker.utils.MainLocalViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
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
        enableEdgeToEdge(navigationBarStyle = SystemBarStyle.dark(Color.Transparent.toArgb()))

        setContent {
            val mainViewModel: MainViewModel = hiltViewModel()
            val themeMode = mainViewModel.themeMode.collectAsState().value

            HabitTrackerTheme(themeMode){
                val navController = rememberNavController()

                RequestPermissions()
                SetStates(mainViewModel)

                CompositionLocalProvider(MainLocalViewModel provides mainViewModel) {
                    AppContent(navController)
                }
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

    val menuItems = remember { listBottomBarScreens }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = currentDestination?.hierarchy?.any { item ->
        menuItems.any {
            it.route::class.qualifiedName == item.route
        }
    }

    val title = setTopBarTitle(currentDestination, navController)

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        title = {
            TitleLargeText(
                text = stringResource(title.title),
                textAlign = TextAlign.Center
            )
        },
        actions = {
            title.listAction.forEach {
                IconButton(onClick = { it.onClick() }) {
                    Icon(
                        painter = painterResource(it.icon),
                        contentDescription = stringResource(R.string.topbar_description),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
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

@Composable
fun BottomNavigationHabit(navController: NavHostController) {

    val menuItems = remember { listBottomBarScreens }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = currentDestination?.hierarchy?.any { item ->
        menuItems.any {
            it.route::class.qualifiedName == item.route
        }
    }

    if (showBottomBar == true) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            menuItems.forEach {
                NavigationBarItem(
                    selected = currentDestination.hierarchy.any { item -> item.route == it.route::class.qualifiedName },
                    onClick = {
                        if(currentDestination.route != it.route::class.qualifiedName){
                            navController.navigate(it.route) {
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
                            painter = painterResource(it.icon),
                            contentDescription = stringResource(it.label),
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = {
                        LabelSmallText(stringResource(it.label))
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

fun setTopBarTitle(navDestination: NavDestination?, navController: NavHostController): TopbarSetUp {
    var title = TopbarSetUp(R.string.topbar_habit, listOf())

    when (navDestination?.route?.substringBefore("/")) {
        Habits::class.qualifiedName -> {
            title = TopbarSetUp(R.string.topbar_habit, listOf(
                ActionIcon(R.drawable.ic_add) {
                    navController.navigate(AddHabit(edit = false, id = null))
                }
            ))
        }

        Statistics::class.qualifiedName -> {
            title = TopbarSetUp(R.string.topbar_stadistics, listOf())
        }

        Settings::class.qualifiedName -> {
            title = TopbarSetUp(R.string.topbar_settings, listOf())
        }

        ImportHabit::class.qualifiedName -> {
            title = TopbarSetUp(R.string.topbar_import_habit, listOf())
        }

        SaveHabit::class.qualifiedName -> {
            title = TopbarSetUp(R.string.topbar_save_habit, listOf())
        }

        AddHabit::class.qualifiedName -> {
            val edit = navController.currentBackStackEntry?.arguments?.getBoolean("edit") ?: true
            val titleText = if (edit) R.string.tobbar_add_habit_true else R.string.topbar_add_habit

            title = TopbarSetUp(titleText, listOf())
        }
    }

    return title
}

@Composable
fun RequestPermissions(){
    val request = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ){}
    LaunchedEffect(Unit) {
        request.launch(permissions)
    }
}

@Composable
fun SetStates(mainViewModel: MainViewModel){

    when(val appState = mainViewModel.getState().appState.collectAsState().value){
        is AppState.Error -> {
            BottomSheetGeneral(
                title = R.string.general_dx_attention,
                colorIconAccept = MaterialTheme.colorScheme.inverseSurface,
                colorTextAccept = MaterialTheme.colorScheme.inverseSurface,
                subtitle = appState.messageInt,
                onCancel = { mainViewModel.setNeutral()},
                onDismiss = { mainViewModel.setNeutral()}
            )
        }
        AppState.Loading -> {
            LoadingScreen()
        }
        AppState.Neutral -> {}
    }
}