package aeb.proyecto.domain.usecase.settings

import aeb.proyecto.datastore.DatastoreInterface
import aeb.proyecto.datastore.model.AppSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.DayOfWeek
import javax.inject.Inject

/**
 * Domain Use Case designed to manage and expose user-defined application preference blueprints
 * inside the global system settings hierarchy.
 *
 * Bridges the presentation state machinery directly with the persistent preference data pipeline,
 * offering both continuous reactive streaming and high-efficiency atomic lookups.
 *
 * @property datastoreInterface The abstracted data-layer preference storage contract handling settings schemas.
 */
class DataSettingsUseCase @Inject constructor(
    private val datastoreInterface: DatastoreInterface,
) {

    /**
     * Continuous reactive data pipeline streaming the comprehensive, live state of global application settings.
     * Feeds UI composition models to dynamically update layouts, localized calendars, and visual aesthetics.
     */
    val dataSettings: Flow<AppSettings> = datastoreInterface.appSettings

    /**
     * Commits and consolidates a modified global application settings profile blueprint into persistent local disk storage.
     *
     * @param appSettings The updated structural [AppSettings] domain specification token to save.
     */
    suspend fun setAppSettings(appSettings: AppSettings){
        datastoreInterface.setAppSettings(appSettings)
    }

    /**
     * Executes a single, high-efficiency synchronous-bound lookup to resolve the active application settings snapshot.
     * Returns a pure structure unlinked from active stream monitoring loops.
     *
     * @return The current [AppSettings] domain specification model state.
     */
    suspend fun getAppSettings(): AppSettings = datastoreInterface.getAppSettings()
}