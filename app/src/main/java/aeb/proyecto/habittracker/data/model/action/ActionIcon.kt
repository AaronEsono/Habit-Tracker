package aeb.proyecto.habittracker.data.model.action

import androidx.annotation.DrawableRes

data class ActionIcon(
    @DrawableRes val icon: Int,
    val onClick: () -> Unit,
)