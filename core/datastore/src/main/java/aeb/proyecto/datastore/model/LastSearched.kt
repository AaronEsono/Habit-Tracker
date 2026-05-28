package aeb.proyecto.datastore.model

/**
 * Audit snapshot representing the user's localized chronological synchronization metadata.
 *
 * Coordinates state tracking for execution logs within the application context. It acts
 * as a defensive local memory fence to check whether background transactional activities
 * have been executed for a specific session lifecycle.
 *
 * @property uid The cryptographic unique identifier (UID) associated with the active cloud profile session.
 * @property date The ISO-8601 or localized string representation tracking the precise execution point.
 * @property searched A structural boolean flag tracking whether the verification pipeline was active.
 */
data class LastSearched(
    val uid:String? = "",
    val date:String? = "",
    val searched:Boolean = false
)