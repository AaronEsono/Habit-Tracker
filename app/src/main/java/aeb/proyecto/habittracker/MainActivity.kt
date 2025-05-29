package aeb.proyecto.habittracker

import aeb.proyecto.habittracker.components.TopBarHabit
import aeb.proyecto.habittracker.components.bottomBars.BottomNavigationHabit
import aeb.proyecto.habittracker.components.bottomBars.BottomRailHabit
import aeb.proyecto.habittracker.navigation.NavigationHabit
import aeb.proyecto.habittracker.navigation.suiteNavigation
import aeb.proyecto.habittracker.permissions.RequestPermissions
import aeb.proyecto.stopwatch.service.StopWatchService
import aeb.proyecto.ui.controllerProvider.LocalNavController
import aeb.proyecto.ui.theme.HabitTrackerTheme
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    private var isBound by mutableStateOf(false)
    private lateinit var stopwatchService: StopWatchService
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as StopWatchService.StopWatchBinder
            stopwatchService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            isBound = false
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, StopWatchService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        isBound = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(navigationBarStyle = SystemBarStyle.dark(Color.Transparent.toArgb()))

        setContent {
            val mainViewModel: MainViewModel = hiltViewModel()
            val themeMode = mainViewModel.themeMode.collectAsState().value
            val navController = rememberNavController()

            LaunchedEffect(Unit){
                mainViewModel.setData()
            }

            HabitTrackerTheme(themeMode){
                RequestPermissions()
                AppContent(navController)
            }
        }
    }
}

@Composable
fun AppContent(navController: NavHostController) {
    CompositionLocalProvider(LocalNavController provides navController) {
        val suiteNavigation = suiteNavigation()
        val showBottomRail = suiteNavigation == NavigationSuiteType.NavigationRail

        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .then(
                    if (showBottomRail)
                        Modifier.padding(WindowInsets.safeDrawing.asPaddingValues())
                    else
                        Modifier
                )
        ){
            if(suiteNavigation == NavigationSuiteType.NavigationRail){
                BottomRailHabit()
            }

            Scaffold(
                topBar = { TopBarHabit() },
                bottomBar = { if(suiteNavigation == NavigationSuiteType.NavigationBar)
                    BottomNavigationHabit()
                }
            ) { innerPadding ->
                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ){
                    NavigationHabit()
                }
            }
        }
    }
}