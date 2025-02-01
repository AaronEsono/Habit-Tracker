package aeb.proyecto.firestore

import aeb.proyecto.firestore.model.FirestoreData
import kotlinx.coroutines.flow.Flow

interface FirestoreInterface {

    fun getDataUser(userId: String): Flow<AuthResponseFirestore>

    fun saveDataUser(data: FirestoreData, userId: String): Flow<AuthResponseFirestore>

    fun deleteDataUser(userId: String): Flow<AuthResponseFirestore>
}