package aeb.proyecto.domain.usecase.main

import aeb.proyecto.datastore.DatastoreInterface
import aeb.proyecto.room.repository.HabitWithDailyHabitRepo
import javax.inject.Inject

/**
 * Use case responsible for managing the onboarding screen state.
 *
 * Provides access to the current onboarding screen state and allows
 * updating whether the onboarding screen has been completed.
 *
 * @property datastoreInterface Data store interface used to persist and retrieve
 * the onboarding screen state.
 */
class ManageOnboardingScreenUseCase @Inject constructor(
    private val datastoreInterface: DatastoreInterface
) {

    /**
     * Observes whether the onboarding screen should be shown.
     *
     * Emits `true` when the onboarding screen is enabled,
     * or `false` when it should not be shown.
     */
    val showOnboardingScreen = datastoreInterface.showOnboardScreen

    /**
     * Updates the onboarding screen state.
     *
     * @param showOnboardScreen `true` to mark the onboarding screen as completed,
     * or `false` to mark it as incomplete.
     */
    suspend fun setShowOnboardingScreen(showOnboardScreen: Boolean){
        datastoreInterface.setShowOnboardScreen(showOnboardScreen)
    }

}