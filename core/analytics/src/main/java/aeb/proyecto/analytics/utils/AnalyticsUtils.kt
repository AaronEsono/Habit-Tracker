package aeb.proyecto.analytics.utils

import java.time.LocalDateTime

/**
 * Generates a standard timestamp representation.
 */
fun getDateTime():String{
    return LocalDateTime.now().toString()
}