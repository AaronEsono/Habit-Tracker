package aeb.proyecto.ui.month

import aeb.proyecto.ui.R
import androidx.annotation.StringRes

enum class EnumMonths (val value:Int,@StringRes val title:Int){
    JANUARY(1,R.string.month_1),
    FEBRUARY(2,R.string.month_2),
    MARCH(3,R.string.month_3),
    APRIL(4,R.string.month_4),
    MAY(5,R.string.month_5),
    JUNE(6,R.string.month_6),
    JULY(7,R.string.month_7),
    AUGUST(8,R.string.month_8),
    SEPTEMBER(9,R.string.month_9),
    OCTOBER(10,R.string.month_10),
    NOVEMBER(11,R.string.month_11),
    DECEMBER(12, R.string.month_12)
}

fun getMonth(monthValue: Int): Int {
    return EnumMonths.entries.find { month -> month.value == monthValue }?.title ?: R.string.month_1
}