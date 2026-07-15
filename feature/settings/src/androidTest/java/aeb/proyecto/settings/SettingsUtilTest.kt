package aeb.proyecto.settings

import aeb.proyecto.settings.constants.SettingsConstants
import aeb.proyecto.settings.utils.getSelectionContainerColor
import aeb.proyecto.settings.utils.openLink
import aeb.proyecto.settings.utils.openOverlayPermissionSettings
import aeb.proyecto.settings.utils.sendEmail
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.intent.matcher.IntentMatchers.hasFlag
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import junit.framework.TestCase.assertEquals
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.core.AllOf.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class SettingsUtilTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context: Context = ApplicationProvider.getApplicationContext()

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun givenCurrentEqualsSelected_whenGetSelectionContainerColor_thenReturnSurfaceContainerColor() {
        // --- GIVEN ---
        val current = "es"
        val selected = "es"

        var resolvedColor: Color = Color.Unspecified
        var expectedColor: Color = Color.Unspecified

        // --- WHEN ---
        composeTestRule.setContent {
            MaterialTheme {
                resolvedColor = getSelectionContainerColor(current, selected)
                expectedColor = MaterialTheme.colorScheme.surfaceContainer
            }
        }

        // --- THEN ---
        assertEquals(expectedColor, resolvedColor)
    }

    @Test
    fun givenCurrentDifferentFromSelected_whenGetSelectionContainerColor_thenReturnBackgroundColor() {
        // --- GIVEN ---
        val current = "es"
        val selected = "en"

        var resolvedColor: Color = Color.Unspecified
        var expectedColor: Color = Color.Unspecified

        // --- WHEN ---
        composeTestRule.setContent {
            MaterialTheme {
                resolvedColor = getSelectionContainerColor(current, selected)
                expectedColor = MaterialTheme.colorScheme.background
            }
        }

        // --- THEN ---
        assertEquals(expectedColor, resolvedColor)
    }

    @Test
    fun whenOpenLinkIsCalled_thenCorrectIntentIsLaunched() {
        val targetUri = "https://github.com"

        openLink(context, targetUri)

        intended(
            allOf(
                hasAction(Intent.ACTION_VIEW),
                hasData(Uri.parse(targetUri))
            )
        )
    }

    @Test
    fun whenSendEmailIsCalled_thenCorrectEmailIntentIsPrepared() {
        sendEmail(context)

        intended(
            allOf(
                hasAction(Intent.ACTION_SENDTO),
                hasData("mailto:"),
                hasExtra(Intent.EXTRA_EMAIL, arrayOf(SettingsConstants.EMAIL)),
                hasExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.settings_email_title)),
                hasExtra(Intent.EXTRA_TEXT, notNullValue())
            )
        )
    }

    @Test
    fun whenOpenOverlayPermissionIsCalled_thenCorrectSystemSettingsIntentIsLaunched() {
        val expectedUri = Uri.parse("package:${context.packageName}")

        openOverlayPermissionSettings(context)

        intended(
            allOf(
                hasAction(Settings.ACTION_MANAGE_OVERLAY_PERMISSION),
                hasData(expectedUri),
                hasFlag(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        )
    }
}