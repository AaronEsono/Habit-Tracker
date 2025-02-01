package aeb.proyecto.firestore

import aeb.proyecto.firestore.model.FirestoreData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class FirestoreManager @Inject constructor(
    firestore: FirebaseFirestore
): FirestoreInterface {

    private val collection = firestore.collection("Habits")

    override fun getDataUser(userId: String): Flow<AuthResponseFirestore> = callbackFlow{
        collection.document(userId).get().addOnSuccessListener {
            trySend(AuthResponseFirestore.Success(it.toObject(FirestoreData::class.java)))
        }.addOnFailureListener {
            trySend(AuthResponseFirestore.Error(it.message.toString()))
        }
        awaitClose()
    }

    override fun saveDataUser(data: FirestoreData, userId: String): Flow<AuthResponseFirestore> = callbackFlow{
        collection.document(userId).set(data).addOnSuccessListener {
            trySend(AuthResponseFirestore.Success(null))
        }.addOnFailureListener {
            trySend(AuthResponseFirestore.Error(it.message.toString()))
        }
        awaitClose()
    }

    override fun deleteDataUser(userId: String): Flow<AuthResponseFirestore> = callbackFlow{
        collection.document(userId).delete().addOnSuccessListener {
            trySend(AuthResponseFirestore.Success(null))
        }.addOnFailureListener {
            trySend(AuthResponseFirestore.Error(it.message.toString()))
        }
        awaitClose()
    }

}

interface AuthResponseFirestore {
    data class Success(val data: FirestoreData?) : AuthResponseFirestore
    data class Error(val message: String) : AuthResponseFirestore
}