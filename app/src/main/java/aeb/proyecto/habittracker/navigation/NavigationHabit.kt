package aeb.proyecto.habittracker.navigation

import aeb.proyecto.addhabit.navigation.addHabitScreen
import aeb.proyecto.addhabit.navigation.navigateToAddHabit
import aeb.proyecto.habit.navigation.Habit
import aeb.proyecto.habit.navigation.habitScreen
import aeb.proyecto.habit.navigation.navigateToHabit
import aeb.proyecto.login.navigation.Login
import aeb.proyecto.login.navigation.loginScreen
import aeb.proyecto.login.navigation.navigateToLogin
import aeb.proyecto.save.navigation.Save
import aeb.proyecto.save.navigation.navigateToSave
import aeb.proyecto.save.navigation.saveScreen
import aeb.proyecto.settings.navigation.settingsScreen
import aeb.proyecto.statistics.navigation.statisticsScreen
import aeb.proyecto.timer.navigation.navigateToTimer
import aeb.proyecto.timer.navigation.timerScreen
import aeb.proyecto.ui.controllerProvider.LocalNavController
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import com.example.attributions.navigation.attributionsScreen
import com.example.attributions.navigation.navigateToAttributions

/**
 * The central Navigation Graph and [NavHost] orchestrator for the entire application.
 *
 * This Composable defines the routing topology by nesting feature-specific screen builders
 * (modularized via extension functions like [habitScreen], [statisticsScreen], etc.). It coordinates
 * deep-linking transitions and explicitly handles backstack cleanups using atomic encapsulation rules
 * (`popUpTo` with `inclusive = true`) during sensitive user flows, such as authentication switching
 * or returning to the root habit tracker viewport.
 *
 * The graph defaults to the strongly-typed [Habit] destination as its application-wide home anchor.
 */
@Composable
fun NavigationHabit(){
    val navController = LocalNavController.current

    NavHost(navController = navController, startDestination = Habit){
        habitScreen(
            onAddHabit = { id -> navController.navigateToAddHabit(id)},
            onClickTimer = {
                navController.navigateToTimer()
            }
        )

        statisticsScreen()

        settingsScreen(
            onImportScreen = {navController.navigateToLogin()},
            onSaveScreen = {navController.navigateToSave()},
            onAttributionsScreen = {navController.navigateToAttributions()}
        )

        addHabitScreen {
            navController.navigateToHabit{
                popUpTo(navController.graph.findStartDestination().id) {
                    inclusive = true
                }
            }
        }

        loginScreen{
            navController.navigateToSave{
                popUpTo(Login) {
                    inclusive = true
                }
            }
        }

        saveScreen {
            navController.navigateToLogin{
                popUpTo(Save) {
                    inclusive = true
                }
            }
        }

        timerScreen()

        attributionsScreen()
    }
}