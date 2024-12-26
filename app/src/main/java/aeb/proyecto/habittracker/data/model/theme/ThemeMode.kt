package aeb.proyecto.habittracker.data.model.theme

import androidx.annotation.StringRes

data class ThemeMode(
    val mode:Int,
    @StringRes val title:Int,
)