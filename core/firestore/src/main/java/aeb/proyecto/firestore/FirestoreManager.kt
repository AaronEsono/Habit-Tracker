package aeb.proyecto.firestore

import aeb.proyecto.analytics.AnalyticsManagerInterface
import aeb.proyecto.analytics.events.FirestoreEvents
import aeb.proyecto.firestore.errors.treatError
import aeb.proyecto.firestore.model.FirestoreData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreManager @Inject constructor(
    firestore: FirebaseFirestore,
    private val analyticsManagerInterface: AnalyticsManagerInterface
): FirestoreInterface {

    private val collection = firestore.collection("Habits")

    override suspend fun getDataUser(userId: String):AuthResponseFirestore {
        return try {
            val document = collection.document(userId).get().await()

            analyticsManagerInterface.logEvent(FirestoreEvents.getDataUser(userId))
            AuthResponseFirestore.Success(document.toObject(FirestoreData::class.java))

        }catch (e:Exception){
            analyticsManagerInterface.logEvent(FirestoreEvents.Error(e.message.toString()))

            val message = treatError(e)
            AuthResponseFirestore.Error(message)
        }
    }

    override suspend fun saveDataUser(data: FirestoreData, userId: String): AuthResponseFirestore {
        return try {
            collection.document(userId).set(data).await()

            analyticsManagerInterface.logEvent(FirestoreEvents.saveDataUser(userId))
            AuthResponseFirestore.Success(null)
        }catch (e:Exception){
            analyticsManagerInterface.logEvent(FirestoreEvents.Error(e.message.toString()))

            val message = treatError(e)
            AuthResponseFirestore.Error(message)
        }
    }

    override suspend fun deleteDataUser(userId: String): AuthResponseFirestore {
        return try {
            collection.document(userId).delete().await()

            analyticsManagerInterface.logEvent(FirestoreEvents.deleteDataUser(userId))
            AuthResponseFirestore.Success(null)
        }catch (e:Exception){
            analyticsManagerInterface.logEvent(FirestoreEvents.Error(e.message.toString()))

            val message = treatError(e)
            AuthResponseFirestore.Error(message)
        }
    }

}

interface AuthResponseFirestore {
    data class Success(val data: FirestoreData?) : AuthResponseFirestore
    data class Error(val message: Int) : AuthResponseFirestore
}