package aeb.proyecto.timer.navigation

import aeb.proyecto.timer.TimerScreen
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class Timer(val id:Long, val date:String)

fun NavController.navigateToTimer(
    id: Long,
    date: String,
    optionsBuilder: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(Timer(id, date), optionsBuilder)
}

fun NavGraphBuilder.timerScreen(navigateToHabit: () -> Unit){
    composable<Timer> { backStackEntry ->
        val idHabit = backStackEntry.arguments?.getLong("id") ?: 1L
        val date = convertToDate(backStackEntry.arguments?.getString("date") ?: "")

        TimerScreen(dayHabitId = Pair(idHabit, date), navigateToHabitScreen = navigateToHabit)
    }
}

fun convertToDate(date: String): LocalDate {
    return try{
        LocalDate.parse(date)
    }catch (e:Exception){
        LocalDate.now()
    }
}