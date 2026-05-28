package aeb.proyecto.datastore.model


/**
 * Immutable data payload representing the local user authentication session state.
 *
 * This data class models the secure boundary credentials required to authorize localized
 * session handshakes. Bundling these structural metrics prevents multi-threaded synchronization
 * anomalies where credentials risk falling out of sync during transactional mutations.
 *
 * @property email The encrypted or raw string sequence tracking the user's account identification.
 * Defaults to an empty string `""` indicating an unauthenticated or anonymous session state.
 * @property password The structural authentication token sequence verifying account ownership.
 * Defaults to an empty string `""`.
 */
data class UserSession(
    val email: String = "",
    val password: String = ""
){
    /**
     * Diagnostic utility property to verify if the current snapshot contains valid session tokens.
     * @return `true` if the session boundary holds established credential metrics.
     */

    val isAuthenticated: Boolean get() = email.isNotEmpty() && password.isNotEmpty()
}