package aeb.proyecto.habittracker.data.model.action

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class TopbarSetUp(
    @StringRes var title:Int,
    var listAction:List<ActionIcon> = listOf()
)

data class ActionIcon(
    @DrawableRes val icon: Int,
    val onClick: () -> Unit,
)