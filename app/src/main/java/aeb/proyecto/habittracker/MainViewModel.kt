package aeb.proyecto.habittracker

import aeb.proyecto.domain.usecase.main.ManageDatastoreUseCase
import aeb.proyecto.domain.usecase.main.ManageDialogTimerUseCase
import aeb.proyecto.domain.usecase.main.ManageHabitsUseCase
import aeb.proyecto.domain.usecase.main.ShowDialogState
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
import java.util.Locale
import javax.inject.Inject


/**
 * Core ViewModel for the application's main entry point.
 *
 * This ViewModel acts as the orchestrator for global application states and side effects.
 * It manages cross-cutting concerns such as user preferences persistent storage via [ManageDatastoreUseCase],
 * regional calendar configurations through [RegionFirstDayProvider], global application dialog timers,
 * and essential background routine synchronizations using [ManageHabitsUseCase].
 *
 * Since it is scoped to the main hosting activity or root navigation graph, it ensures that
 * foundational user configurations (e.g., theme settings, language preferences, and global UI events)
 * are eagerly loaded and sustained throughout the application lifecycle.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val manageDatastoreUseCase: ManageDatastoreUseCase,
    private val firstDayProvider: RegionFirstDayProvider,
    manageDialogTimerUseCase: ManageDialogTimerUseCase,
    private val manageHabitsUseCase: ManageHabitsUseCase
) : ViewModel(){

    private val _dataSet = MutableStateFlow(false)

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
            setDayWeek()
            setLanguage()
            _dataSet.value = true
        }
    }

    /**
     * Inspects current local storage for the preferred first day of the week.
     * Fallbacks to reading the device's default system [Locale] if no preference exists.
     */
    private suspend fun setDayWeek(){
        val day = manageDatastoreUseCase.getDayOfWeek()
        if(day == null){
            val firstDay = firstDayProvider.getFirstDayOfWeekByLocale().name
            manageDatastoreUseCase.setDayOfWeek(firstDay)
        }
    }

    /**
     * Inspects current local storage for the application's language configuration.
     * Fallbacks to the native device language context, defaulting to English if the
     * scanned system language profile is unsupported by the app's catalog.
     */
    private suspend fun setLanguage(){
        if(manageDatastoreUseCase.getLanguage() == null){
            val language = Locale.getDefault().language

            if(findLanguage(language) != null){
                manageDatastoreUseCase.setLanguage(language)
            }else{
                manageDatastoreUseCase.setLanguage(EnumLanguage.ENGLISH.value)
            }
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

}