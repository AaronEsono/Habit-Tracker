package aeb.proyecto.habittracker.ui.navigation

import aeb.proyecto.habittracker.MainViewModel
import aeb.proyecto.habittracker.ui.screens.addHabit.AddHabitScreen
import aeb.proyecto.habittracker.ui.screens.habits.HabitsScreen
import aeb.proyecto.habittracker.ui.screens.settings.SettingsScreen
import aeb.proyecto.habittracker.ui.screens.statistics.StatisticsScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavigationWrapper(navController: NavHostController,mainViewModel: MainViewModel){

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
            SettingsScreen()
        }
        composable<AddHabit>{backStackEntry ->
            val edit = backStackEntry.arguments?.getBoolean("edit") ?: false
            val id = backStackEntry.arguments?.getLong("id")

            AddHabitScreen(
                edit = edit,
                id = id,
                navigateToHabit = { navController.navigate(Habits) })
        }
    }

}