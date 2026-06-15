package aeb.proyecto.save.model

import java.time.LocalDateTime


/**
 * Holds the view-state data for the main Synchronization screen.
 * * @property name The identifier of the current user, used for personalization.
 * @property localDateTime The timestamp of the last successful synchronization operation.
 */
data class DataSaveScreen(
    var name:String? = null,
    val localDateTime: LocalDateTime? = null
)