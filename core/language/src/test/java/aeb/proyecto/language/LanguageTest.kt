package aeb.proyecto.language

import android.app.LocaleManager
import android.content.Context
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
class LanguageTest {

    @get:Rule
    val mainDispatchersRule = MainDispatchersRule()

    private val context: Context = mockk(relaxed = true)
    private val localeManager: LocaleManager = mockk(relaxed = true)

    private lateinit var languageManager: LanguageManager

    @Before
    fun setup() {
        mockkStatic(AppCompatDelegate::class)
        every { context.getSystemService(LocaleManager::class.java) } returns localeManager

        languageManager = LanguageManager(context)
    }

    @After
    fun teardown() {
        unmockkStatic(AppCompatDelegate::class)
    }

    @Test
    @Config(sdk = [33])
    fun `given SDK 33 when setLanguage is called then modifies applicationLocales via LocaleManager`() {
        //When
        languageManager.setLanguage("es")

        //Then
        verify(exactly = 1) {
            localeManager.applicationLocales = any<LocaleList>()
        }
    }

    @Test
    @Config(sdk = [33])
    fun `given SDK 33 when getLanguage is called then resolves language from LocaleManager`() {
        // --- GIVEN ---
        val mockLocaleList = LocaleList.forLanguageTags("es-ES")
        every { localeManager.applicationLocales } returns mockLocaleList

        // --- WHEN ---
        val result = languageManager.getLanguage()

        // --- THEN ---
        assertEquals("es-ES", result)
    }

    @Test
    @Config(sdk = [31]) // 🟢 Robolectric simula que corremos en Android 12
    fun `given SDK prior to Tiramisu when setLanguage is called then does nothing via LocaleManager`() {
        // --- WHEN ---
        languageManager.setLanguage("en")

        // --- THEN ---
        verify(exactly = 0) { context.getSystemService(LocaleManager::class.java) }
    }

    @Test
    @Config(sdk = [31])
    fun `given SDK prior to Tiramisu when getLanguage is called then resolves language from AppCompatDelegate`() {
        // --- GIVEN ---
        val mockLocaleListCompat = LocaleListCompat.forLanguageTags("fr-FR")
        every { AppCompatDelegate.getApplicationLocales() } returns mockLocaleListCompat

        // --- WHEN ---
        val result = languageManager.getLanguage()

        // --- THEN ---
        assertEquals("fr", result)
    }

    @Test
    @Config(sdk = [31])
    fun `given SDK prior to Tiramisu and empty locales when getLanguage is called then returns fallback en`() {
        // --- GIVEN ---
        every { AppCompatDelegate.getApplicationLocales() } returns LocaleListCompat.getEmptyLocaleList()

        // --- WHEN ---
        val result = languageManager.getLanguage()

        // --- THEN ---
        assertEquals("en", result)
    }
}