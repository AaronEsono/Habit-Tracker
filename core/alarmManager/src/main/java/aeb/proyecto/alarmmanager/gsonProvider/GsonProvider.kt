package aeb.proyecto.alarmmanager.gsonProvider

import aeb.proyecto.alarmmanager.converters.LocalTimeAdapter
import aeb.proyecto.alarmmanager.converters.TypeNotificationAdapter
import aeb.proyecto.room.model.classes.TypeNotification
import androidx.compose.ui.graphics.vector.ImageVector
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.time.LocalTime

/**
 * A centralized, immutable configuration provider that governs the JSON serialization engine
 * for the alarm manager ecosystem.
 *
 * This singleton object uses a thread-safe initialization model to build and expose a unified [Gson]
 * mapper instance. It pre-registers custom platform type adapters—specifically [LocalTimeAdapter] for
 * compliant clock structures and [TypeNotificationAdapter] for abstract polymorphic scheduling boundaries—ensuring
 * total consistency across local preference stores, intent payloads, or internal caches.
 */
object GsonProvider {

    /**
     * The globally configured, immutable [Gson] instance embedded with module-specific type adapters.
     */
    val gson: Gson = GsonBuilder()
        .registerTypeAdapter(LocalTime::class.java, LocalTimeAdapter())
        .registerTypeAdapter(TypeNotification::class.java, TypeNotificationAdapter())
        .create()
}