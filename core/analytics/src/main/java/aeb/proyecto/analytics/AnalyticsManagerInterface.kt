package aeb.proyecto.analytics

import aeb.proyecto.analytics.model.AnalyticsEvent

interface AnalyticsManagerInterface {
    fun logEvent(event: AnalyticsEvent)
}