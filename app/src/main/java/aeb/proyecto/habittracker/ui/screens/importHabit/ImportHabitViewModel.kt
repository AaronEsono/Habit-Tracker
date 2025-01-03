package aeb.proyecto.habittracker.ui.screens.importHabit

import aeb.proyecto.habittracker.MainViewModel
import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.data.model.state.ImportState
import aeb.proyecto.habittracker.utils.SharedState
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImportHabitViewModel @Inject constructor(
    private val sharedState: SharedState
) : ViewModel() {


    private val _uiState:MutableStateFlow<ImportState> = MutableStateFlow(ImportState())
    val uiState: StateFlow<ImportState> = _uiState.asStateFlow()


    fun setRegister(){
        _uiState.update { currentState ->
            currentState.copy(
                isInLogin = false,
                title = R.string.import_habit_screen_register,
                subtitle = R.string.import_habit_screen_subtitle_register,
                textSignIn = R.string.import_habit_screen_yes_account,
                textSignUp = R.string.import_habit_screen_sign_in
            )
        }
    }

    fun setLogin(){
        _uiState.update { currentState ->
            currentState.copy(
                isInLogin = true,
                title = R.string.import_habit_screen_login,
                subtitle = R.string.import_habit_screen_subtitle_login,
                textSignIn = R.string.import_habit_screen_no_account,
                textSignUp = R.string.import_habit_screen_sign_up
            )
        }
    }

    fun setLoading(){
        sharedState.setLoading()
    }

    fun setNeutral(){
        sharedState.setNeutral()
    }

}