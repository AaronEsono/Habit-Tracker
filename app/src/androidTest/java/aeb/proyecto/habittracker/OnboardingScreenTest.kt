package aeb.proyecto.habittracker

import aeb.proyecto.habittracker.components.onboardScreen.components.button.OnboardingButton
import aeb.proyecto.habittracker.components.onboardScreen.components.constants.OnboardingPage
import aeb.proyecto.habittracker.components.onboardScreen.components.pages.OnboardingPageScreen
import aeb.proyecto.habittracker.components.onboardScreen.components.pages.PageIndicator
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.width
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class OnboardingScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun given_onboarding_button_when_clicked_then_performs_the_action(){
        var clicked = false

        composeTestRule.setContent {
            OnboardingButton(
                title = R.string.onboarding_previous,
                onClick = { clicked = true }
            )
        }

        composeTestRule.onNodeWithTag("onboarding_button").performClick()

        assertEquals(true,clicked)
    }

    @Test
    fun given_OnboardingPageScreen_when_set_a_determined_page_then_show_the_correct_page(){

        // Given
        val targetPage = OnboardingPage.First
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val expectedTitle = context.getString(targetPage.title)
        val expectedSubtitle = context.getString(targetPage.subtitle)

        // When
        composeTestRule.setContent {
            OnboardingPageScreen(
                pageSelected = targetPage
            )
        }

        // Then
        composeTestRule.onNodeWithTag("onboarding_title")
            .assertIsDisplayed()
            .assertTextEquals(expectedTitle)

        composeTestRule.onNodeWithTag("onboarding_subtitle")
            .assertIsDisplayed()
            .assertTextEquals(expectedSubtitle)

        composeTestRule.onNodeWithTag("onboarding_image")
            .assertIsDisplayed()
    }

    @Test
    fun given_PageIndicator_when_pageSelectedIsX_then_indexXIsExpandedAndOthersAreUnselected() {
        val totalPages = 5
        val selectedIndex = 2

        composeTestRule.setContent {
            PageIndicator(
                pageCount = totalPages,
                currentPageIndex = selectedIndex
            )
        }

        composeTestRule.waitForIdle()

        for (index in 0 until totalPages) {
            val indicatorNode = composeTestRule.onNodeWithTag("onboarding_indicator_$index")
            indicatorNode.assertIsDisplayed()

            val bounds = indicatorNode.getUnclippedBoundsInRoot()
            val width: Dp = bounds.width

            if (index == selectedIndex) {
                val unselectedWidth: Dp = composeTestRule
                    .onNodeWithTag("onboarding_indicator_0")
                    .getUnclippedBoundsInRoot().width

                assert(width > unselectedWidth) {
                    "El indicador $index debía estar expandido, pero su ancho ($width) no es mayor que el inactivo ($unselectedWidth)"
                }
            } else {
                val unselectedWidth: Dp = composeTestRule
                    .onNodeWithTag("onboarding_indicator_0")
                    .getUnclippedBoundsInRoot().width

                assertEquals(unselectedWidth, width)
            }
        }
    }

}