package aeb.proyecto.habittracker.ui.navigation

import aeb.proyecto.habittracker.ui.screens.addHabit.AddHabitScreen
import aeb.proyecto.habittracker.ui.screens.habits.HabitsScreen
import aeb.proyecto.habittracker.ui.screens.importHabit.ImportHabitScreen
import aeb.proyecto.habittracker.ui.screens.saveHabit.SaveHabit
import aeb.proyecto.habittracker.ui.screens.statistics.StatisticsScreen
import aeb.proyecto.save.navigation.Save
import aeb.proyecto.save.navigation.navigateToSave
import aeb.proyecto.save.navigation.saveScreen
import aeb.proyecto.settings.navigation.settingsScreen
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
                navController.navigate(AddHabit(id))
            }
        }
        composable<Statistics>{
            StatisticsScreen()
        }

        settingsScreen(
            onImportScreen = {navController.navigate(ImportHabit)},
            onSaveScreen = {navController.navigateToSave()}
        )

        composable<AddHabit>{backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id")

            AddHabitScreen(
                id = id,
                navigateToHabit = {
                    navController.navigate(Habits) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable<ImportHabit>{
            ImportHabitScreen(){
                navController.navigate(Save){
                    popUpTo(ImportHabit) {
                        inclusive = true
                    }
                }
            }
        }
        saveScreen {
            navController.navigate(ImportHabit){
                popUpTo(Save) {
                    inclusive = true
                }
            }
        }
    }
}