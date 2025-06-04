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
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreManager @Inject constructor(
    firestore: FirebaseFirestore,
    private val analyticsManagerInterface: AnalyticsManagerInterface
): FirestoreInterface {

    private val collection = firestore.collection("Habits")

    override suspend fun getDataUser(userId: String): Flow<AuthResponseFirestore> = callbackFlow {
        trySend(AuthResponseFirestore.Loading)

        try {
            collection.document(userId).get()
                .addOnSuccessListener { data ->
                    analyticsManagerInterface.logEvent(FirestoreEvents.getDataUser(userId))
                    trySend(AuthResponseFirestore.Success(data.toObject(FirestoreData::class.java)))
                }
        } catch (e: Exception) {
            analyticsManagerInterface.logEvent(FirestoreEvents.Error(e.message.toString()))

            val message = treatError(e)
            trySend(AuthResponseFirestore.Error(message))
        }

        awaitClose()
    }

    override suspend fun saveDataUser(data: FirestoreData, userId: String): Flow<AuthResponseFirestore> = callbackFlow {
        trySend(AuthResponseFirestore.Loading)

        try {
            collection.document(userId).set(data)
                .addOnSuccessListener {
                    analyticsManagerInterface.logEvent(FirestoreEvents.saveDataUser(userId))
                    trySend(AuthResponseFirestore.Success(null))
                }
        }catch (e:Exception){
            analyticsManagerInterface.logEvent(FirestoreEvents.Error(e.message.toString()))
            val message = treatError(e)
            trySend(AuthResponseFirestore.Error(message))
        }

        awaitClose()
    }

    override suspend fun deleteDataUser(userId: String): Flow<AuthResponseFirestore> = callbackFlow {
        trySend(AuthResponseFirestore.Loading)

        try {
            collection.document(userId).delete()
                .addOnSuccessListener {
                    analyticsManagerInterface.logEvent(FirestoreEvents.deleteDataUser(userId))
                    trySend(AuthResponseFirestore.Success(null))
                }
        }catch (e:Exception){
            analyticsManagerInterface.logEvent(FirestoreEvents.Error(e.message.toString()))
            val message = treatError(e)
            trySend(AuthResponseFirestore.Error(message))
        }

        awaitClose()
    }

}

interface AuthResponseFirestore {
    data class Success(val data: FirestoreData?) : AuthResponseFirestore
    data class Error(val message: Int) : AuthResponseFirestore
    data object Loading : AuthResponseFirestore
}