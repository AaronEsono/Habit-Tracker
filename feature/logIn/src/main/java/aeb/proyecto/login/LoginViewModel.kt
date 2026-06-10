package aeb.proyecto.login

import aeb.proyecto.authentication.AuthResponseAuthentication
import aeb.proyecto.authentication.AuthenticationInterface
import aeb.proyecto.datastore.DatastoreInterface
import aeb.proyecto.datastore.model.UserSession
import aeb.proyecto.domain.usecase.login.LoginAuthenticationUseCase
import aeb.proyecto.domain.usecase.login.SaveLoginCredentialUseCase
import aeb.proyecto.login.model.BottomSheetState
import aeb.proyecto.login.model.DataLoginBottomSheet
import aeb.proyecto.login.model.DataLoginScreen
import android.content.Context
import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing the authentication flow, including user login,
 * credential persistence, and UI state management.
 *
 * @param loginAuthenticationUseCase UseCase to handle the authentication logic (e.g., Firebase Auth).
 * @param saveLoginCredentialUseCase UseCase to persist user credentials locally.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginAuthenticationUseCase: LoginAuthenticationUseCase,
    private val saveLoginCredentialUseCase: SaveLoginCredentialUseCase
):ViewModel() {

    /** Current authentication flow state. */
    private val _uiState: MutableStateFlow<LoginUIState> = MutableStateFlow(LoginUIState.Success)
    val uiState = _uiState.asStateFlow()

    /** Data holding the fields for the login screen (email, password, etc.). */
    private val _dataLoginScreen = MutableStateFlow(DataLoginScreen())
    val dataLoginScreen = _dataLoginScreen.asStateFlow()

    /** State for managing modal bottom sheets related to the login flow. */
    private val _dataBottomSheet = MutableStateFlow(BottomSheetState())
    val dataBottomSheet = _dataBottomSheet.asStateFlow()

    /** Internal flag to track if a search or data validation operation has been performed. */
    private val _dataSearched: MutableStateFlow<Boolean> = MutableStateFlow(false)

    /**
     * Toggles the "remember me" or terms of service checkbox state.
     */
    fun setChecked(){
        _dataLoginScreen.update { currentState ->
            currentState.copy(isChecked = !currentState.isChecked)
        }
    }

    /**
     * Switches between Login and Registration modes.
     * Resets all text field states to ensure a clean slate when changing modes.
     */
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

    /**
     * Determines whether to trigger [signIn] or [register]
     * based on the current UI mode.
     */
    fun handleAcceptButton(){
        if(_dataLoginScreen.value.isInLoginMode){
            signIn()
        }else{
            register()
        }
    }

    /**
     * Performs the sign-in authentication process.
     * Updates [uiState] based on the authentication result.
     */
    private fun signIn() = viewModelScope.launch{
        try {
            val email = _dataLoginScreen.value.emailTextFieldState.text.toString()
            val password = _dataLoginScreen.value.passwordTextFieldState.text.toString()

            loginAuthenticationUseCase.signIn(email,password)
                .onEach { task ->
                    when(task){
                        is AuthResponseAuthentication.Success -> {
                            saveData(email,password)
                            _uiState.update { LoginUIState.Login }
                        }
                        is AuthResponseAuthentication.UnverifiedEmail -> {
                            setDataBottomSheet(DataLoginBottomSheet.UNVERIFIED_EMAIL)
                            _uiState.update { LoginUIState.Error }
                        }
                        is AuthResponseAuthentication.Error -> {
                            setError(task.message)
                        }
                    }
                }
                .onStart {
                    _uiState.update { LoginUIState.Loading }
                }
                .launchIn(viewModelScope)
        }catch (e:Exception){
            setError(R.string.login_error_default)
        }
    }

    /**
     * Executes the account registration process using the provided email and password.
     * On success, it triggers a bottom sheet notification and updates the state.
     */
    private fun register() = viewModelScope.launch{
        try {
            val email = _dataLoginScreen.value.emailTextFieldState.text.toString()
            val password = _dataLoginScreen.value.passwordTextFieldState.text.toString()

            loginAuthenticationUseCase.createAccount(email,password)
                .onEach { task ->
                    when(task){
                        AuthResponseAuthentication.Success -> {
                            setDataBottomSheet(DataLoginBottomSheet.ACCOUNT_CREATED)
                            _uiState.update { LoginUIState.Success }
                        }
                        is AuthResponseAuthentication.Error -> {
                            setError(task.message)
                        }
                    }
                }
                .onStart {
                    _uiState.update { LoginUIState.Loading }
                }.launchIn(viewModelScope)

        }catch (e:Exception){
            setError(R.string.login_error_default)
        }
    }

    /**
     * Initiates Google Sign-In authentication.
     * * @param context Required to launch the Google Sign-In intent.
     */
    fun signInGoogle(context: Context) {
        try {
            loginAuthenticationUseCase.signInWithGoogle(context).onEach { response ->
                when (response) {
                    is AuthResponseAuthentication.Success -> {
                        _uiState.update { LoginUIState.Login }
                    }

                    is AuthResponseAuthentication.Error -> {
                        setError(response.message)
                    }
                    is AuthResponseAuthentication.Loading -> {
                        _uiState.update { LoginUIState.Loading }
                    }
                    is AuthResponseAuthentication.UnverifiedEmail -> {
                        // Tratar si fuera necesario en un futuro
                    }
                }
            }
                .onStart {
                    _uiState.update { LoginUIState.Loading }
                }
                .launchIn(viewModelScope)
        }catch (e:Exception){
            setError(R.string.login_error_default)
        }
    }

    /**
     * Handles the primary action button inside a modal bottom sheet.
     * Orchestrates different workflows (resending email, switching modes, etc.)
     * based on the current context of the [DataLoginBottomSheet].
     */
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

    /**
     * Attempts to retrieve saved user credentials (e.g., from DataStore or EncryptedSharedPreferences).
     * This operation runs only once per ViewModel lifecycle to prevent redundant data fetching.
     */
    fun getSaveCredentials() = viewModelScope.launch{
        if(!_dataSearched.value){
            _uiState.update { LoginUIState.Loading }

            val credentials = saveLoginCredentialUseCase.getCredentials()
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

    /**
     * Initiates the password recovery flow for the specified email.
     * Updates the UI state to show a confirmation bottom sheet upon success.
     */
    private fun forgotPassword() = viewModelScope.launch {
        try {
            val email = _dataBottomSheet.value.emailSentForgotPassword.text.toString()

            loginAuthenticationUseCase.forgotPassword(email)
                .onEach {task ->
                    when(task){
                        is AuthResponseAuthentication.Success -> {
                            setDataBottomSheet(DataLoginBottomSheet.EMAIL_SENT_FORGOT_PASSWORD)
                            _uiState.update { LoginUIState.Success }
                        }
                        is AuthResponseAuthentication.Error -> {
                            setError(task.message)
                        }
                    }
                }
                .onStart { _uiState.update { LoginUIState.Loading } }
                .launchIn(viewModelScope)
        }catch (e:Exception){
            setError(R.string.login_error_default)
        }

    }

    /**
     * Triggers a resend of the email verification link to the user.
     * Uses current credentials to re-authenticate or verify the session context.
     */
    private fun resendEmail() = viewModelScope.launch{
        try{
            val email = _dataLoginScreen.value.emailTextFieldState.text.toString()
            val password = _dataLoginScreen.value.passwordTextFieldState.text.toString()

            loginAuthenticationUseCase.resendEmail(email,password)
                .onEach { task ->
                    when(task){
                        is AuthResponseAuthentication.Success -> {
                            setDataBottomSheet(DataLoginBottomSheet.EMAIL_SENT)
                            _uiState.update { LoginUIState.Success }
                        }
                        is AuthResponseAuthentication.Error -> {
                            setError(task.message)
                        }
                    }
                }
                .onStart { _uiState.update { LoginUIState.Loading } }
                .launchIn(viewModelScope)
        }catch (e:Exception){
            setError(R.string.login_error_default)
        }
    }

    /**
     * Persists or clears user credentials based on the "Remember Me" checkbox status.
     * * @param email The user's email address.
     * @param password The user's password.
     */
    private suspend fun saveData(email: String, password: String) {
        if (_dataLoginScreen.value.isChecked) {
            saveLoginCredentialUseCase.saveUserSession(
                UserSession(
                    email = email,
                    password = password
                )
            )
        } else {
            saveLoginCredentialUseCase.clearData()
        }
    }

    /**
     * Updates the UI state to show an error bottom sheet with a specific message.
     * * @param errorInt Resource ID of the error message string.
     */
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

    /**
     * Hides the currently visible modal bottom sheet.
     */
    fun closeBottomSheet(){
        _dataBottomSheet.update { currentState ->
            currentState.copy(
                showBottomSheet = false
            )
        }
    }

    /**
     * Sets the data for the bottom sheet and makes it visible.
     * * @param dataLoginBottomSheet The type of bottom sheet to display.
     */
    fun setDataBottomSheet(dataLoginBottomSheet: DataLoginBottomSheet){
        _dataBottomSheet.update { currentState ->
            currentState.copy(
                dataBottomSheet = dataLoginBottomSheet,
                showBottomSheet = true
            )
        }
    }

}

/**
 * Represents the various states of the Login/Authentication flow.
 *
 * This sealed class is used by the ViewModel to communicate the current UI
 * status to the [LoginScreen], enabling reactive updates based on
 * authentication progress and results.
 */
sealed class LoginUIState{

    /** Indicates that the authentication process was completed successfully. */
    data object Success: LoginUIState()

    /** Indicates that an error occurred during the authentication process. */
    data object Error: LoginUIState()

    /** Indicates that an authentication request is currently in progress. */
    data object Loading:LoginUIState()

    /** The default state, indicating that the user is currently on the login screen. */
    data object Login: LoginUIState()
}