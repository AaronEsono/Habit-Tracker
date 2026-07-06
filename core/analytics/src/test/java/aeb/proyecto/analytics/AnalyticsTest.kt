package aeb.proyecto.analytics

import aeb.proyecto.analytics.model.AnalyticsEvent
import aeb.proyecto.analytics.model.TypeLog
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.slot
import io.mockk.unmockkConstructor
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AnalyticsTest {

    @get:Rule
    val mainDispatchersRule = MainDispatchersRule()

    private val firebaseAnalytics: FirebaseAnalytics = mockk(relaxed = true)
    private lateinit var analyticsManager: AnalyticsManager

    @Before
    fun setup() {
        analyticsManager = AnalyticsManager(firebaseAnalytics)
    }

    @Test
    fun `given register flag is false when logEvent is called then firebaseAnalytics is never invoked`() {
        // --- GIVEN ---
        val typeLog = TypeLog(name = "TEST_EVENT", register = false)
        val event = AnalyticsEvent(type = typeLog, extras = mapOf("key" to "value"))

        // --- WHEN ---
        analyticsManager.logEvent(event)

        // --- THEN ---
        verify(exactly = 0) { firebaseAnalytics.logEvent(any(), any()) }
    }

    @Test
    fun `given register flag is true and long parameters when logEvent is called then truncates keys and values correctly`() {
        // --- GIVEN ---
        mockkConstructor(Bundle::class)
        every { anyConstructed<Bundle>().putString(any(), any()) } returns Unit

        val typeLog = TypeLog(name = "TEST_EVENT", register = true)
        val longKey = "a".repeat(50)
        val longValue = "b".repeat(120)
        val event = AnalyticsEvent(type = typeLog, extras = mapOf(longKey to longValue))

        // --- WHEN ---
        analyticsManager.logEvent(event)

        // --- THEN ---
        val expectedKey = "a".repeat(40)
        val expectedValue = "b".repeat(100)

        verify(exactly = 1) {
            firebaseAnalytics.logEvent("TEST_EVENT", any())
        }

        verify {
            anyConstructed<Bundle>().putString(expectedKey, expectedValue)
        }

        unmockkConstructor(Bundle::class)
    }

}