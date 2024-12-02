package aeb.proyecto.habittracker.utils

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.data.model.NavigationClassItem
import aeb.proyecto.habittracker.ui.navigation.Achievements
import aeb.proyecto.habittracker.ui.navigation.Habits
import aeb.proyecto.habittracker.ui.navigation.Settings
import aeb.proyecto.habittracker.ui.navigation.Statistics

object Constans {

    val NAVIGATIONBARITEMS = listOf(
        NavigationClassItem(
            icon = R.drawable.ic_calendar,
            title = R.string.bottombar_habit,
            Habits
        ),
        NavigationClassItem(
            icon = R.drawable.ic_statistics,
            title = R.string.bottombar_stadistics,
            Statistics
        ),
        NavigationClassItem(
            icon = R.drawable.ic_achievement,
            title = R.string.bottombar_achievements,
            Achievements
        ),
        NavigationClassItem(
            icon = R.drawable.ic_settings,
            title = R.string.bottombar_settins,
            Settings
        )
    )

}