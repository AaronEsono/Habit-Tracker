package aeb.proyecto.analytics

import aeb.proyecto.analytics.model.AnalyticsEvent
import aeb.proyecto.analytics.model.TypeLog
import aeb.proyecto.analytics.utils.getDateTime
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * The core telemetry engine orchestrating outbound event propagation toward remote logging platforms.
 *
 * This component acts as the concrete implementation of [AnalyticsManagerInterface]. It encapsulates the
 * native [FirebaseAnalytics] SDK subsystem, acting as a defensive boundary layer. Instead of piping raw data
 * streams blindly, this manager sanitizes metadata keys and values to strictly comply with downstream vendor
 * API payload size architectures, preventing tracking degradation or silent framework rejections.
 *
 * @property firebaseAnalytics The pre-provisioned native Google Firebase tracking driver injected by the platform infrastructure.
 */
class AnalyticsManager(
    private val firebaseAnalytics: FirebaseAnalytics
) : AnalyticsManagerInterface {

    /**
     * intercepts, sanitizes, and transmits a unified telemetry token to the Cloud Firebase console.
     *
     * The propagation lifecycle runs through two explicit structural gates:
     * 1. **Auditing Verification:** It evaluates [TypeLog.register]. If the flag returns `false`, execution
     * short-circuits immediately, preventing local telemetry from consuming cloud bandwidth.
     * 2. **Defensive Value Truncation:** To mitigate systemic vendor crashes or silent dropouts caused by platform-enforced
     * constraints, parameter keys are restricted to their first $40$ characters ($key.take(40)$), while stringified metadata
     * values are bound to a maximum ceiling of $100$ characters ($value.take(100)$).
     *
     * @param event The structured [AnalyticsEvent] configuration context undergoing processing.
     */
    override fun logEvent(event: AnalyticsEvent) {
        if (event.type.register) {
            val bundle = Bundle().apply {
                event.extras.forEach { (key, value) ->
                    // Enforce structural vendor boundary constraints defensively
                    putString(key.take(40), value.take(100))
                }
            }

            firebaseAnalytics.logEvent(event.type.name, bundle)
        }
    }

}