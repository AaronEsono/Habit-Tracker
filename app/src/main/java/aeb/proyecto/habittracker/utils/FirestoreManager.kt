package aeb.proyecto.habittracker.utils

import aeb.proyecto.habittracker.data.model.data.FirestoreData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreManager @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    val collection = firestore.collection("Habits")

    fun getData(userId: String): Flow<AuthResponseGetData> = callbackFlow  {
        collection.document(userId).get().addOnSuccessListener {
            trySend(AuthResponseGetData.Success(it.toObject(FirestoreData::class.java)))
        }.addOnFailureListener {
            trySend(AuthResponseGetData.Error(it.message.toString()))
        }
        awaitClose()
    }

    fun saveData(data: FirestoreData, userId: String): Flow<AuthResponse> = callbackFlow {
        collection.document(userId).set(data).addOnSuccessListener {
            trySend(AuthResponse.Success)
        }.addOnFailureListener {
            trySend(AuthResponse.Error(it.message.toString()))
        }
        awaitClose()
    }

    fun deleteData(userId: String): Flow<AuthResponse> = callbackFlow {
        collection.document(userId).delete().addOnSuccessListener {
            trySend(AuthResponse.Success)
        }.addOnFailureListener {
            trySend(AuthResponse.Error(it.message.toString()))
        }
        awaitClose()
    }

}

interface AuthResponseGetData {
    data class Success(val data: FirestoreData?) : AuthResponseGetData
    data class Error(val message: String) : AuthResponseGetData
}