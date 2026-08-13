package aeb.proyecto.settings

import aeb.proyecto.datastore.model.AppSettings
import aeb.proyecto.settings.components.vertical.VerticalSettingsScreen
import aeb.proyecto.settings.components.vertical.components.dialog.VerticalDialogSettings
import aeb.proyecto.settings.model.DataDialog
import aeb.proyecto.settings.model.DataResult
import aeb.proyecto.settings.model.SettingsDialogState
import android.os.Build
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.filters.SmallTest
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class SettingsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun givenLoadingState_whenScreenRendered_thenShowLoadingLayout() {
        // --- WHEN ---
        composeTestRule.setContent {
            VerticalSettingsScreen(
                settingsUIState = SettingsUIState.Loading,
                dialogState = SettingsDialogState(showDialog = false),
                onClickTheme = {},
                onClickLanguage = {},
                onClickGeneralSettings = {},
                onClickOverlay = {},
                onClickExport = {},
                onClickEmail = {},
                onClickGithub = {},
                onClickTerms = {},
                onClickAttributions = {},
                onClickPrivacy = {},
                onDismissDialog = {},
                onAcceptDialog = {}
            )
        }

        // --- THEN ---
        composeTestRule.onNodeWithTag("settings_loading").assertIsDisplayed()
    }

    @Test
    fun givenSuccessState_whenClickThemeOption_thenTriggerOnClickLanguageLambda() {
        // --- GIVEN ---
        var themeClicked = false
        val fakeSettings = AppSettings(language = "es")

        composeTestRule.setContent {
            VerticalSettingsScreen(
                settingsUIState = SettingsUIState.Success(fakeSettings),
                dialogState = SettingsDialogState(showDialog = false),
                onClickTheme = {themeClicked = true},
                onClickLanguage = {},
                onClickGeneralSettings = {},
                onClickOverlay = {},
                onClickExport = {},
                onClickEmail = {},
                onClickGithub = {},
                onClickTerms = {},
                onClickAttributions = {},
                onClickPrivacy = {},
                onDismissDialog = {},
                onAcceptDialog = {}
            )
        }

        // --- WHEN ---
        composeTestRule.onNodeWithTag("settings_button_theme").performClick()

        // --- THEN ---
        assertTrue(themeClicked)
    }

    @Test
    fun givenSuccessState_whenClickDay_thenTriggerOnClickLanguageLambda() {
        // --- GIVEN ---
        var dayClicked = false
        val fakeSettings = AppSettings(language = "es")

        composeTestRule.setContent {
            VerticalSettingsScreen(
                settingsUIState = SettingsUIState.Success(fakeSettings),
                dialogState = SettingsDialogState(showDialog = false),
                onClickTheme = {},
                onClickLanguage = {},
                onClickGeneralSettings = {dayClicked = true},
                onClickOverlay = {},
                onClickExport = {},
                onClickEmail = {},
                onClickGithub = {},
                onClickTerms = {},
                onClickAttributions = {},
                onClickPrivacy = {},
                onDismissDialog = {},
                onAcceptDialog = {}
            )
        }

        // --- WHEN ---
        composeTestRule.onNodeWithTag("settings_button_dayWeek").performClick()

        // --- THEN ---
        assertTrue(dayClicked)
    }

    @Test
    fun givenSuccessState_whenClickOverlay_thenTriggerOnClickLanguageLambda() {
        // --- GIVEN ---
        var overlayClicked = false
        val fakeSettings = AppSettings(language = "es")

        composeTestRule.setContent {
            VerticalSettingsScreen(
                settingsUIState = SettingsUIState.Success(fakeSettings),
                dialogState = SettingsDialogState(showDialog = false),
                onClickTheme = {},
                onClickLanguage = {},
                onClickGeneralSettings = {},
                onClickOverlay = { overlayClicked = true},
                onClickExport = {},
                onClickEmail = {},
                onClickGithub = {},
                onClickTerms = {},
                onClickAttributions = {},
                onClickPrivacy = {},
                onDismissDialog = {},
                onAcceptDialog = {}
            )
        }

        // --- WHEN ---
        composeTestRule.onNodeWithTag("settings_button_overlay").performClick()

        // --- THEN ---
        assertTrue(overlayClicked)
    }

    @Test
    fun givenSuccessState_whenClickExport_thenTriggerOnClickLanguageLambda() {
        // --- GIVEN ---
        var exportClicked = false
        val fakeSettings = AppSettings(language = "es")

        composeTestRule.setContent {
            VerticalSettingsScreen(
                settingsUIState = SettingsUIState.Success(fakeSettings),
                dialogState = SettingsDialogState(showDialog = false),
                onClickTheme = {},
                onClickLanguage = {},
                onClickGeneralSettings = {},
                onClickOverlay = {},
                onClickExport = { exportClicked = true},
                onClickEmail = {},
                onClickGithub = {},
                onClickTerms = {},
                onClickAttributions = {},
                onClickPrivacy = {},
                onDismissDialog = {},
                onAcceptDialog = {}
            )
        }

        // --- WHEN ---
        composeTestRule.onNodeWithTag("settings_button_export").performClick()

        // --- THEN ---
        assertTrue(exportClicked)
    }

    @Test
    fun givenSuccessState_whenClickEmail_thenTriggerOnClickLanguageLambda() {
        // --- GIVEN ---
        var emailClicked = false
        val fakeSettings = AppSettings(language = "es")

        composeTestRule.setContent {
            VerticalSettingsScreen(
                settingsUIState = SettingsUIState.Success(fakeSettings),
                dialogState = SettingsDialogState(showDialog = false),
                onClickTheme = {},
                onClickLanguage = {},
                onClickGeneralSettings = {},
                onClickOverlay = {},
                onClickExport = {},
                onClickEmail = { emailClicked = true},
                onClickGithub = {},
                onClickTerms = {},
                onClickAttributions = {},
                onClickPrivacy = {},
                onDismissDialog = {},
                onAcceptDialog = {}
            )
        }

        // --- WHEN ---
        composeTestRule.onNodeWithTag("settings_button_email").performClick()

        // --- THEN ---
        assertTrue(emailClicked)
    }

    @Test
    fun givenSuccessState_whenClickGithub_thenTriggerOnClickLanguageLambda() {
        // --- GIVEN ---
        var githubClicked = false
        val fakeSettings = AppSettings(language = "es")

        composeTestRule.setContent {
            VerticalSettingsScreen(
                settingsUIState = SettingsUIState.Success(fakeSettings),
                dialogState = SettingsDialogState(showDialog = false),
                onClickTheme = {},
                onClickLanguage = {},
                onClickGeneralSettings = {},
                onClickOverlay = {},
                onClickExport = {},
                onClickEmail = {},
                onClickGithub = {githubClicked = true},
                onClickTerms = {},
                onClickAttributions = {},
                onClickPrivacy = {},
                onDismissDialog = {},
                onAcceptDialog = {}
            )
        }

        // --- WHEN ---
        composeTestRule.onNodeWithTag("settings_button_github").performClick()

        // --- THEN ---
        assertTrue(githubClicked)
    }

    @Test
    fun givenSuccessState_whenClickMedia_thenTriggerOnClickLanguageLambda() {
        // --- GIVEN ---
        var termsClicked = false
        val fakeSettings = AppSettings(language = "es")

        composeTestRule.setContent {
            VerticalSettingsScreen(
                settingsUIState = SettingsUIState.Success(fakeSettings),
                dialogState = SettingsDialogState(showDialog = false),
                onClickTheme = {},
                onClickLanguage = {},
                onClickGeneralSettings = {},
                onClickOverlay = {},
                onClickExport = {},
                onClickEmail = {},
                onClickGithub = {},
                onClickTerms = { termsClicked = true},
                onClickPrivacy = {},
                onClickAttributions = {},
                onDismissDialog = {},
                onAcceptDialog = {}
            )
        }

        // --- WHEN ---
        composeTestRule.onNodeWithTag("settings_button_terms").performClick()

        // --- THEN ---
        assertTrue(termsClicked)
    }

    @Test
    @SdkSuppress(minSdkVersion = Build.VERSION_CODES.TIRAMISU)
    fun givenSuccessState_whenClickLanguage_thenTriggerOnClickLanguageLambda() {
        // --- GIVEN ---
        var languageClicked = false
        val fakeSettings = AppSettings(language = "es")

        composeTestRule.setContent {
            VerticalSettingsScreen(
                settingsUIState = SettingsUIState.Success(fakeSettings),
                dialogState = SettingsDialogState(showDialog = false),
                onClickTheme = {},
                onClickLanguage = {languageClicked = true},
                onClickGeneralSettings = {},
                onClickOverlay = {},
                onClickExport = {},
                onClickEmail = {},
                onClickGithub = {},
                onClickTerms = {},
                onClickAttributions = {},
                onClickPrivacy = {},
                onDismissDialog = {},
                onAcceptDialog = {}
            )
        }

        // --- WHEN ---
        composeTestRule.onNodeWithTag("settings_button_language").performClick()

        // --- THEN ---
        assertTrue(languageClicked)
    }

    @Test
    @SdkSuppress(
        minSdkVersion = Build.VERSION_CODES.O,
        maxSdkVersion = Build.VERSION_CODES.S_V2
    )
    fun givenSuccessState_whenClickLanguageAndPreviousSDK_thenNoTriggerOnClickLanguageLambda() {
        // --- GIVEN ---
        var languageClicked = false
        val fakeSettings = AppSettings(language = "es")

        composeTestRule.setContent {
            VerticalSettingsScreen(
                settingsUIState = SettingsUIState.Success(fakeSettings),
                dialogState = SettingsDialogState(showDialog = false),
                onClickTheme = {},
                onClickLanguage = { languageClicked = true },
                onClickGeneralSettings = {},
                onClickOverlay = {},
                onClickExport = {},
                onClickEmail = {},
                onClickGithub = {},
                onClickTerms = {},
                onClickAttributions = {},
                onClickPrivacy = {},
                onDismissDialog = {},
                onAcceptDialog = {}
            )
        }

        // --- WHEN & THEN ---
        composeTestRule.onNodeWithTag("settings_button_language").assertDoesNotExist()
    }

    @Test
    fun givenShowDialogTrue_whenScreenRendered_thenShowDialogComposable() {
        // --- GIVEN ---
        val dialogState = SettingsDialogState(
            showDialog = true,
            dataDialog = DataDialog.THEME // O el tipo de diálogo correspondiente
        )
        val fakeSettings = AppSettings(language = "es")

        // --- WHEN ---
        composeTestRule.setContent {
            VerticalSettingsScreen(
                settingsUIState = SettingsUIState.Success(fakeSettings),
                dialogState = dialogState,
                onClickTheme = {},
                onClickLanguage = {},
                onClickGeneralSettings = {},
                onClickOverlay = {},
                onClickExport = {},
                onClickEmail = {},
                onClickGithub = {},
                onClickTerms = {},
                onClickAttributions = {},
                onClickPrivacy = {},
                onDismissDialog = {},
                onAcceptDialog = {}
            )
        }

        // --- THEN ---
        composeTestRule.onNodeWithTag("vertical_settings_dialog").assertIsDisplayed()
    }

    @Test
    fun givenShowDialogFalse_whenScreenRendered_thenDoNotShowDialogComposable() {
        // --- GIVEN ---
        val dialogState = SettingsDialogState(showDialog = false)
        val fakeSettings = AppSettings(language = "es")

        // --- WHEN ---
        composeTestRule.setContent {
            VerticalSettingsScreen(
                settingsUIState = SettingsUIState.Success(fakeSettings),
                dialogState = dialogState,
                onClickTheme = {},
                onClickLanguage = {},
                onClickGeneralSettings = {},
                onClickOverlay = {},
                onClickExport = {},
                onClickEmail = {},
                onClickGithub = {},
                onClickTerms = {},
                onClickAttributions = {},
                onClickPrivacy = {},
                onDismissDialog = {},
                onAcceptDialog = {}
            )
        }

        // --- THEN ---
        // Comprobamos que el nodo del diálogo no existe en el árbol de Compose
        composeTestRule.onNodeWithTag("vertical_settings_dialog").assertDoesNotExist()
    }

    @Test
    fun givenOpenDialog_whenOnDismissTriggeredInDialog_thenCallOnDismissDialog() {
        // --- GIVEN ---
        var dismissCalled = false
        val dialogState = SettingsDialogState(
            showDialog = true,
            dataDialog = DataDialog.THEME
        )
        val fakeSettings = AppSettings(language = "es")

        composeTestRule.setContent {
            VerticalSettingsScreen(
                settingsUIState = SettingsUIState.Success(fakeSettings),
                dialogState = dialogState,
                onClickTheme = {},
                onClickLanguage = {},
                onClickGeneralSettings = {},
                onClickOverlay = {},
                onClickExport = {},
                onClickEmail = {},
                onClickGithub = {},
                onClickTerms = {},
                onClickAttributions = {},
                onClickPrivacy = {},
                onDismissDialog = { dismissCalled = true }, // Capturamos la acción
                onAcceptDialog = {}
            )
        }

        // --- WHEN ---
        composeTestRule.onNodeWithTag("vertical_settings_dialog_close").performClick()

        // --- THEN ---
        assertTrue(dismissCalled)
    }

    @Test
    fun givenAnyDialog_whenClickCloseIcon_thenTriggerOnDismissRequest() {
        // --- GIVEN ---
        var dismissClicked = false

        val fakeDialogData = DataDialog.LANGUAGE

        composeTestRule.setContent {
            VerticalDialogSettings(
                dataDialog = fakeDialogData,
                themeSelected = 0,
                languageSelected = "es",
                daySelected = "MONDAY",
                onDismissRequest = { dismissClicked = true },
                onClickButton = {}
            )
        }

        // --- WHEN ---
        composeTestRule.onNodeWithTag("vertical_settings_dialog_close").performClick()

        // --- THEN ---
        assertTrue(dismissClicked)
    }

    @Test
    fun givenLanguageDialog_whenLanguageSelected_thenTriggerOnClickButtonWithLanguageResult() {
        // --- GIVEN ---
        var resultCaptured: DataResult? = null

        val fakeDialogData = DataDialog.LANGUAGE

        composeTestRule.setContent {
            VerticalDialogSettings(
                dataDialog = fakeDialogData,
                themeSelected = 0,
                languageSelected = "es",
                daySelected = "MONDAY",
                onDismissRequest = {},
                onClickButton = { result -> resultCaptured = result }
            )
        }

        // --- THEN---
        composeTestRule.onNodeWithTag("vertical_settings_dialog").assertIsDisplayed()

        // --- WHEN (Interacción) ---
        composeTestRule.onNodeWithTag("dialog_language_option_en").performClick()

        // --- THEN (Verificación lógica) ---
        assertTrue(resultCaptured is DataResult.LanguageResult)
        assertEquals("en", (resultCaptured as DataResult.LanguageResult).language)
    }

    @Test
    fun givenThemeDialog_whenThemeSelected_thenTriggerOnClickButtonWithThemeResult() {
        // --- GIVEN ---
        var resultCaptured: DataResult? = null
        val fakeDialogData = DataDialog.THEME

        composeTestRule.setContent {
            VerticalDialogSettings(
                dataDialog = fakeDialogData,
                themeSelected = 0,
                languageSelected = "es",
                daySelected = "MONDAY",
                onDismissRequest = {},
                onClickButton = { result -> resultCaptured = result }
            )
        }

        // --- WHEN ---
        composeTestRule.onNodeWithTag("dialog_theme_option_1").performClick()

        // --- THEN ---
        assertTrue(resultCaptured is DataResult.ThemeResult)
        assertEquals(1, (resultCaptured as DataResult.ThemeResult).theme)
    }

    @Test
    fun givenDayWeekDialog_whenDaySelected_thenTriggerOnClickButtonWithDayOfWeekResult() {
        // --- GIVEN ---
        var resultCaptured: DataResult? = null
        val fakeDialogData = DataDialog.DAY_WEEK

        composeTestRule.setContent {
            VerticalDialogSettings(
                dataDialog = fakeDialogData,
                themeSelected = 0,
                languageSelected = "es",
                daySelected = "MONDAY",
                onDismissRequest = {},
                onClickButton = { result -> resultCaptured = result }
            )
        }

        // --- WHEN ---
        composeTestRule.onNodeWithTag("dialog_day_option_MONDAY").performClick()

        // --- THEN ---
        assertTrue(resultCaptured is DataResult.DayOfWeekResult)
        assertEquals("MONDAY", (resultCaptured as DataResult.DayOfWeekResult).dayOfWeek.name)
    }

    @Test
    fun givenSuccessState_whenClickAttributions_thenTriggerOnClickLanguageLambda() {
        // --- GIVEN ---
        var attributionsClicked = false
        val fakeSettings = AppSettings(language = "es")

        composeTestRule.setContent {
            VerticalSettingsScreen(
                settingsUIState = SettingsUIState.Success(fakeSettings),
                dialogState = SettingsDialogState(showDialog = false),
                onClickTheme = {},
                onClickLanguage = {},
                onClickGeneralSettings = {},
                onClickOverlay = {},
                onClickExport = {},
                onClickEmail = {},
                onClickGithub = {},
                onClickTerms = {},
                onClickAttributions = { attributionsClicked = true },
                onClickPrivacy = {},
                onDismissDialog = {},
                onAcceptDialog = {}
            )
        }

        // --- WHEN ---
        composeTestRule.onNodeWithTag("settings_button_attributions").performClick()

        // --- THEN ---
        assertTrue(attributionsClicked)
    }

    @Test
    fun givenSuccessState_whenClickPrivacy_thenTriggerOnClickLanguageLambda() {
        // --- GIVEN ---
        var privacyClicked = false
        val fakeSettings = AppSettings(language = "es")

        composeTestRule.setContent {
            VerticalSettingsScreen(
                settingsUIState = SettingsUIState.Success(fakeSettings),
                dialogState = SettingsDialogState(showDialog = false),
                onClickTheme = {},
                onClickLanguage = {},
                onClickGeneralSettings = {},
                onClickOverlay = {},
                onClickExport = {},
                onClickEmail = {},
                onClickGithub = {},
                onClickTerms = {},
                onClickAttributions = {},
                onClickPrivacy = { privacyClicked = true  },
                onDismissDialog = {},
                onAcceptDialog = {}
            )
        }

        // --- WHEN ---
        composeTestRule.onNodeWithTag("settings_button_privacy").performClick()

        // --- THEN ---
        assertTrue(privacyClicked)
    }


}