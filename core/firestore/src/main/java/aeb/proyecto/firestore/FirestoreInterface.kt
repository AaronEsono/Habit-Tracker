package aeb.proyecto.firestore

import aeb.proyecto.firestore.model.FirestoreData
import kotlinx.coroutines.flow.Flow

interface FirestoreInterface {

    suspend fun getDataUser(userId: String): Flow<AuthResponseFirestore>

    suspend fun saveDataUser(data: FirestoreData, userId: String): Flow<AuthResponseFirestore>

    suspend fun deleteDataUser(userId: String): Flow<AuthResponseFirestore>

}