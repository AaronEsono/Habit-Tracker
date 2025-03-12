package aeb.proyecto.save

import aeb.proyecto.alarmmanager.NotificationUtils
import aeb.proyecto.authentication.AuthenticationInterface
import aeb.proyecto.firestore.AuthResponseFirestore
import aeb.proyecto.firestore.FirestoreInterface
import aeb.proyecto.firestore.model.FirestoreData
import aeb.proyecto.room.repository.EntireHabitRepo
import aeb.proyecto.save.model.BottomSheetState
import aeb.proyecto.save.model.DataBottomSheet
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class SaveViewModel @Inject constructor(
    private val authenticationInterface: AuthenticationInterface,
    private val firestoreInterface: FirestoreInterface,
    private val entireHabitRepo: EntireHabitRepo,
    private val notificationUtils: NotificationUtils
):ViewModel() {

    private val _bottomSheetState: MutableStateFlow<BottomSheetState> =
        MutableStateFlow(BottomSheetState())
    val bottomSheetState: StateFlow<BottomSheetState> = _bottomSheetState.asStateFlow()

    private val _saveUIState: MutableStateFlow<SaveUIState> = MutableStateFlow(SaveUIState.Loading)
    val saveUIState: StateFlow<SaveUIState> = _saveUIState.asStateFlow()

    private val _localDateTime: MutableStateFlow<LocalDateTime?> = MutableStateFlow(null)
    val localDateTime: StateFlow<LocalDateTime?> = _localDateTime.asStateFlow()

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
                    _saveUIState.update { SaveUIState.Loading }

                    val user = authenticationInterface.getCurrentId()
                    val response = firestoreInterface.getDataUser(user)

                    when (response) {
                        is AuthResponseFirestore.Success -> {
                            val dateString = response.data?.date
                            val date:LocalDateTime? = dateString?.let { LocalDateTime.parse(it) }

                            _localDateTime.update { date }

                            _saveUIState.update { SaveUIState.Success }
                            _dataSearched.update { true }
                        }

                        is AuthResponseFirestore.Error -> {
                            _saveUIState.update { SaveUIState.Error }
                            treatError(message = response.message)
                        }
                    }
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

                val user = authenticationInterface.getCurrentId()
                val habits = entireHabitRepo.getAll()
                val firestoreData = FirestoreData(habit = habits)

                val response = firestoreInterface.saveDataUser(firestoreData,user)

                when(response){
                    is AuthResponseFirestore.Success -> {
                        _saveUIState.update { SaveUIState.Success }
                        _localDateTime.update { LocalDateTime.now() }
                        setBottomSheetState(DataBottomSheet.SAVED_DATA)
                    }

                    is AuthResponseFirestore.Error -> {
                        _saveUIState.update { SaveUIState.Error }
                        treatError(message = response.message)
                    }
                }

            }catch (e:Exception){
                _saveUIState.update { SaveUIState.Error }
                treatError(R.string.save_error_generic)
            }
        }
    }

    private fun restoreHabit(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _saveUIState.update { SaveUIState.Loading }
                closeBottomSheet()

                val user = authenticationInterface.getCurrentId()
                val response = firestoreInterface.getDataUser(user)

                when(response){
                    is AuthResponseFirestore.Success -> {
                        val data = response.data?.habit ?: ""
                        val notifications = entireHabitRepo.setData(data)

                        notifications.forEach { notification ->
                            notificationUtils.setUpAlarm(notification)
                        }

                        _saveUIState.update { SaveUIState.Success }
                        _localDateTime.update { LocalDateTime.now() }
                        setBottomSheetState(DataBottomSheet.RESTORED_DATA)
                    }

                    is AuthResponseFirestore.Error -> {
                        _saveUIState.update { SaveUIState.Error }
                        treatError(message = response.message)
                    }
                }
            }catch (e:Exception){
                _saveUIState.update { SaveUIState.Error }
                treatError(R.string.save_error_generic)
            }
        }
    }

    private fun deleteHabit(){
        viewModelScope.launch {
            try {
                _saveUIState.update { SaveUIState.Loading }
                closeBottomSheet()

                val user = authenticationInterface.getCurrentId()
                val response = firestoreInterface.deleteDataUser(user)

                when(response){
                    is AuthResponseFirestore.Success -> {
                        _saveUIState.update { SaveUIState.Success }
                        _localDateTime.update { null }
                        setBottomSheetState(DataBottomSheet.DELETED_DATA)
                    }

                    is AuthResponseFirestore.Error -> {
                        _saveUIState.update { SaveUIState.Error }
                        treatError(message = response.message)
                    }
                }
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
            authenticationInterface.logOut()
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