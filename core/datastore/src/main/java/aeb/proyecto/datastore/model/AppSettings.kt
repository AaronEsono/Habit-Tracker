package aeb.proyecto.datastore.model

import java.time.DayOfWeek

data class AppSettings(
    val themeMode: Int = 0,
    val language: String = "",
    val dayStartWeek: String = DayOfWeek.MONDAY.name
)