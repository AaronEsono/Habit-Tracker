package aeb.proyecto.analytics.events

import aeb.proyecto.analytics.model.AnalyticsEvent
import aeb.proyecto.analytics.model.TypeLog
import aeb.proyecto.analytics.utils.getDateTime

object AuthenticationEvents {
    private val CREATED_ACCOUNT = TypeLog("CREATED_ACCOUNT", true)
    private val USER_LOGGED = TypeLog("USER_LOGGED", true)
    private val LOGGED_WITH_GOOGLE = TypeLog("LOGGED_WITH_GOOGLE", true)
    private val RESEND_EMAIL = TypeLog("RESEND_EMAIL", true)
    private val FORGOT_PASSWORD = TypeLog("FORGOT_PASSWORD", true)
    private val LOG_OUT = TypeLog("LOG_OUT", true)
    private val RECONNECTED = TypeLog("RECONNECTED", true)
    private val ERROR = TypeLog("ERROR_AUTHENTICATION", false)


    fun logUserLogged(userId: String,): AnalyticsEvent {
        return AnalyticsEvent(
            type = USER_LOGGED,
            extras = mapOf(
                "user_id" to userId,
                "fecha" to "$userId - ${getDateTime()}"
            )
        )
    }

    fun reconnected(userId: String,): AnalyticsEvent {
        return AnalyticsEvent(
            type = RECONNECTED,
            extras = mapOf(
                "user_id" to userId,
                "fecha" to "$userId - ${getDateTime()}"
            )
        )
    }

    fun loggedWithGoogle(userId: String): AnalyticsEvent {
        return AnalyticsEvent(
            type = LOGGED_WITH_GOOGLE,
            extras = mapOf(
                "user_id" to userId,
                "fecha" to "$userId - ${getDateTime()}"
            )
        )
    }

    fun createdAccount(userId: String): AnalyticsEvent {
        return AnalyticsEvent(
            type = CREATED_ACCOUNT,
            extras = mapOf(
                "user_id" to userId,
                "fecha" to "$userId - ${getDateTime()}"
            )
        )
    }

    fun logOut(userId: String): AnalyticsEvent {
        return AnalyticsEvent(
            type = LOG_OUT,
            extras = mapOf(
                "user_id" to userId,
                "fecha" to "$userId - ${getDateTime()}"
            )
        )
    }

    fun forgotPassword(email: String): AnalyticsEvent {
        return AnalyticsEvent(
            type = FORGOT_PASSWORD,
            extras = mapOf(
                "email" to email,
                "fecha" to "$email - ${getDateTime()}"
            )
        )
    }

    fun resendEmail(email: String): AnalyticsEvent {
        return AnalyticsEvent(
            type = RESEND_EMAIL,
            extras = mapOf(
                "email" to email,
                "fecha" to "$email - ${getDateTime()}"
            )
        )
    }

    fun error(message: String): AnalyticsEvent {
        return AnalyticsEvent(
            type = ERROR,
            extras = mapOf(
                "message" to message,
                "fecha" to "$message - ${getDateTime()}"
            )
        )
    }
}