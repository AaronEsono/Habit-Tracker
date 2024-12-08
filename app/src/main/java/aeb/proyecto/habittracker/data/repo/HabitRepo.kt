package aeb.proyecto.habittracker.data.repo

import aeb.proyecto.habittracker.data.dao.HabitDao
import javax.inject.Inject
import javax.inject.Singleton

class HabitRepo @Inject constructor(
    private val habitDao: HabitDao
){

}