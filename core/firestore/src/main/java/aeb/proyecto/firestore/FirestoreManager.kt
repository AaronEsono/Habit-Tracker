package aeb.proyecto.firestore

import aeb.proyecto.firestore.model.FirestoreData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreManager @Inject constructor(
    firestore: FirebaseFirestore
): FirestoreInterface {

    private val collection = firestore.collection("Habits")

    override suspend fun getDataUser(userId: String):AuthResponseFirestore {
        return try {
            val document = collection.document(userId).get().await()
            AuthResponseFirestore.Success(document.toObject(FirestoreData::class.java))
        }catch (e:Exception){
            AuthResponseFirestore.Error(e.message.toString())
        }
    }

    override suspend fun saveDataUser(data: FirestoreData, userId: String): AuthResponseFirestore {
        return try {
            collection.document(userId).set(data).await()
            AuthResponseFirestore.Success(null)
        }catch (e:Exception){
            AuthResponseFirestore.Error(e.message.toString())
        }
    }

    override suspend fun deleteDataUser(userId: String): AuthResponseFirestore {
        return try {
            collection.document(userId).delete().await()
            AuthResponseFirestore.Success(null)
        }catch (e:Exception){
            AuthResponseFirestore.Error(e.message.toString())
        }
    }

}

interface AuthResponseFirestore {
    data class Success(val data: FirestoreData?) : AuthResponseFirestore
    data class Error(val message: String) : AuthResponseFirestore
}