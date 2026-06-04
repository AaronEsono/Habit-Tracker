package aeb.proyecto.domain.usecase.save

import aeb.proyecto.firestore.FirestoreInterface
import aeb.proyecto.firestore.model.FirestoreData
import javax.inject.Inject

/**
 * Domain Use Case designed to orchestrate cloud-bound data synchronization pipelines
 * over Cloud Firestore infrastructures.
 *
 * Handles the secure backup, recovery, and absolute remote eviction of consolidated
 * user tracking matrices ([FirestoreData]) using unique structural identity keys.
 *
 * @property firestoreInterface The boundary contract abstracting remote NoSQL cloud storage operations.
 */
class SaveFirestoreUseCase @Inject constructor(
    private val firestoreInterface: FirestoreInterface,
) {

    /**
     * Pulls the consolidated backup dataset linked to a specific user from the remote cloud repository.
     *
     * @param userId The unique global identifier (UID) of the targeted account owner.
     * @return A response payload wrapping the retrieved [FirestoreData] mapping, or null if no backup exists.
     */
    suspend fun getDataUser(userId: String) = firestoreInterface.getDataUser(userId)

    /**
     * Commits and overwrites the consolidated local tracking snapshot into the remote cloud document storage.
     *
     * @param data The comprehensive domain snapshot packaging active habits, setups, and milestones.
     * @param userId The unique global identifier (UID) acting as the relational storage partition path.
     */
    suspend fun saveDataUser(data: FirestoreData, userId: String) =
        firestoreInterface.saveDataUser(data, userId)

    /**
     * Executes an absolute, permanent purge of all remote documents and user data structures from cloud collections.
     * Typically executed alongside definitive account self-deletion requests.
     *
     * @param userId The unique global identifier (UID) identifying the target partition to wipe.
     */
    suspend fun deleteDataUser(userId: String) = firestoreInterface.deleteDataUser(userId)
}