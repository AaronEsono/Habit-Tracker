package aeb.proyecto.habittracker.data.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class NavigationClassItem<T>(
    @DrawableRes val icon:Int,
    @StringRes val title:Int,
    val optionNavigation: T
)