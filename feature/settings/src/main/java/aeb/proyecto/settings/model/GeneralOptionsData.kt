package aeb.proyecto.settings.model

import androidx.core.text.util.LocalePreferences.FirstDayOfWeek
import java.time.DayOfWeek

data class GeneralOptionsData(
    val firstDayOfWeek: DayOfWeek = DayOfWeek.MONDAY
)