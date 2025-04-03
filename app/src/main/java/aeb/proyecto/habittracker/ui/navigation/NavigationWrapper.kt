package aeb.proyecto.habittracker.ui.navigation

import aeb.proyecto.addhabit.navigation.addHabitScreen
import aeb.proyecto.addhabit.navigation.navigateToAddHabit
import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.data.model.action.TopbarSetUp
import aeb.proyecto.habittracker.ui.screens.habits.HabitsScreen
import aeb.proyecto.habittracker.ui.screens.statistics.StatisticsScreen
import aeb.proyecto.login.navigation.Login
import aeb.proyecto.login.navigation.loginScreen
import aeb.proyecto.login.navigation.navigateToLogin
import aeb.proyecto.save.navigation.Save
import aeb.proyecto.save.navigation.navigateToSave
import aeb.proyecto.save.navigation.saveScreen
import aeb.proyecto.settings.navigation.settingsScreen
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavigationWrapper(navController: NavHostController){

    NavHost(navController = navController, startDestination = Habits){
        composable<Habits>{
            HabitsScreen(){ id ->
                navController.navigateToAddHabit(id)
            }
        }
        composable<Statistics>{
            StatisticsScreen()
        }

        settingsScreen(
            onImportScreen = {navController.navigateToLogin()},
            onSaveScreen = {navController.navigateToSave()}
        )

        addHabitScreen {
            navController.navigate(Habits){
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
    }
}