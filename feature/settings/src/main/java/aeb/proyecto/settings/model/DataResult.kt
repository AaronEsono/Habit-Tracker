package aeb.proyecto.settings.model

import java.time.DayOfWeek

sealed class DataResult{
    data class ThemeResult(val theme:Int):DataResult()
    data class LanguageResult(val language:String):DataResult()
    data class DayOfWeekResult(val dayOfWeek:DayOfWeek):DataResult()
}