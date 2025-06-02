package aeb.proyecto.domain.usecase.settings

import aeb.proyecto.datastore.DatastoreInterface
import javax.inject.Inject

class SetValueDataStoreSettingsUseCase @Inject constructor(
    private val datastoreInterface: DatastoreInterface,
) {
    suspend fun setTheme(themeMode:Int) = datastoreInterface.setModeTheme(themeMode)
    suspend fun setLanguage(language:String) = datastoreInterface.setLanguage(language)
    suspend fun setDaySelected(dayOfWeek: String) = datastoreInterface.setDayStartWeek(dayOfWeek)
}