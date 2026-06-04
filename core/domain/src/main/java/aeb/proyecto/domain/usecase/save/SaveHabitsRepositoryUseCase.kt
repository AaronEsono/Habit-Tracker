package aeb.proyecto.domain.usecase.save

import aeb.proyecto.room.model.NotificationWithNameAndColor
import aeb.proyecto.room.repository.EntireHabitRepo
import javax.inject.Inject

/**
 * Domain Use Case executing structural backup extractions and bulk data hydration transactions
 * over the local persistence database tree.
 *
 * Facilitates the localized import/export data mapping loops required to sync local Room data
 * with cloud-bound JSON storage payloads.
 *
 * @property entireHabitRepo The comprehensive repository contract handling mass database state updates.
 */
class SaveHabitsRepositoryUseCase @Inject constructor(
    private val entireHabitRepo: EntireHabitRepo,
){

    /**
     * Extrae un snapshot masivo e integral de todas las tablas y entidades relacionales de la base de datos local
     * listo para ser empaquetado y subido a los servidores de sincronización.
     *
     * @return Una colección o flujo conteniendo la estructura unificada de la persistencia local.
     */
    fun getAll() = entireHabitRepo.getAll()

    /**
     * Sobreescribe e inyecta una cadena de datos serializada masiva directamente en el motor de persistencia local.
     * Recompila y devuelve la matriz de notificaciones activas para que el sistema operativo pueda volver a
     * programar los disparadores de alarma físicos del hardware.
     *
     * @param data La cadena de texto cruda (JSON/Mapeo) que contiene el snapshot de la copia de seguridad.
     * @return Una lista de entidades de notificación enriquecidas listas para ser vinculadas al AlarmManager.
     */
    fun setData(data:String):List<NotificationWithNameAndColor> = entireHabitRepo.setData(data)
}