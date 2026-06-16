package aeb.proyecto.settings.model

import java.time.DayOfWeek

/**
 * Encapsulates the user's selection from a dialog.
 * Used by the ViewModel to determine which logic branch to execute.
 */
sealed class DataResult{
    data class ThemeResult(val theme:Int):DataResult()
    data class LanguageResult(val language:String):DataResult()
    data class DayOfWeekResult(val dayOfWeek:DayOfWeek):DataResult()
}