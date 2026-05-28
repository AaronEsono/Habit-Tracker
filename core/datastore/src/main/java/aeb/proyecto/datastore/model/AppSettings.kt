package aeb.proyecto.datastore.model

import java.time.DayOfWeek

/**
 * Atomic data payload representing the global application runtime configuration snapshot.
 *
 * This data class models the immutable state boundary for localization, regional calendar topologies,
 * and user interface presentation modes. Bundling these preferences into a unified transaction model
 * prevents multi-threaded synchronization anomalies across the data storage boundary.
 *
 * @property themeMode Structural integer identifier mapping to the active visual presentation state
 * (e.g., 0 for System Default, 1 for Light Mode, 2 for Dark Mode). Defaults to `0`.
 * @property language ISO-639 structural language code sequence tracking localized asset configuration
 * overrides. Defaults to an empty string `""` to enforce initial automated system device discovery.
 * @property dayStartWeek The localized calendar system boundary key tracking the preferred chronological
 * first day of the week. Defaults to [DayOfWeek.MONDAY.name].
 */
data class AppSettings(
    val themeMode: Int = 0,
    val language: String = "",
    val dayStartWeek: String = DayOfWeek.MONDAY.name
)