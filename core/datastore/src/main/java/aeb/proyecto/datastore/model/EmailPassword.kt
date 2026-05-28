package aeb.proyecto.datastore.model


/**
 * Data payload wrapper representing account validation credentials.
 *
 * This data class encapsulates transient identification and cryptographic secrets required
 * during local session initialization and automated re-authentication pipelines.
 *
 * @property email The account identification string sequence. Defaults to an empty string.
 * @property password The structural authentication secret payload. Defaults to an empty string.
 */
data class EmailPassword(
    val email: String = "",
    val password: String = ""
)