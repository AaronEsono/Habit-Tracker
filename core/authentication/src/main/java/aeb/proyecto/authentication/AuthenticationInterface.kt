package aeb.proyecto.authentication

import android.content.Context
import kotlinx.coroutines.flow.Flow

/**
 * A reactive domain-level architecture contract governing all application identity and session operations.
 *
 * This interface establishes the unified boundary layer for user authentication. By abstracting data propagation
 * through Kotlin [Flow] streams and asynchronous [suspend] execution pipelines, it ensures that long-running
 * network transactions are offloaded cleanly from the main UI thread, preventing framework frame drops.
 *
 * Implementation structures (e.g., Firebase Auth drivers) are forced to encapsulate state management inside
 * a unified wrapper payload (`AuthResponseAuthentication`), standardizing success, loading, and error states.
 */
interface AuthenticationInterface {

    /**
     * Initiates a federated single sign-on (SSO) authentication pipeline utilizing external Google credentials.
     *
     * @return A cold [Flow] emitting asynchronous state updates of the active transaction lifecycle.
     */
    fun signInWithGoogle(context: Context): Flow<AuthResponseAuthentication>

    /**
     * Validates traditional user identity credentials using an encrypted password combination.
     *
     * @param email The target communication address registry associated with the profile.
     * @param password The raw verification pass-phrase token submitted by the user.
     * @return A cold [Flow] emitting asynchronous states reflecting the network validation results.
     */
    suspend fun signInWithEmail(email: String, password: String): Flow<AuthResponseAuthentication>

    /**
     * Triggers a remote registration pipeline to instantiate a new user entity profile.
     *
     * @param email The unique communication address requested to anchor the profile identity.
     * @param password The security pass-phrase token selected to protect the account resource.
     * @return A cold [Flow] emitting transactional milestones during account provisioning.
     */
    suspend fun createAccountWithEmail(email: String, password: String): Flow<AuthResponseAuthentication>

    /**
     * Re-dispatches a verification email activation payload to a newly registered account node.
     *
     * Useful for out-of-the-box security mechanics where access remains locked until a link validation is complete.
     *
     * @param email The targeted communication account registry.
     * @param password The valid verification credentials required to re-authorize the transmission.
     * @return A cold [Flow] monitoring the state of the outbound network dispatch task.
     */
    suspend fun resendEmail(email: String, password: String): Flow<AuthResponseAuthentication>

    /**
     * Dispatches an asynchronous credential recovery payload to allow remote password resets.
     *
     * @param email The destination communication channel requested to host the recovery link frame.
     * @return A cold [Flow] mapping the operational success or failure boundaries of the recovery task.
     */
    suspend fun forgotPassword(email: String): Flow<AuthResponseAuthentication>

    /**
     * Terminates the active user session context, purging local runtime security token caches immediately.
     */
    fun logOut()

    /**
     * Queries the immediate, local caching state to determine active session coordinates synchronously.
     *
     * @return A snapshot state wrapper capturing the currently logged-in entity parameters.
     */
    fun currentUser(): AuthResponseAuthentication

    /**
     * Retrieves the display name string bound to the authenticated user context profile.
     *
     * @return The localized identity name string, or an empty string representation if unauthenticated.
     */
    fun getName(): String

    /**
     * Retrieves the structural unique relational database reference key identifying the authenticated user.
     *
     * @return A unique system identifier string (UID) assigned by the identity provider platform.
     */
    fun getCurrentId(): String

}