package aeb.proyecto.habittracker.data.model.state

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.ui.theme.pickColors
import aeb.proyecto.habittracker.utils.Constans
import aeb.proyecto.habittracker.utils.Constans.ListIcons
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class AddHabitScreenState(
    var colorSelected: Boolean = false,
    var iconSelected: Boolean = false,
    var showBottomSheet: Boolean = false,
    var showTimePicker: Boolean = false,
    var showGeneralDx: Boolean = false,
    var color: Color = pickColors[0],
    var icon: ImageVector = ListIcons[0],
    var unitPicked: Constans.Units = Constans.Units.TIMES,
    var attentionText: Int = R.string.general_dx_attention_subtitile_time_picker
)