package aeb.proyecto.domain.usecase.settings

import aeb.proyecto.datastore.DatastoreInterface
import aeb.proyecto.datastore.model.AppSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.DayOfWeek
import javax.inject.Inject

class DataSettingsUseCase @Inject constructor(
    private val datastoreInterface: DatastoreInterface,
) {
    val dataSettings: Flow<AppSettings> = datastoreInterface.appSettings

    suspend fun setAppSettings(appSettings: AppSettings){
        datastoreInterface.setAppSettings(appSettings)
    }

    suspend fun getAppSettings(): AppSettings = datastoreInterface.getAppSettings()
}