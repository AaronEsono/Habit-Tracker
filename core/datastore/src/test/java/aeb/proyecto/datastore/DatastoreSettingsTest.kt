package aeb.proyecto.datastore

import aeb.proyecto.datastore.model.AppSettings
import aeb.proyecto.datastore.repository.DatastoreRepository
import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.math.exp


class DatastoreSettingsTest{

    @get:Rule
    val mainDispatchersRule = MainDispatcherRuleUnit()

    private var datastoreManager: DataStoreManager = mockk()
    private var datastoreRepository: DatastoreRepository = mockk()

    // THEME MODE FUNCTIONALITY ----------------------------------------------------------------
    @Test
    fun `given a expected value when called theme mode function on repository then return the same value`() = runTest{
        // GIVEN
        val expectedTheme = 1
        every { datastoreRepository.themeMode } returns flowOf(expectedTheme)

        //WHEN
        datastoreRepository.themeMode.test {
            //THEN
            assertEquals(expectedTheme, awaitItem())

            awaitComplete()
        }
    }

    @Test
    fun `given a expected value when called theme mode function on datastore then return the same value`() = runTest{
        // GIVEN
        val expectedTheme = 1
        every { datastoreManager.themeMode } returns flowOf(expectedTheme)

        //WHEN
        datastoreManager.themeMode.test {
            //THEN
            assertEquals(expectedTheme, awaitItem())

            awaitComplete()
        }
    }

    // LANGUAGE FUNCTIONALITY ----------------------------------------------------------------
    @Test
    fun `given a expected value when called language mode function on repository then return the same value`() = runTest{
        // GIVEN
        val languageExpected = "es"
        every { datastoreRepository.language } returns flowOf(languageExpected)

        //WHEN
        datastoreRepository.language.test {
            //THEN
            assertEquals(languageExpected, awaitItem())

            awaitComplete()
        }
    }

    @Test
    fun `given a expected value when called language mode function on datastore then return the same value`() = runTest{
        // GIVEN
        val languageExpected = "es"
        every { datastoreManager.languageMode } returns flowOf(languageExpected)

        //WHEN
        datastoreManager.languageMode.test {
            //THEN
            assertEquals(languageExpected, awaitItem())

            awaitComplete()
        }
    }

    // DAY OF WEEK FUNCTIONALITY ----------------------------------------------------------------
    @Test
    fun `given a expected value when called day of week function on repository then return the same value`() = runTest{
        // GIVEN
        val dayExpected = "LUN"
        every { datastoreRepository.language } returns flowOf(dayExpected)

        //WHEN
        datastoreRepository.language.test {
            //THEN
            assertEquals(dayExpected, awaitItem())

            awaitComplete()
        }
    }

    @Test
    fun `given a expected value when called day of week function on datastore then return the same value`() = runTest{
        // GIVEN
        val dayExpected = "LUN"
        every { datastoreManager.languageMode } returns flowOf(dayExpected)

        //WHEN
        datastoreManager.languageMode.test {
            //THEN
            assertEquals(dayExpected, awaitItem())

            awaitComplete()
        }
    }

    //GET SETTINGS ----------------------------------------------------------------
    @Test
    fun `given a expected AppSettings when called getSettings function on repository then return the same value`() = runTest{
        // GIVEN
        val expectedAppSettings = AppSettings()
        coEvery { datastoreRepository.getAppSettings() } returns expectedAppSettings

        //When
        val result = datastoreRepository.getAppSettings()

        //THEN
        assertEquals(expectedAppSettings, result)
        coVerify { datastoreRepository.getAppSettings() }
    }

    @Test
    fun `given a expected AppSettings when called getSettings function on datastore then return the same value`() = runTest{
        // GIVEN
        val expectedAppSettings = AppSettings()
        coEvery { datastoreManager.getAppSettings() } returns expectedAppSettings

        //When
        val result = datastoreManager.getAppSettings()

        //THEN
        assertEquals(expectedAppSettings, result)
        coVerify { datastoreManager.getAppSettings() }
    }


    //SET SETTINGS ----------------------------------------------------------------
    @Test
    fun `given a expected AppSettings when called setSettings function on repository then calls the function`() = runTest{
        // GIVEN
        val expectedAppSettings = AppSettings()
        coEvery { datastoreRepository.setAppSettings(expectedAppSettings) } returns Unit

        //When
        datastoreRepository.setAppSettings(expectedAppSettings)

        //THEN
        coVerify { datastoreRepository.setAppSettings(expectedAppSettings) }
    }

    @Test
    fun `given a expected AppSettings when called setSettings function on datastore then calls the function`() = runTest{
        // GIVEN
        val expectedAppSettings = AppSettings()
        coEvery { datastoreManager.saveAppSettings(expectedAppSettings) } returns Unit

        //When
        datastoreManager.saveAppSettings(expectedAppSettings)

        //THEN
        coVerify { datastoreManager.saveAppSettings(expectedAppSettings) }
    }

    //SET FIRST DAY OF WEEK ----------------------------------------------------------------
    @Test
    fun `given nothing when called setFirstDayOfWeek function on repository then calls the function`() = runTest{
        // GIVEN
        coEvery { datastoreRepository.setFirstDayOfWeek() } returns Unit

        //When
        datastoreRepository.setFirstDayOfWeek()

        //THEN
        coVerify { datastoreRepository.setFirstDayOfWeek() }
    }

    @Test
    fun `given nothing when called setFirstDayOfWeek function on datastore then calls the function`() = runTest{
        // GIVEN
        coEvery { datastoreManager.setFirstDayOfWeek() } returns Unit

        //When
        datastoreManager.setFirstDayOfWeek()

        //THEN
        coVerify { datastoreManager.setFirstDayOfWeek() }
    }

    //APP SETTINGS ----------------------------------------------------------------
    @Test
    fun `given a mock appSettings when called appSettings function on repository then returns the same value`() = runTest{
        // GIVEN
        val expectedAppSettings = AppSettings()
        coEvery { datastoreRepository.appSettings } returns flowOf(expectedAppSettings)

        //When
        datastoreRepository.appSettings.test {

            //Then
            assertEquals(expectedAppSettings, awaitItem())

            awaitComplete()
        }
    }

    @Test
    fun `given a mock appSettings when called appSettings function on datastore then returns the same value`() = runTest{
        // GIVEN
        val expectedAppSettings = AppSettings()
        coEvery { datastoreManager.appSettings } returns flowOf(expectedAppSettings)

        //When
        datastoreManager.appSettings.test {

            //Then
            assertEquals(expectedAppSettings, awaitItem())

            awaitComplete()
        }
    }

}