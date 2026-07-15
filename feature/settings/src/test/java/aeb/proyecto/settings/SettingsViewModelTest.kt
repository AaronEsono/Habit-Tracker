package aeb.proyecto.settings

import aeb.proyecto.datastore.model.AppSettings
import aeb.proyecto.domain.usecase.settings.DataSettingsUseCase
import aeb.proyecto.domain.usecase.settings.SetLanguageUseCase
import aeb.proyecto.domain.usecase.settings.SettingsAuthenticationUseCase
import aeb.proyecto.settings.model.DataDialog
import aeb.proyecto.settings.model.DataResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.DayOfWeek

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    @get:Rule
    val mainDispatchersRule = MainDispatchersRule()

    private val mockDataSettingsUseCase = mockk<DataSettingsUseCase>(relaxed = true)
    private val mockSetLanguageUseCase = mockk<SetLanguageUseCase>(relaxed = true)
    private val mockAuthUseCase = mockk<SettingsAuthenticationUseCase>(relaxed = true)

    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setUp() {
        viewModel = SettingsViewModel(
            mockDataSettingsUseCase,
            mockSetLanguageUseCase,
            mockAuthUseCase
        )
    }

    @Test
    fun `when setDataDialogMode is called, dialog state updates correctly`() {
        viewModel.setDataDialogMode(DataDialog.LANGUAGE)
        assertEquals(true, viewModel.settingDialogState.value.dataDialog == DataDialog.LANGUAGE)

        viewModel.setDataDialogMode(DataDialog.DAY_WEEK)
        assertEquals(true, viewModel.settingDialogState.value.dataDialog == DataDialog.DAY_WEEK)
    }

    @Test
    fun `when setStateDialog is called, dialog state updates correctly`() {
        viewModel.setStateDialog(true)
        assertEquals(true, viewModel.settingDialogState.value.showDialog)

        viewModel.setStateDialog(false)
        assertEquals(false, viewModel.settingDialogState.value.showDialog)
    }

    @Test
    fun `given LanguageResult, when treatResultDialog is processed, then update language in use case`() = runTest {
        val mockSettings = AppSettings(language = "es")
        coEvery { mockDataSettingsUseCase.getAppSettings() } returns mockSettings
        val newLang = "en"
        val result = DataResult.LanguageResult(newLang)

        viewModel.treatResultDialog(result)

        coVerify { mockSetLanguageUseCase.setLanguage(newLang) }
        coVerify { mockDataSettingsUseCase.setAppSettings(any()) }
        assertEquals(false, viewModel.settingDialogState.value.showDialog)
    }

    @Test
    fun `given themeResult, when treatResultDialog is processed, then update language in use case`() = runTest {
        // --- GIVEN ---
        val mockSettings = AppSettings(themeMode = 2)
        coEvery { mockDataSettingsUseCase.getAppSettings() } returns mockSettings

        val newTheme = 1
        val result = DataResult.ThemeResult(newTheme)

        // --- WHEN ---
        viewModel.treatResultDialog(result)

        // --- THEN ---
        coVerify(exactly = 1) {
            mockDataSettingsUseCase.setAppSettings(withArg { updatedSettings ->
                assertEquals(newTheme, updatedSettings.themeMode)
            })
        }
        assertEquals(false, viewModel.settingDialogState.value.showDialog)
    }

    @Test
    fun `given DayOfWeekResult, when treatResultDialog is processed, then update language in use case`() = runTest {
        // --- GIVEN ---
        val mockSettings = AppSettings(dayStartWeek = "MONDAY")
        coEvery { mockDataSettingsUseCase.getAppSettings() } returns mockSettings

        val newDay = DayOfWeek.TUESDAY
        val result = DataResult.DayOfWeekResult(newDay)

        // --- WHEN ---
        viewModel.treatResultDialog(result)

        // --- THEN ---
        coVerify(exactly = 1) {
            mockDataSettingsUseCase.setAppSettings(withArg { updatedSettings ->
                assertEquals(newDay.name, updatedSettings.dayStartWeek)
            })
        }
        assertEquals(false, viewModel.settingDialogState.value.showDialog)
    }

}