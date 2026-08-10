package aeb.proyecto.authentication

import aeb.proyecto.analytics.AnalyticsManagerInterface
import aeb.proyecto.analytics.events.AuthenticationEvents
import aeb.proyecto.authentication.errors.treatError
import aeb.proyecto.authentication.utils.ERROR_EMAIL_EXISTS
import aeb.proyecto.authentication.utils.ERROR_UNVERIFIED_EMAIL
import aeb.proyecto.authentication.utils.createNonce
import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.userProfileChangeRequest
import dagger.Binds
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * The core orchestration engine driving all user session validation and profile state transitions.
 *
 * Acting as the concrete [internal] implementation of [AuthenticationInterface], this manager bridges
 * local client requests with the remote Google Cloud Identity ecosystem. It encapsulates synchronous and
 * reactive asynchronous streaming operations, safeguarding runtime sessions against corruption while executing
 * decoupled behavioral telemetry propagation.
 *
 * @property context The globally scoped [@ApplicationContext] utilized to handle resource translation lookups
 * and localized asset checks safely without introducing memory leak vulnerabilities.
 * @property analyticsManagerInterface The decoupled abstraction contract leveraged to dispatch business intelligence
 * logs during auth lifecycle milestones.
 * @property auth The pre-provisioned, thread-safe framework driver managing remote security token sync states.
 */
internal class AuthenticationManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val analyticsManagerInterface: AnalyticsManagerInterface,
    private val auth: FirebaseAuth
):AuthenticationInterface {

    /**
     * Executes a sequential, asynchronous remote registration pipeline to provision a new user
     * profile registry using an email and password combination.
     *
     * This routine leverages the native Kotlin Coroutines integration framework via [await] extension tasks
     * wrapped within a reactive cold [Flow] stream builder. This architecture replaces volatile listener-based
     * execution chains with structured, non-blocking sequential execution steps.
     *
     * ### Execution Pipeline Lifecycle:
     * 1. **Visual State Initialization:** Immediately emits [AuthResponseAuthentication.Loading] to notify the active presentation UI layer.
     * 2. **Credential Provisioning:** Invokes the remote Google authentication server. Execution suspends non-blockingly until the network transaction resolves.
     * 3. **Profile Metadata Mutation:** Attaches the input email reference as the localized display name attribute, suspending until registration is verified.
     * 4. **Security Token Dispatch:** Requests an outbound email verification link payload via [sendEmailVerification], blocking onward progress until the transmission frame registers success.
     * 5. **Defensive Session Teardown:** Invokes [FirebaseAuth.signOut] sequentially to clear the default session auto-login cache, locking the profile until physical validation occurs.
     * 6. **Terminal Emission:** Streams [AuthResponseAuthentication.Success] out to consumers and logs metrics via [analyticsManagerInterface].
     *
     * ### Exception Boundary Propagation:
     * Any asynchronous network failure, security rules violation (e.g., duplicate email entries), or platform runtime exception
     * thrown during suspension points is intercepted by the single `catch` block. This trigger guarantees a clean state teardown via
     * an emergency [auth.signOut], maps the anomaly to an Android resource string through [treatError], logs telemetry data,
     * and safely wraps the outcome inside [AuthResponseAuthentication.Error].
     *
     * @param email The unique communication address requested to anchor the new identity profile.
     * @param password The raw security pass-phrase token selected to safeguard the resource.
     * @return A cold [Flow] streaming the transactional state transitions ([Loading], [Success], [Error]).
     * @throws Exception If the internal SDK core context drops the user reference layer unexpectedly during runtime initialization.
     */
    override suspend fun createAccountWithEmail(
        email: String,
        password: String
    ): Flow<AuthResponseAuthentication> =
        flow {
            // 1. Broadcast immediate loading transaction state to the presentation layers
            emit(AuthResponseAuthentication.Loading)

            try {
                // 2. Provision account node and suspend until the cloud transaction completes
                val authResult = auth.createUserWithEmailAndPassword(email, password).await()

                authResult.user?.let { user ->
                    val profileUpdates = userProfileChangeRequest {
                        displayName = user.email
                    }

                    // 3. Suspend sequentially until profile metadata synchronization completes
                    user.updateProfile(profileUpdates).await()

                    // 4. Suspend until verification dispatch confirmation registers
                    user.sendEmailVerification().await()

                    // 5. Enforce defensive sign-out rules before emitting success states
                    auth.signOut()

                    // Telemetry tracking & success stream resolution
                    analyticsManagerInterface.logEvent(AuthenticationEvents.createdAccount(user.uid))
                    emit(AuthResponseAuthentication.Success)
                } ?: throw Exception("User reference was lost after registration initialization.")

            } catch (e: Exception) {
                // 6. Centralized safety net: enforce session cleaning and propagate mapped error keys
                auth.signOut()
                val errorResId = treatError(e)
                analyticsManagerInterface.logEvent(
                    AuthenticationEvents.error(
                        e.localizedMessage ?: e.toString()
                    )
                )
                emit(AuthResponseAuthentication.Error(errorResId))
            }
        }

    /**
     * Executes an asynchronous identity validation transaction using traditional email and password credentials.
     *
     * This routine leverages non-blocking sequential suspension points via [await] to verify identity coordinates
     * against cloud registries. It implements strict post-authentication business logic gates to enforce email
     * verification compliance rules before granting full session access.
     *
     * ### Sequential Execution Lifecycle:
     * 1. **Visual State Initialization:** Immediately emits [AuthResponseAuthentication.Loading] to transition the presentation layer.
     * 2. **Credential Validation:** Suspends non-blockingly while invoking the remote network login handshake.
     * 3. **Security Gate Assessment:** Evaluates [com.google.firebase.auth.FirebaseUser.isEmailVerified]:
     * * **Gate Passed:** Logs telemetry success metrics via [analyticsManagerInterface] and emits [AuthResponseAuthentication.Success].
     * * **Gate Violated:** If the account activation link remains unclicked, it logs a specialized error tracking event,
     * purges the session instantly via [auth.signOut], and routes [AuthResponseAuthentication.UnverifiedEmail] to the UI.
     *
     * ### Exception Boundary Propagation:
     * Any invalid credentials runtime error, network timeout, or platform-level exception thrown during cloud suspension
     * is intercepted natively by the single `catch` block. The routine executes a defensive emergency [auth.signOut],
     * maps the exception to its corresponding localized string resource pointer via [treatError], logs telemetry tracking,
     * and structuralizes the failure inside [AuthResponseAuthentication.Error].
     *
     * @param email The communication address registry matching the profile credentials.
     * @param password The raw verification pass-phrase token submitted by the user.
     * @return A cold [Flow] streaming the transactional state transitions ([Loading], [Success], [UnverifiedEmail], [Error]).
     */
    override suspend fun signInWithEmail(
        email: String,
        password: String
    ): Flow<AuthResponseAuthentication> = flow {
        // 1. Broadcast immediate loading transaction state to the presentation layers
        emit(AuthResponseAuthentication.Loading)

        try {
            // 2. Validate credentials against remote storage and suspend until network resolution
            val authResult = auth.signInWithEmailAndPassword(email, password).await()

            authResult.user?.let { user ->
                // 3. Enforce strict email verification business policies
                if (user.isEmailVerified) {
                    analyticsManagerInterface.logEvent(AuthenticationEvents.logUserLogged(user.uid))
                    emit(AuthResponseAuthentication.Success)
                } else {
                    analyticsManagerInterface.logEvent(
                        AuthenticationEvents.error(
                            ERROR_UNVERIFIED_EMAIL
                        )
                    )
                    auth.signOut()
                    emit(AuthResponseAuthentication.UnverifiedEmail)
                }
            }
                ?: throw Exception("User session allocation failed immediately after authentication handshake.")

        } catch (e: Exception) {
            // 4. Centralized safety net: enforce session cleaning and propagate mapped error keys
            auth.signOut()
            val errorResId = treatError(e)
            analyticsManagerInterface.logEvent(
                AuthenticationEvents.error(
                    e.localizedMessage ?: e.toString()
                )
            )
            emit(AuthResponseAuthentication.Error(errorResId))
        }
    }

    /**
     * Executes a federated single sign-on (SSO) authentication pipeline utilizing the modern Android
     * [CredentialManager] subsystem integrated with remote Google Identity services.
     *
     * This routine coordinates a multi-layered cryptographic validation pipeline, wrapping jetpack identity
     * options and Firebase Cloud authentication handshakes into a streamlined, sequential asynchronous [Flow].
     *
     * ### Federated Execution Topology:
     * 1. **Configuration & Anti-Replay Payload:** Instantiates a [GetGoogleIdOption] specifying structural client
     * IDs and injects a unique SHA-256 cryptographic token via [createNonce] to prevent mitigation replay exploits.
     * 2. **OS Credential Sheet Suspension:** Invokes the native system UI credentials sheet. Execution suspends
     * non-blockingly until the user selects an account profile or dismisses the interface layout frame.
     * 3. **Token Extraction & Parsing:** Intercepts the custom identity payload data and validates the signature
     * structure using [GoogleIdTokenCredential.createFrom].
     * 4. **Firebase Cloud Exchange:** Converts the parsed Google ID token into a structural [com.google.firebase.auth.AuthCredential]
     * token map, requesting remote cloud session synchronization via [FirebaseAuth.signInWithCredential] using sequential [.await] tasks.
     *
     * ### Comprehensive Exception Boundary Architecture:
     * The overarching `catch` block functions as a unified defensive perimeter catching diverse cross-platform failure points:
     * * **[GetCredentialException]:** Captures user cancelation events, missing Google Play Services, or missing local accounts.
     * * **[GoogleIdTokenParsingException]:** Intercepts data corruption issues during binary parsing execution frames.
     * * **[Exception]:** Catches Firebase cloud transaction rejections or network timeout configurations.
     * Any caught failure triggers an immediate automated session sanitization cleanup map through [auth.signOut].
     *
     * @param context The context from the activity
     * @return A cold [Flow] emitting the transactional identity milestones ([Loading], [Success], [Error]).
     */
    override fun signInWithGoogle(context: Context): Flow<AuthResponseAuthentication> = flow {
        // 1. Broadcast immediate loading transaction state to the presentation layers
        emit(AuthResponseAuthentication.Loading)

        try {
            // 2. Build secure federated Google identity validation options
            val googleValidation = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(context.getString(R.string.web_id))
                .setNonce(createNonce())
                .setAutoSelectEnabled(true)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleValidation)
                .build()

            val credentialManager = CredentialManager.create(context)

            // 3. Request systemic UI selection sheet and suspend until user response registers
            val result = credentialManager.getCredential(
                context = context,
                request = request
            )

            val credential = result.credential

            // 4. Evaluate and securely unpack type token signatures
            if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

                val firebaseCredential = GoogleAuthProvider.getCredential(
                    googleIdTokenCredential.idToken,
                    null
                )

                // 5. Exchange identity tokens with Firebase cloud nodes and suspend until synchronized
                val authResult = auth.signInWithCredential(firebaseCredential).await()

                authResult.user?.let { user ->
                    analyticsManagerInterface.logEvent(AuthenticationEvents.loggedWithGoogle(user.uid))
                    emit(AuthResponseAuthentication.Success)
                } ?: throw Exception("Identity framework dropped the user session reference post-exchange.")

            } else {
                throw Exception("Received unsupported credential type token boundary: ${credential.type}")
            }
        } catch (e: Exception){
            // 6. Centralized safety perimeter: purge session logs and propagate localized presentation markers
            auth.signOut()
            val errorResId = treatError(e)
            analyticsManagerInterface.logEvent(
                AuthenticationEvents.error(
                    e.localizedMessage ?: e.toString()
                )
            )
            emit(AuthResponseAuthentication.Error(errorResId))
        }
    }

    /**
     * Re-initiates the email verification handshake for an existing user account context.
     *
     * This function coordinates a sequential asynchronous pipeline to safely resend a verification token.
     * It enforces temporary session establishment to fetch the cryptographic user profile before dispatching
     * the outbound verification event.
     *
     * ### Architectural Lifecycle Topology:
     * 1. **Session Provisioning:** Synchronously requests temporary authentication against Firebase Auth nodes
     * using the provided [email] and [password] credentials via [.await].
     * 2. **Token Dispatch:** Upon establishing a non-null token context, triggers [FirebaseUser.sendEmailVerification]
     * to invoke the cloud-side SMTP relay infrastructure.
     * 3. **Session Sanitization:** Implements an atomic defensive design pattern where [auth.signOut] is guaranteed
     * to execute within the terminal success block and the global overarching `catch` perimeter. This prevents
     * unverified residual sessions from hijacking or polling memory reference graphs.
     *
     * @param email The target account identification string.
     * @param password The account validation secret payload.
     * @return A cold [Flow] emitting the sequential transactional states ([Loading], [Success], [Error]).
     */
    override suspend fun resendEmail(
        email: String,
        password: String
    ): Flow<AuthResponseAuthentication> =
        flow {
            // 1. Broadcast immediate loading transaction state to presentation layers
            emit(AuthResponseAuthentication.Loading)

            try {
                // 2. Establish temporary cloud session to acquire user reference topology
                val authResult = auth.signInWithEmailAndPassword(email, password).await()
                val currentUser = authResult.user

                if (currentUser != null) {
                    // 3. Dispatch verification payload through Firebase SMTP infrastructure
                    currentUser.sendEmailVerification().await()

                    analyticsManagerInterface.logEvent(
                        AuthenticationEvents.resendEmail(
                            currentUser.email ?: email
                        )
                    )

                    // 4. Enforce structural safety state: purge active session before emitting success
                    auth.signOut()
                    emit(AuthResponseAuthentication.Success)
                } else {
                    throw Exception("Firebase transaction returned an empty user reference during email pipeline execution.")
                }

            } catch (e: Exception) {
                // 5. Centralized boundary cleanup: terminate residual logs and propagate local resource identifier
                auth.signOut()
                val errorResId = treatError(e)
                analyticsManagerInterface.logEvent(
                    AuthenticationEvents.error(
                        e.localizedMessage ?: e.toString()
                    )
                )
                emit(AuthResponseAuthentication.Error(errorResId))
            }
        }

    /**
     * Initiates an asynchronous password recovery handshake for a specific user identifier.
     *
     * This function triggers the cloud-side Firebase authentication SMTP system to dispatch a secure
     * password reset token link to the provided [email] address.
     *
     * ### Architectural Lifecycle Topology:
     * 1. **Dispatched Verification Request:** Calls [FirebaseAuth.sendPasswordResetEmail] non-blockingly using
     * the sequential [.await] architecture pattern.
     * 2. **Telemetry Logging:** Upon a successful cloud confirmation response, fires a dedicated business
     * analytic tracking event via [analyticsManagerInterface.logEvent].
     * 3. **Defensive Perimeter Architecture:** Implements an overarching `catch` safety boundary. Any structural
     * cloud rejection (e.g., [com.google.firebase.auth.FirebaseAuthInvalidUserException] for non-existent users
     * or network drops) bypasses residual allocations, routes directly through [treatError], logs the structural failure
     * footprint, and safely propagates an error token payload to the presentation layer.
     *
     * @param email The target account identification string requesting credential recovery.
     * @return A cold [Flow] emitting the sequential transactional milestones ([Loading], [Success], [Error]).
     */
    override suspend fun forgotPassword(email: String): Flow<AuthResponseAuthentication> =
        flow {
            // 1. Broadcast immediate loading transaction state to presentation layers
            emit(AuthResponseAuthentication.Loading)

            try {
                // 2. Dispatch the recovery token request to Firebase Cloud nodes and suspend execution
                auth.sendPasswordResetEmail(email).await()

                // 3. Log business metrics event post-transaction confirmation
                analyticsManagerInterface.logEvent(AuthenticationEvents.forgotPassword(email))

                // 4. Emit terminal success state
                emit(AuthResponseAuthentication.Success)

            } catch (e: Exception) {
                // 5. Centralized boundary cleanup: map raw exceptions to structural UI resource resources
                val errorResId = treatError(e)
                analyticsManagerInterface.logEvent(
                    AuthenticationEvents.error(
                        e.localizedMessage ?: e.toString()
                    )
                )
                emit(AuthResponseAuthentication.Error(errorResId))
            }
        }


    /**
     * Executes a sequential, asynchronous account deletion pipeline.
     *
     * ### Execution Pipeline Lifecycle:
     * 1. **Visual State Initialization:** Emits [AuthResponseAuthentication.Loading].
     * 2. **Session Audit:** Validates if an active [currentUser] session context exists.
     * 3. **Terminal Emission:** Emits [AuthResponseAuthentication.Success] and logs telemetry metrics.
     *
     * @return A cold [Flow] streaming state transitions ([Loading], [Success], [Error]).
     */
    override suspend fun deleteAccount(): Flow<AuthResponseAuthentication> = flow {
        emit(AuthResponseAuthentication.Loading)

        try {
            val user = auth.currentUser ?: throw Exception("No active user session context found to perform deletion.")
            val uid = user.uid

            // Delete the acount
            user.delete().await()

            analyticsManagerInterface.logEvent(AuthenticationEvents.deletedAccount(uid))
            emit(AuthResponseAuthentication.Success)

        } catch (e: Exception) {
            if (e is kotlinx.coroutines.CancellationException) throw e

            auth.signOut()

            val errorResId = treatError(e)
            analyticsManagerInterface.logEvent(
                AuthenticationEvents.error(
                    e.localizedMessage ?: e.toString()
                )
            )
            emit(AuthResponseAuthentication.Error(errorResId))
        }
    }

    /**
     * Terminates the active cloud session context securely.
     *
     * This function handles session sanitization by logging transactional metrics before invoking
     * [FirebaseAuth.signOut]. It implements a defensive design pattern to prevent runtime exceptions
     * if the user state is already nullified.
     */
    override fun logOut() {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            analyticsManagerInterface.logEvent(AuthenticationEvents.logOut(uid))
        }
        auth.signOut()
    }

    /**
     * Synchronously audits the structural persistence of the local authentication state.
     *
     * This function serves as a non-blocking gatekeeper to verify if a valid session reference
     * exists within the cache boundary.
     *
     * @return [AuthResponseAuthentication.Success] if an active session state is verified;
     * otherwise [AuthResponseAuthentication.Error] carrying the local missing resource identifier.
     */
    override fun currentUser(): AuthResponseAuthentication {
        auth.currentUser?.let{
            analyticsManagerInterface.logEvent(AuthenticationEvents.reconnected(it.uid))
            return AuthResponseAuthentication.Success
        } ?: return AuthResponseAuthentication.Error(R.string.error_auth_no_user_found)
    }

    /**
     * Extracts a display moniker parsed directly from the active credential metadata.
     *
     * @return The sub-string segment preceding the structural email delimiter ("@"),
     * or an empty string if the user profile context is unestablished or anonymous.
     */
    override fun getName(): String {
        return auth.currentUser?.email?.substringBefore("@") ?: ""
    }

    /**
     * Retrieves the cryptographic unique resource identifier (UID) for the currently authenticated profile.
     *
     * @return The unique user ID string generated by Firebase Auth nodes, or an empty string
     * if no terminal state session exists.
     */
    override fun getCurrentId(): String {
        return auth.currentUser?.uid ?: ""
    }
}

/**
 * A sealed architectural state wrapper representing the terminal and intermediate boundaries
 * of the authentication lifecycle.
 *
 * This contract functions as a discriminated state machine union, standardizing payload propagation
 * from data execution managers toward upstream presentation UI engines (such as Jetpack Compose ViewModels).
 * It ensures compile-time exhaustiveness when evaluating execution outcomes via structural pattern matching.
 */
interface AuthResponseAuthentication {

    /**
     * A high-performance terminal allocation signaling that the requested identity operation
     * was fully validated, synchronized, and resolved successfully by the cloud provider.
     */
    data object Success : AuthResponseAuthentication

    /**
     * An intermediate allocation signaling that an active asynchronous transaction is currently
     * traversing network execution boundaries.
     *
     * Presentation layers should intercept this token to trigger non-blocking UI loading indicators
     * (e.g., ProgressBars or shimmer animations) and suspend concurrent user interactive inputs.
     */
    data class Error(val message: Int) : AuthResponseAuthentication

    /**
     * A conditional validation roadblock signaling that credentials passed initial security checks,
     * but access remains structurally restricted until physical email validation verification is complete.
     */
    data object UnverifiedEmail: AuthResponseAuthentication

    /**
     * A terminal failure node encapsulating an immutable pointer reference to a localized user-facing
     * description explaining the infrastructure breakdown.
     *
     * @property message An integer primitive value representing an Android string resource pointer (`@StringRes`)
     * derived from resource dictionary maps (e.g., `R.string.error_auth_network_request_failed`).
     */
    data object Loading: AuthResponseAuthentication
}