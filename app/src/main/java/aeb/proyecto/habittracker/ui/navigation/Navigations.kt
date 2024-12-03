package aeb.proyecto.habittracker.ui.navigation

import aeb.proyecto.habittracker.R
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.serialization.Serializable

@Serializable
object Habits

@Serializable
object Statistics

@Serializable
object Achievements

@Serializable
object Settings

@Serializable
object AddHabit

@Serializable
sealed class BottomBarScreens<T>(@StringRes val label:Int, @DrawableRes val icon:Int, val route:T){
    @Serializable
    data object HabitsBottom:BottomBarScreens<Habits>(R.string.bottombar_habit, R.drawable.ic_calendar, Habits)

    @Serializable
    data object StatisticsBottom:BottomBarScreens<Statistics>(R.string.bottombar_stadistics, R.drawable.ic_statistics, Statistics)

    @Serializable
    data object AchievementsBottom:BottomBarScreens<Achievements>(R.string.bottombar_achievements, R.drawable.ic_achievement, Achievements)

    @Serializable
    data object SettingsBottom:BottomBarScreens<Settings>(R.string.bottombar_settins, R.drawable.ic_settings, Settings)
}

val listBottomBarScreens = listOf(
    BottomBarScreens.HabitsBottom,
    BottomBarScreens.StatisticsBottom,
    BottomBarScreens.AchievementsBottom,
    BottomBarScreens.SettingsBottom
).map { it as BottomBarScreens<Any> }