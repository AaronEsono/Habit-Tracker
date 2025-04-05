package aeb.proyecto.alarmmanager.gsonProvider

import aeb.proyecto.alarmmanager.converters.LocalTimeAdapter
import aeb.proyecto.alarmmanager.converters.TypeNotificationAdapter
import aeb.proyecto.room.model.classes.TypeNotification
import androidx.compose.ui.graphics.vector.ImageVector
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.time.LocalTime

object GsonProvider {
    val gson: Gson = GsonBuilder()
        .registerTypeAdapter(LocalTime::class.java, LocalTimeAdapter())
        .registerTypeAdapter(TypeNotification::class.java, TypeNotificationAdapter())
        .create()
}