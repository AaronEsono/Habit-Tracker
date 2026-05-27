package aeb.proyecto.analytics.events

import aeb.proyecto.analytics.model.AnalyticsEvent
import aeb.proyecto.analytics.model.TypeLog
import aeb.proyecto.analytics.utils.getDateTime

/**
 * A strongly-typed analytics event registry dedicated to auditing Cloud Firestore database transactions.
 *
 * This singleton object encapsulates telemetry configurations associated with cloud-based NoSQL persistence
 * operations. Monitoring these asynchronous integration boundaries is critical for assessing database execution
 * performance, measuring read/write operational overhead volumes against billing quotas, and detecting structural
 * data synchronization regressions.
 */
object FirestoreEvents {

    // --- Core Cloud Firestore Database Transaction Logs ---
    private val GET_DATA_USER = TypeLog("GET_DATA_USER", true)
    private val SAVE_DATA_USER = TypeLog("SAVE_DATA_USER", true)
    private val DELETE_DATA_USER = TypeLog("DELETE_DATA_USER", true)
    private val ERROR = TypeLog("ERROR_FIRESTORE", true)

    /**
     * Constructs a tracking token auditing remote data retrieval queries executed against
     * the user's remote cloud profile document snapshot.
     *
     * @param userId The unique reference key identifying the targeted user document context.
     * @return A compiled [AnalyticsEvent] encapsulating database synchronization retrieval metrics.
     */
    fun getDataUser(userId: String,): AnalyticsEvent {
        return AnalyticsEvent(
            type = GET_DATA_USER,
            extras = mapOf(
                "user_id" to userId,
                "fecha" to "$userId - ${getDateTime()}"
            )
        )
    }

    /**
     * Constructs a tracking token auditing network mutation writes, overwrites, or transactional
     * updates committed toward the cloud document store.
     *
     * @param userId The unique reference key identifying the targeted user document undergoing mutation.
     * @return A compiled [AnalyticsEvent] encapsulating database update metrics.
     */
    fun saveDataUser(userId: String,): AnalyticsEvent {
        return AnalyticsEvent(
            type = SAVE_DATA_USER,
            extras = mapOf(
                "user_id" to userId,
                "fecha" to "$userId - ${getDateTime()}"
            )
        )
    }

    /**
     * Constructs a tracking token logging explicit structural deletion tasks executed to clear
     * a user's data nodes from the remote database schema.
     *
     * @param userId The unique reference key representing the identity context undergoing permanent teardown.
     * @return A compiled [AnalyticsEvent] capturing structural deletion trajectories.
     */
    fun deleteDataUser(userId: String): AnalyticsEvent {
        return AnalyticsEvent(
            type = DELETE_DATA_USER,
            extras = mapOf(
                "user_id" to userId,
                "fecha" to "$userId - ${getDateTime()}"
            )
        )
    }

    /**
     * Constructs a tracking token capturing network timeouts, security rule rejections, or critical
     * exceptions thrown during asynchronous execution pipelines with Cloud Firestore.
     *
     * @param message The localized error message or raw exception signature string transmitted from the network driver.
     * @return A compiled [AnalyticsEvent] storing remote persistence failure diagnostics.
     */
    fun error(error: String): AnalyticsEvent {
        return AnalyticsEvent(
            type = ERROR,
            extras = mapOf(
                "message" to error,
                "fecha" to "$error - ${getDateTime()}"
            )
        )
    }
}