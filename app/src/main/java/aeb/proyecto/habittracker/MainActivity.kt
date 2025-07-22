package aeb.proyecto.habittracker

import aeb.proyecto.habittracker.components.bottomBars.BottomNavigationHabit
import aeb.proyecto.habittracker.components.bottomBars.bottomRail.BottomRailHabit
import aeb.proyecto.habittracker.components.dialog.ManageDialogScreen
import aeb.proyecto.habittracker.components.toast.ManageToastFinish
import aeb.proyecto.habittracker.components.topbar.TopBarHabit
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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
            val showDialogTimer = mainViewModel.showDialogTimer.collectAsStateWithLifecycle().value
            val toastFinishState = mainViewModel.showToast.collectAsStateWithLifecycle().value

            val navController = rememberNavController()

            LaunchedEffect(Unit){
                mainViewModel.setData()
            }

            HabitTrackerTheme(themeMode){
                RequestPermissions()

                ManageDialogScreen(
                    showDialogTimer,
                    onDismissRequest = { mainViewModel.closeDialog() },
                    onConfirm = {mainViewModel.updateHabit()}
                )

                ManageToastFinish(toastFinishState){
                    mainViewModel.clearToast()
                }

                AppContent(navController)
            }
        }
    }
}

@Composable
fun AppContent(navController: NavHostController) {
    CompositionLocalProvider(LocalNavController provides navController) {
        val suiteNavigation = suiteNavigation()

        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .padding(
                    PaddingValues(
                        end = WindowInsets.safeDrawing
                            .asPaddingValues()
                            .calculateEndPadding(LayoutDirection.Ltr),
                        start = WindowInsets.safeDrawing
                            .asPaddingValues()
                            .calculateStartPadding(LayoutDirection.Ltr)
                    )
                )
        ){
            if(suiteNavigation == NavigationSuiteType.NavigationRail){
                Box(
                    modifier = Modifier
                        .padding(
                            PaddingValues(
                                top = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding(),
                                bottom = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()
                            )
                        )
                ) {
                    BottomRailHabit()
                }
            }

            Scaffold(
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
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