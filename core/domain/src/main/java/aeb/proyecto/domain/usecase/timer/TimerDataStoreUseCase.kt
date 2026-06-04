package aeb.proyecto.domain.usecase.timer

import aeb.proyecto.datastore.DatastoreInterface
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import javax.inject.Inject

/**
 * Domain Use Case designed to manage real-time, granular property mutations over the active
 * countdown timer persistent preference slots.
 *
 * Provides a framework-agnostic pipeline to commit wheel positions, interval configurations,
 * and context-binding linkages directly into the local storage engine.
 *
 * @property datastoreInterface The abstracted data-layer preference storage contract handling active timer configurations.
 */
class TimerDataStoreUseCase @Inject constructor(
    private val datastoreInterface: DatastoreInterface
) {

    /**
     * Persiste el componente de horas seleccionado en la rueda principal del temporizador de enfoque.
     */
    suspend fun saveHourWheelTimer(hour: Int) {
        datastoreInterface.setHourWheelTimer(hour)
    }

    /**
     * Persiste el componente de minutos seleccionado en la rueda principal del temporizador de enfoque.
     */
    suspend fun saveMinuteWheelTimer(minute: Int) {
        datastoreInterface.setMinuteWheelTimer(minute)
    }

    /**
     * Persiste el componente de segundos seleccionado en la rueda principal del temporizador de enfoque.
     */
    suspend fun saveSecondWheelTimer(second: Int) {
        datastoreInterface.setSecondWheelTimer(second)
    }

    /**
     * Persiste el componente de horas seleccionado en la rueda de intervalos destinada al tiempo de descanso.
     */
    suspend fun saveRestHourWheelTimer(hour: Int) {
        datastoreInterface.setRestIntervalHourTimer(hour)
    }

    /**
     * Persiste el componente de minutos seleccionado en la rueda de intervalos destinada al tiempo de descanso.
     */
    suspend fun saveRestMinuteWheelTimer(minute: Int) {
        datastoreInterface.setRestIntervalMinuteTimer(minute)
    }

    /**
     * Persiste el componente de segundos seleccionado en la rueda de intervalos destinada al tiempo de descanso.
     */
    suspend fun saveRestSecondWheelTimer(second: Int) {
        datastoreInterface.setRestIntervalSecondTimer(second)
    }

    /**
     * Actualiza el modo operativo del temporizador (ej: 0 para cronómetro, 1 para cuenta atrás, 2 para intervalos).
     *
     * @param value El código entero de enrutamiento que define el comportamiento del sistema de tiempo.
     */
    suspend fun saveTypeButtonTimer(value:Int){
        datastoreInterface.setTypeTimerSelected(value)
    }

    /**
     * Establece el número total de series o ciclos (sets) configurados para las rutinas de intervalos de alta intensidad.
     *
     * @param value Cantidad de iteraciones operacionales deseadas.
     */
    suspend fun setTimer(value:Int){
        datastoreInterface.setNumberSetsTimer(value)
    }

    /**
     * Enlaza atómicamente un hábito específico y una fecha de ejecución al contexto activo del temporizador
     * para asegurar que el progreso acumulado impacte directamente en las estadísticas correctas del día.
     *
     * @param id El identificador único relacional del hábito objetivo.
     * @param date La fecha de calendario que enmarca el bloque de trabajo actual.
     */
    suspend fun setHabitLinked(id:Long, date:LocalDate){
        datastoreInterface.setIdHabitLinkedTimer(id)
        datastoreInterface.setDateHabitLinkedTimer(date.toString())
    }

    /**
     * Rompe el enlace activo de cualquier hábito vinculado al temporizador actual, inyectando un valor centinela (-1)
     * para forzar el comportamiento del cronómetro hacia el modo libre/general.
     */
    suspend fun removeHabitLinked(){
        datastoreInterface.setIdHabitLinkedTimer(-1)
    }

}