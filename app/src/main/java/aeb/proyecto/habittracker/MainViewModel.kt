package aeb.proyecto.habittracker

import aeb.proyecto.domain.usecase.main.ManageDatastoreUseCase
import aeb.proyecto.domain.usecase.main.ManageDialogTimerUseCase
import aeb.proyecto.domain.usecase.main.ManageHabitsUseCase
import aeb.proyecto.domain.usecase.main.ManageOnboardingScreenUseCase
import aeb.proyecto.domain.usecase.main.ShowDialogState
import aeb.proyecto.habittracker.components.onboardScreen.components.constants.OnboardingPage
import aeb.proyecto.habittracker.components.onboardScreen.components.constants.ResultOptions
import aeb.proyecto.habittracker.components.onboardScreen.components.constants.onboardingPages
import aeb.proyecto.language.model.EnumLanguage
import aeb.proyecto.language.model.findLanguage
import aeb.proyecto.language.provider.RegionFirstDayProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.util.Locale
import javax.inject.Inject


/**
 * Core ViewModel for the application's main entry point.
 *
 * This ViewModel acts as the orchestrator for global application states and side effects.
 * It manages cross-cutting concerns such as persistent user preferences via
 * [ManageDatastoreUseCase], regional calendar configurations through
 * [RegionFirstDayProvider], global application dialog timers, onboarding screen state
 * through [ManageOnboardingScreenUseCase], and essential background routine
 * synchronizations using [ManageHabitsUseCase].
 *
 * Since it is scoped to the main hosting activity or root navigation graph, it ensures that
 * foundational user configurations (e.g., theme settings, language preferences, onboarding
 * state, and global UI events) are eagerly loaded and sustained throughout the application
 * lifecycle.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val manageDatastoreUseCase: ManageDatastoreUseCase,
    private val firstDayProvider: RegionFirstDayProvider,
    manageDialogTimerUseCase: ManageDialogTimerUseCase,
    private val manageHabitsUseCase: ManageHabitsUseCase,
    private val manageOnboardingScreenUseCase: ManageOnboardingScreenUseCase
) : ViewModel(){

    private val _dataSet = MutableStateFlow(false)

    private val _onboardingPageSelected: MutableStateFlow<OnboardingPage> = MutableStateFlow(OnboardingPage.First)
    val onboardingPageSelected: StateFlow<OnboardingPage> = _onboardingPageSelected.asStateFlow()

    /**
     * Emits the global visibility and scheduling status of the final timer dialog.
     */
    val showDialogTimer:StateFlow<ShowDialogState> = manageDialogTimerUseCase.showDialogTimer
        .stateIn(
            scope = viewModelScope,
            initialValue = ShowDialogState.NoShowDialog,
            started = SharingStarted.WhileSubscribed(5000)
        )

    /**
     * Exposes the current onboarding screen state as a [StateFlow].
     *
     * The flow is collected while the ViewModel is active and subscribed to,
     * and defaults to `false` until the first value is emitted.
     */
    val showOnboardScreen:StateFlow<Boolean> = manageOnboardingScreenUseCase.showOnboardingScreen
        .stateIn(
            scope = viewModelScope,
            initialValue = true,
            started = SharingStarted.WhileSubscribed(5000)
        )


    /**
     * Emits the user's selected UI theme mode preference from the persistent storage.
     * Represented as an integer index for theme selection.
     */
    val themeMode: StateFlow<Int> = manageDatastoreUseCase.themeMode.stateIn(
        scope = viewModelScope,
        initialValue = 0,
        started = SharingStarted.WhileSubscribed(5000)
    )

    /**
     * Exposes whether a global dynamic informational toast message should be displayed.
     */
    private val _showToast: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showToast: StateFlow<Boolean> = _showToast.asStateFlow()

    /**
     * Triggers the foundational baseline configuration initialization for the session.
     *
     * Enforces a single-execution guard layer to ensure system localization values,
     * language preferences, and regional first-day-of-week settings are loaded precisely
     * once per application lifecycle sequence.
     */
    fun setData() = viewModelScope.launch{
        if(!_dataSet.value){
            // 1. Retrieve the atomic multi-preference configuration state snapshot
            val currentSettings = manageDatastoreUseCase.getAppSettings()

            // Local transaction variables to batch potential mutations
            var updatedDay = currentSettings.dayStartWeek
            var updatedLanguage = currentSettings.language
            var needsUpdate = false

            // 2. Evaluate calendar topology: fallback to localized device metrics if preference is unestablished
            if (currentSettings.dayStartWeek.isEmpty() || currentSettings.dayStartWeek == DayOfWeek.MONDAY.name) {
                updatedDay = firstDayProvider.getFirstDayOfWeekByLocale().name
                needsUpdate = true
            }

            // 3. Evaluate internationalization state: match system ISO-639 codes against supported app catalog
            if (currentSettings.language.isEmpty()) {
                val systemLanguage = Locale.getDefault().language
                updatedLanguage = if (findLanguage(systemLanguage) != null) {
                    systemLanguage
                } else {
                    EnumLanguage.ENGLISH.value
                }
                needsUpdate = true
            }

            // 4. Atomic Transaction Commit: Dispatch structural payload mutations in a single disk I/O operation
            if (needsUpdate) {
                manageDatastoreUseCase.saveSettingsApp(
                    currentSettings.copy(
                        dayStartWeek = updatedDay,
                        language = updatedLanguage
                    )
                )
            }

            _dataSet.value = true
        }
    }

    /**
     * Closes the active dialog timer state in persistent storage.
     */
    fun closeDialog() = viewModelScope.launch{
        manageDatastoreUseCase.closeDialog()
    }

    /**
     * Synchronizes and updates the status of a specific habit based on the current
     * dialog state visibility context if authorized by the user.
     * * This operation runs safely on a background worker thread ([Dispatchers.IO]), extracts the
     * required operational metadata (habit ID, target evaluation date, and modern time context)
     * from the active [ShowDialogState.ShowDialog] state, commits changes to the database layer via
     * [ManageHabitsUseCase], dismisses the dialog, and updates the local state to trigger a success toast.
     */
    fun updateHabit() = viewModelScope.launch(Dispatchers.IO) {
        when(showDialogTimer.value){
            ShowDialogState.NoShowDialog -> Unit
            is ShowDialogState.ShowDialog -> {
                manageHabitsUseCase.updateHabit(
                    (showDialogTimer.value as ShowDialogState.ShowDialog).habit.habit.id,
                    (showDialogTimer.value as ShowDialogState.ShowDialog).habit.day.date,
                    (showDialogTimer.value as ShowDialogState.ShowDialog).time
                )
            }
        }

        manageDatastoreUseCase.closeDialog()
        _showToast.update { true }
    }

    /**
     * Resets the global toast state visibility flag back to false.
     */
    fun clearToast(){
        _showToast.update { false }
    }

    /**
     * Updates the onboarding screen state.
     *
     * Launches a coroutine within the ViewModel scope to persist the new onboarding
     * state through [ManageOnboardingScreenUseCase].
     *
     * @param onboardState `true` to mark the onboarding screen as completed,
     * or `false` to mark it as incomplete.
     */
    fun setOnboardScreen(onboardState: Boolean) = viewModelScope.launch {
        manageOnboardingScreenUseCase.setShowOnboardingScreen(onboardState)
    }

    /**
     * Handles the selected option on an onboarding page.
     *
     * @param resultOptions The action selected by the user for the current onboarding page.
     */
    fun manageResultOptionOnboardingPage(resultOptions: ResultOptions){
        when(resultOptions){
            ResultOptions.Skip -> setOnboardScreen(false)
            ResultOptions.Next -> getNextPage()
            ResultOptions.Previous -> getPreviousPage()
            ResultOptions.Finish -> setOnboardScreen(false)
        }
    }

    /**
     * Moves to the next onboarding page.
     *
     * If the current page is the last one, the selection remains unchanged. **/
    fun getNextPage(){
        val currentIndex = onboardingPages.indexOf(_onboardingPageSelected.value)
        val nextIndex = (currentIndex + 1).coerceIn(0, onboardingPages.lastIndex)

        _onboardingPageSelected.value = onboardingPages[nextIndex]
    }

    /**
     * Moves to the previous onboarding page.
     *
     * If the current page is the first one, the selection remains unchanged.
     */
    fun getPreviousPage() {
        val currentIndex = onboardingPages.indexOf(_onboardingPageSelected.value)
        val previousIndex = (currentIndex - 1).coerceIn(0, onboardingPages.lastIndex)

        _onboardingPageSelected.value = onboardingPages[previousIndex]
    }

}