package aeb.proyecto.domain.usecase.save

import aeb.proyecto.authentication.AuthResponseAuthentication
import aeb.proyecto.authentication.AuthenticationInterface
import aeb.proyecto.firestore.AuthResponseFirestore
import aeb.proyecto.firestore.FirestoreInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * High-level orchestration use case responsible for coordinating the account teardown workflow across decoupled domains.
 *
 * Acting as the bridge between [FirestoreInterface] and [AuthenticationInterface], this interactor enforces a
 * strict sequential execution order to guarantee data integrity during profile deletion. It isolates domain-specific
 * response abstractions from the presentation layer by mapping downstream reactive states into a single [DeleteAccountResult] flow.
 *
 * @property firestoreInterface The remote database repository abstraction handling document purging.
 * @property authenticationInterface The identity management engine handling session lifecycle and credential destruction.
 */
class SaveDeleteAccountUseCase @Inject constructor(
    private val firestoreInterface: FirestoreInterface,
    private val authenticationInterface: AuthenticationInterface,
) {

    /**
     * Executes a two-stage sequential transaction pipeline to completely remove a user's cloud identity and personal database footprint.
     *
     * This routine consumes internal domain [Flow] emissions synchronously using non-blocking `.first {}` criteria,
     * evaluating terminal pipeline markers before proceeding to downstream operations.
     *
     * ### Sequential Pipeline Lifecycle:
     * 1. **Visual State Initialization:** Immediately emits [DeleteAccountResult.Loading] to transition upstream presentation layers.
     * 2. **Database Footprint Purge:** Calls [FirestoreInterface.deleteDataUser] to remove all personal documents anchored to [userId].
     *    Suspends execution non-blockingly until a non-loading state resolves.
     *    * **Safety Check (Circuit Breaker):** If database teardown fails, execution terminates early via `return@flow`,
     *      emitting [DeleteAccountResult.Error] to prevent leaving un-deletable auth profiles or orphan data structures.
     * 3. **Cloud Identity Teardown:** Upon database purge confirmation, invokes [AuthenticationInterface.deleteAccount] to destroy
     *    the remote authentication record and invalidate local session state.
     * 4. **Terminal Emission:** Evaluates final auth result and broadcasts either [DeleteAccountResult.Success] or [DeleteAccountResult.Error].
     *
     * @param userId The unique authenticated resource identifier (UID) targeting the database deletion zone.
     * @return A cold [Flow] streaming unified transactional pipeline states ([DeleteAccountResult]).
     */
    fun deleteAccount(userId: String): Flow<DeleteAccountResult> = flow {
        emit(DeleteAccountResult.Loading)

        val firestoreResult = firestoreInterface.deleteDataUser(userId)
            .first { it !is AuthResponseFirestore.Loading }

        when (firestoreResult) {
            is AuthResponseFirestore.Error -> {
                emit(DeleteAccountResult.Error(firestoreResult.message))
                return@flow
            }
            is AuthResponseFirestore.Success -> {
                //All completed
            }
            else -> {}
        }

        val authResult = authenticationInterface.deleteAccount()
            .first { it !is AuthResponseAuthentication.Loading }

        when (authResult) {
            is AuthResponseAuthentication.Error -> {
                emit(DeleteAccountResult.Error(authResult.message))
            }
            is AuthResponseAuthentication.Success -> {
                emit(DeleteAccountResult.Success)
            }
            else -> {}
        }
    }
}

/**
 * A sealed architectural state wrapper representing the discrete operational phases
 * of the account deletion lifecycle.
 *
 * This contract standardizes cross-module result tokens into an exhaustive discriminated
 * union consumed directly by presentation ViewModel layers.
 */
sealed interface DeleteAccountResult {
    /**
     * An intermediate allocation signaling that a multi-step asynchronous deletion transaction
     * is currently executing across remote cloud networks.
     */
    data object Loading : DeleteAccountResult

    /**
     * A high-performance terminal state signaling that both user database records and authentication
     * credentials were successfully purged from remote systems.
     */
    data object Success : DeleteAccountResult

    /**
     * A terminal failure node encapsulating a localized Android string resource pointer
     * explaining the infrastructure or security breakdown.
     *
     * @property messageResId An integer primitive representing an Android string resource pointer (`@StringRes`).
     */
    data class Error(val messageResId: Int) : DeleteAccountResult
}