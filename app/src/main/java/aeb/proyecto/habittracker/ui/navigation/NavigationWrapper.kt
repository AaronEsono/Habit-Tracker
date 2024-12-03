package aeb.proyecto.habittracker.ui.navigation

import aeb.proyecto.habittracker.ui.screens.habits.HabitsScreen
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavigationWrapper(navController: NavHostController){

    val context = LocalContext.current

    NavHost(navController = navController, startDestination = Habits){
        composable<Habits>{
            HabitsScreen()
        }
        composable<Statistics>{
            LaunchedEffect(Unit) {
                Toast.makeText(context, "Statistics", Toast.LENGTH_SHORT).show()
            }
        }
        composable<Achievements>{
            LaunchedEffect(Unit) {
                Toast.makeText(context, "Achievements", Toast.LENGTH_SHORT).show()
            }
        }
        composable<Settings>{
            LaunchedEffect(Unit) {
                Toast.makeText(context, "Settins", Toast.LENGTH_SHORT).show()
            }
        }
    }

}