package aeb.proyecto.habittracker.utils

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import java.time.LocalDateTime
import javax.inject.Inject

class AnalyticsManager @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) {

    fun logEvent(event: AnalyticsEvent) {
        if(event.type.register){
            val bundle = Bundle().apply {
                event.extras.forEach { (key, value) ->
                    putString(key.take(40), value.take(100)) //que no sobrepase los límites de Firebase
                }
            }

            firebaseAnalytics.logEvent(event.type.name, bundle)
        }
    }
}

fun getDateTime():String{
    return LocalDateTime.now().toString()
}

data class AnalyticsEvent(
    val type: TypesLogs,
    val extras: Map<String, String> = emptyMap()
)

data class TypesLogs(
    val name: String,
    val register: Boolean
)

object TypeEvent {
    private val USER_LOGGED = TypesLogs("USER_LOGGED", true)
    private val RECONNECTED = TypesLogs("RECONNECTED", true)
    private val LOGGED_WITH_GOOGLE = TypesLogs("LOGGED_WITH_GOOGLE", true)
    private val CREATED_ACCOUNT = TypesLogs("CREATED_ACCOUNT", true)
    private val LOG_OUT = TypesLogs("LOG_OUT", true)
    private val FORGOT_PASSWORD = TypesLogs("FORGOT_PASSWORD", true)
    private val RESEND_EMAIL = TypesLogs("RESEND_EMAIL", true)
    private val ERROR = TypesLogs("ERROR", false)


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