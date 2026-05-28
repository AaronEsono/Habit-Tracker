package aeb.proyecto.domain.usecase.main

import aeb.proyecto.datastore.DatastoreInterface
import aeb.proyecto.datastore.model.AppSettings
import javax.inject.Inject

class ManageDatastoreUseCase @Inject constructor(
    private val datastoreInterface: DatastoreInterface
) {
    val themeMode = datastoreInterface.themeMode
    suspend fun getAppSettings() = datastoreInterface.getAppSettings()
    suspend fun saveSettingsApp(data: AppSettings) = datastoreInterface.setAppSettings(data)
    suspend fun closeDialog() = datastoreInterface.setIsLinkedHabitAndFinished(false)

}