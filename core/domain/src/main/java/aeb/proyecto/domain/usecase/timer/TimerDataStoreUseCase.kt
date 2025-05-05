package aeb.proyecto.domain.usecase.timer

import aeb.proyecto.datastore.DatastoreInterface
import javax.inject.Inject

class TimerDataStoreUseCase @Inject constructor(
    private val datastoreInterface: DatastoreInterface
) {
    suspend fun saveHourWheelTimer(hour: Int) {
        datastoreInterface.setHourWheelTimer(hour)
    }

    suspend fun saveMinuteWheelTimer(minute: Int) {
        datastoreInterface.setMinuteWheelTimer(minute)
    }

    suspend fun saveSecondWheelTimer(second: Int) {
        datastoreInterface.setSecondWheelTimer(second)
    }

    suspend fun saveTypeButtonTimer(value:Int){
        datastoreInterface.setTypeTimerSelected(value)
    }

}