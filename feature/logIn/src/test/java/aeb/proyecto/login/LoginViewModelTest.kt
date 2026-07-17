package aeb.proyecto.login

import aeb.proyecto.authentication.AuthResponseAuthentication
import aeb.proyecto.datastore.model.UserSession
import aeb.proyecto.domain.usecase.login.LoginAuthenticationUseCase
import aeb.proyecto.domain.usecase.login.SaveLoginCredentialUseCase
import aeb.proyecto.login.model.BottomSheetState
import aeb.proyecto.login.model.DataLoginBottomSheet
import com.ibm.icu.impl.CharacterPropertiesImpl.clear
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest {

    @get:Rule
    val mainDispatchersRule = MainDispatchersRule()

    private val mockLoginAuthenticationUseCase = mockk<LoginAuthenticationUseCase>(relaxed = true)
    private val mockSaveLoginCredentialUseCase = mockk<SaveLoginCredentialUseCase>(relaxed = true)

    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        viewModel = LoginViewModel(
            loginAuthenticationUseCase = mockLoginAuthenticationUseCase,
            saveLoginCredentialUseCase = mockSaveLoginCredentialUseCase
        )
    }

    @Test
    fun `given viewModel, when initialized, then verify default states`() {
        assertEquals(LoginUIState.Success, viewModel.uiState.value)

        val currentDataScreen = viewModel.dataLoginScreen.value

        assertFalse(currentDataScreen.isChecked)
        assertTrue(currentDataScreen.isInLoginMode)
        assertEquals("", currentDataScreen.emailTextFieldState.text.toString())
        assertEquals("", currentDataScreen.passwordTextFieldState.text.toString())

        assertEquals(BottomSheetState().dataBottomSheet, viewModel.dataBottomSheet.value.dataBottomSheet)
    }

    @Test
    fun `given current state, when setChecked is called, then toggle isChecked flag`() {
        // --- GIVEN ---
        assertFalse(viewModel.dataLoginScreen.value.isChecked)

        // --- WHEN ---
        viewModel.setChecked()

        // --- THEN ---
        assertTrue(viewModel.dataLoginScreen.value.isChecked)

        viewModel.setChecked()
        assertFalse(viewModel.dataLoginScreen.value.isChecked)
    }

    @Test
    fun `given any mode, when setLoginMode is called, then toggle mode and clear all text fields`() {
        // --- GIVEN ---
        assertTrue(viewModel.dataLoginScreen.value.isInLoginMode)

        // --- WHEN ---
        viewModel.setLoginMode()

        // --- THEN ---
        val stateAfterToggle = viewModel.dataLoginScreen.value
        assertFalse(stateAfterToggle.isInLoginMode)
        assertEquals("", stateAfterToggle.emailTextFieldState.text.toString())
        assertEquals("", stateAfterToggle.passwordTextFieldState.text.toString())
        assertEquals("", stateAfterToggle.rememberTextFieldState.text.toString())
    }

    @Test
    fun `given valid credentials, when signIn completes successfully, then update UI state to Login and save data`() = runTest {
        // --- GIVEN ---
        val email = "test@example.com"
        val password = "password123"

        viewModel.dataLoginScreen.value.emailTextFieldState.edit {
            clear()
            append(email)
        }
        viewModel.dataLoginScreen.value.passwordTextFieldState.edit {
            clear()
            append(password)
        }

        assertTrue(viewModel.dataLoginScreen.value.isInLoginMode)

        val successResponse = AuthResponseAuthentication.Success
        coEvery {
            mockLoginAuthenticationUseCase.signIn(email, password)
        } returns flowOf(successResponse)


        // --- WHEN ---
        viewModel.handleAcceptButton()
        advanceUntilIdle()

        // --- THEN ---
        assertEquals(LoginUIState.Login, viewModel.uiState.value)

        coVerify(exactly = 1) { mockLoginAuthenticationUseCase.signIn(email, password) }
    }

    @Test
    fun `given valid credentials, when email is unverified, then show unverified email bottom sheet and set state to Error`() = runTest {
        // --- GIVEN ---
        val email = "unverified@example.com"
        val password = "password123"

        viewModel.dataLoginScreen.value.emailTextFieldState.edit {
            clear()
            append(email)
        }
        viewModel.dataLoginScreen.value.passwordTextFieldState.edit {
            clear()
            append(password)
        }

        val unverifiedResponse = AuthResponseAuthentication.UnverifiedEmail
        coEvery {
            mockLoginAuthenticationUseCase.signIn(email, password)
        } returns flowOf(unverifiedResponse)

        // --- WHEN ---
        viewModel.handleAcceptButton()
        advanceUntilIdle()

        // --- THEN ---
        assertEquals(LoginUIState.Error, viewModel.uiState.value)
        assertEquals(DataLoginBottomSheet.UNVERIFIED_EMAIL, viewModel.dataBottomSheet.value.dataBottomSheet)
    }

    @Test
    fun `given invalid credentials, when signIn returns error state, then invoke setError`() = runTest {
        // --- GIVEN ---
        val email = "wrong@example.com"
        val password = "wrong_password"

        viewModel.dataLoginScreen.value.emailTextFieldState.edit {
            clear()
            append(email)
        }
        viewModel.dataLoginScreen.value.passwordTextFieldState.edit {
            clear()
            append(password)
        }

        val errorResponse = AuthResponseAuthentication.Error(1)
        coEvery {
            mockLoginAuthenticationUseCase.signIn(email, password)
        } returns flowOf(errorResponse)

        // --- WHEN ---
        viewModel.handleAcceptButton()
        advanceUntilIdle()

        // --- THEN ---
        assertEquals(LoginUIState.Error, viewModel.uiState.value)
    }

    @Test
    fun `given signIn execution, when an unexpected exception occurs, then catch it and show generic error`() = runTest {
        // --- GIVEN ---
        val email = "crash@example.com"
        val password = "password"

        viewModel.dataLoginScreen.value.emailTextFieldState.edit {
            clear()
            append(email)
        }
        viewModel.dataLoginScreen.value.passwordTextFieldState.edit {
            clear()
            append(password)
        }

        coEvery {
            mockLoginAuthenticationUseCase.signIn(email, password)
        } throws RuntimeException("Network crash")

        // --- WHEN ---
        viewModel.handleAcceptButton()
        advanceUntilIdle()

        // --- THEN ---
        assertEquals(LoginUIState.Error, viewModel.uiState.value)
    }

    @Test
    fun `given valid credentials, when register completes successfully, then update UI state to Success and show ACCOUNT_CREATED bottom sheet`() = runTest {
        // --- GIVEN ---
        val email = "newuser@example.com"
        val password = "securePassword123"

        viewModel.setLoginMode()
        assertFalse(viewModel.dataLoginScreen.value.isInLoginMode)

        viewModel.dataLoginScreen.value.emailTextFieldState.edit { clear(); append(email) }
        viewModel.dataLoginScreen.value.passwordTextFieldState.edit { clear(); append(password) }

        val successResponse = AuthResponseAuthentication.Success
        coEvery {
            mockLoginAuthenticationUseCase.createAccount(email, password)
        } returns flowOf(successResponse)

        // --- WHEN ---
        viewModel.handleAcceptButton()
        advanceUntilIdle()

        // --- THEN ---
        assertEquals(LoginUIState.Success, viewModel.uiState.value)
        assertEquals(DataLoginBottomSheet.ACCOUNT_CREATED, viewModel.dataBottomSheet.value.dataBottomSheet)

        coVerify(exactly = 1) { mockLoginAuthenticationUseCase.createAccount(email, password) }
    }

    @Test
    fun `given register is called, when repository returns error, then invoke setError`() = runTest {
        // --- GIVEN ---
        val email = "existing@example.com"
        val password = "password123"

        viewModel.setLoginMode()
        viewModel.dataLoginScreen.value.emailTextFieldState.edit { clear(); append(email) }
        viewModel.dataLoginScreen.value.passwordTextFieldState.edit { clear(); append(password) }

        val errorResponse = AuthResponseAuthentication.Error(1)
        coEvery {
            mockLoginAuthenticationUseCase.createAccount(email, password)
        } returns flowOf(errorResponse)

        // --- WHEN ---
        viewModel.handleAcceptButton()
        advanceUntilIdle()

        // --- THEN ---
        assertEquals(LoginUIState.Error, viewModel.uiState.value)
    }

    @Test
    fun `given register execution, when an unexpected exception occurs, then catch it and fallback to default error`() = runTest {
        // --- GIVEN ---
        val email = "crash@example.com"
        val password = "password"

        viewModel.setLoginMode()
        viewModel.dataLoginScreen.value.emailTextFieldState.edit { clear(); append(email) }
        viewModel.dataLoginScreen.value.passwordTextFieldState.edit { clear(); append(password) }

        coEvery {
            mockLoginAuthenticationUseCase.createAccount(email, password)
        } throws RuntimeException("Unexpected fatal crash")

        // --- WHEN ---
        viewModel.handleAcceptButton()
        advanceUntilIdle()

        // --- THEN ---
        assertEquals(LoginUIState.Error, viewModel.uiState.value)
    }

    @Test
    fun `given UNVERIFIED_EMAIL bottom sheet, when accept is clicked, then invoke resendEmail`() = runTest {
        // --- GIVEN ---
        viewModel.setDataBottomSheet(DataLoginBottomSheet.UNVERIFIED_EMAIL)


        // --- WHEN ---
        viewModel.requestAcceptBottomSheet()
        advanceUntilIdle()

        // --- THEN ---
        assertFalse(viewModel.dataBottomSheet.value.showBottomSheet)
    }

    @Test
    fun `given ACCOUNT_CREATED bottom sheet, when accept is clicked, then toggle login mode`() = runTest {
        // --- GIVEN ---
        viewModel.setLoginMode()
        assertFalse(viewModel.dataLoginScreen.value.isInLoginMode)
        viewModel.setDataBottomSheet(DataLoginBottomSheet.ACCOUNT_CREATED)

        // --- WHEN ---
        viewModel.requestAcceptBottomSheet()
        advanceUntilIdle()

        // --- THEN ---
        assertTrue(viewModel.dataLoginScreen.value.isInLoginMode)
        assertFalse(viewModel.dataBottomSheet.value.showBottomSheet)
    }

    @Test
    fun `given FORGOT_PASSWORD bottom sheet, when accept is clicked, then invoke forgotPassword`() = runTest {
        // --- GIVEN ---
        viewModel.setDataBottomSheet(DataLoginBottomSheet.FORGOT_PASSWORD)

        // --- WHEN ---
        viewModel.requestAcceptBottomSheet()
        advanceUntilIdle()

        // --- THEN ---
        assertFalse(viewModel.dataBottomSheet.value.showBottomSheet)
    }

    @Test
    fun `given saved credentials exist, when getSaveCredentials is called for the first time, then populate text fields and check remember me`() = runTest {
        // --- GIVEN ---
        val savedEmail = "remembered@example.com"
        val savedPassword = "secretPassword"

        coEvery { mockSaveLoginCredentialUseCase.getCredentials() } returns UserSession(savedEmail, savedPassword)

        // --- WHEN ---
        viewModel.getSaveCredentials()
        advanceUntilIdle()

        // --- THEN ---
        val currentScreenState = viewModel.dataLoginScreen.value

        assertEquals(savedEmail, currentScreenState.emailTextFieldState.text.toString())
        assertEquals(savedPassword, currentScreenState.passwordTextFieldState.text.toString())

        assertTrue(currentScreenState.isChecked)

        assertEquals(LoginUIState.Success, viewModel.uiState.value)
        coVerify(exactly = 1) { mockSaveLoginCredentialUseCase.getCredentials() }
    }

    @Test
    fun `given getSaveCredentials has already executed once, when called again, then do absolutely nothing`() = runTest {
        // --- GIVEN ---
        coEvery { mockSaveLoginCredentialUseCase.getCredentials() } returns UserSession("user@test.com", "1234")

        viewModel.getSaveCredentials()
        advanceUntilIdle()

        io.mockk.clearMocks(mockSaveLoginCredentialUseCase)

        // --- WHEN ---
        viewModel.getSaveCredentials()
        advanceUntilIdle()

        // --- THEN ---
        coVerify(exactly = 0) { mockSaveLoginCredentialUseCase.getCredentials() }
    }

    @Test
    fun `given email in bottom sheet, when forgotPassword completes successfully, then show EMAIL_SENT_FORGOT_PASSWORD`() = runTest {
        // --- GIVEN ---
        val email = "recover@example.com"

        viewModel.setDataBottomSheet(DataLoginBottomSheet.FORGOT_PASSWORD)
        viewModel.dataBottomSheet.value.emailSentForgotPassword.edit { clear(); append(email) }

        val successResponse = AuthResponseAuthentication.Success
        coEvery {
            mockLoginAuthenticationUseCase.forgotPassword(email)
        } returns flowOf(successResponse)

        // --- WHEN ---
        viewModel.requestAcceptBottomSheet()
        advanceUntilIdle()

        // --- THEN ---
        assertEquals(LoginUIState.Success, viewModel.uiState.value)

        assertEquals(DataLoginBottomSheet.EMAIL_SENT_FORGOT_PASSWORD, viewModel.dataBottomSheet.value.dataBottomSheet)

        coVerify(exactly = 1) { mockLoginAuthenticationUseCase.forgotPassword(email) }
    }

    @Test
    fun `given email in bottom sheet, when forgotPassword returns error, then invoke setError`() = runTest {
        // --- GIVEN ---
        val email = "invalid@example.com"
        viewModel.setDataBottomSheet(DataLoginBottomSheet.FORGOT_PASSWORD)
        viewModel.dataBottomSheet.value.emailSentForgotPassword.edit { clear(); append(email) }

        val errorResponse = AuthResponseAuthentication.Error(1)
        coEvery {
            mockLoginAuthenticationUseCase.forgotPassword(email)
        } returns flowOf(errorResponse)

        // --- WHEN ---
        viewModel.requestAcceptBottomSheet()
        advanceUntilIdle()

        // --- THEN ---
        assertEquals(LoginUIState.Error, viewModel.uiState.value)
    }

    @Test
    fun `given credentials in login screen, when resendEmail completes successfully, then show EMAIL_SENT`() = runTest {
        // --- GIVEN ---
        val email = "unverified@example.com"
        val password = "password123"

        viewModel.setDataBottomSheet(DataLoginBottomSheet.UNVERIFIED_EMAIL)

        viewModel.dataLoginScreen.value.emailTextFieldState.edit { clear(); append(email) }
        viewModel.dataLoginScreen.value.passwordTextFieldState.edit { clear(); append(password) }

        val successResponse = AuthResponseAuthentication.Success
        coEvery {
            mockLoginAuthenticationUseCase.resendEmail(email, password)
        } returns flowOf(successResponse)

        // --- WHEN ---
        viewModel.requestAcceptBottomSheet()
        advanceUntilIdle()

        // --- THEN ---
        assertEquals(LoginUIState.Success, viewModel.uiState.value)
        assertEquals(DataLoginBottomSheet.EMAIL_SENT, viewModel.dataBottomSheet.value.dataBottomSheet)

        coVerify(exactly = 1) { mockLoginAuthenticationUseCase.resendEmail(email, password) }
    }

    @Test
    fun `given credentials in login screen, when resendEmail returns error, then invoke setError`() = runTest {
        // --- GIVEN ---
        val email = "test@example.com"
        val password = "wrong"

        viewModel.setDataBottomSheet(DataLoginBottomSheet.UNVERIFIED_EMAIL)
        viewModel.dataLoginScreen.value.emailTextFieldState.edit { clear(); append(email) }
        viewModel.dataLoginScreen.value.passwordTextFieldState.edit { clear(); append(password) }

        val errorResponse = AuthResponseAuthentication.Error(1)
        coEvery {
            mockLoginAuthenticationUseCase.resendEmail(email, password)
        } returns flowOf(errorResponse)

        // --- WHEN ---
        viewModel.requestAcceptBottomSheet()
        advanceUntilIdle()

        // --- THEN ---
        assertEquals(LoginUIState.Error, viewModel.uiState.value)
    }

}