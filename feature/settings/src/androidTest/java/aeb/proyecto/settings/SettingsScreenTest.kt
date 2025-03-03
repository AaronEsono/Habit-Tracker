package aeb.proyecto.settings

import aeb.proyecto.language.model.EnumLanguage
import aeb.proyecto.settings.components.dialog.DialogSettings
import aeb.proyecto.settings.model.DataDialog
import aeb.proyecto.ui.theme.EnumTheme
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.junit.*
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class SettingsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    val context = ApplicationProvider.getApplicationContext<Context>()


    @Test
    fun settingsScreenShowCorrectly() {
        composeTestRule.setContent {
            SettingsScreen(
                onClickTheme = {},
                onClickLanguage = {},
                onClickExport = {},
                onClickEmail = {},
                onClickGithub = {},
                onClickLinkedin = {}
            )
        }

        //Texto configuracion
        composeTestRule.onNodeWithText(context.getString(R.string.settings_configuration))
            .assertExists().assertIsDisplayed()

        //Texto acerda de
        composeTestRule.onNodeWithText(context.getString(R.string.settings_about)).assertExists()
            .assertIsDisplayed()

        //Botones
        composeTestRule.onNodeWithText(context.getString(R.string.settings_theme)).assertExists()
            .assertIsDisplayed()

        composeTestRule.onNodeWithText(context.getString(R.string.settings_language)).assertExists()
            .assertIsDisplayed()

        composeTestRule.onNodeWithText(context.getString(R.string.settings_export_import))
            .assertExists().assertIsDisplayed()

        composeTestRule.onNodeWithText(context.getString(R.string.settings_email)).assertExists()
            .assertIsDisplayed()

        composeTestRule.onNodeWithText(context.getString(R.string.settings_github)).assertExists()
            .assertIsDisplayed()

        composeTestRule.onNodeWithText(context.getString(R.string.settings_link)).assertExists()
            .assertIsDisplayed()

    }

    @Test
    fun settingsButtonPerformCorrectly() {
        var clickedTheme = false
        var clickedLanguage = false
        var clickedExport = false
        var clickedEmail = false
        var clickedGithub = false
        var clickedLinkedin = false

        composeTestRule.setContent {
            SettingsScreen(
                onClickTheme = { clickedTheme = true },
                onClickLanguage = { clickedLanguage = true },
                onClickExport = { clickedExport = true },
                onClickEmail = { clickedEmail = true },
                onClickGithub = { clickedGithub = true },
                onClickLinkedin = { clickedLinkedin = true }
            )
        }

        assertFalse(clickedTheme)
        assertFalse(clickedLanguage)
        assertFalse(clickedExport)
        assertFalse(clickedEmail)
        assertFalse(clickedGithub)
        assertFalse(clickedLinkedin)

        composeTestRule.onNodeWithText(context.getString(R.string.settings_theme)).performClick()
        composeTestRule.onNodeWithText(context.getString(R.string.settings_language)).performClick()
        composeTestRule.onNodeWithText(context.getString(R.string.settings_export_import)).performClick()
        composeTestRule.onNodeWithText(context.getString(R.string.settings_email)).performClick()
        composeTestRule.onNodeWithText(context.getString(R.string.settings_github)).performClick()
        composeTestRule.onNodeWithText(context.getString(R.string.settings_link)).performClick()

        assertTrue(clickedTheme)
        assertTrue(clickedLanguage)
        assertTrue(clickedExport)
        assertTrue(clickedEmail)
        assertTrue(clickedGithub)
        assertTrue(clickedLinkedin)
    }

    @Test
    fun settingsDialogThemeShowCorrectly() {
        composeTestRule.setContent {
            DialogSettings(
                dataDialog = DataDialog.THEME,
                themeSelected = 1,
                languageSelected = "English",
                onDismissRequest = {},
                onClickButton = {}
            )
        }

        //Texto configuracion
        composeTestRule.onNodeWithText(context.getString(R.string.settings_theme_pick)).assertExists().assertIsDisplayed()

        //Imagen dialog
        composeTestRule.onNodeWithContentDescription("image dialog").assertExists().assertIsDisplayed()

        //Botones
        EnumTheme.entries.forEach {
            composeTestRule.onNodeWithText(context.getString(it.title)).assertExists()
                .assertIsDisplayed()
        }
    }

    @Test
    fun settingsDialogLanguageShowCorrectly() {
        composeTestRule.setContent {
            DialogSettings(
                dataDialog = DataDialog.LANGUAGE,
                themeSelected = 1,
                languageSelected = "English",
                onDismissRequest = {},
                onClickButton = {}
            )
        }

        //Texto configuracion
        composeTestRule.onNodeWithText(context.getString(R.string.settings_language_pick)).assertExists().assertIsDisplayed()

        //Imagen dialog
        composeTestRule.onNodeWithContentDescription("image dialog").assertExists().assertIsDisplayed()

        //Botones
        EnumLanguage.entries.forEach {
            composeTestRule.onNodeWithText(context.getString(it.title)).assertExists()
                .assertIsDisplayed()
        }
    }

    @Test
    fun dialogNotDisplayWhenItsClosed(){
        var isEnabled = true

        composeTestRule.setContent {
            DialogSettings(
                dataDialog = DataDialog.LANGUAGE,
                themeSelected = 1,
                languageSelected = "English",
                onDismissRequest = { isEnabled = false },
                onClickButton = {}
            )
        }

        composeTestRule.onNodeWithText(context.getString(R.string.settings_language_pick))
            .assertExists().assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("close button").performClick()

        assertFalse(isEnabled)

        composeTestRule.onNodeWithText(context.getString(R.string.settings_language_pick))
            .isNotDisplayed()
    }

    @Test
    fun dialogDoesntShowWhenSettingsIsShow(){
        var showDialog = false

        composeTestRule.setContent {
            SettingsScreen(
                onClickTheme = {},
                onClickLanguage = {},
                onClickExport = {},
                onClickEmail = {},
                onClickGithub = {},
                onClickLinkedin = {}
            )

            if(showDialog){
                DialogSettings(
                    dataDialog = DataDialog.LANGUAGE,
                    themeSelected = 1,
                    languageSelected = "English",
                    onDismissRequest = { },
                    onClickButton = {}
                )
            }
        }

        //Texto configuracion
        composeTestRule.onNodeWithText(context.getString(R.string.settings_configuration))
            .assertExists().assertIsDisplayed()

        //Texto acerda de
        composeTestRule.onNodeWithText(context.getString(R.string.settings_about)).assertExists()
            .assertIsDisplayed()


        composeTestRule.onNodeWithText(context.getString(R.string.settings_language_pick))
            .isNotDisplayed()
    }

    @Test
    fun performClickDialogSettings(){
        var clicked = false

        composeTestRule.setContent {
            DialogSettings(
                dataDialog = DataDialog.LANGUAGE,
                themeSelected = 1,
                languageSelected = "English",
                onDismissRequest = { },
                onClickButton = { clicked = true }
            )
        }

        assertFalse(clicked)

        EnumLanguage.entries.forEach {
            composeTestRule.onNodeWithText(context.getString(it.title)).performClick()
        }

        assertTrue(clicked)
    }

    @Test
    fun settingsButtonDisplaysTheCorrectDialog(){
        var dataDialog = DataDialog.LANGUAGE

        composeTestRule.setContent {
            var isShow by remember { mutableStateOf(false) }

            SettingsScreen(
                onClickTheme = { dataDialog = DataDialog.THEME; isShow = true },
                onClickLanguage = { dataDialog = DataDialog.LANGUAGE; isShow = true },
                onClickExport = {},
                onClickEmail = {},
                onClickGithub = {},
                onClickLinkedin = {}
            )

            if(isShow){
                DialogSettings(
                    dataDialog = dataDialog,
                    themeSelected = 1,
                    languageSelected = "English",
                    onDismissRequest = { isShow = false },
                    onClickButton = {})
            }

        }

        //DIALOG DISABLED
        composeTestRule.onNodeWithText(context.getString(R.string.settings_configuration))
            .assertExists().assertIsDisplayed()

        composeTestRule.onNodeWithText(context.getString(R.string.settings_about)).assertExists()
            .assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("image dialog").assertIsNotDisplayed()
        assertTrue(DataDialog.LANGUAGE == dataDialog)

        // DIALOG ENABLED, THEME MODE
        composeTestRule.onNodeWithText(context.getString(R.string.settings_theme)).performClick()
        composeTestRule.onNodeWithText(context.getString(R.string.settings_theme_pick))
            .assertExists().assertIsDisplayed()
        assertTrue(DataDialog.THEME == dataDialog)

        // DIALOG DISABLED
        composeTestRule.onNodeWithContentDescription("close button").performClick()
        composeTestRule.onNodeWithContentDescription("image dialog").assertIsNotDisplayed()

        // DIALOG, ENABLED, LANGUAGE MODE
        composeTestRule.onNodeWithText(context.getString(R.string.settings_language)).performClick()
        composeTestRule.onNodeWithText(context.getString(R.string.settings_language_pick))
            .assertExists().assertIsDisplayed()
        assertTrue(DataDialog.LANGUAGE == dataDialog)
    }

}