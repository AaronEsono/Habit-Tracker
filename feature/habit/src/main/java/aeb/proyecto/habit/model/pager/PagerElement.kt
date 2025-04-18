package aeb.proyecto.habit.model.pager

import aeb.proyecto.habit.R
import aeb.proyecto.room.model.classes.DAILY_TAG
import aeb.proyecto.room.model.classes.MONTHLY_TAG
import aeb.proyecto.room.model.classes.RECURRING_TAG
import aeb.proyecto.room.model.classes.WEEKLY_TAG
import androidx.annotation.StringRes

enum class PagerElement(
    val tag:String,
    @StringRes val title:Int,
){
    DAILY(
        tag = DAILY_TAG,
        title = R.string.pager_element_daily
    ),
    WEEKLY(
        tag = WEEKLY_TAG,
        title = R.string.pager_element_weekly
    ),
    MONTHLY(
        tag = MONTHLY_TAG,
        title = R.string.pager_element_monthly
    ),
    RECURRING(
        tag = RECURRING_TAG,
        title = R.string.pager_element_recurring
    )
}

fun findPagerElement(tag:String): PagerElement {
    return PagerElement.entries.find { it.tag == tag } ?: PagerElement.DAILY
}

val orderPagerElements = listOf(
    PagerElement.DAILY,
    PagerElement.WEEKLY,
    PagerElement.MONTHLY,
    PagerElement.RECURRING
)