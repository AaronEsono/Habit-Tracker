package aeb.proyecto.habittracker.ui.screens.addHabit

import aeb.proyecto.habittracker.data.entities.Habit
import aeb.proyecto.habittracker.data.entities.Notification
import aeb.proyecto.habittracker.data.repo.HabitRepo
import aeb.proyecto.habittracker.data.repo.HabitWithNotificacionRepo
import aeb.proyecto.habittracker.data.repo.NotificationRepo
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddHabitViewModel @Inject constructor(
    private val habitWithNotificacionRepo: HabitWithNotificacionRepo
) : ViewModel() {

    fun createHabit(habit: Habit, notifications: List<Notification>, done:() -> Unit) = viewModelScope.launch(Dispatchers.IO){
        habitWithNotificacionRepo.insertaHabit(habit,notifications)

        withContext(Dispatchers.Main){
            done()
        }
    }

}