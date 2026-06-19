package aeb.proyecto.timer.components.common.typeTimer.intervalSegmented.model

/**
 * Enumeration for internal identification of picker states.
 * Provides a numeric identifier for easier database or logic mapping.
 */
enum class TypePickState (val value:Int){
    WORK_TIME(1),
    REST_TIME(2)
}