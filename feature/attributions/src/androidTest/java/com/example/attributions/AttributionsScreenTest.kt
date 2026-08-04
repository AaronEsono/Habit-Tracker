package com.example.attributions

import aeb.proyecto.attributions.R
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.attributions.components.AuthorButton
import com.example.attributions.components.FlaticonButton
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.verify

@RunWith(AndroidJUnit4::class)
@SmallTest
class AttributionsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenFlaticonButtonIsClicked_thenOnClickIsTriggeredWithCorrectUri() {
        // GIVEN
        val titleResId = R.string.app_name
        val testUri = "https://www.flaticon.com/free-icons/habit"
        val expectedTag = "attribution_flaticon_button_$testUri"

        var capturedUri: String? = null

        composeTestRule.setContent {
            FlaticonButton(
                title = titleResId,
                uri = testUri,
                onClick = { uri -> capturedUri = uri }
            )
        }

        // WHEN
        composeTestRule
            .onNodeWithTag(expectedTag)
            .assertIsDisplayed()
            .performClick()

        // THEN
        assertEquals(testUri, capturedUri)
    }

    @Test
    fun whenAuthorButtonIsClicked_thenOnClickIsTriggeredWithCorrectUri() {
        // GIVEN
        val iconName = "Check"
        val authorName = "John Doe"
        val testUri = "https://www.svgrepo.com/author/john"
        val expectedTag = "attribution_author_${iconName}_$authorName"

        var capturedUri: String? = null

        composeTestRule.setContent {
            AuthorButton(
                iconName = iconName,
                authorName = authorName,
                uri = testUri,
                onClick = { uri -> capturedUri = uri }
            )
        }

        // WHEN: Localizamos el Row por su testTag y simulamos el click
        composeTestRule
            .onNodeWithTag(expectedTag)
            .assertIsDisplayed()
            .performClick()

        // THEN: Validamos que la lambda recibe la URL adecuada
        assertEquals(testUri, capturedUri)
    }

}