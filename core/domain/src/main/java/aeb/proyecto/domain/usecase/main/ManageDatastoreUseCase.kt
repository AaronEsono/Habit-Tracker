package aeb.proyecto.domain.usecase.main

import aeb.proyecto.datastore.DatastoreInterface
import aeb.proyecto.datastore.model.AppSettings
import javax.inject.Inject

/**
 * Root Domain Use Case engineered to govern global application state parameters and structural
 * setup profiles directly linked to the application's main runtime launch window ([MainActivity]).
 *
 * Exposes continuous reactive streams to drive application-wide styling matrices and manages
 * configuration transactions across application boot cycles.
 *
 * @property datastoreInterface The abstracted data-layer preference storage contract.
 */
class ManageDatastoreUseCase @Inject constructor(
    private val datastoreInterface: DatastoreInterface
) {

    /**
     * Continuous reactive data pipeline streaming the active user interface theme configuration selection.
     * Feeds the root composition layout tree to trigger seamless visual runtime changes.
     */
    val themeMode = datastoreInterface.themeMode

    /**
     * Resolves the complete global application settings profile blueprint from local storage.
     *
     * @return The current [AppSettings] domain specification model state.
     */
    suspend fun getAppSettings() = datastoreInterface.getAppSettings()

    /**
     * Commits a modified global application settings profile blueprint into the local persistence layer.
     *
     * @param data The updated [AppSettings] structural domain specification token.
     */
    suspend fun saveSettingsApp(data: AppSettings) = datastoreInterface.setAppSettings(data)

    /**
     * Dismisses and resets the global persistent dialog flags tracking active or finished linked habits.
     * Wipes state variables to clean up interceptor overlays on the view hierarchy.
     */
    suspend fun closeDialog() = datastoreInterface.setIsLinkedHabitAndFinished(false)

}