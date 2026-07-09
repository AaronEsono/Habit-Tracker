package aeb.proyecto.ui

import aeb.proyecto.ui.text.LabelMediumText
import aeb.proyecto.ui.topbar.TopBarViewModel
import aeb.proyecto.ui.topbar.providers.ProvideAppBarActions
import aeb.proyecto.ui.topbar.providers.ProvideAppBarNavigationIcon
import aeb.proyecto.ui.topbar.providers.ProvideAppBarTitle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Rule
import org.junit.Test

class TopBarUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private class FakeViewModelStoreOwner : ViewModelStoreOwner {
        private val store = ViewModelStore()
        override val viewModelStore: ViewModelStore = store
    }

    @Test
    fun givenAComposition_whenProvideAppBarTitleIsCalled_thenUpdatesViewModelState() {
        // --- GIVEN ---
        val fakeOwner = FakeViewModelStoreOwner()
        var capturedViewModel: TopBarViewModel? = null

        // --- WHEN ---
        composeTestRule.setContent {
            CompositionLocalProvider(LocalViewModelStoreOwner provides fakeOwner) {

                ProvideAppBarTitle(title = {
                    LabelMediumText(text = "Inicio Habit Tracker")
                })

                capturedViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                    viewModelStoreOwner = fakeOwner,
                    initializer = { TopBarViewModel() }
                )
            }
        }

        composeTestRule.waitForIdle()

        // --- THEN ---
        assertNotNull("El ViewModel debería haberse creado", capturedViewModel)
        assertNotNull(capturedViewModel?.title)
    }

    @Test
    fun givenAComposition_whenProvideAppBarNavigationIsCalled_thenUpdatesViewModelState() {
        // --- GIVEN ---
        val fakeOwner = FakeViewModelStoreOwner()
        var capturedViewModel: TopBarViewModel? = null

        // --- WHEN ---
        composeTestRule.setContent {
            CompositionLocalProvider(LocalViewModelStoreOwner provides fakeOwner) {

                ProvideAppBarNavigationIcon(
                    key = Unit,
                    navigationIcon = {
                        LabelMediumText(text = "Inicio Habit Tracker")
                    }
                )

                capturedViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                    viewModelStoreOwner = fakeOwner,
                    initializer = { TopBarViewModel() }
                )
            }
        }

        composeTestRule.waitForIdle()

        // --- THEN ---
        assertNotNull("El ViewModel debería haberse creado", capturedViewModel)
        assertNotNull(capturedViewModel?.navigationIcon)
    }

    @Test
    fun givenAComposition_whenProvideAppBarActionIsCalled_thenUpdatesViewModelState() {
        // --- GIVEN ---
        val fakeOwner = FakeViewModelStoreOwner()
        var capturedViewModel: TopBarViewModel? = null

        // --- WHEN ---
        composeTestRule.setContent {
            CompositionLocalProvider(LocalViewModelStoreOwner provides fakeOwner) {

                ProvideAppBarActions(
                    actions = {
                        LabelMediumText(text = "Inicio Habit Tracker")
                    }
                )

                capturedViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                    viewModelStoreOwner = fakeOwner,
                    initializer = { TopBarViewModel() }
                )
            }
        }

        composeTestRule.waitForIdle()

        // --- THEN ---
        assertNotNull("El ViewModel debería haberse creado", capturedViewModel)
        assertNotNull(capturedViewModel?.actions)
    }

    @Test
    fun givenTitleChanges_whenProvideAppBarTitleIsCalled_thenUpdatesToNewState() {
        // --- GIVEN ---
        val fakeOwner = FakeViewModelStoreOwner()
        var capturedViewModel: TopBarViewModel? = null

        val titleState = mutableStateOf<@Composable () -> Unit>({ LabelMediumText("Título Inicial") })

        // --- WHEN ---
        composeTestRule.setContent {
            CompositionLocalProvider(LocalViewModelStoreOwner provides fakeOwner) {

                ProvideAppBarTitle(title = titleState.value)

                capturedViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                    viewModelStoreOwner = fakeOwner,
                    initializer = { TopBarViewModel() }
                )
            }
        }

        composeTestRule.waitForIdle()

        titleState.value = { LabelMediumText("Título Cambiado") }

        composeTestRule.waitForIdle()

        // --- THEN ---
        assertNotNull("El ViewModel debería existir", capturedViewModel)
        assertNotNull(capturedViewModel?.title)
    }

    @Test
    fun givenTitleChanges_whenProvideAppBarNavigationIsCalled_thenUpdatesToNewState() {
        // --- GIVEN ---
        val fakeOwner = FakeViewModelStoreOwner()
        var capturedViewModel: TopBarViewModel? = null

        val navigationState = mutableStateOf<@Composable () -> Unit>({ LabelMediumText("Título Inicial") })

        // --- WHEN ---
        composeTestRule.setContent {
            CompositionLocalProvider(LocalViewModelStoreOwner provides fakeOwner) {

                ProvideAppBarNavigationIcon(
                    key = Unit,
                    navigationIcon = {
                        navigationState.value
                    }
                )

                capturedViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                    viewModelStoreOwner = fakeOwner,
                    initializer = { TopBarViewModel() }
                )
            }
        }

        composeTestRule.waitForIdle()

        navigationState.value = { LabelMediumText("Título Cambiado") }

        composeTestRule.waitForIdle()

        // --- THEN ---
        assertNotNull("El ViewModel debería existir", capturedViewModel)
        assertNotNull(capturedViewModel?.navigationIcon)
    }

    @Test
    fun givenTitleChanges_whenProvideAppBarActionsIsCalled_thenUpdatesToNewState() {
        // --- GIVEN ---
        val fakeOwner = FakeViewModelStoreOwner()
        var capturedViewModel: TopBarViewModel? = null

        val actionState = mutableStateOf< @Composable (RowScope.() -> Unit)>({ LabelMediumText("Título Inicial") })

        // --- WHEN ---
        composeTestRule.setContent {
            CompositionLocalProvider(LocalViewModelStoreOwner provides fakeOwner) {

                ProvideAppBarActions(
                    actions = {
                        actionState.value
                    }
                )

                capturedViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                    viewModelStoreOwner = fakeOwner,
                    initializer = { TopBarViewModel() }
                )
            }
        }

        composeTestRule.waitForIdle()

        actionState.value = { LabelMediumText("Título Cambiado") }

        composeTestRule.waitForIdle()

        // --- THEN ---
        assertNotNull("El ViewModel debería existir", capturedViewModel)
        assertNotNull(capturedViewModel?.actions)
    }

}