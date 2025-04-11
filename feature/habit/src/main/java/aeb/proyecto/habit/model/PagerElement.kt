package aeb.proyecto.habit.model

import aeb.proyecto.habit.R
import androidx.annotation.StringRes

enum class PagerElement(
    val tag:String,
    @StringRes val title:Int,
){
    DAILY(
        tag = "DAILY",
        title = R.string.pager_element_daily
    ),
    WEEKLY(
        tag = "WEEKLY",
        title = R.string.pager_element_weekly
    ),
    MONTHLY(
        tag = "MONTHLY",
        title = R.string.pager_element_monthly
    ),
    RECURRING(
        tag = "RECURRING",
        title = R.string.pager_element_recurring
    )
}

fun findPagerElement(tag:String):PagerElement {
    return PagerElement.entries.find { it.tag == tag } ?: PagerElement.DAILY
}

fun findPossiblePagerElement(tag:String):PagerElement? {
    return PagerElement.entries.find { it.tag == tag }
}

val orderPagerElements = listOf(
    PagerElement.DAILY,
    PagerElement.WEEKLY,
    PagerElement.MONTHLY,
    PagerElement.RECURRING
)