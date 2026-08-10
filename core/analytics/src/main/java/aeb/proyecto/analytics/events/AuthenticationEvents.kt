package aeb.proyecto.analytics.events

import aeb.proyecto.analytics.model.AnalyticsEvent
import aeb.proyecto.analytics.model.TypeLog
import aeb.proyecto.analytics.utils.getDateTime

/**
 * A strongly-typed analytics event catalog dedicated to mapping the complete authentication lifecycle.
 *
 * This component acts as a structural domain factory, enforcing unified compile-time contracts for telemetric
 * payloads before they stream toward downstream platform analytics managers. Every tracking function guarantees
 * that vital metadata attributes—such as localized audit timestamps via [getDateTime] and contextual identifiers—are
 * structured accurately inside immutable extra maps.
 */
object AuthenticationEvents {

    // --- Structural Event Type Definitions (TypeLog Configuration Registry) ---
    private val CREATED_ACCOUNT = TypeLog("CREATED_ACCOUNT", true)
    private val USER_LOGGED = TypeLog("USER_LOGGED", true)
    private val LOGGED_WITH_GOOGLE = TypeLog("LOGGED_WITH_GOOGLE", true)
    private val RESEND_EMAIL = TypeLog("RESEND_EMAIL", true)
    private val FORGOT_PASSWORD = TypeLog("FORGOT_PASSWORD", true)
    private val LOG_OUT = TypeLog("LOG_OUT", true)
    private val RECONNECTED = TypeLog("RECONNECTED", true)
    private val ERROR = TypeLog("ERROR_AUTHENTICATION", false)

    private val DELETED_ACCOUNT = TypeLog("DELETED_ACCOUNT", true)


    /**
     * Constructs a tracking token capturing standard credential validation success coordinates.
     *
     * @param userId The unique, obfuscated database reference key identifying the logged-in user context.
     * @return A compiled [AnalyticsEvent] encapsulating the tracking payload metadata.
     */
    fun logUserLogged(userId: String,): AnalyticsEvent {
        return AnalyticsEvent(
            type = USER_LOGGED,
            extras = mapOf(
                "user_id" to userId,
                "fecha" to "$userId - ${getDateTime()}"
            )
        )
    }

    /**
     * Constructs a tracking token capturing a session resumption event when an active user reconnects
     * after network dropouts or app hot-restarts.
     *
     * @param userId The unique database reference key identifying the targeted user context.
     * @return A compiled [AnalyticsEvent] encapsulating the session reconnection metrics.
     */
    fun reconnected(userId: String,): AnalyticsEvent {
        return AnalyticsEvent(
            type = RECONNECTED,
            extras = mapOf(
                "user_id" to userId,
                "fecha" to "$userId - ${getDateTime()}"
            )
        )
    }

    /**
     * Constructs a tracking token specifying federated authentication resolution via Google Single Sign-On (SSO).
     *
     * @param userId The provider-scoped unique identifier assigned to the authenticated user context.
     * @return A compiled [AnalyticsEvent] highlighting federated acquisition metrics.
     */
    fun loggedWithGoogle(userId: String): AnalyticsEvent {
        return AnalyticsEvent(
            type = LOGGED_WITH_GOOGLE,
            extras = mapOf(
                "user_id" to userId,
                "fecha" to "$userId - ${getDateTime()}"
            )
        )
    }

    /**
     * Constructs a tracking token capturing the absolute creation milestone of a new user profile registry.
     * Useful for monitoring registration conversion funnel completions.
     *
     * @param userId The newly generated unique structural identifier associated with the registration entry.
     * @return A compiled [AnalyticsEvent] anchoring the sign-up conversion metric.
     */
    fun createdAccount(userId: String): AnalyticsEvent {
        return AnalyticsEvent(
            type = CREATED_ACCOUNT,
            extras = mapOf(
                "user_id" to userId,
                "fecha" to "$userId - ${getDateTime()}"
            )
        )
    }

    /**
     * Constructs a tracking token signaling explicit session termination workflows initiated by the user.
     *
     * @param userId The unique identity reference undergoing active teardown operations.
     * @return A compiled [AnalyticsEvent] logging session termination trajectories.
     */
    fun logOut(userId: String): AnalyticsEvent {
        return AnalyticsEvent(
            type = LOG_OUT,
            extras = mapOf(
                "user_id" to userId,
                "fecha" to "$userId - ${getDateTime()}"
            )
        )
    }

    /**
     * Constructs a tracking token auditing credential reset intent pipelines.
     *
     * @param email The target communication channel requested to host the recovery transmission frame.
     * @return A compiled [AnalyticsEvent] logging verification recovery attempts.
     */
    fun forgotPassword(email: String): AnalyticsEvent {
        return AnalyticsEvent(
            type = FORGOT_PASSWORD,
            extras = mapOf(
                "email" to email,
                "fecha" to "$email - ${getDateTime()}"
            )
        )
    }

    /**
     * Constructs a tracking token logging explicit requests to re-dispatch pending email verification payloads.
     * Helps identify systemic delivery delays or verification friction zones.
     *
     * @param email The targeted communication channel scheduled for verification re-transmission.
     * @return A compiled [AnalyticsEvent] tracking validation retry requests.
     */
    fun resendEmail(email: String): AnalyticsEvent {
        return AnalyticsEvent(
            type = RESEND_EMAIL,
            extras = mapOf(
                "email" to email,
                "fecha" to "$email - ${getDateTime()}"
            )
        )
    }

    /**
     * Constructs a tracking token logging severe, unexpected failures occurring across the authentication pipeline.
     * Critical for remote diagnostics, real-time alerting, and regression monitoring.
     *
     * @param message The localized error description, exception signature, or failure reason string.
     * @return A compiled [AnalyticsEvent] storing descriptive failure diagnostics.
     */
    fun error(message: String): AnalyticsEvent {
        return AnalyticsEvent(
            type = ERROR,
            extras = mapOf(
                "message" to message,
                "fecha" to "$message - ${getDateTime()}"
            )
        )
    }

    /**
     * Constructs a tracking token capturing the absolute creation milestone of a new user profile registry.
     * Useful for monitoring the deleted accounts.
     *
     * @param userId The newly generated unique structural identifier associated with the registration entry.
     * @return A compiled [AnalyticsEvent] anchoring the sign-up conversion metric.
     */
    fun deletedAccount(userId: String): AnalyticsEvent {
        return AnalyticsEvent(
            type = DELETED_ACCOUNT,
            extras = mapOf(
                "user_id" to userId,
                "fecha" to "$userId - ${getDateTime()}"
            )
        )
    }


}