package aeb.proyecto.addhabit.constants

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

enum class GridOption(){
    COLORS,
    ICONS
}

sealed class GridOptionResult(){
    data class colorResult(val color:Color): GridOptionResult()
    data class iconResult(val icon:ImageVector): GridOptionResult()
}