package aeb.proyecto.habittracker.ui.screens.addHabit

import aeb.proyecto.habittracker.data.entities.Habit
import aeb.proyecto.habittracker.data.entities.Notification
import aeb.proyecto.habittracker.data.repo.HabitRepo
import aeb.proyecto.habittracker.data.repo.NotificationRepo
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddHabitViewModel @Inject constructor(
    private val notificationRepo: NotificationRepo,
    private val habitRepo: HabitRepo
) : ViewModel() {


    fun createHabit(habit: Habit, notifications: List<Notification>){

    }

}