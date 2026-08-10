package aeb.proyecto.authentication

import aeb.proyecto.analytics.AnalyticsManagerInterface
import android.content.Context
import app.cash.turbine.test
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AuthenticationTest {

    @get:Rule
    val mainDispatchersRule = MainDispatchersRule()

    private val context: Context = mockk(relaxed = true)
    private val analyticsManager: AnalyticsManagerInterface = mockk(relaxed = true)
    private val auth: FirebaseAuth = mockk(relaxed = true)

    private val authResultTask: Task<AuthResult> = mockk()
    private val authResult: AuthResult = mockk()
    private val firebaseUser: FirebaseUser = mockk()
    private val updateProfileTask: Task<Void> = mockk()
    private val emailVerificationTask: Task<Void> = mockk()

    private lateinit var authenticationManager: AuthenticationManager

    @Before
    fun setup() {
        authenticationManager = AuthenticationManager(context, analyticsManager, auth)
    }

    @Test
    fun `given valid credentials when createAccountWithEmail is called then executes pipeline and returns Success`() = runTest {
        // --- GIVEN ---
        val email = "test@example.com"
        val password = "securePassword123"
        val mockUid = "uid_ficticia_99"

        // Paso A: Mockear createUserWithEmailAndPassword().await()
        every { auth.createUserWithEmailAndPassword(email, password) } returns authResultTask
        coEvery { authResultTask.isComplete } returns true
        coEvery { authResultTask.isCanceled } returns false
        coEvery { authResultTask.exception } returns null
        coEvery { authResultTask.result } returns authResult

        // Paso B: Devolver el usuario mockeado desde el resultado de autenticación
        every { authResult.user } returns firebaseUser
        every { firebaseUser.email } returns email
        every { firebaseUser.uid } returns mockUid

        // Paso C: Mockear user.updateProfile().await()
        // Firebase usa una función estática interna o de extensión, pero al pasarle 'any()' MockK lo intercepta en el objeto user
        every { firebaseUser.updateProfile(any()) } returns updateProfileTask
        coEvery { updateProfileTask.isComplete } returns true
        coEvery { updateProfileTask.isCanceled } returns false
        coEvery { updateProfileTask.exception } returns null
        coEvery { updateProfileTask.result } returns null

        // Paso D: Mockear user.sendEmailVerification().await()
        every { firebaseUser.sendEmailVerification() } returns emailVerificationTask
        coEvery { emailVerificationTask.isComplete } returns true
        coEvery { emailVerificationTask.isCanceled } returns false
        coEvery { emailVerificationTask.exception } returns null
        coEvery { emailVerificationTask.result } returns null

        // --- WHEN & THEN (Ejecución con Turbine) ---
        authenticationManager.createAccountWithEmail(email, password).test {

            // 1. Emite de inmediato el estado de carga
            assertEquals(AuthResponseAuthentication.Loading, awaitItem())

            assertEquals(AuthResponseAuthentication.Success, awaitItem())

            awaitComplete()
        }

        // --- VERIFICATIONS ---
        // Verificamos el cierre de sesión defensivo
        verify (exactly = 1) { auth.signOut() }

        // Verificamos que se lanzó la analítica de cuenta creada con la UID correcta
        verify(exactly = 1) {
            analyticsManager.logEvent(match { event ->
                event.type.name == "CREATED_ACCOUNT" || event.extras["user_id"] == mockUid
            })
        }
    }

    @Test
    fun `given invalid credentials when createAccountWithEmail is called then fires exception and returns Error state`() = runTest {
        // --- GIVEN ---
        val email = "error@example.com"
        val password = "123" // Contraseña supuestamente inválida/débil
        val expectedErrorResId = R.string.error_auth_default // El ID que devuelva tu función treatError(e)

        // Hacemos que la tarea de creación de cuenta falle lanzando una excepción
        every { auth.createUserWithEmailAndPassword(email, password) } returns authResultTask
        coEvery { authResultTask.isComplete } returns true
        coEvery { authResultTask.isCanceled } returns false
        coEvery { authResultTask.exception } returns Exception("FirebaseAuthWeakPasswordException")
        coEvery { authResultTask.result } throws Exception("Firebase Auth Error") // 💥 Forzamos la explosión aquí

        // --- WHEN & THEN (Ejecución con Turbine) ---
        authenticationManager.createAccountWithEmail(email, password).test {

            // 1. El Loading siempre se emite primero
            assertEquals(AuthResponseAuthentication.Loading, awaitItem())

            // 2. Al saltar la excepción en el primer .await(), el código va directo al catch
            val errorResult = awaitItem() as AuthResponseAuthentication.Error
            assertEquals(expectedErrorResId, errorResult.message)

            awaitComplete()
        }

        // --- VERIFICATIONS ---
        // Verificamos que, a pesar de fallar, se ejecutó el signOut() de seguridad del bloque catch
        verify(exactly = 1) { auth.signOut() }

        // Verificamos que se registró el analítico de ERROR
        verify(exactly = 1) {
            analyticsManager.logEvent(match { event ->
                event.type.name == "ERROR_AUTHENTICATION" &&
                        event.extras["message"] == "FirebaseAuthWeakPasswordException"
            })
        }
    }

    @Test
    fun `given valid credentials when signInWithEmail is called then executes pipeline and returns Success`() = runTest {
        // --- GIVEN ---
        val email = "test@example.com"
        val password = "securePassword123"
        val mockUid = "uid_ficticia_99"

        // Paso A: Mockear createUserWithEmailAndPassword().await()
        every { auth.signInWithEmailAndPassword(email, password) } returns authResultTask
        coEvery { authResultTask.isComplete } returns true
        coEvery { authResultTask.isCanceled } returns false
        coEvery { authResultTask.exception } returns null
        coEvery { authResultTask.result } returns authResult

        // Paso B: Devolver el usuario mockeado desde el resultado de autenticación
        every { authResult.user } returns firebaseUser
        every { firebaseUser.email } returns email
        every { firebaseUser.uid } returns mockUid
        every { firebaseUser.isEmailVerified } returns true

        authenticationManager.signInWithEmail(email, password).test {

            // 1. Emite de inmediato el estado de carga
            assertEquals(AuthResponseAuthentication.Loading, awaitItem())

            assertEquals(AuthResponseAuthentication.Success, awaitItem())

            awaitComplete()
        }

        // Verificamos que se lanzó la analítica de cuenta creada con la UID correcta
        verify(exactly = 1) {
            analyticsManager.logEvent(match { event ->
                event.type.name == "USER_LOGGED" && event.extras["user_id"] == mockUid
            })
        }
    }

    @Test
    fun `given valid credentials but unverified when signInWithEmail is called then executes pipeline and returns Success`() = runTest {
        // --- GIVEN ---
        val email = "test@example.com"
        val password = "securePassword123"
        val mockUid = "uid_ficticia_99"

        // Paso A: Mockear createUserWithEmailAndPassword().await()
        every { auth.signInWithEmailAndPassword(email, password) } returns authResultTask
        coEvery { authResultTask.isComplete } returns true
        coEvery { authResultTask.isCanceled } returns false
        coEvery { authResultTask.exception } returns null
        coEvery { authResultTask.result } returns authResult

        // Paso B: Devolver el usuario mockeado desde el resultado de autenticación
        every { authResult.user } returns firebaseUser
        every { firebaseUser.email } returns email
        every { firebaseUser.uid } returns mockUid
        every { firebaseUser.isEmailVerified } returns false

        authenticationManager.signInWithEmail(email, password).test {

            // 1. Emite de inmediato el estado de carga
            assertEquals(AuthResponseAuthentication.Loading, awaitItem())

            assertEquals(AuthResponseAuthentication.UnverifiedEmail, awaitItem())

            awaitComplete()
        }

        verify(exactly = 1) { auth.signOut() }

        // Verificamos que se lanzó la analítica de cuenta creada con la UID correcta
        verify(exactly = 1) {
            analyticsManager.logEvent(match { event ->
                event.type.name == "ERROR_AUTHENTICATION" &&
                        event.extras["message"] == "ERROR_UNVERIFIED_EMAIL"
            })
        }
    }

    @Test
    fun `given invalid credentials when signInWithEmail is called then executes pipeline and returns Success`() = runTest {
        // --- GIVEN ---
        val email = "test@example.com"
        val password = "securePassword123"
        val expectedErrorResId = R.string.error_auth_default // El ID que devuelva tu función treatError(e)

        // Paso A: Mockear createUserWithEmailAndPassword().await()
        every { auth.signInWithEmailAndPassword(email, password) } returns authResultTask
        coEvery { authResultTask.isComplete } returns true
        coEvery { authResultTask.isCanceled } returns false
        coEvery { authResultTask.exception } returns Exception("FirebaseAuthWeakPasswordException")
        coEvery { authResultTask.result } throws Exception("Firebase Auth Error")

        // --- WHEN & THEN ---
        authenticationManager.signInWithEmail(email, password).test {

            assertEquals(AuthResponseAuthentication.Loading, awaitItem())

            val errorResult = awaitItem() as AuthResponseAuthentication.Error
            assertEquals(expectedErrorResId, errorResult.message)

            awaitComplete()
        }

        verify(exactly = 1) { auth.signOut() }

        verify(exactly = 1) {
            analyticsManager.logEvent(match { event ->
                event.type.name == "ERROR_AUTHENTICATION" &&
                        event.extras["message"] == "FirebaseAuthWeakPasswordException"
            })
        }
    }

    @Test
    fun `given valid credentials when resendEmail is called then executes pipeline and returns Success`() = runTest {
        // --- GIVEN ---
        val email = "test@example.com"
        val password = "securePassword123"

        // Paso A: Mockear el inicio de sesión inicial (.signInWithEmailAndPassword)
        every { auth.signInWithEmailAndPassword(email, password) } returns authResultTask
        coEvery { authResultTask.isComplete } returns true
        coEvery { authResultTask.isCanceled } returns false
        coEvery { authResultTask.exception } returns null
        coEvery { authResultTask.result } returns authResult

        // Paso B: Hacer que el AuthResult exponga a nuestro usuario ficticio
        every { authResult.user } returns firebaseUser
        every { firebaseUser.email } returns email

        every { firebaseUser.sendEmailVerification() } returns emailVerificationTask
        coEvery { emailVerificationTask.isComplete } returns true
        coEvery { emailVerificationTask.isCanceled } returns false
        coEvery { emailVerificationTask.exception } returns null
        coEvery { emailVerificationTask.result } returns null

        // --- WHEN & THEN (Turbine) ---
        authenticationManager.resendEmail(email, password).test {

            assertEquals(AuthResponseAuthentication.Loading, awaitItem())

            assertEquals(AuthResponseAuthentication.Success, awaitItem())

            awaitComplete()
        }

        // --- VERIFICATIONS ---
        // Comprobamos el cierre de sesión de seguridad obligatorio
        verify(exactly = 1) { auth.signOut() }

        // Verificamos que se lance la analítica propia de reenvío con el email correcto
        verify(exactly = 1) {
            analyticsManager.logEvent(match { event ->
                event.type.name == "RESEND_EMAIL" && event.extras["email"] == email
            })
        }
    }

    @Test
    fun `given invalid credentials when resendEmail is called then executes pipeline and returns Success`() = runTest {
        // --- GIVEN ---
        val email = "test@example.com"
        val password = "securePassword123"
        val expectedErrorResId = R.string.error_auth_default // El ID que mapee tu función treatError()

        // Paso A: Mockear el inicio de sesión inicial
        every { auth.signInWithEmailAndPassword(email, password) } returns authResultTask
        coEvery { authResultTask.isComplete } returns true
        coEvery { authResultTask.isCanceled } returns false
        coEvery { authResultTask.exception } returns null
        coEvery { authResultTask.result } returns authResult

        // Paso B: Provocamos el fallo haciendo que el usuario sea null
        every { authResult.user } returns null

        // --- WHEN & THEN (Turbine) ---
        authenticationManager.resendEmail(email, password).test {

            assertEquals(AuthResponseAuthentication.Loading, awaitItem())

            val errorResult = awaitItem() as AuthResponseAuthentication.Error
            assertEquals(expectedErrorResId, errorResult.message)

            awaitComplete()
        }

        // --- VERIFICATIONS ---
        verify(exactly = 1) { auth.signOut() }

        verify(exactly = 1) {
            analyticsManager.logEvent(match { event ->
                event.type.name == "ERROR_AUTHENTICATION" &&
                        event.extras["message"]?.contains("empty user reference") == true
            })
        }
    }

    @Test
    fun `given valid credentials when forgotPassword is called then executes pipeline and returns Success`() = runTest {
        // --- GIVEN ---
        val email = "test@example.com"
        val task: Task<Void> = mockk()

        // Paso A: Mockear el inicio de sesión inicial (.signInWithEmailAndPassword)
        every { auth.sendPasswordResetEmail(email) } returns task
        coEvery { task.isComplete } returns true
        coEvery { task.isCanceled } returns false
        coEvery { task.exception } returns null
        coEvery { task.result } returns null

        // --- WHEN & THEN (Turbine) ---
        authenticationManager.forgotPassword(email).test {

            assertEquals(AuthResponseAuthentication.Loading, awaitItem())

            assertEquals(AuthResponseAuthentication.Success, awaitItem())

            awaitComplete()
        }

        // Verificamos que se lance la analítica propia de reenvío con el email correcto
        verify(exactly = 1) {
            analyticsManager.logEvent(match { event ->
                event.type.name == "FORGOT_PASSWORD" && event.extras["email"] == email
            })
        }
    }

    @Test
    fun `given invalid credentials when forgotPassword is called then executes pipeline and returns Success`() = runTest {
        // --- GIVEN ---
        val email = "test@example.com"
        val task: Task<Void> = mockk()
        val expectedErrorResId = R.string.error_auth_default // El ID que devuelva tu función treatError(e)

        // Paso A: Mockear el inicio de sesión inicial (.signInWithEmailAndPassword)
        every { auth.sendPasswordResetEmail(email) } returns task
        coEvery { task.isComplete } returns true
        coEvery { task.isCanceled } returns false
        coEvery { task.exception } returns Exception("FirebaseAuthWeakPasswordException")
        coEvery { task.result } throws Exception("Firebase Auth Error")

        // --- WHEN & THEN (Turbine) ---
        authenticationManager.forgotPassword(email).test {

            assertEquals(AuthResponseAuthentication.Loading, awaitItem())

            val errorResult = awaitItem() as AuthResponseAuthentication.Error
            assertEquals(expectedErrorResId, errorResult.message)

            awaitComplete()
        }

        verify(exactly = 1) {
            analyticsManager.logEvent(match { event ->
                event.type.name == "ERROR_AUTHENTICATION" &&
                        event.extras["message"] == "FirebaseAuthWeakPasswordException"
            })
        }
    }

    @Test
    fun `given active user session when logOut is called then logs telemetry and signs out`() {
        // --- GIVEN ---
        every { auth.currentUser } returns firebaseUser
        every { firebaseUser.uid } returns "user_logged_out_123"

        // --- WHEN ---
        authenticationManager.logOut()

        // --- THEN ---
        // Verificamos que se deslogueó de Firebase
        verify(exactly = 1) { auth.signOut() }

        // Verificamos que se lanzó el evento de logOut con su UID
        verify(exactly = 1) {
            analyticsManager.logEvent(match { event ->
                event.type.name == "LOG_OUT" && event.extras["user_id"] == "user_logged_out_123"
            })
        }
    }

    @Test
    fun `given no active user session when logOut is called then signs out without telemetry`() {
        // --- GIVEN ---
        every { auth.currentUser } returns null

        // --- WHEN ---
        authenticationManager.logOut()

        // --- THEN ---
        verify(exactly = 1) { auth.signOut() }
        verify(exactly = 0) { analyticsManager.logEvent(any()) }
    }

    @Test
    fun `given persistent user session when currentUser is called then logs reconnection and returns Success`() {
        // --- GIVEN ---
        every { auth.currentUser } returns firebaseUser
        every { firebaseUser.uid } returns "user_reconnected_123"

        // --- WHEN ---
        val result = authenticationManager.currentUser()

        // --- THEN ---
        assertEquals(AuthResponseAuthentication.Success, result)

        verify(exactly = 1) {
            analyticsManager.logEvent(match { event ->
                event.type.name == "RECONNECTED" && event.extras["user_id"] == "user_reconnected_123"
            })
        }
    }

    @Test
    fun `given expired or empty session when currentUser is called then returns Error`() {
        // --- GIVEN ---
        every { auth.currentUser } returns null
        val expectedErrorRes = R.string.error_auth_no_user_found

        // --- WHEN ---
        val result = authenticationManager.currentUser()

        // --- THEN ---
        val errorResult = result as AuthResponseAuthentication.Error
        assertEquals(expectedErrorRes, errorResult.message)

        verify(exactly = 0) { analyticsManager.logEvent(any()) }
    }

    @Test
    fun `given active session with email when getName is called then returns username segment`() {
        // --- GIVEN ---
        every { auth.currentUser } returns firebaseUser
        every { firebaseUser.email } returns "desarrollador@gmail.com"

        // --- WHEN ---
        val result = authenticationManager.getName()

        // --- THEN ---
        assertEquals("desarrollador", result)
    }

    @Test
    fun `given no active session when getName is called then returns empty string`() {
        // --- GIVEN ---
        every { auth.currentUser } returns null

        // --- WHEN ---
        val result = authenticationManager.getName()

        // --- THEN ---
        assertEquals("", result)
    }

    @Test
    fun `given active session when getCurrentId is called then returns user uid`() {
        // --- GIVEN ---
        every { auth.currentUser } returns firebaseUser
        every { firebaseUser.uid } returns "uid_pro_12345"

        // --- WHEN ---
        val result = authenticationManager.getCurrentId()

        // --- THEN ---
        assertEquals("uid_pro_12345", result)
    }

    @Test
    fun `given no active session when getCurrentId is called then returns empty string`() {
        // --- GIVEN ---
        every { auth.currentUser } returns null

        // --- WHEN ---
        val result = authenticationManager.getCurrentId()

        // --- THEN ---
        assertEquals("", result)
    }

    @Test
    fun `given active user session when deleteAccount is called then deletes account and returns Success`() = runTest {
        // --- GIVEN ---
        val voidTask: Task<Void> = mockk()
        val mockUid = "uid_delete_123"

        every { auth.currentUser } returns firebaseUser
        every { firebaseUser.uid } returns mockUid

        // Mockear user.delete().await()
        every { firebaseUser.delete() } returns voidTask
        coEvery { voidTask.isComplete } returns true
        coEvery { voidTask.isCanceled } returns false
        coEvery { voidTask.exception } returns null
        coEvery { voidTask.result } returns null

        // --- WHEN & THEN (Turbine) ---
        authenticationManager.deleteAccount().test {
            // 1. Emite Loading de inmediato
            assertEquals(AuthResponseAuthentication.Loading, awaitItem())

            // 2. Emite Success tras el borrado
            assertEquals(AuthResponseAuthentication.Success, awaitItem())

            awaitComplete()
        }

        // --- VERIFICATIONS ---
        // Verificamos que se llamó a la función delete() del usuario
        verify(exactly = 1) { firebaseUser.delete() }

        // Verificamos el evento de analítica con la UID correcta
        verify(exactly = 1) {
            analyticsManager.logEvent(match { event ->
                event.type.name == "DELETED_ACCOUNT" || event.extras["user_id"] == mockUid
            })
        }
    }

    @Test
    fun `given no active user session when deleteAccount is called then returns Error state`() = runTest {
        // --- GIVEN ---
        every { auth.currentUser } returns null
        val expectedErrorResId = R.string.error_auth_default // O el ID que devuelva treatError para excepción genérica

        // --- WHEN & THEN (Turbine) ---
        authenticationManager.deleteAccount().test {
            assertEquals(AuthResponseAuthentication.Loading, awaitItem())

            val errorResult = awaitItem() as AuthResponseAuthentication.Error
            assertEquals(expectedErrorResId, errorResult.message)

            awaitComplete()
        }

        // --- VERIFICATIONS ---
        // Al no haber usuario activo, se ejecuta el signOut defensivo del catch
        verify(exactly = 1) { auth.signOut() }

        verify(exactly = 1) {
            analyticsManager.logEvent(match { event ->
                event.type.name == "ERROR_AUTHENTICATION"
            })
        }
    }

    @Test
    fun `given user deletion fails when deleteAccount is called then signs out and returns Error state`() = runTest {
        // --- GIVEN ---
        val voidTask: Task<Void> = mockk()
        every { auth.currentUser } returns firebaseUser
        every { firebaseUser.uid } returns "uid_delete_error"

        val deletionException = Exception("FirebaseAuthRecentLoginRequiredException")
        val expectedErrorResId = R.string.error_auth_default // El ID mapeado por treatError(e)

        // Forzamos fallo al llamar a user.delete()
        every { firebaseUser.delete() } returns voidTask
        coEvery { voidTask.isComplete } returns true
        coEvery { voidTask.isCanceled } returns false
        coEvery { voidTask.exception } returns deletionException
        coEvery { voidTask.result } throws deletionException

        // --- WHEN & THEN (Turbine) ---
        authenticationManager.deleteAccount().test {
            assertEquals(AuthResponseAuthentication.Loading, awaitItem())

            val errorResult = awaitItem() as AuthResponseAuthentication.Error
            assertEquals(expectedErrorResId, errorResult.message)

            awaitComplete()
        }

        // --- VERIFICATIONS ---
        // Al fallar, el bloque catch debe llamar a auth.signOut() por seguridad
        verify(exactly = 1) { auth.signOut() }

        verify(exactly = 1) {
            analyticsManager.logEvent(match { event ->
                event.type.name == "ERROR_AUTHENTICATION" &&
                        event.extras["message"] == "FirebaseAuthRecentLoginRequiredException"
            })
        }
    }
}