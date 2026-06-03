package aeb.proyecto.ui.timer

/**
 * Static design system indexing repository compiling compilation-time primitive anchors
 * for the time-tracking domain modules.
 * * Maps structural layout index targets (e.g., TabRow coordinates or HorizontalPager slots)
 * to concrete functional time modalities, completely eliminating primitive magic numbers.
 */
object TimerIndex {

    /** Index assignment representing the progressive count-up execution module. */
    const val STOPWATCH = 0

    /** Index assignment representing the retrograde countdown target module. */
    const val TIMER = 1

    /** Index assignment representing the specialized structural work-rest loop cycle module. */
    const val INTERVAL = 2
}