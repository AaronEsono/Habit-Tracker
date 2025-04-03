package aeb.proyecto.addhabit

import aeb.proyecto.addhabit.constants.GridOption
import aeb.proyecto.addhabit.constants.GridOptionResult
import aeb.proyecto.addhabit.constants.PICK_NOTIFICATION
import aeb.proyecto.addhabit.constants.TypeHabit
import aeb.proyecto.addhabit.constants.TypeNotificationResult
import aeb.proyecto.addhabit.constants.getContrastColor
import aeb.proyecto.addhabit.converter.fromHabitScreen
import aeb.proyecto.addhabit.converter.fromNotificationScreen
import aeb.proyecto.addhabit.converter.toHabitScreen
import aeb.proyecto.addhabit.model.AddHabit
import aeb.proyecto.addhabit.model.AddHabitNotification
import aeb.proyecto.addhabit.model.BottomSheetState
import aeb.proyecto.addhabit.model.DEFAULT_TIME
import aeb.proyecto.addhabit.model.DataAddHabitScreen
import aeb.proyecto.addhabit.model.DataBottomSheet
import aeb.proyecto.room.model.classes.TypeNotification
import aeb.proyecto.room.model.classes.UnitHabit
import aeb.proyecto.room.repository.HabitWithNotificacionRepo
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class AddHabitViewModel @Inject constructor(
    private val habitWithNotificacionRepo: HabitWithNotificacionRepo
):ViewModel() {

    private val _dataAddHabit = MutableStateFlow(DataAddHabitScreen())
    val dataAddHabit = _dataAddHabit.asStateFlow()

    private val _addHabitUIState = MutableStateFlow<AddHabitUIState>(AddHabitUIState.Success)
    val addHabitUIState = _addHabitUIState.asStateFlow()

    private val _dataSearched = MutableStateFlow(false)

    fun onClickGridOption(gridOptionResult: GridOptionResult){
        when(gridOptionResult){
            is GridOptionResult.colorResult -> setColor(gridOptionResult.color)
            is GridOptionResult.iconResult -> setIcon(gridOptionResult.icon)
        }
    }

    fun onClickCard( gridOption: GridOption){
        when(gridOption){
            GridOption.COLORS -> colorGridState()
            GridOption.ICONS -> iconGridState()
        }
    }

    private fun setColor(color:Color){
        _dataAddHabit.update { currentState ->
            currentState.copy(
                habitScreen = currentState.habitScreen.copy(color = color),
                contrastColor = getContrastColor(color),
                isColorSelected = false
            )
        }
    }

    private fun setIcon(icon: ImageVector){
        _dataAddHabit.update { currentState ->
            currentState.copy(
                habitScreen = currentState.habitScreen.copy(icon = icon),
                isIconSelected = false
            )
        }
    }

    private fun colorGridState(){
        _dataAddHabit.update { currentState ->
            currentState.copy(
                isColorSelected =  !currentState.isColorSelected,
                isIconSelected = false
            )
        }
    }

    private fun iconGridState(){
        _dataAddHabit.update { currentState ->
            currentState.copy(
                isIconSelected =  !currentState.isIconSelected,
                isColorSelected = false
            )
        }
    }

    fun setDialog(typeDialog:Int){
        _dataAddHabit.update { currentState ->
            currentState.copy(
                showDialog = true,
                typeDialog = typeDialog
            )
        }
    }

    fun closeDialog(){
        _dataAddHabit.update { currentState ->
            currentState.copy(
                showDialog = false
            )
        }
    }

    fun onClickTypeHabit(typeHabit: TypeHabit){
        _dataAddHabit.update { currentState ->
            currentState.copy(
                habitScreen = currentState.habitScreen.copy(typeHabit = typeHabit)
            )
        }
    }

    fun onClickWeekly(numberDays:Int){
        _dataAddHabit.update { currentState ->
            currentState.copy(
                habitScreen = currentState.habitScreen.copy(numberOfDaysWeek = numberDays)
            )
        }
    }

    fun monthNumberSelected(numberDays:Int) = viewModelScope.launch(Dispatchers.IO){
        _dataAddHabit.update { currentState ->
            currentState.copy(
                habitScreen = currentState.habitScreen.copy(numberOfDaysMonth = numberDays)
            )
        }
    }

    fun onClickDate(localDate:LocalDate){
        _dataAddHabit.update { currentState->
            currentState.copy(
                habitScreen = currentState.habitScreen.copy(dateRecurringStartDate = localDate)
            )
        }
    }

    fun onPickUnit(unit: UnitHabit){
        _dataAddHabit.update { currentState ->
            currentState.copy(
                habitScreen = currentState.habitScreen.copy(unit = unit)
            )
        }
    }

    fun onClickTypeNotification(typeNotification: TypeNotification){
        _dataAddHabit.update { currentState ->
            currentState.copy(
                notificationSelected = DEFAULT_TIME.copy(type = typeNotification),
                showDialog = true,
                typeDialog = PICK_NOTIFICATION
            )
        }
    }

    fun onTimeSelected(time:LocalTime){
        val idSelected = _dataAddHabit.value.notificationSelected.id

        //Notificacion nueva
        if(idSelected == "-1"){
            val notification = AddHabitNotification(time = time,type = _dataAddHabit.value.notificationSelected.type)

            _dataAddHabit.update { currentState ->
                currentState.copy(
                    habitScreen = currentState.habitScreen.copy(
                        notifications = currentState.habitScreen.notifications + notification
                    )
                )
            }
        }else{
            //Editar notificacion existente
            _dataAddHabit.update { currentState ->
                currentState.copy(
                    habitScreen = currentState.habitScreen.copy(
                        notifications = currentState.habitScreen.notifications.map {
                            if (it.id == idSelected) {
                                it.copy(time = time)
                            } else it
                        }
                    )
                )
            }
        }
    }

    fun onClickDeleteNotification(id:String){
        _dataAddHabit.update { currentState ->
            currentState.copy(
                notificationSelected = AddHabitNotification(id = id),
                bottomSheetState = BottomSheetState(
                    isVisible = true,
                    dataBottomSheet = DataBottomSheet.DELETE_NOTIFICATION
                )
            )
        }
    }

    fun closeBottomSheet(){
        _dataAddHabit.update { currentState ->
            currentState.copy(
                bottomSheetState = currentState.bottomSheetState.copy(isVisible = false)
            )
        }
    }

    fun onAcceptBottomSheet(){
        when(_dataAddHabit.value.bottomSheetState.dataBottomSheet){
            DataBottomSheet.DELETE_NOTIFICATION -> {deleteNotification()}
        }
    }

    private fun deleteNotification(){
        _dataAddHabit.update { currentState ->
            currentState.copy(
                habitScreen = currentState.habitScreen.copy(
                    notifications = currentState.habitScreen.notifications.filter { it.id != _dataAddHabit.value.notificationSelected.id }
                )
            )
        }
    }

    fun onClickTypeNotificationResult(typeNotificationResult: TypeNotificationResult){
        when(typeNotificationResult){
            is TypeNotificationResult.Daily -> { editNotificationDaily(typeNotificationResult) }
            is TypeNotificationResult.Recurring -> { editNotificationRecurring(typeNotificationResult)  }
        }
    }

    private fun editNotificationDaily(editNotification: TypeNotificationResult.Daily) {
        val notification = findNotification(editNotification.id)

        if (notification.type is TypeNotification.Daily) {
            val currentDays = notification.type.days

            if (currentDays.size != 1 || !currentDays.contains(editNotification.day)){

                val updatedDays = currentDays.toMutableList().apply {
                    if (!remove(editNotification.day)) add(editNotification.day)
                }

                _dataAddHabit.update { currentState ->
                    currentState.copy(
                        habitScreen = currentState.habitScreen.copy(
                            notifications = currentState.habitScreen.notifications.map {
                                if (it.id == editNotification.id) {
                                    it.copy(type = TypeNotification.Daily(updatedDays.toMutableList()))
                                } else it
                            }
                        )
                    )
                }
            }
        }
    }

    private fun editNotificationRecurring(editNotification: TypeNotificationResult.Recurring) {
        val notification = findNotification(editNotification.id)

        if (notification.type is TypeNotification.Recurring) {
            val intervalDays = notification.type.interval

            if (editNotification.action || intervalDays != 1) {
                val newInterval = intervalDays + if (editNotification.action) 1 else -1

                _dataAddHabit.update { currentState ->
                    currentState.copy(
                        habitScreen = currentState.habitScreen.copy(
                            notifications = currentState.habitScreen.notifications.map {
                                if (it.id == editNotification.id) {
                                    it.copy(type = TypeNotification.Recurring(newInterval))
                                } else it
                            }
                        )
                    )
                }
            }
        }
    }

    private fun findNotification(id:String):AddHabitNotification{
        return _dataAddHabit.value.habitScreen.notifications.find { it.id == id }!!
    }

    fun onEditNotification(id:String,time:LocalTime){
        _dataAddHabit.update { currentState ->
            currentState.copy(
                notificationSelected = DEFAULT_TIME.copy(id = id,time = time),
                showDialog = true,
                typeDialog = PICK_NOTIFICATION
            )
        }
    }

    fun saveHabit() = viewModelScope.launch(Dispatchers.IO){
        val habitWithNotifications = fromHabitScreen(_dataAddHabit.value.habitScreen)
        habitWithNotificacionRepo.insertHabit(habitWithNotifications.habit, listOf())
    }

    fun getData(id:Long){
        if(!_dataSearched.value && id != -1L){
            viewModelScope.launch (Dispatchers.IO){
                _addHabitUIState.update { AddHabitUIState.Loading }

                val habit = habitWithNotificacionRepo.getHabitById(id)

                _dataAddHabit.update { currentState ->
                    currentState.copy(
                        habitScreen = toHabitScreen(habit),
                    )
                }

                _addHabitUIState.update { AddHabitUIState.Success }
            }
        }
    }

}

sealed class AddHabitUIState{
    data object Success: AddHabitUIState()
    data object Loading: AddHabitUIState()
    data object Error: AddHabitUIState()
}