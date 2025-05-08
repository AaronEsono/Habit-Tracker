package aeb.proyecto.domain.usecase.timer

import aeb.proyecto.datastore.DatastoreInterface
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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

    suspend fun saveRestHourWheelTimer(hour: Int) {
        datastoreInterface.setRestIntervalHourTimer(hour)
    }

    suspend fun saveRestMinuteWheelTimer(minute: Int) {
        datastoreInterface.setRestIntervalMinuteTimer(minute)
    }

    suspend fun saveRestSecondWheelTimer(second: Int) {
        datastoreInterface.setRestIntervalSecondTimer(second)
    }

    suspend fun saveTypeButtonTimer(value:Int){
        datastoreInterface.setTypeTimerSelected(value)
    }

    suspend fun setTimer(value:Int){
        datastoreInterface.setSetsTimer(value)
    }
}