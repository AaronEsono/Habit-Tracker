package aeb.proyecto.room.repository

import aeb.proyecto.room.dao.EntireHabitDao
import aeb.proyecto.room.entities.relations.EntireHabit
import aeb.proyecto.room.model.NotificationWithNameAndColor
import aeb.proyecto.room.utils.decompressJson
import aeb.proyecto.room.utils.decompressJsonFirestore
import aeb.proyecto.room.utils.jsonCompressed
import android.util.Log
import javax.inject.Inject

class EntireHabitRepo @Inject constructor(
    private val completeDaoHabit: EntireHabitDao
) {
    fun getAll():String{
        val habits = completeDaoHabit.getAll()
        return jsonCompressed(habits)
    }

    fun setData(data:String):List<NotificationWithNameAndColor>{
        val habits = decompressJsonFirestore(data)
        return completeDaoHabit.setData(habits)
    }
}