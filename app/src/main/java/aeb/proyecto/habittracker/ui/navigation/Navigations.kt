package aeb.proyecto.habittracker.ui.navigation

import aeb.proyecto.habittracker.R
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.remember
import kotlinx.serialization.Serializable

@Serializable
object Habits

@Serializable
object Statistics

@Serializable
object Settings

@Serializable
data class AddHabit(val edit:Boolean, val id:Long?)

@Serializable
object ImportHabit

@Serializable
object SaveHabit

@Serializable
sealed class BottomBarScreens<T>(@StringRes val label:Int, @DrawableRes val icon:Int, val route:T){
    @Serializable
    data object HabitsBottom:BottomBarScreens<Habits>(R.string.bottombar_habit, R.drawable.ic_calendar, Habits)

    @Serializable
    data object StatisticsBottom:BottomBarScreens<Statistics>(R.string.bottombar_stadistics, R.drawable.ic_statistics, Statistics)

    @Serializable
    data object SettingsBottom:BottomBarScreens<Settings>(R.string.bottombar_settins, R.drawable.ic_settings, Settings)
}

val menuItems = {
    listOf(
        BottomBarScreens.HabitsBottom,
        BottomBarScreens.StatisticsBottom,
        BottomBarScreens.SettingsBottom,
    )
}