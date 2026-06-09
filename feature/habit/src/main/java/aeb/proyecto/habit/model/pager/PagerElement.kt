package aeb.proyecto.habit.model.pager

import aeb.proyecto.habit.R
import aeb.proyecto.room.model.classes.DAILY_TAG
import aeb.proyecto.room.model.classes.MONTHLY_TAG
import aeb.proyecto.room.model.classes.RECURRING_TAG
import aeb.proyecto.room.model.classes.WEEKLY_TAG
import androidx.annotation.StringRes

/**
 * Enterprise-grade structural definitions mapping navigational nodes and localized resource signatures.
 */
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

/**
 * Evaluates raw identification tags defensively to resolve their corresponding Enum instances.
 * Falls back safely to the standard daily index trace if database records return unknown signatures.
 */
fun findPagerElement(tag:String): PagerElement {
    return PagerElement.entries.find { it.tag == tag } ?: PagerElement.DAILY
}

/**
 * Absolute inmutable structural matrix tracking the presentation order of categories inside layout tab rows.
 */
val orderPagerElements = listOf(
    PagerElement.DAILY,
    PagerElement.WEEKLY,
    PagerElement.MONTHLY,
    PagerElement.RECURRING
)