package aeb.proyecto.habittracker.navigation

import aeb.proyecto.habit.navigation.Habit
import aeb.proyecto.habittracker.R
import aeb.proyecto.settings.navigation.Settings
import aeb.proyecto.statistics.navigation.Statistics
import aeb.proyecto.timer.navigation.Timer
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.serialization.Serializable

/**
 * Represents the foundational root-level destinations accessible from the primary
 * application navigation layout infrastructure (e.g., Bottom Navigation Bar or Navigation Rail).
 *
 * This structure leverages Kotlin's [Serializable] sealed classes to implement compile-time
 * **Type-Safe Navigation**, mapping each top-level entry point directly to its corresponding
 * architecture feature route contract.
 *
 * Consistent with Material 3 design ergonomics, the interface restricts core destinations
 * to a maximum of 4 primary viewports to ensure high discoverability and an optimal,
 * uncrowded touch target layout.
 *
 * @param T The explicit type configuration representing the feature's navigation contract route.
 * @property title The localized string resource pointer used for rendering the destination's textual label.
 * @property icon The drawable resource pointer representing the destination's visual indicator.
 * @property route The strongly-typed navigation destination object instance.
 */
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

/**
 * Supplies an immutable utility list containing all active top-level screen destinations
 * to feed the application's global navigation suite views.
 */
val menuItems = {
    listOf(
        TopLevelDestinations.HabitsBottom,
        TopLevelDestinations.StatisticsBottom,
        TopLevelDestinations.TimerBottom,
        TopLevelDestinations.SettingsBottom,
    )
}