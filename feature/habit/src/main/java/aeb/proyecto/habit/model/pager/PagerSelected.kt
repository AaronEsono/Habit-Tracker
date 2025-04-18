package aeb.proyecto.habit.model.pager

data class PagerSelected(
    val index:Int = 0,
    val pagerElement: PagerElement = PagerElement.DAILY
)