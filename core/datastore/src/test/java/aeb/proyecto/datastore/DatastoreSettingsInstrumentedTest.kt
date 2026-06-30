package aeb.proyecto.datastore

import aeb.proyecto.datastore.model.AppSettings
import aeb.proyecto.datastore.repository.DatastoreRepository
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import app.cash.turbine.test
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File
import java.time.DayOfWeek
import java.util.Calendar
import java.util.Locale

class DatastoreSettingsInstrumentedTest {

    @get:Rule
    val mainDispatchersInstrumentedRule = MainDispatcherRuleUnit()

    private lateinit var testFile: File
    private lateinit var datastore: DataStore<Preferences>
    private lateinit var datastoreManager: DataStoreManager
    private lateinit var datastoreRepository: DatastoreRepository

    @Before
    fun setUp(){
        testFile = File.createTempFile("test_prefs", ".preferences_pb")

        datastore = PreferenceDataStoreFactory.create(
            scope = CoroutineScope(mainDispatchersInstrumentedRule.testDispatcher + Job()),
            produceFile = { testFile }
        )

        datastoreManager = DataStoreManager(datastore)
        datastoreRepository = DatastoreRepository(datastoreManager)
    }

    @After
    fun tearDown(){
        if (::testFile.isInitialized) {
            testFile.delete()
        }
    }

    @Test
    fun `given a expected AppSettings when called setSettings function on repository then set the data and return the same value`() = runTest{
        //Given
        val appSettings = AppSettings(themeMode = 2, language = "es", dayStartWeek = "LUN")

        //When
        datastoreRepository.setAppSettings(appSettings)


        //Then
        val result = datastoreRepository.getAppSettings()
        assertEquals(appSettings, result)
    }

    @Test
    fun `given none when called getSettings function on repository then return the defaultValue`() = runTest{
        //Given
        val expectedSettings = AppSettings()

        //When
        val settings = datastoreRepository.getAppSettings()


        //Then
        assertEquals(expectedSettings, settings)
    }

    @Test
    fun `given none when called setFirstDayOfWeek function then returns the day expected`() = runTest{
        //Given
        val dayExpected = DayOfWeek.of(Calendar.getInstance(Locale.getDefault()).firstDayOfWeek).name

        //When
        datastoreRepository.setFirstDayOfWeek()


        //Then
        val dayOfWeek = datastoreRepository.getAppSettings().dayStartWeek
        assertEquals(dayExpected, dayOfWeek)
    }

    @Test
    fun `given a themeMode when called themeMode function then returns the value expected`() = runTest{
        //Given
        val appSettings = AppSettings(themeMode = 2)

        //When
        datastoreRepository.setAppSettings(appSettings)

        //Then
        datastoreRepository.themeMode.test {
            assertEquals(2, awaitItem())
        }
    }

    @Test
    fun `given two themeModes when called themeMode function then returns the value expected and then the other one`() = runTest{
        //Given
        val firstThemeMode = AppSettings(themeMode = 2)
        val secondThemeMode = AppSettings(themeMode = 3)

        datastoreRepository.setAppSettings(firstThemeMode)
        //When
        datastoreRepository.themeMode.test {
            //Then
            assertEquals(2, awaitItem())

            datastoreRepository.setAppSettings(secondThemeMode)
            assertEquals(3, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given a language when called language function then returns the value expected`() = runTest{
        //Given
        val appSettings = AppSettings(language = "es")

        //When
        datastoreRepository.setAppSettings(appSettings)

        //Then
        datastoreRepository.language.test {
            assertEquals("es", awaitItem())
        }
    }

    @Test
    fun `given two languages when called language function then returns the value expected and then the other one`() = runTest{
        //Given
        val firstAppSettings = AppSettings(language = "es")
        val secondAppSettings = AppSettings(language = "en")

        datastoreRepository.setAppSettings(firstAppSettings)
        //When
        datastoreRepository.language.test {
            //Then
            assertEquals("es", awaitItem())

            datastoreRepository.setAppSettings(secondAppSettings)
            assertEquals("en", awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given a dayOfWeek when called dayOfWeek function then returns the value expected`() = runTest{
        //Given
        val appSettings = AppSettings(dayStartWeek = "LUN")

        //When
        datastoreRepository.setAppSettings(appSettings)

        //Then
        datastoreRepository.dayOfWeek.test {
            assertEquals("LUN", awaitItem())
        }
    }

    @Test
    fun `given two dayOfWeeks when called dayOfWeek function then returns the value expected and then the other one`() = runTest{
        //Given
        val firstAppSettings = AppSettings(dayStartWeek = "LUN")
        val secondAppSettings = AppSettings(dayStartWeek = "MAR")

        datastoreRepository.setAppSettings(firstAppSettings)
        //When
        datastoreRepository.dayOfWeek.test {
            //Then
            assertEquals("LUN", awaitItem())

            datastoreRepository.setAppSettings(secondAppSettings)
            assertEquals("MAR", awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given an appSettings when called setSettings then appSettings return the value`() = runTest{
        //Given
        val appSettings = AppSettings(themeMode = 3, language = "es", dayStartWeek = "LUN")

        //When
        datastoreRepository.setAppSettings(appSettings)

        //Then
        datastoreRepository.appSettings.test {
            assertEquals(appSettings, awaitItem())
        }

    }

    @Test
    fun `given two appSettings when called setSettings function then returns the value expected and then the other one`() = runTest{
        //Given
        val firstAppSettings = AppSettings(language = "es",themeMode = 3, dayStartWeek = "LUN")
        val secondAppSettings = AppSettings(language = "en",themeMode = 1, dayStartWeek = "MAR")

        datastoreRepository.setAppSettings(firstAppSettings)

        //When
        datastoreRepository.appSettings.test {
            //Then
            assertEquals(firstAppSettings, awaitItem())

            datastoreRepository.setAppSettings(secondAppSettings)
            assertEquals(secondAppSettings, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }


}