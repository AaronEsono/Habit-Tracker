package aeb.proyecto.habit.model.pager

/**
 * Operational wrapper snapshot capturing index placements alongside their categorical references.
 */
data class PagerSelected(
    val index:Int = 0,
    val pagerElement: PagerElement = PagerElement.DAILY
)