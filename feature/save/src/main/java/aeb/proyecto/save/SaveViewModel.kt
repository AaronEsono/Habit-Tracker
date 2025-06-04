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

@HiltViewModel
class SaveViewModel @Inject constructor(
    private val saveHabitsRepositoryUseCase: SaveHabitsRepositoryUseCase,
    private val saveNotificationUseCase: SaveNotificationUseCase,
    private val saveFirestoreUseCase: SaveFirestoreUseCase,
    private val saveAuthenticationUseCase: SaveAuthenticationUseCase
):ViewModel() {

    private val _bottomSheetState: MutableStateFlow<BottomSheetState> =
        MutableStateFlow(BottomSheetState())
    val bottomSheetState: StateFlow<BottomSheetState> = _bottomSheetState.asStateFlow()

    private val _saveUIState: MutableStateFlow<SaveUIState> = MutableStateFlow(SaveUIState.Loading)
    val saveUIState: StateFlow<SaveUIState> = _saveUIState.asStateFlow()

    private val _dataSaveScreen: MutableStateFlow<DataSaveScreen> = MutableStateFlow(DataSaveScreen())
    val dataSaveScreen: StateFlow<DataSaveScreen> = _dataSaveScreen.asStateFlow()

    private val _dataSearched: MutableStateFlow<Boolean> = MutableStateFlow(false)

    fun closeBottomSheet() {
        _bottomSheetState.update { currentState ->
            currentState.copy(showBottomSheet = false)
        }
    }

    fun setBottomSheetState(dataBottomSheet: DataBottomSheet) {
        _bottomSheetState.update { currentState ->
            currentState.copy(showBottomSheet = true, dataBottomSheet = dataBottomSheet)
        }
    }

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

    fun requestAcceptBottomSheet(){
        when(bottomSheetState.value.dataBottomSheet){
            DataBottomSheet.SAVE_HABIT -> { saveHabit() }
            DataBottomSheet.RESTORE_HABIT -> { restoreHabit() }
            DataBottomSheet.DELETE_HABIT -> { deleteHabit() }
            DataBottomSheet.LOG_OUT -> { logOut() }
            else -> { closeBottomSheet() }
        }
    }

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

    private fun logOut() {
        viewModelScope.launch {
            closeBottomSheet()
            _saveUIState.update { SaveUIState.LogOut }
            saveAuthenticationUseCase.logOut()
        }
    }

    private fun treatError(message:Int){
        val error = DataBottomSheet.ERROR
        error.label = message
        setBottomSheetState(error)
    }
}

sealed class SaveUIState{
    data object Loading:SaveUIState()
    data object Success:SaveUIState()
    data object Error:SaveUIState()
    data object LogOut:SaveUIState()
}