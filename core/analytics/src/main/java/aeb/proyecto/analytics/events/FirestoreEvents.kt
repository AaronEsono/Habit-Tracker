package aeb.proyecto.analytics.events

import aeb.proyecto.analytics.model.AnalyticsEvent
import aeb.proyecto.analytics.model.TypeLog
import aeb.proyecto.analytics.utils.getDateTime

object FirestoreEvents {
    private val GET_DATA_USER = TypeLog("GET_DATA_USER", true)
    private val SAVE_DATA_USER = TypeLog("SAVE_DATA_USER", true)
    private val DELETE_DATA_USER = TypeLog("DELETE_DATA_USER", true)
    private val ERROR = TypeLog("ERROR_FIRESTORE", true)

    fun getDataUser(userId: String,): AnalyticsEvent {
        return AnalyticsEvent(
            type = GET_DATA_USER,
            extras = mapOf(
                "user_id" to userId,
                "fecha" to "$userId - ${getDateTime()}"
            )
        )
    }

    fun saveDataUser(userId: String,): AnalyticsEvent {
        return AnalyticsEvent(
            type = SAVE_DATA_USER,
            extras = mapOf(
                "user_id" to userId,
                "fecha" to "$userId - ${getDateTime()}"
            )
        )
    }

    fun deleteDataUser(userId: String): AnalyticsEvent {
        return AnalyticsEvent(
            type = DELETE_DATA_USER,
            extras = mapOf(
                "user_id" to userId,
                "fecha" to "$userId - ${getDateTime()}"
            )
        )
    }

    fun Error(error: String): AnalyticsEvent {
        return AnalyticsEvent(
            type = ERROR,
            extras = mapOf(
                "message" to error,
                "fecha" to "$error - ${getDateTime()}"
            )
        )
    }
}