package aeb.proyecto.habittracker.ui.screens.addHabit

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.data.entities.Habit
import aeb.proyecto.habittracker.data.entities.HabitWithDailyHabit
import aeb.proyecto.habittracker.data.entities.HabitWithNotification
import aeb.proyecto.habittracker.data.entities.Notification
import aeb.proyecto.habittracker.data.model.state.AddHabitScreenState
import aeb.proyecto.habittracker.data.repo.HabitRepo
import aeb.proyecto.habittracker.data.repo.HabitWithNotificacionRepo
import aeb.proyecto.habittracker.data.repo.NotificationRepo
import aeb.proyecto.habittracker.ui.components.dailyHabit.iconByName
import aeb.proyecto.habittracker.utils.Constans
import aeb.proyecto.habittracker.utils.Constans.ListColors
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ListItemColors
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddHabitViewModel @Inject constructor(
    private val habitWithNotificacionRepo: HabitWithNotificacionRepo,
    private val habitRepo: HabitRepo,
) : ViewModel() {

    private val _habit: MutableStateFlow<HabitWithNotification> =
        MutableStateFlow(HabitWithNotification())
    val habit: StateFlow<HabitWithNotification> = _habit.asStateFlow()

    private val _uiState: MutableStateFlow<AddHabitScreenState> =
        MutableStateFlow(AddHabitScreenState())
    val uiState: StateFlow<AddHabitScreenState> = _uiState.asStateFlow()

    private val _notificationSelected: MutableState<Notification?> = mutableStateOf(null)

    fun procesateHabit(
        name: String,
        description: String?,
        times: String,
        edit: Boolean,
        done: () -> Unit
    ) = viewModelScope.launch(Dispatchers.IO) {
        val habitUpt = _habit.value

        // Actualizar los campos del hábito
        habitUpt.habit = habitUpt.habit.copy(
            name = name,
            description = description,
            times = times.toInt(),
            unit = uiState.value.unitPicked.id,
            color = uiState.value.color.toArgb(),
            icon = uiState.value.icon.name.split(".")[1]
        )

        habitUpt.notifications = _habit.value.notifications.map {
            it.copy(habitId = habitUpt.habit.id)
        }.toMutableList()

        if (!edit)
            habitWithNotificacionRepo.insertaHabit(habitUpt.habit, habitUpt.notifications)
        else {
            habitWithNotificacionRepo.updateHabit(habitUpt.habit, habitUpt.notifications)
        }

        withContext(Dispatchers.Main) {
            done()
        }
    }

    fun insertNotification(notification: Notification) {
        if (_notificationSelected.value == null) {

            if (habit.value.notifications.find { item -> item.hour == notification.hour && item.minute == notification.minute } == null) {
                val notifications = habit.value.notifications
                notifications.add(notification)
                _habit.value = habit.value.copy(notifications = notifications)
            } else {
                _uiState.update {
                    it.copy(
                        attentionText = R.string.general_dx_attention_subtitile_time_picker,
                        showGeneralDx = true
                    )
                }
            }
        } else {
            val notifications = habit.value.notifications
            notifications.map {
                if (it.hour == _notificationSelected.value!!.hour && it.minute == _notificationSelected.value!!.minute) {
                    it.hour = notification.hour
                    it.minute = notification.minute
                }
                _habit.value = habit.value.copy(notifications = notifications)
            }
        }
    }

    fun deleteNotificacion(notification: Notification) {
        val notifications = habit.value.notifications.filter { it != notification }
        _habit.value = habit.value.copy(notifications = notifications.toMutableList())
    }

    fun setData(color: Int,icon:String,unit:Int){
        setColor(Color(color))
        setIcon(icon)
        setUnit(unit)
    }

    fun setColor(color: Color) {
        _uiState.update { currentState ->
            currentState.copy(color = color)
        }
    }

    fun setUnit(id: Int) {
        _uiState.update { currentState ->
            currentState.copy(unitPicked = Constans.Units.entries.find { it.id == id }!!)
        }
    }

    fun setUnit(unit: Constans.Units) {
        _uiState.update { currentState ->
            currentState.copy(unitPicked = unit)
        }
    }

    fun getHabit(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _habit.value = habitWithNotificacionRepo.getHabitById(id)
        }
    }

    fun openColor() {
        _uiState.update { currentState ->
            currentState.copy(colorSelected = !currentState.colorSelected, iconSelected = false)
        }
    }

    fun closeColor(color: Color) {
        setColor(color)

        _uiState.update { currentState ->
            currentState.copy(colorSelected = false)
        }
    }

    fun openIcon() {
        _uiState.update { currentState ->
            currentState.copy(colorSelected = false, iconSelected = !currentState.iconSelected)
        }
    }

    fun closeIcon(icon: ImageVector) {
        _uiState.update { currentState ->
            currentState.copy(iconSelected = false, icon = icon)
        }
    }

    fun closeBottomSheet() {
        _uiState.update { currentState ->
            currentState.copy(showBottomSheet = false)
        }
    }

    fun closeGeneralDx() {
        _uiState.update { currentState ->
            currentState.copy(showGeneralDx = false)
        }
    }

    fun openBottomSheet() {
        _uiState.update { currentState ->
            currentState.copy(showBottomSheet = true)
        }
    }

    fun openShowTimePicker(notification: Notification? = null) {
        _notificationSelected.value = notification

        _uiState.update { currentState ->
            currentState.copy(showTimePicker = true)
        }
    }

    fun closeShowTimePicker() {
        _uiState.update { currentState ->
            currentState.copy(showTimePicker = false)
        }
    }

    fun getNotification(): Notification? {
        return _notificationSelected.value
    }

    fun openShowGeneralDx() {
        _uiState.update { currentState ->
            currentState.copy(showGeneralDx = true)
        }

    }

    fun setText() {
        _uiState.update { currentState ->
            currentState.copy(
                attentionText = R.string.general_dx_attention_fill_data,
                showGeneralDx = true
            )
        }
    }

    fun setIcon(icon: String) {
        _uiState.update { currentState ->
            currentState.copy(icon = iconByName(icon))
        }
    }
}