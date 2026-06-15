package aeb.proyecto.save

import aeb.proyecto.domain.usecase.save.SaveAuthenticationUseCase
import aeb.proyecto.domain.usecase.save.SaveFirestoreUseCase
import aeb.proyecto.domain.usecase.save.SaveHabitsRepositoryUseCase
import aeb.proyecto.domain.usecase.save.SaveNotificationUseCase
import aeb.proyecto.firestore.AuthResponseFirestore
import aeb.proyecto.firestore.model.FirestoreData
import aeb.proyecto.save.model.BottomSheetState
import aeb.proyecto.save.model.DataBottomSheet
import aeb.proyecto.save.model.DataSaveScreen
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * ViewModel orchestrator for cloud synchronization and local data persistence.
 * Manages the transition states for Firestore operations, authentication,
 * and notification scheduling.
 *
 * @property saveHabitsRepositoryUseCase Handles local persistence of habit data.
 * @property saveNotificationUseCase Manages scheduled tasks and alarms.
 * @property saveFirestoreUseCase Bridges the gap between local state and cloud database.
 * @property saveAuthenticationUseCase Orchestrates session management and identity.
 */
@HiltViewModel
class SaveViewModel @Inject constructor(
    private val saveHabitsRepositoryUseCase: SaveHabitsRepositoryUseCase,
    private val saveNotificationUseCase: SaveNotificationUseCase,
    private val saveFirestoreUseCase: SaveFirestoreUseCase,
    private val saveAuthenticationUseCase: SaveAuthenticationUseCase
):ViewModel() {

    /** Orchestrates the visibility and content state of the UI bottom sheets. */
    private val _bottomSheetState: MutableStateFlow<BottomSheetState> =
        MutableStateFlow(BottomSheetState())
    val bottomSheetState: StateFlow<BottomSheetState> = _bottomSheetState.asStateFlow()

    /** Tracks the current synchronization status (Loading, Success, Error, LogOut). */
    private val _saveUIState: MutableStateFlow<SaveUIState> = MutableStateFlow(SaveUIState.Loading)
    val saveUIState: StateFlow<SaveUIState> = _saveUIState.asStateFlow()

    /** Holds the data model for the Save Screen UI components. */
    private val _dataSaveScreen: MutableStateFlow<DataSaveScreen> = MutableStateFlow(DataSaveScreen())
    val dataSaveScreen: StateFlow<DataSaveScreen> = _dataSaveScreen.asStateFlow()

    /** Tracks if the cloud synchronization search/fetch has been performed. */
    private val _dataSearched: MutableStateFlow<Boolean> = MutableStateFlow(false)

    /**
     * Closes the active bottom sheet by updating the UI state.
     */
    fun closeBottomSheet() {
        _bottomSheetState.update { currentState ->
            currentState.copy(showBottomSheet = false)
        }
    }

    /**
     * Displays a bottom sheet with specific context data for user interaction.
     * @param dataBottomSheet The configuration data for the modal to be displayed.
     */
    fun setBottomSheetState(dataBottomSheet: DataBottomSheet) {
        _bottomSheetState.update { currentState ->
            currentState.copy(showBottomSheet = true, dataBottomSheet = dataBottomSheet)
        }
    }

    /**
     * Fetches user profile data and synchronization metadata from Firestore.
     * Uses a guarded execution pattern: only fetches if [dataSearched] is false.
     * Transforms Firestore raw date strings into [LocalDateTime] for domain usage.
     * Updates [saveUIState] to reflect the operation result (Success/Error/Loading).
     */
    fun getDataUser() {
        if (!_dataSearched.value) {
            viewModelScope.launch {
                try {
                    val user = saveAuthenticationUseCase.getCurrentId()
                    saveFirestoreUseCase.getDataUser(user)
                        .onEach {task ->
                            when(task){
                                is AuthResponseFirestore.Success -> {
                                    val dateString = task.data?.date
                                    val date:LocalDateTime? = dateString?.let { LocalDateTime.parse(it) }

                                    val name = saveAuthenticationUseCase.getName()

                                    _dataSaveScreen.update { currentState ->
                                        currentState.copy(localDateTime = date, name = name)
                                    }

                                    _saveUIState.update { SaveUIState.Success }
                                    _dataSearched.update { true }
                                }

                                is AuthResponseFirestore.Error -> {
                                    _saveUIState.update { SaveUIState.Error }
                                    treatError(message = task.message)
                                }
                            }

                        }
                        .onStart {
                            _saveUIState.update { SaveUIState.Loading }
                        }
                        .launchIn(viewModelScope)
                } catch (e: Exception) {
                    _saveUIState.update { SaveUIState.Error }
                    treatError(R.string.save_error_generic)
                }
            }
        }
    }

    /**
     * Routes the confirmation action from the UI bottom sheet to the specific
     * business logic method based on the current sheet context.
     */
    fun requestAcceptBottomSheet(){
        when(bottomSheetState.value.dataBottomSheet){
            DataBottomSheet.SAVE_HABIT -> { saveHabit() }
            DataBottomSheet.RESTORE_HABIT -> { restoreHabit() }
            DataBottomSheet.DELETE_HABIT -> { deleteHabit() }
            DataBottomSheet.LOG_OUT -> { logOut() }
            else -> { closeBottomSheet() }
        }
    }

    /**
     * Persists the current local habit state to the cloud (Firestore).
     * Orchestrates [saveHabitsRepositoryUseCase] to fetch local state,
     * [saveFirestoreUseCase] for cloud upload, and updates the local sync timestamp.
     */
    private fun saveHabit(){
        viewModelScope.launch (Dispatchers.IO){
            try {
                _saveUIState.update { SaveUIState.Loading }
                closeBottomSheet()

                val user = saveAuthenticationUseCase.getCurrentId()
                val habits = saveHabitsRepositoryUseCase.getAll()
                val firestoreData = FirestoreData(habit = habits)

                saveFirestoreUseCase.saveDataUser(firestoreData,user)
                    .onEach {task ->
                        when(task){
                            is AuthResponseFirestore.Success -> {
                                _saveUIState.update { SaveUIState.Success }
                                _dataSaveScreen.update { currentState ->
                                    currentState.copy(localDateTime = LocalDateTime.now())
                                }
                                setBottomSheetState(DataBottomSheet.SAVED_DATA)
                            }
                            is AuthResponseFirestore.Error -> {
                                _saveUIState.update { SaveUIState.Error }
                                treatError(message = task.message)
                            }
                        }
                    }
                    .onStart {_saveUIState.update { SaveUIState.Loading }}
                    .launchIn(viewModelScope)
            }catch (e:Exception){
                _saveUIState.update { SaveUIState.Error }
                treatError(R.string.save_error_generic)
            }
        }
    }

    /**
     * Restores the user's habit data from the cloud to the local database.
     * Orchestrates data retrieval, persistence into the local database,
     * and re-scheduling of local notifications.
     */
    private fun restoreHabit(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                closeBottomSheet()
                val user = saveAuthenticationUseCase.getCurrentId()

                saveFirestoreUseCase.getDataUser(user)
                    .onStart {_saveUIState.update { SaveUIState.Loading }}
                    .collect  {task ->
                        when(task){
                            is AuthResponseFirestore.Success -> {
                                val data = task.data?.habit ?: ""
                                val notifications = saveHabitsRepositoryUseCase.setData(data)
                                saveNotificationUseCase.setNotifications(notifications)

                                _saveUIState.update { SaveUIState.Success }
                                _dataSaveScreen.update { currentState ->
                                    currentState.copy(localDateTime = LocalDateTime.now())
                                }
                                setBottomSheetState(DataBottomSheet.RESTORED_DATA)
                            }
                            is AuthResponseFirestore.Error -> {
                                _saveUIState.update { SaveUIState.Error }
                                treatError(message = task.message)
                            }
                        }

                    }
            }catch (e:Exception){
                Log.e("Error",e.message.toString())
                _saveUIState.update { SaveUIState.Error }
                treatError(R.string.save_error_generic)
            }
        }
    }

    /**
     * Removes user habit data from Firestore and clears the local sync timestamp.
     * * Updates the UI state to reflect the successful deletion and triggers a
     * confirmation bottom sheet.
     */
    private fun deleteHabit(){
        viewModelScope.launch {
            try {
                closeBottomSheet()
                val user = saveAuthenticationUseCase.getCurrentId()

                saveFirestoreUseCase.deleteDataUser(user)
                    .onEach {task ->
                        when(task){
                            is AuthResponseFirestore.Success -> {
                                _saveUIState.update { SaveUIState.Success }
                                _dataSaveScreen.update { currentState ->
                                    currentState.copy(localDateTime = null)
                                }
                                setBottomSheetState(DataBottomSheet.DELETED_DATA)
                            }
                            is AuthResponseFirestore.Error -> {
                                _saveUIState.update { SaveUIState.Error }
                                treatError(message = task.message)
                            }
                        }
                    }
                    .onStart {_saveUIState.update { SaveUIState.Loading }}
                    .launchIn(viewModelScope)
            }catch (e:Exception){
                _saveUIState.update { SaveUIState.Error }
                treatError(R.string.save_error_generic)
            }
        }
    }

    /**
     * Executes the application logout flow.
     * * Transitions the UI state to [SaveUIState.LogOut], which allows the navigation
     * layer to trigger a redirection to the authentication module.
     */
    private fun logOut() {
        viewModelScope.launch {
            closeBottomSheet()
            _saveUIState.update { SaveUIState.LogOut }
            saveAuthenticationUseCase.logOut()
        }
    }

    /**
     * Centralized error management system.
     * * Dynamically configures the [DataBottomSheet.ERROR] template with a specific
     * string resource ID and surfaces it to the user.
     * * @param message The string resource ID to be displayed in the error modal.
     */
    private fun treatError(message:Int){
        val error = DataBottomSheet.ERROR
        error.label = message
        setBottomSheetState(error)
    }
}

/**
 * Represents the distinct states for cloud synchronization and session operations.
 * This sealed hierarchy allows the UI layer to reactively handle asynchronous
 * interactions with Firestore and authentication providers.
 */
sealed class SaveUIState{

    /** Indicates that a cloud operation (upload/download/delete) is in progress. */
    data object Loading:SaveUIState()

    /** Indicates that the requested cloud operation completed successfully. */
    data object Success:SaveUIState()

    /** Indicates that the cloud operation failed due to network or logic errors. */
    data object Error:SaveUIState()

    /** Indicates a successful logout operation, triggering a navigation state change. */
    data object LogOut:SaveUIState()
}