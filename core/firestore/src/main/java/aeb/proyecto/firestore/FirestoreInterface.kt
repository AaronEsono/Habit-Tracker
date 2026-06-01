package aeb.proyecto.firestore

import aeb.proyecto.firestore.model.FirestoreData
import kotlinx.coroutines.flow.Flow

/**
 * Formal domain contract establishing the architectural boundaries for remote NoSQL operations.
 *
 * This boundary isolates cloud database communication, returning reactive streams
 * to monitor execution state updates seamlessly within upstream business layers.
 */
interface FirestoreInterface {

    /**
     * Establishes a cold reactive stream to monitor or fetch the user's remote data profile payload.
     *
     * @param userId The unique authenticated client database identifier token used to locate the document boundary.
     * @return A [Flow] emitting operational lifecycle wrapper states [AuthResponseFirestore] (Loading, Success, Failure).
     */
    suspend fun getDataUser(userId: String): Flow<AuthResponseFirestore>

    /**
     * Commits or updates a cloud database entry matching the target client's schema payload.
     *
     * @param data The structured [FirestoreData] DTO payload to serialize into the cloud document.
     * @param userId The unique authenticated client database identifier token targeting the write destination.
     * @return A [Flow] emitting operational lifecycle wrapper states [AuthResponseFirestore] (Loading, Success, Failure).
     */
    suspend fun saveDataUser(data: FirestoreData, userId: String): Flow<AuthResponseFirestore>

    /**
     * Truncates or deletes the specified client tracking data tree completely from the cloud storage engine.
     *
     * @param userId The unique authenticated client database identifier token targeting the destruction zone.
     * @return A [Flow] emitting operational lifecycle wrapper states [AuthResponseFirestore] (Loading, Success, Failure).
     */
    suspend fun deleteDataUser(userId: String): Flow<AuthResponseFirestore>

}