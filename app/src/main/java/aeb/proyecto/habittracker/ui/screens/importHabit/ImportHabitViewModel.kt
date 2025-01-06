package aeb.proyecto.habittracker.ui.screens.importHabit

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.data.model.state.ImportState
import aeb.proyecto.habittracker.di.DataStoreManager
import aeb.proyecto.habittracker.di.EmailPassword
import aeb.proyecto.habittracker.utils.AuthResponse
import aeb.proyecto.habittracker.utils.Constans.DEFAULT_ERROR_FIREBASE
import aeb.proyecto.habittracker.utils.Constans.ERROR_UNVERIFIED_EMAIL
import aeb.proyecto.habittracker.utils.Constans.FIREBASE_ERRORS
import aeb.proyecto.habittracker.utils.SharedState
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImportHabitViewModel @Inject constructor(
    private val sharedState: SharedState,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {


    private val _uiState:MutableStateFlow<ImportState> = MutableStateFlow(ImportState())
    val uiState: StateFlow<ImportState> = _uiState.asStateFlow()

    private val _emailPassword : MutableStateFlow<EmailPassword?> = MutableStateFlow(null)
    val emailPassword: StateFlow<EmailPassword?> = _emailPassword.asStateFlow()

    init {
        viewModelScope.launch {
            _emailPassword.value = dataStoreManager.emailPassword.first()
        }
    }

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

    fun setError(message:Int){
        sharedState.setError(message)
    }

    fun handleSignInGoogle(response: AuthResponse,navigate:() -> Unit){
        if(response is AuthResponse.Success){
            setNeutral()
            navigate()
        }else{
            setNeutral()
        }
    }

    fun handleSignUp(response: AuthResponse){
        if(response is AuthResponse.Success){
            setNeutral()
            openGeneralDxCreateAccount()
        }else{
            handleError(response as AuthResponse.Error)
        }
    }

    fun handleSignIn(response: AuthResponse,email:String, password:String,saveCredentials:Boolean, navigate:() -> Unit){
        if(response is AuthResponse.Success){
            setNeutral()

            if(saveCredentials){
                viewModelScope.launch {
                    dataStoreManager.setEmail(email)
                    dataStoreManager.setPassword(password)
                }
            }else{
                viewModelScope.launch {
                    dataStoreManager.clearDataUser()
                }
            }

            navigate()
        }else{
            handleError(response as AuthResponse.Error)
        }
    }

    private fun handleError(response: AuthResponse.Error){
        val message = response.message

        val errorInt = FIREBASE_ERRORS.find { it.id == message } ?: DEFAULT_ERROR_FIREBASE

        if(errorInt.id == ERROR_UNVERIFIED_EMAIL ){
            setNeutral()
            openSendEmail()
        }else{
            setError(errorInt.text)
        }
    }

    fun handleResendEmail(response: AuthResponse, showToast: () -> Unit) {
        if (response is AuthResponse.Success) {
            setNeutral()
            showToast()
        } else {
            handleError(response as AuthResponse.Error)
        }
    }

    fun handleForgotPassword(response: AuthResponse,showToast: () -> Unit) {
        if (response is AuthResponse.Success) {
            setNeutral()
            showToast()
        } else {
            handleError(response as AuthResponse.Error)
        }
    }

    fun openGeneralDxCreateAccount() {
        _uiState.update { currentState ->
            currentState.copy(
                showGeneralDx = true,
                subtitleDx = R.string.import_habit_create_account,
                titleButton =  R.string.buttons_accept,
                titleDx = R.string.general_dx_create_account
            )
        }
    }

    fun openSendEmail() {
        _uiState.update { currentState ->
            currentState.copy(
                subtitleDx = R.string.error_unverified_email,
                showGeneralDx = true,
                titleButton = R.string.import_habit_resend_email,
                titleDx = R.string.import_habit_verify_email
            )
        }
    }

    fun closeGeneralDx(){
        _uiState.update { currentState ->
            currentState.copy(showGeneralDx = false)
        }
    }

    fun openPasswordDx(){
        _uiState.update { currentState ->
            currentState.copy(dxPassword = true)
        }
    }

    fun closePasswordDx(){
        _uiState.update { currentState ->
            currentState.copy(dxPassword = false)
        }
    }

}