package aeb.proyecto.language

import aeb.proyecto.language.provider.RegionFirstDayProvider
import aeb.proyecto.language.provider.getCountryCode
import aeb.proyecto.language.provider.getCountryFromNetwork
import aeb.proyecto.language.provider.getFirstDayOfWeekByLocale
import android.content.Context
import android.telephony.TelephonyManager
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import java.time.DayOfWeek

@RunWith(RobolectricTestRunner::class)
class RegionProviderTest {

    @get:Rule
    val mainDispatchersRule = MainDispatchersRule()

    private val context: Context = mockk(relaxed = true)
    private val telephonyManager: TelephonyManager = mockk()

    private lateinit var regionProvider: RegionFirstDayProvider

    @Before
    fun setup() {
        regionProvider = RegionFirstDayProvider(context)
    }

    @Test
    fun `given a valid country code when getCountryCode is called then returns return the expected day`() = run {
        //Given
        val countryCode = "BN"
        val expectedDay = DayOfWeek.SUNDAY

        //When
        val result = getCountryCode(countryCode)

        //Then
        assertEquals(expectedDay, result)
    }

    @Test
    fun `given a invalid country code when getCountryCode is called then returns return the else value`() = run {
        //Given
        val countryCode = "XX"
        val expectedDay = DayOfWeek.MONDAY

        //When
        val result = getCountryCode(countryCode)

        //Then
        assertEquals(expectedDay, result)
    }

    @Test
    fun `given valid network country ISO when getCountryFromNetwork is called then returns uppercase ISO`() {
        // --- GIVEN ---
        every { context.getSystemService(Context.TELEPHONY_SERVICE) } returns telephonyManager
        every { telephonyManager.networkCountryIso } returns "es"

        // --- WHEN ---
        val result = getCountryFromNetwork(context)

        // --- THEN ---
        assertEquals("ES", result)
    }

    @Test
    fun `given blank network country ISO when getCountryFromNetwork is called then returns null`() {
        // --- GIVEN ---
        every { context.getSystemService(Context.TELEPHONY_SERVICE) } returns telephonyManager
        every { telephonyManager.networkCountryIso } returns "   "

        // --- WHEN ---
        val result = getCountryFromNetwork(context)

        // --- THEN ---
        assertNull(result)
    }

    @Test
    fun `given telephony service unavailable when getCountryFromNetwork is called then returns null`() {
        // --- GIVEN ---
        every { context.getSystemService(Context.TELEPHONY_SERVICE) } returns null

        // --- WHEN ---
        val result = getCountryFromNetwork(context)

        // --- THEN ---
        assertNull(result)
    }
}