package aeb.proyecto.domain.usecase.statistics

import aeb.proyecto.datastore.DatastoreInterface
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHabitSelectedUseCase @Inject constructor(
    private val datastore: DatastoreInterface
) {

    fun getHabitSelected(): Flow<Long?>{
        return datastore.habitSelected
    }

    suspend fun setHabitSelected(id:Long){
        datastore.setHabitSelected(id)
    }

}