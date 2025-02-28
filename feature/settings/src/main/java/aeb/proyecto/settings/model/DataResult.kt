package aeb.proyecto.settings.model

sealed class DataResult{
    data class ThemeResult(val theme:Int):DataResult()
    data class LanguageResult(val language:String):DataResult()
}