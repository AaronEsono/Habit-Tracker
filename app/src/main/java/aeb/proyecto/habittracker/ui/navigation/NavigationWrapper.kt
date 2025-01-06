package aeb.proyecto.habittracker.ui.navigation

import aeb.proyecto.habittracker.MainViewModel
import aeb.proyecto.habittracker.ui.screens.addHabit.AddHabitScreen
import aeb.proyecto.habittracker.ui.screens.habits.HabitsScreen
import aeb.proyecto.habittracker.ui.screens.importHabit.ImportHabitScreen
import aeb.proyecto.habittracker.ui.screens.saveHabit.SaveHabit
import aeb.proyecto.habittracker.ui.screens.settings.SettingsScreen
import aeb.proyecto.habittracker.ui.screens.statistics.StatisticsScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavigationWrapper(navController: NavHostController){

    NavHost(navController = navController, startDestination = Habits){
        composable<Habits>{
            HabitsScreen(){ id ->
                navController.navigate(AddHabit(true,id))
            }
        }
        composable<Statistics>{
            StatisticsScreen()
        }
        composable<Settings>{
            SettingsScreen(onImportScreen = {
                navController.navigate(ImportHabit)
            }, onSaveScreen = {
                navController.navigate(SaveHabit)
            })
        }
        composable<AddHabit>{backStackEntry ->
            val edit = backStackEntry.arguments?.getBoolean("edit") ?: false
            val id = backStackEntry.arguments?.getLong("id")

            AddHabitScreen(
                edit = edit,
                id = id,
                navigateToHabit = { navController.navigate(Habits){
                    popUpTo(navController.graph.findStartDestination().id) {
                        inclusive = true
                    }
                } })
        }
        composable<ImportHabit>{
            ImportHabitScreen(){
                navController.navigate(SaveHabit)
            }
        }
        composable<SaveHabit>{
            SaveHabit(){
                navController.navigate(Settings){
                    popUpTo(0) {
                        inclusive = true
                    }
                }
            }
        }
    }
}