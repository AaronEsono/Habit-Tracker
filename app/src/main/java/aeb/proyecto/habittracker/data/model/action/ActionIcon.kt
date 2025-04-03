package aeb.proyecto.habittracker.data.model.action

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable

data class TopbarSetUp(
    @StringRes var title:Int,
    var listAction:List< @Composable () -> Unit> = listOf()
)