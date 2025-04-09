package aeb.proyecto.habittracker.navigation

import aeb.proyecto.habit.navigation.Habit
import aeb.proyecto.habittracker.R
import aeb.proyecto.settings.navigation.Settings
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlin.reflect.KClass


enum class TopLevelDestinations (
    @StringRes val titleTextId: Int,
    @DrawableRes val iconTextId: Int,
    val route: KClass<*>
){
    Habits(
        titleTextId = R.string.bottombar_habit,
        iconTextId = R.drawable.ic_calendar,
        route = Habit::class
    ),
    SETTINGS(
        titleTextId = R.string.bottombar_settins,
        iconTextId = R.drawable.ic_settings,
        route = Settings::class
    )
}