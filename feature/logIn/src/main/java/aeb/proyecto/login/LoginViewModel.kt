package aeb.proyecto.login

import aeb.proyecto.authentication.AuthResponseAuthentication
import aeb.proyecto.authentication.AuthenticationInterface
import aeb.proyecto.datastore.DatastoreInterface
import aeb.proyecto.login.model.BottomSheetState
import aeb.proyecto.login.model.DataLoginBottomSheet
import aeb.proyecto.login.model.DataLoginScreen
import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val datastoreInterface: DatastoreInterface,
    private val authenticationInterface: AuthenticationInterface,
):ViewModel() {

    private val _uiState: MutableStateFlow<LoginUIState> = MutableStateFlow(LoginUIState.Success)
    val uiState = _uiState.asStateFlow()

    private val _dataLoginScreen = MutableStateFlow(DataLoginScreen())
    val dataLoginScreen = _dataLoginScreen.asStateFlow()

    private val _dataBottomSheet = MutableStateFlow(BottomSheetState())
    val dataBottomSheet = _dataBottomSheet.asStateFlow()

    private val _dataSearched: MutableStateFlow<Boolean> = MutableStateFlow(false)

    fun setChecked(){
        _dataLoginScreen.update { currentState ->
            currentState.copy(isChecked = !currentState.isChecked)
        }
    }

    fun setLoginMode() {
        _dataLoginScreen.update { currentState ->
            currentState.copy(
                isInLoginMode = !currentState.isInLoginMode,
                emailTextFieldState = TextFieldState(),
                passwordTextFieldState = TextFieldState(),
                rememberTextFieldState = TextFieldState()
            )
        }
    }

    fun handleAcceptButton(){
        if(_dataLoginScreen.value.isInLoginMode){
            signIn()
        }else{
            register()
        }
    }

    private fun signIn() = viewModelScope.launch{
        _uiState.update { LoginUIState.Loading }
        try {
            val email = _dataLoginScreen.value.emailTextFieldState.text.toString()
            val password = _dataLoginScreen.value.passwordTextFieldState.text.toString()

            when(val response = authenticationInterface.signInWithEmail(email,password)){
                is AuthResponseAuthentication.Success -> {
                    saveData(email,password)
                    _uiState.update { LoginUIState.Login }
                }

                is AuthResponseAuthentication.UnverifiedEmail -> {
                    setDataBottomSheet(DataLoginBottomSheet.UNVERIFIED_EMAIL)
                    _uiState.update { LoginUIState.Error }
                }

                is AuthResponseAuthentication.Error -> {
                    setError(response.message)
                }
            }

        }catch (e:Exception){
            setError(R.string.login_error_default)
        }
    }

    private fun register() = viewModelScope.launch{
        _uiState.update { LoginUIState.Loading }

        try {
            val email = _dataLoginScreen.value.emailTextFieldState.text.toString()
            val password = _dataLoginScreen.value.passwordTextFieldState.text.toString()

            when(val response = authenticationInterface.createAccountWithEmail(email,password)){
                is AuthResponseAuthentication.Success -> {
                    setDataBottomSheet(DataLoginBottomSheet.ACCOUNT_CREATED)
                    _uiState.update { LoginUIState.Success }
                }
                is AuthResponseAuthentication.Error -> {
                    setError(response.message)
                }
                else -> {
                    setError(R.string.login_error_default)
                }
            }

        }catch (e:Exception){
            setError(R.string.login_error_default)
        }

    }

    fun signInGoogle() {
        _uiState.update { LoginUIState.Loading }

        try {
            authenticationInterface.signInWithGoogle().onEach { response ->
                when (response) {
                    is AuthResponseAuthentication.Success -> {
                        _uiState.update { LoginUIState.Login }
                    }

                    is AuthResponseAuthentication.Error -> {
                        setError(response.message)
                    }
                    else -> {
                        setError(R.string.login_error_default)
                    }
                }
            }.launchIn(viewModelScope)
        }catch (e:Exception){
            setError(R.string.login_error_default)
        }
    }

    fun requestAcceptBottomSheet() {
        closeBottomSheet()
        when (_dataBottomSheet.value.dataBottomSheet) {
            DataLoginBottomSheet.ERROR, DataLoginBottomSheet.EMAIL_SENT, DataLoginBottomSheet.EMAIL_SENT_FORGOT_PASSWORD -> Unit
            DataLoginBottomSheet.UNVERIFIED_EMAIL -> {
                resendEmail()
            }

            DataLoginBottomSheet.ACCOUNT_CREATED -> {
                setLoginMode()
            }

            DataLoginBottomSheet.FORGOT_PASSWORD -> {
                forgotPassword()
            }
        }
    }

    fun getSaveCredentials() = viewModelScope.launch{
        if(!_dataSearched.value){
            _uiState.update { LoginUIState.Loading }

            val credentials = datastoreInterface.getEmailAndPassword()
            _dataLoginScreen.update { currentState ->
                currentState.copy(
                    emailTextFieldState = TextFieldState(credentials.email),
                    passwordTextFieldState = TextFieldState(credentials.password),
                    isChecked = credentials.email.isNotEmpty() && credentials.password.isNotEmpty()
                )
            }

            _dataSearched.update { true }
            _uiState.update { LoginUIState.Success }
        }
    }

    private fun forgotPassword() = viewModelScope.launch {
        _uiState.update { LoginUIState.Loading }

        try {
            val email = _dataBottomSheet.value.emailSentForgotPassword.text.toString()

            when(val response = authenticationInterface.forgotPassword(email)){
                is AuthResponseAuthentication.Success -> {
                    setDataBottomSheet(DataLoginBottomSheet.EMAIL_SENT_FORGOT_PASSWORD)
                    _uiState.update { LoginUIState.Success }
                }
                is AuthResponseAuthentication.Error -> {
                    setError(response.message)
                }
            }

        }catch (e:Exception){
            setError(R.string.login_error_default)
        }

    }

    private fun resendEmail() = viewModelScope.launch{
        _uiState.update { LoginUIState.Loading }

        try{
            val email = _dataLoginScreen.value.emailTextFieldState.text.toString()
            val password = _dataLoginScreen.value.passwordTextFieldState.text.toString()

            when(val response = authenticationInterface.resendEmail(email,password)){
                is AuthResponseAuthentication.Success -> {
                    setDataBottomSheet(DataLoginBottomSheet.EMAIL_SENT)
                    _uiState.update { LoginUIState.Success }
                }
                is AuthResponseAuthentication.Error -> {
                    setError(response.message)
                }
            }

        }catch (e:Exception){
            setError(R.string.login_error_default)
        }
    }

    private suspend fun saveData(email:String, password:String){
        if(_dataLoginScreen.value.isChecked){
            datastoreInterface.setEmail(email)
            datastoreInterface.setPassword(password)
        }else{
            datastoreInterface.clearUser()
        }
    }

    private fun setError(errorInt:Int){
        val error = DataLoginBottomSheet.ERROR
        error.subtitle = errorInt

        _dataBottomSheet.update { currentState ->
            currentState.copy(
                dataBottomSheet = error,
                showBottomSheet = true
            )
        }
        _uiState.update { LoginUIState.Error }
    }

    fun closeBottomSheet(){
        _dataBottomSheet.update { currentState ->
            currentState.copy(
                showBottomSheet = false
            )
        }
    }

    fun setDataBottomSheet(dataLoginBottomSheet: DataLoginBottomSheet){
        _dataBottomSheet.update { currentState ->
            currentState.copy(
                dataBottomSheet = dataLoginBottomSheet,
                showBottomSheet = true
            )
        }
    }

}

sealed class LoginUIState{
    data object Success: LoginUIState()
    data object Error: LoginUIState()
    data object Loading:LoginUIState()
    data object Login: LoginUIState()
}