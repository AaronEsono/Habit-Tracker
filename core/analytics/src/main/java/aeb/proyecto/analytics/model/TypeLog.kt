package aeb.proyecto.analytics.model

/**
 * A metadata structural configuration class defining the taxonomy and auditing rules for telemetry events.
 *
 * This component acts as a signature classification token paired with [AnalyticsEvent]. Instead of processing
 * events as raw, unvalidated string parameters, this model wraps the event identity and its architectural
 * behavior into a unified, type-safe contract.
 *
 * @property name The unique string identifier representing the event signature key (e.g., `"USER_LOGGED"`, `"ERROR_FIRESTORE"`)
 * recognized by downstream backend console tracking platforms.
 * @property register A conditional control flag utilizing boolean evaluation logic. It dictates whether the associated
 * event meets the criteria for persistence logging, database caching pipelines, or real-time remote stream transmission frameworks.
 */
data class TypeLog (
    val name: String,
    val register: Boolean
)