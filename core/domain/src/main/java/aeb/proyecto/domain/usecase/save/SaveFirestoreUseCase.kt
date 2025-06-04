package aeb.proyecto.domain.usecase.save

import aeb.proyecto.firestore.FirestoreInterface
import aeb.proyecto.firestore.model.FirestoreData
import javax.inject.Inject

class SaveFirestoreUseCase @Inject constructor(
    private val firestoreInterface: FirestoreInterface,
) {

    suspend fun getDataUser(userId: String) = firestoreInterface.getDataUser(userId)

    suspend fun saveDataUser(data: FirestoreData, userId: String) =
        firestoreInterface.saveDataUser(data, userId)

    suspend fun deleteDataUser(userId: String) = firestoreInterface.deleteDataUser(userId)
}