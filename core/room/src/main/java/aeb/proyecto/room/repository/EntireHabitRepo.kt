package aeb.proyecto.room.repository

import aeb.proyecto.room.dao.EntireHabitDao
import aeb.proyecto.room.entities.relations.EntireHabit
import aeb.proyecto.room.model.NotificationWithNameAndColor
import aeb.proyecto.room.utils.decompressJson
import aeb.proyecto.room.utils.decompressJsonFirestore
import aeb.proyecto.room.utils.jsonCompressed
import android.util.Log
import javax.inject.Inject

/**
 * Repository pattern implementation coordinating external network synchronization workflows
 * for the aggregate habit database graph.
 *
 * This layer abstracts data compression pipelines (GZIP + Base64) and serialization engines (Gson),
 * acting as a clean facade that exposes direct cloud-ready payloads to background sync workers.
 *
 * @property completeDaoHabit Core relational Data Access Object executing atomic SQLite operations.
 */
class EntireHabitRepo @Inject constructor(
    private val completeDaoHabit: EntireHabitDao
) {

    /**
     * Extracts the complete localized relational snapshot from the database, maps it onto
     * serialization-safe data transport objects, and collapses the timeline graph into a highly
     * compressed Base64 payload.
     *
     * Targeted directly at massive single-transaction upstream writes (e.g., Cloud Firestore sync).
     *
     * @return A compressed, web-safe Base64 [String] payload representing the global database state.
     */
    fun getAll():String{
        val habits = completeDaoHabit.getAll()
        return jsonCompressed(habits)
    }

    /**
     * Decodes, inflates, and deserializes a compressed upstream string payload back into a rich relational
     * entity graph, then overrides the localized cache atomically within a single SQLite transaction block.
     *
     * @param data The compressed Base64 structural string retrieved from cloud infrastructure.
     * @return A filtered projection [List] of active alert profiles utilized to rebuild device notification schedules.
     */
    fun setData(data:String):List<NotificationWithNameAndColor>{
        val habits = decompressJsonFirestore(data)
        return completeDaoHabit.setData(habits)
    }
}