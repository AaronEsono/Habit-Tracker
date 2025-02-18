package aeb.proyecto.analytics

import aeb.proyecto.analytics.model.AnalyticsEvent
import aeb.proyecto.analytics.model.TypeLog
import aeb.proyecto.analytics.utils.getDateTime
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import java.time.LocalDateTime
import javax.inject.Inject

class AnalyticsManager @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) : AnalyticsManagerInterface {

    override fun logEvent(event: AnalyticsEvent) {
        if (event.type.register) {
            val bundle = Bundle().apply {
                event.extras.forEach { (key, value) ->
                    putString(key.take(40), value.take(100))
                }
            }

            firebaseAnalytics.logEvent(event.type.name, bundle)
        }
    }

}