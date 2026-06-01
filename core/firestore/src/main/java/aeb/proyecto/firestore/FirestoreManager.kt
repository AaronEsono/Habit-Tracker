package aeb.proyecto.firestore

import aeb.proyecto.analytics.AnalyticsManagerInterface
import aeb.proyecto.analytics.events.FirestoreEvents
import aeb.proyecto.firestore.errors.treatError
import aeb.proyecto.firestore.model.FirestoreData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Core infrastructure orchestrator implementing [FirestoreInterface] to manage NoSQL remote storage operations.
 *
 * This component acts as the definitive bridge between upstream domain requests and the cloud database,
 * while automatically dispatching relevant telemetry trackers to the analytics layer to monitor
 * user sync interaction patterns.
 *
 * @property firestore The global centralized remote database gateway instance.
 * @property analyticsManagerInterface The telemetry recording channel wrapper used to track storage interaction events.
 */
class FirestoreManager @Inject constructor(
    firestore: FirebaseFirestore,
    private val analyticsManagerInterface: AnalyticsManagerInterface
): FirestoreInterface {

    private val collection = firestore.collection("Habits")

    /**
     * Establishes an asynchronous execution pipeline to fetch the target user's habit data structure.
     *
     * This implementation utilizes the play-services coroutines await extension to transform
     * Firebase's task-based callbacks into sequential, non-blocking suspension points.
     *
     * @param userId The unique authenticated database key reference identifying the client document.
     * @return A reactive cold [Flow] broadcasting the wrapped [AuthResponseFirestore] structural pipeline phases.
     */
    override suspend fun getDataUser(userId: String): Flow<AuthResponseFirestore> = flow {
        // 1. Emit loading state immediately to the UI
        emit(AuthResponseFirestore.Loading)

        try {
            // 2. Await the single-fetch document snapshot sequentially
            val snapshot = collection.document(userId).get().await()

            analyticsManagerInterface.logEvent(FirestoreEvents.getDataUser(userId))

            // 3. Deserialize and emit success
            val dataModel = snapshot.toObject(FirestoreData::class.java)
            emit(AuthResponseFirestore.Success(dataModel))

        } catch (e: Exception) {
            // 4. Any network, permission, or internal failure triggers this block perfectly
            analyticsManagerInterface.logEvent(FirestoreEvents.error(e.message.toString()))

            val errorCodeStringRes = treatError(e)
            emit(AuthResponseFirestore.Error(errorCodeStringRes))
        }
    }

    /**
     * Commits or overrides a cloud database entry matching the target client's habit schema payload.
     *
     * This implementation utilizes the non-blocking suspension framework via play-services await
     * extension to safely write documents while capturing standard execution exceptions inline.
     *
     * @param data The structured [FirestoreData] DTO payload to serialize into the remote document.
     * @param userId The unique authenticated client database identifier token targeting the write destination.
     * @return A reactive cold [Flow] broadcasting the wrapped [AuthResponseFirestore] structural pipeline phases.
     */
    override suspend fun saveDataUser(data: FirestoreData, userId: String): Flow<AuthResponseFirestore> = flow {
        // 1. Trigger the immediate loading signal to notify the UI transaction states
        emit(AuthResponseFirestore.Loading)

        try {
            // 2. Execute the remote network write point sequentially
            collection.document(userId).set(data).await()

            analyticsManagerInterface.logEvent(FirestoreEvents.saveDataUser(userId))

            // 3. Emit the seamless success token to confirm serialization completion
            emit(AuthResponseFirestore.Success(null))

        } catch (e: Exception) {
            // 4. Intercept potential permission blocks or network dropouts instantly
            analyticsManagerInterface.logEvent(FirestoreEvents.error(e.message.toString()))

            val errorCodeStringRes = treatError(e)
            emit(AuthResponseFirestore.Error(errorCodeStringRes))
        }
    }

    /**
     * Truncates or deletes the specified client tracking data tree completely from the cloud storage engine.
     *
     * This implementation utilizes the non-blocking suspension framework via play-services await
     * extension to safely purge documents while capturing standard execution exceptions inline.
     *
     * @param userId The unique authenticated client database identifier token targeting the destruction zone.
     * @return A reactive cold [Flow] broadcasting the wrapped [AuthResponseFirestore] structural pipeline phases.
     */
    override suspend fun deleteDataUser(userId: String): Flow<AuthResponseFirestore> = flow {
        // 1. Trigger the immediate loading signal to notify the UI transaction states
        emit(AuthResponseFirestore.Loading)

        try {
            // 2. Execute the remote network deletion point sequentially
            collection.document(userId).delete().await()

            analyticsManagerInterface.logEvent(FirestoreEvents.deleteDataUser(userId))

            // 3. Emit the seamless success token to confirm document removal
            emit(AuthResponseFirestore.Success(null))

        } catch (e: Exception) {
            // 4. Intercept potential permission blocks or network dropouts instantly
            analyticsManagerInterface.logEvent(FirestoreEvents.error(e.message.toString()))

            val errorCodeStringRes = treatError(e)
            emit(AuthResponseFirestore.Error(errorCodeStringRes))
        }
    }

}

/**
 * Sealed domain layout pattern architectural wrapper modeling the finite execution state
 * topologies returned by cloud database worker streams.
 *
 * Upstream UI layer models can seamlessly process notifications natively inside stateful
 * pattern-matching blocks (e.g., Kotlin 'when' expressions).
 */
interface AuthResponseFirestore {
    /**
     * Signals a successful operations boundary resolution, encapsulating the retrieved
     * remote cloud model snapshot.
     *
     * @property data The downloaded [FirestoreData] DTO payload object, or null if the target document is blank.
     */
    data class Success(val data: FirestoreData?) : AuthResponseFirestore

    /**
     * Signals an unhandled exception intercept or localized transaction failure condition.
     *
     * @property message An explicit [Int] layout resource reference token pointing to the localized error string.
     */
    data class Error(val message: Int) : AuthResponseFirestore

    /**
     * Signals an active, non-blocking background connection network pipeline task sequence execution.
     */
    data object Loading : AuthResponseFirestore
}