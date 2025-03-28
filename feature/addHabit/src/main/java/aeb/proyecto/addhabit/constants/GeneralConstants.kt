package aeb.proyecto.addhabit.constants

import aeb.proyecto.addhabit.R
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

enum class TypeHabit(val id: Int, @StringRes val title: Int, @StringRes val subtitle: Int) {
    DAILY(1, R.string.add_habit_type_habit_daily, R.string.add_habit_type_habit_daily_label),
    WEEKLY(2, R.string.add_habit_type_habit_weekly, R.string.add_habit_type_habit_weekly_label),
    MONTHLY(3, R.string.add_habit_type_habit_monthly, R.string.add_habit_type_habit_monthly_label),
    CYCLIC(4, R.string.add_habit_type_habit_cyclic, R.string.add_habit_type_habit_cyclic_label)
}

enum class GridOption(){
    COLORS,
    ICONS
}

sealed class GridOptionResult(){
    data class colorResult(val color: Color): GridOptionResult()
    data class iconResult(val icon: ImageVector): GridOptionResult()
}

const val PICK_TYPE_HABIT = 1
const val PICK_DATE = 2
const val PICK_UNIT = 3
const val PICK_TYPE_NOTIFICATION = 4
const val PICK_NOTIFICATION = 5

val onlyDigits = "-?[0-9]+(\\\\.[0-9]+)?".toRegex()