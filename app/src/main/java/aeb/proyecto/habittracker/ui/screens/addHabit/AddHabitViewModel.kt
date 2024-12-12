package aeb.proyecto.habittracker.ui.screens.addHabit

import aeb.proyecto.habittracker.data.entities.Habit
import aeb.proyecto.habittracker.data.entities.HabitWithDailyHabit
import aeb.proyecto.habittracker.data.entities.HabitWithNotification
import aeb.proyecto.habittracker.data.entities.Notification
import aeb.proyecto.habittracker.data.repo.HabitRepo
import aeb.proyecto.habittracker.data.repo.HabitWithNotificacionRepo
import aeb.proyecto.habittracker.data.repo.NotificationRepo
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddHabitViewModel @Inject constructor(
    private val habitWithNotificacionRepo: HabitWithNotificacionRepo,
    private val habitRepo: HabitRepo,
) : ViewModel() {

    private val _habit: MutableStateFlow<HabitWithNotification> = MutableStateFlow(HabitWithNotification())
    val habit: StateFlow<HabitWithNotification> = _habit.asStateFlow()


    fun procesateHabit(habitTr: Habit, notifications: List<Notification>,edit:Boolean, done:() -> Unit) = viewModelScope.launch(Dispatchers.IO){
        if(!edit)
            habitWithNotificacionRepo.insertaHabit(habitTr,notifications)
        else{
            val habitUpt = habitTr.copy(id = habit.value.habit.id)
            val notificationsUpt = notifications.map { it.copy(habitId = habit.value.habit.id) }

            habitWithNotificacionRepo.updateHabit(habitUpt,notificationsUpt)
        }

        withContext(Dispatchers.Main){
            done()
        }
    }

    fun getHabit(id:Long){
        viewModelScope.launch(Dispatchers.IO){
            _habit.value = habitWithNotificacionRepo.getHabitById(id)
        }
    }

}