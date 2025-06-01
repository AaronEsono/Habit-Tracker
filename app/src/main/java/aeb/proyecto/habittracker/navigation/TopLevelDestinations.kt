package aeb.proyecto.habittracker.navigation

import aeb.proyecto.habit.navigation.Habit
import aeb.proyecto.habit.navigation.navigateToHabit
import aeb.proyecto.habittracker.R
import aeb.proyecto.settings.navigation.Settings
import aeb.proyecto.settings.navigation.navigateToSettings
import aeb.proyecto.statistics.navigation.Statistics
import aeb.proyecto.statistics.navigation.navigateToStatistics
import aeb.proyecto.timer.navigation.Timer
import aeb.proyecto.timer.navigation.navigateToTimer
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.navigation.NavHostController
import kotlinx.serialization.Serializable
import java.io.Serial
import kotlin.reflect.KClass

@Serializable
sealed class TopLevelDestinations<T>(@StringRes val title:Int, @DrawableRes val icon:Int, val route:T){
    @Serializable
    data object HabitsBottom:TopLevelDestinations<Habit>(R.string.bottombar_habit, R.drawable.ic_calendar, Habit)

    @Serializable
    data object StatisticsBottom:TopLevelDestinations<Statistics>(R.string.bottombar_stadistics, R.drawable.ic_statistics, Statistics)

    @Serializable
    data object TimerBottom: TopLevelDestinations<Timer>(R.string.bottombar_timer, R.drawable.ic_timer, Timer)

    @Serializable
    data object SettingsBottom:TopLevelDestinations<Settings>(R.string.bottombar_settins, R.drawable.ic_settings, Settings)
}

val menuItems = {
    listOf(
        TopLevelDestinations.HabitsBottom,
        TopLevelDestinations.StatisticsBottom,
        TopLevelDestinations.TimerBottom,
        TopLevelDestinations.SettingsBottom,
    )
}