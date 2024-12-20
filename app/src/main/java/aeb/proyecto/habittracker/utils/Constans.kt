package aeb.proyecto.habittracker.utils

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.ui.theme.pickColor1
import aeb.proyecto.habittracker.ui.theme.pickColor2
import aeb.proyecto.habittracker.ui.theme.pickColor3
import aeb.proyecto.habittracker.ui.theme.pickColor4
import aeb.proyecto.habittracker.ui.theme.pickColor5
import aeb.proyecto.habittracker.ui.theme.pickColor6
import aeb.proyecto.habittracker.ui.theme.pickColor7
import aeb.proyecto.habittracker.ui.theme.pickColor8
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessAlarm
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Headset
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Shower
import androidx.compose.material.icons.filled.SmartToy
import java.time.DayOfWeek

object Constans {

    val onlyDigits = "-?[0-9]+(\\\\.[0-9]+)?".toRegex()

    val InPlural = listOf(
        "","0","1"
    )

    val dayOfWeek = DayOfWeek.MONDAY.value
    val numberOfDays = 90

    val ListColors = listOf(
        pickColor1,
        pickColor2,
        pickColor3,
        pickColor4,
        pickColor5,
        pickColor6,
        pickColor7,
        pickColor8
    )

    private val IconsPr = Icons.Filled
    val ListIcons = listOf(
        IconsPr.AccessTimeFilled,
        IconsPr.AccessAlarm,
        IconsPr.ShoppingCart,
        IconsPr.Shower,
        IconsPr.Headset,
        IconsPr.DirectionsCar,
        IconsPr.Call,
        IconsPr.SmartToy
    )

    const val PICK = 1
    const val CHOOSE = 2

    enum class DaysWeek(
        val id: Int,
        val day: Int,
    ){
        MONDAY(1, R.string.day_1),
        TUESDAY(2, R.string.day_2),
        WEDNESDAY(3, R.string.day_3),
        THURSDAY(4, R.string.day_4),
        FRIDAY(5, R.string.day_5),
        SATURDAY(6, R.string.day_6),
        SUNDAY(7, R.string.day_7)
    }

    enum class Units(
        val id: Int,
        @DrawableRes val icon: Int,
        @StringRes val title: Int,
        val action: Int,
        @StringRes val question: Int,
        @StringRes val pluralTitle: Int,
        needTimer: Boolean
    ) {
        TIMES(
            1,
            R.drawable.ic_time,
            R.string.add_habit_unit_1,
            PICK,
            R.string.add_habit_question_1,
            R.string.add_habit_unit_pl_1,
            false
        ),
        STEPS(
            2,
            R.drawable.ic_step,
            R.string.add_habit_unit_2,
            CHOOSE,
            R.string.add_habit_question_2,
            R.string.add_habit_unit_pl_2,
            false
        ),
        PAGES(
            3,
            R.drawable.ic_book,
            R.string.add_habit_unit_3,
            CHOOSE,
            R.string.add_habit_question_3,
            R.string.add_habit_unit_pl_3,
            false
        ),
        KM(
            4,
            R.drawable.ic_km,
            R.string.add_habit_unit_4,
            CHOOSE,
            R.string.add_habit_question_4,
            R.string.add_habit_unit_pl_4,
            false
        ),
        MINUTES(
            5,
            R.drawable.ic_minutes,
            R.string.add_habit_unit_5,
            CHOOSE,
            R.string.add_habit_question_5,
            R.string.add_habit_unit_pl_5,
            true
        ),
        CALORIES(
            6,
            R.drawable.ic_calorie,
            R.string.add_habit_unit_6,
            CHOOSE,
            R.string.add_habit_question_6,
            R.string.add_habit_unit_pl_6,
            true
        ),
        GLASSES(
            7,
            R.drawable.ic_glass,
            R.string.add_habit_unit_7,
            PICK,
            R.string.add_habit_question_7,
            R.string.add_habit_unit_pl_7,
            false
        ),
        EXERCISES(
            8,
            R.drawable.ic_exercise,
            R.string.add_habit_unit_8,
            CHOOSE,
            R.string.add_habit_question_8,
            R.string.add_habit_unit_pl_8,
            false
        )
    }

    val permissions = android.Manifest.permission.POST_NOTIFICATIONS



}