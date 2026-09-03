package aeb.proyecto.habittracker

import aeb.proyecto.habittracker.components.bottomBars.BottomNavigationHabit
import aeb.proyecto.habittracker.components.bottomBars.bottomRail.BottomRailHabit
import aeb.proyecto.habittracker.components.dialog.ManageDialogScreen
import aeb.proyecto.habittracker.components.onboardScreen.OnboardScreen
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
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
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

// Por hacer antes de la pantalla de bienvenida y crear la cuenta de desarrolador
// Investigar sobre la seguridad y ofuscacion del codigo


/**
 * The primary host [ComponentActivity] and single-activity entry point for the application.
 *
 * Grounded via [@AndroidEntryPoint] to enable Dagger Hilt dependency provisioning, this class
 * acts as the operational bridge between native Android system services and the Jetpack Compose engine.
 * It manages the lifecycle-aware execution of a bound background [StopWatchService] to guarantee
 * uninterrupted habit progression tracking when the application scales to background contexts or system sleep states.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    /**
     * Reflects whether the activity holds an active IPC link to the background timer service.
     */
    private var isBound by mutableStateOf(false)
    private lateinit var stopwatchService: StopWatchService

    /**
     * Intercepts service connection lifecycle updates to establish a communication channel
     * with the bound tracking service.
     */
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

    /**
     * Binds the application instance to the tracking service as it transitions to the foreground.
     */
    override fun onStart() {
        super.onStart()
        Intent(this, StopWatchService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    /**
     * Unbinds the service link to prevent memory leaks when the UI layer is no longer visible.
     */
    override fun onStop() {
        super.onStop()
        unbindService(connection)
        isBound = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Establishes modern, system-level Edge-to-Edge immersion parameters
        enableEdgeToEdge(navigationBarStyle = SystemBarStyle.dark(Color.Transparent.toArgb()))

        setContent {
            val mainViewModel: MainViewModel = hiltViewModel()
            val themeMode = mainViewModel.themeMode.collectAsState().value
            val showDialogTimer = mainViewModel.showDialogTimer.collectAsStateWithLifecycle().value
            val toastFinishState = mainViewModel.showToast.collectAsStateWithLifecycle().value
            val showOnboardScreen = mainViewModel.showOnboardScreen.collectAsStateWithLifecycle().value
            val onboardingPageSelected = mainViewModel.onboardingPageSelected.collectAsStateWithLifecycle().value

            val navController = rememberNavController()

            // Safe baseline metadata loading single-shot event trigger
            LaunchedEffect(Unit){
                mainViewModel.setData()
            }

            HabitTrackerTheme(themeMode){
                // Request critical runtime permissions for notifications
                RequestPermissions()

                // Intercept and resolve pending background tracking time syncs
                ManageDialogScreen(
                    showDialogTimer,
                    onDismissRequest = { mainViewModel.closeDialog() },
                    onConfirm = {mainViewModel.updateHabit()}
                )

                // Render operational completion notifications
                ManageToastFinish(toastFinishState){
                    mainViewModel.clearToast()
                }

                // Render onboarding screen or the content of the app
                AnimatedContent(
                    targetState = showOnboardScreen,
                    transitionSpec = {
                        if (targetState) {
                            (fadeIn(animationSpec = tween(400)) + scaleIn(initialScale = 0.95f))
                                .togetherWith(fadeOut(animationSpec = tween(300)) + scaleOut(targetScale = 1.05f))
                        } else {
                            (fadeIn(animationSpec = tween(durationMillis = 400, delayMillis = 100)) + scaleIn(initialScale = 0.92f, animationSpec = tween(400)))
                                .togetherWith(fadeOut(animationSpec = tween(300)) + scaleOut(targetScale = 0.96f, animationSpec = tween(300)))
                        }
                    },
                ) { showOnboardState ->
                    when (showOnboardState) {
                        true -> {
                            OnboardScreen(
                                pageSelected = onboardingPageSelected,
                                onClickResultOption = { option ->
                                    mainViewModel.manageResultOptionOnboardingPage(
                                        option
                                    )
                                }
                            )
                        }

                        false -> {
                            AppContent(navController)
                        }
                    }
                }

            }
        }
    }
}

/**
 * The master root structural layout orchestrator for the entire user interface.
 *
 * This Composable establishes the baseline UI tree by registering the active [NavHostController]
 * into a scoped dependency context via [CompositionLocalProvider], effectively neutralizing parameter
 * propagation overhead (*prop drilling*) across deep downstream view branches.
 *
 * It monitors adaptive screen metrics in real time via [suiteNavigation] to structurally mutate
 * the orientation layout paradigm:
 * * - **Large Screens/Landscape:** Injects a persistent [BottomRailHabit] via an external structural [Row].
 * - **Compact Devices/Portrait:** Dynamically binds [BottomNavigationHabit] inside the native [Scaffold] bottom boundary slot.
 *
 * Additionally, it enforces aggressive, surgical [WindowInsets.safeDrawing] padding calculations to
 * guarantee an edge-to-edge rendering flow completely isolated from display hardware intrusions
 * (such as system gesture bars or camera cutouts).
 *
 * @param navController The central [NavHostController] governing the lifecycle graph routing behaviors.
 */
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