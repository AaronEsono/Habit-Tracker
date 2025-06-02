package aeb.proyecto.domain.usecase.settings

import aeb.proyecto.datastore.DatastoreInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.DayOfWeek
import javax.inject.Inject

class DataSettingsUseCase @Inject constructor(
    private val datastoreInterface: DatastoreInterface,
) {
    val dataSettings: Flow<SettingsData> = combine(
        datastoreInterface.themeMode,
        datastoreInterface.language,
        datastoreInterface.dayOfWeek
    ){ theme, language, dayOfWeek ->
        SettingsData(
            language = language,
            theme = theme,
            dayOfWeek = dayOfWeek
        )
    }
}

data class SettingsData(
    val language:String,
    val theme:Int,
    val dayOfWeek: String
)