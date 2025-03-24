package aeb.proyecto.addhabit.constants

import aeb.proyecto.addhabit.R
import androidx.annotation.StringRes

enum class TypeHabit(val id: Int, @StringRes val title: Int, @StringRes val subtitle: Int) {
    DAILY(1, R.string.add_habit_type_habit_daily, R.string.add_habit_type_habit_daily_label),
    WEEKLY(2, R.string.add_habit_type_habit_weekly, R.string.add_habit_type_habit_weekly_label),
    MONTHLY(3, R.string.add_habit_type_habit_monthly, R.string.add_habit_type_habit_monthly_label),
    CYCLIC(4, R.string.add_habit_type_habit_cyclic, R.string.add_habit_type_habit_cyclic_label)
}