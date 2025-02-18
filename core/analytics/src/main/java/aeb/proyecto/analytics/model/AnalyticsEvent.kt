package aeb.proyecto.analytics.model

data class AnalyticsEvent(
    val type: TypeLog,
    val extras: Map<String, String> = emptyMap()
)