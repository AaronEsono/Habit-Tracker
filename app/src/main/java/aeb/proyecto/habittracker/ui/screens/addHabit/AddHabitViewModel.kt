package aeb.proyecto.habittracker.ui.screens.addHabit

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.data.entities.HabitWithNotification
import aeb.proyecto.habittracker.data.entities.Notification
import aeb.proyecto.habittracker.data.model.state.AddHabitScreenState
import aeb.proyecto.habittracker.data.repo.HabitRepo
import aeb.proyecto.habittracker.data.repo.HabitWithNotificacionRepo
import aeb.proyecto.habittracker.ui.components.dailyHabit.iconByName
import aeb.proyecto.habittracker.utils.Constans
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

    private val _notifications: MutableStateFlow<List<Notification>> =
        MutableStateFlow(emptyList())
    val notifications: StateFlow<List<Notification>> = _notifications.asStateFlow()

    private val _uiState: MutableStateFlow<AddHabitScreenState> =
        MutableStateFlow(AddHabitScreenState())
    val uiState: StateFlow<AddHabitScreenState> = _uiState.asStateFlow()

    private val _notificationSelected: MutableState<Notification?> = mutableStateOf(null)
    private val _notificationsCancel:MutableStateFlow<List<Long>> = MutableStateFlow(emptyList())

    fun procesateHabit(
        name: String,
        description: String?,
        times: String,
        edit: Boolean,
        done: (List<Notification>,List<Long>) -> Unit
    ) = viewModelScope.launch(Dispatchers.IO) {
        val habitUpt = _habit.value
        var id = _habit.value.habit.id

        // Actualizar los campos del hábito
        habitUpt.habit = habitUpt.habit.copy(
            name = name,
            description = if (description.isNullOrEmpty()) null else description,
            times = times.toInt(),
            unit = uiState.value.unitPicked.id,
            color = uiState.value.color.toArgb(),
            icon = uiState.value.icon.name.split(".")[1]
        )

        if (!edit){
            id = habitWithNotificacionRepo.insertaHabit(habitUpt.habit, notifications.value)
        }
        else {
            habitWithNotificacionRepo.updateHabit(habitUpt.habit, notifications.value)
        }

        val notificationsWithId = habitWithNotificacionRepo.getNotificationById(id)

        withContext(Dispatchers.Main) {
            done(notificationsWithId.filter { !_notificationsCancel.value.contains(it.id) },
                _notificationsCancel.value.filter { _notifications.value.find { notification -> notification.id == it } == null })
        }
    }

    fun insertNotification(notification: Notification) {
        if (_notificationSelected.value == null) {
            if (_notifications.value.find { it.hour == notification.hour && it.minute == notification.minute } == null) {
                notification.habitId = _habit.value.habit.id
                _notifications.update { currentState ->
                    currentState + notification
                }
            } else {
                _uiState.update { currentState ->
                    currentState.copy(
                        showGeneralDx = true,
                        attentionText = R.string.general_dx_attention_subtitile_time_picker
                    )
                }
            }
        } else {
            _notifications.update { currentState ->
                currentState.map {
                    if (it == _notificationSelected.value) {
                        it.copy(hour = notification.hour, minute = notification.minute)
                    } else {
                        it
                    }
                }
            }
        }
    }

    fun deleteNotificacion(notification: Notification) {
        _notifications.update { currentState ->
            currentState.filter { it != notification }
        }
    }

    fun setData(color: Int, icon: String, unit: Int) {
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
            _notifications.value = _habit.value.notifications

            _notificationsCancel.value = _notifications.value.map { it.id }
        }
    }

    fun openColor() {
        _uiState.update { currentState ->
            currentState.copy(
                colorSelected = !currentState.colorSelected,
                iconSelected = false
            )
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
            currentState.copy(
                colorSelected = false,
                iconSelected = !currentState.iconSelected
            )
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

    fun setUiState(addHabitScreenState: AddHabitScreenState){
        _uiState.update { currentState ->
            currentState.copy(
                color = addHabitScreenState.color,
                icon = addHabitScreenState.icon,
                unitPicked = addHabitScreenState.unitPicked
            )
        }
    }
}
