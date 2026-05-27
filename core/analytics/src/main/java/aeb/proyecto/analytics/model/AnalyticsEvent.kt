package aeb.proyecto.analytics.model

/**
 * A core immutable data transfer object representing a unified telemetry event framework.
 *
 * This data class serves as the standardized semantic container across the entire application architecture.
 * Instead of enforcing rigid, non-extendable class parameters for every unique track target, it pairs a
 * structured structural definition ([type]) with a flexible metadata registry ([extras]). This layout guarantees
 * that downstream analytics engine processors can unwrap and translate any event payload context uniformly.
 *
 * @property type The formal classification token (encapsulated via [TypeLog]) governing the event signature name
 * and its diagnostic tracking level behavior.
 * @property extras An immutable dictionary map of contextual key-value string pairs used to attach dynamic
 * payload metadata variables (e.g., user identifiers, system exceptions, or audit operational timestamps).
 * Defaults directly to an optimized `emptyMap()`.
 */
data class AnalyticsEvent(
    val type: TypeLog,
    val extras: Map<String, String> = emptyMap()
)