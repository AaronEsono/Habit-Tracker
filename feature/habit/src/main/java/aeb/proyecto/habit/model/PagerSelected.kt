package aeb.proyecto.habit.model

data class PagerSelected(
    val index:Int = 0,
    val pagerElement: PagerElement = PagerElement.DAILY
)