package aeb.proyecto.domain.usecase.addHabit

import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.relations.HabitWithNotification
import aeb.proyecto.room.repository.HabitWithNotificacionRepo
import android.app.Notification
import javax.inject.Inject

/**
 * Domain Use Case executing the baseline data lifecycle transactions needed during the
 * creation, retrieval, and structural update sequences of habits paired with notifications.
 *
 * Coordinates direct interactions with the local Room infrastructure layer through abstract repo contracts.
 *
 * @property habitWithNotificacionRepo The boundary repository contract managing habit-notification entity relationships.
 */
class RoomRepositoryAddHabitUseCase @Inject constructor(
    private val habitWithNotificacionRepo: HabitWithNotificacionRepo,
) {

    /**
     * Resolves a single notification entry filtered by its unique identifier marker.
     *
     * @param id The unique database key identifier of the targeted notification.
     * @return A reactive data stream pipeline wrapping the specific notification entity.
     */
    fun getNotificationsById(id:Long) = habitWithNotificacionRepo.getNotificationById(id)

    /**
     * Retrieves all notifications associated with a parent habit container ID.
     *
     * @param id The unique database key identifier of the parent habit.
     * @return A reactive data stream list containing all linked notification entities.
     */
    fun getAllNotifications(id:Long) = habitWithNotificacionRepo.getAllNotificationsWithId(id)

    /**
     * Resolves the baseline habit attributes filtered by its unique database identifier.
     *
     * @param id The unique database key identifier of the habit.
     * @return A reactive data stream wrapping the specific structural habit entity.
     */
    fun getHabitById(id:Long) = habitWithNotificacionRepo.getHabitById(id)

    /**
     * Persists a newly configured habit structure along with its notification payload matrix into local storage.
     *
     * @param habit The unified composition domain model representing the habit and its alerts.
     */
    fun insertHabit(habit: HabitWithNotification) = habitWithNotificacionRepo.insertHabit(habit)

    /**
     * Mutates and updates an existing notification's structural attributes inside the persistence pipeline.
     *
     * @param notification The updated unified composition model containing the modified alert metrics.
     */
    fun updateNotification(notification: HabitWithNotification) = habitWithNotificacionRepo.updateHabit(notification)
}