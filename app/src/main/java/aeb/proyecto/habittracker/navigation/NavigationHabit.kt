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
            onSaveScreen = {navController.navigateToSave()}
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

        timerScreen(
            navigateToHabit = {
                navController.navigateToHabit{
                    popUpTo(navController.graph.findStartDestination().id) {
                        inclusive = true
                    }
                }
            }
        )
    }
}