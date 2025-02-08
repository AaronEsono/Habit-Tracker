package aeb.proyecto.habittracker.ui.screens.importHabit

import aeb.proyecto.authentication.AuthResponseAuthentication
import aeb.proyecto.authentication.AuthenticationManager
import aeb.proyecto.datastore.DatastoreInterface
import aeb.proyecto.datastore.model.EmailPassword
import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.data.model.state.ImportState
import aeb.proyecto.habittracker.utils.Constans.DEFAULT_ERROR_FIREBASE
import aeb.proyecto.habittracker.utils.Constans.ERROR_UNVERIFIED_EMAIL
import aeb.proyecto.habittracker.utils.Constans.FIREBASE_ERRORS
import aeb.proyecto.habittracker.utils.SharedState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImportHabitViewModel @Inject constructor(
    private val sharedState: SharedState,
    private val datastoreInterface: DatastoreInterface,
    private val authenticationManager: AuthenticationManager
) : ViewModel() {


    private val _uiState:MutableStateFlow<ImportState> = MutableStateFlow(ImportState())
    val uiState: StateFlow<ImportState> = _uiState.asStateFlow()

    private val _emailPassword : MutableStateFlow<EmailPassword> = MutableStateFlow(EmailPassword())
    val emailPassword: StateFlow<EmailPassword> = _emailPassword.asStateFlow()

    init {
        viewModelScope.launch {
            _emailPassword.value = datastoreInterface.getEmailAndPassword()
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

    fun signInGoogle(navigate:() -> Unit){
        setLoading()

        authenticationManager.signInWithGoogle().onEach {
            response -> handleSignInGoogle(response,navigate)
        }.launchIn(viewModelScope)
    }

    fun signIn(email: String, password: String, saveCredentials: Boolean, navigate: () -> Unit) = viewModelScope.launch {
        setLoading()

        val response = authenticationManager.signInWithEmail(email, password)
        handleSignIn(response, email, password, saveCredentials, navigate)
    }

    fun signUp(email: String, password: String) = viewModelScope.launch {
        setLoading()

        val response = authenticationManager.createAccountWithEmail(email, password)
        handleSignUp(response)
    }

    fun resendEmail(showToast: () -> Unit) = viewModelScope.launch {
        setLoading()

        val response = authenticationManager.resendEmail()
        handleResendEmail(response){
            showToast()
        }
    }

    fun forgotPassword(email: String,showToast: () -> Unit) = viewModelScope.launch {
        setLoading()

        val response = authenticationManager.forgotPassword(email)
        handleForgotPassword(response){
            showToast()
        }
    }

    private fun handleSignInGoogle(response: AuthResponseAuthentication, navigate:() -> Unit){
        if(response is AuthResponseAuthentication.Success){
            setNeutral()
            navigate()
        }else{
            setNeutral()
        }
    }

    private fun handleSignUp(response: AuthResponseAuthentication){
        if(response is AuthResponseAuthentication.Success){
            setNeutral()
            openGeneralDxCreateAccount()
        }else{
            handleError(response as AuthResponseAuthentication.Error)
        }
    }

    private fun handleSignIn(response: AuthResponseAuthentication, email:String, password:String, saveCredentials:Boolean, navigate:() -> Unit){
        if(response is AuthResponseAuthentication.Success){
            setNeutral()

            if(saveCredentials){
                viewModelScope.launch {
                    datastoreInterface.setEmail(email)
                    datastoreInterface.setPassword(password)
                }
            }else{
                viewModelScope.launch {
                    datastoreInterface.clearUser()
                }
            }

            navigate()
        }else{
            handleError(response as AuthResponseAuthentication.Error)
        }
    }

    private fun handleError(response: AuthResponseAuthentication.Error){
        val message = response.message

        val errorInt = FIREBASE_ERRORS.find { it.id == message } ?: DEFAULT_ERROR_FIREBASE

        if(errorInt.id == ERROR_UNVERIFIED_EMAIL ){
            setNeutral()
            openSendEmail()
        }else{
            setError(errorInt.text)
        }
    }

    private fun handleResendEmail(response: AuthResponseAuthentication, showToast: () -> Unit) {
        if (response is AuthResponseAuthentication.Success) {
            setNeutral()
            showToast()
        } else {
            handleError(response as AuthResponseAuthentication.Error)
        }
    }


    private fun handleForgotPassword(response: AuthResponseAuthentication, showToast: () -> Unit) {
        if (response is AuthResponseAuthentication.Success) {
            setNeutral()
            showToast()
        } else {
            handleError(response as AuthResponseAuthentication.Error)
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