package aeb.proyecto.domain.usecase.main

import aeb.proyecto.datastore.DatastoreInterface
import javax.inject.Inject

class ManageDatastoreUseCase @Inject constructor(
    private val datastoreInterface: DatastoreInterface
) {
    val themeMode = datastoreInterface.themeMode

    suspend fun getDayOfWeek() = datastoreInterface.getDayStartWeek()

    suspend fun setDayOfWeek(day:String) = datastoreInterface.setDayStartWeek(day)

    suspend fun getLanguage() = datastoreInterface.getLanguage()

    suspend fun setLanguage(language:String) = datastoreInterface.setLanguage(language)

    suspend fun closeDialog() = datastoreInterface.setIsLinkedHabitAndFinished(false)

}