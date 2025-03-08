package aeb.proyecto.save

import aeb.proyecto.authentication.AuthenticationInterface
import aeb.proyecto.save.model.BottomSheetState
import aeb.proyecto.save.model.DataBottomSheet
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SaveViewModel @Inject constructor(
    private val authenticationInterface: AuthenticationInterface
):ViewModel() {

    private val _bottomSheetState: MutableStateFlow<BottomSheetState> =
        MutableStateFlow(BottomSheetState())

    val bottomSheetState: StateFlow<BottomSheetState> = _bottomSheetState.asStateFlow()

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

    fun requestAcceptBottomSheet(){
        when(bottomSheetState.value.dataBottomSheet){
            DataBottomSheet.SAVE_HABIT -> { saveHabit() }
            DataBottomSheet.DELETE_HABIT -> { deleteHabit() }
            DataBottomSheet.LOG_OUT -> { logOut() }
        }
    }

    private fun saveHabit(){

    }

    private fun deleteHabit(){

    }

    private fun logOut(){

    }

}

sealed class SaveUIState{
    data object Loading:SaveUIState()
    data object Error:SaveUIState()
    data object Success:SaveUIState()
}