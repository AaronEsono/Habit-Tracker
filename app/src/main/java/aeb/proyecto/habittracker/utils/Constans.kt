package aeb.proyecto.habittracker.utils

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.data.model.NavigationClassItem

object Constans {

    val NAVIGATIONBARITEMS = listOf(
        NavigationClassItem(
            icon = R.drawable.ic_calendar,
            title = R.string.bottombar_habit
        ),
        NavigationClassItem(
            icon = R.drawable.ic_statistics,
            title = R.string.bottombar_stadistics
        ),
        NavigationClassItem(
            icon = R.drawable.ic_achievement,
            title = R.string.bottombar_achievements
        ),
        NavigationClassItem(
            icon = R.drawable.ic_settings,
            title = R.string.bottombar_settins
        )
    )

}