package aeb.proyecto.addhabit

import aeb.proyecto.addhabit.constants.GridOption
import aeb.proyecto.addhabit.constants.GridOptionResult
import aeb.proyecto.addhabit.constants.PICK_NOTIFICATION
import aeb.proyecto.addhabit.constants.TypeHabit
import aeb.proyecto.addhabit.constants.TypeNotificationResult
import aeb.proyecto.ui.constants.getContrastColor
import aeb.proyecto.addhabit.converter.fromHabitScreen
import aeb.proyecto.addhabit.converter.toHabitScreen
import aeb.proyecto.addhabit.model.AddHabitNotification
import aeb.proyecto.addhabit.model.BottomSheetState
import aeb.proyecto.addhabit.model.DEFAULT_TIME
import aeb.proyecto.addhabit.model.DataAddHabitScreen
import aeb.proyecto.addhabit.model.DataBottomSheet
import aeb.proyecto.alarmmanager.NotificationUtils
import aeb.proyecto.datastore.DatastoreInterface
import aeb.proyecto.room.model.classes.TIPO_UNIDAD
import aeb.proyecto.room.model.classes.TypeNotification
import aeb.proyecto.room.model.classes.UnitHabit
import aeb.proyecto.room.repository.HabitWithNotificacionRepo
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class AddHabitViewModel @Inject constructor(
    private val habitWithNotificacionRepo: HabitWithNotificacionRepo,
    private val notificationUtils: NotificationUtils,
    private val datastoreInterface: DatastoreInterface
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
        //Si es una unidad de tiempo, quitamos los decimales
        if(unit.unitType == TIPO_UNIDAD.TIEMPO){
            val text = _dataAddHabit.value.habitScreen.numberTimesTextField.text.toString()
            val integerPart = text.substringBefore(".")

            _dataAddHabit.update { currentState ->
                currentState.copy(
                    habitScreen = currentState.habitScreen.copy(
                        numberTimesTextField = TextFieldState(initialText = integerPart)
                    )
                )
            }
        }

        _dataAddHabit.update { currentState ->
            currentState.copy(
                habitScreen = currentState.habitScreen.copy(unit = unit)
            )
        }
    }

    fun onClickTypeNotification(typeNotification: TypeNotification){
        _dataAddHabit.update { currentState ->
            currentState.copy(
                notificationSelected = DEFAULT_TIME.copy(type = typeNotification, time = LocalTime.now()),
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
        when(_dataAddHabit.value.bottomSheetState.dataBottomSheet){
            DataBottomSheet.GENERAL_ERROR,DataBottomSheet.ERROR_NAME_UNIT,DataBottomSheet.ERROR_INTERVAL_UNIT -> {
                _addHabitUIState.update { AddHabitUIState.Success }
            }
            DataBottomSheet.DELETE_NOTIFICATION -> Unit
        }
    }

    fun onAcceptBottomSheet(){
        when(_dataAddHabit.value.bottomSheetState.dataBottomSheet){
            DataBottomSheet.DELETE_NOTIFICATION -> {deleteNotification()}
            DataBottomSheet.ERROR_NAME_UNIT,DataBottomSheet.ERROR_INTERVAL_UNIT,DataBottomSheet.GENERAL_ERROR -> {
                _addHabitUIState.update { AddHabitUIState.Success }
            }
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

    fun saveData(){
        //Preguntamos si la data esta bien metida
        if(dataNameUnitIsCorrect()){
            // Si es ciclico, comprobar
            if(cyclicDataIsCorrect()){
                //Ver si es actualizacion o creacion
                if(_dataAddHabit.value.habitScreen.id == null || _dataAddHabit.value.habitScreen.id == -1L){
                    //Creacion
                    saveHabit()
                }else{
                    //Actualizacion
                    updateHabit()
                }
            }else{
                setBottomSheetError(DataBottomSheet.ERROR_INTERVAL_UNIT)
            }
        }else{
            setBottomSheetError(DataBottomSheet.ERROR_NAME_UNIT)
        }
    }

    private fun saveHabit() = viewModelScope.launch(Dispatchers.IO){
        try {
            _addHabitUIState.update { AddHabitUIState.Loading }
            val habitWithNotifications = fromHabitScreen(_dataAddHabit.value.habitScreen)

            val id = habitWithNotificacionRepo.insertHabit(habitWithNotifications)

            setNotifications(id)

            _addHabitUIState.update { AddHabitUIState.ToHabit }
        }catch (e:Exception){
            _addHabitUIState.update { AddHabitUIState.Error }
            setBottomSheetError(DataBottomSheet.GENERAL_ERROR)
        }
    }

    private fun updateHabit() = viewModelScope.launch(Dispatchers.IO){
        try {
            _addHabitUIState.update { AddHabitUIState.Loading }
            val habitWithNotifications = fromHabitScreen(_dataAddHabit.value.habitScreen)
            habitWithNotifications.habit.id = _dataAddHabit.value.habitScreen.id ?: 0L

            cancelNotifications(habitWithNotifications.habit.id)

            val id = habitWithNotificacionRepo.updateHabit(habitWithNotifications)

            setNotifications(id)

            _addHabitUIState.update { AddHabitUIState.ToHabit }
        }catch (e:Exception){
            _addHabitUIState.update { AddHabitUIState.Error }
            setBottomSheetError(DataBottomSheet.GENERAL_ERROR)
        }
    }

    fun getData(id: Long) = viewModelScope.launch(Dispatchers.IO) {
        _dataAddHabit.update { currentState ->
            currentState.copy(
                dayStartWeek = DayOfWeek.valueOf(datastoreInterface.getDayStartWeek() ?: "Monday")
            )
        }

        if (!_dataSearched.value && id != -1L) {
            _addHabitUIState.update { AddHabitUIState.Loading }

            val habit = habitWithNotificacionRepo.getHabitById(id)

            _dataAddHabit.update { currentState ->
                currentState.copy(
                    habitScreen = toHabitScreen(habit),
                )
            }

            _dataSearched.update { true }
            _addHabitUIState.update { AddHabitUIState.Success }
        }
    }

    private fun setBottomSheetError(dataBottomSheet: DataBottomSheet){
        _addHabitUIState.update { AddHabitUIState.Error }
        _dataAddHabit.update { currentState ->
            currentState.copy(
                bottomSheetState = BottomSheetState(
                    isVisible = true,
                    dataBottomSheet = dataBottomSheet)
            )
        }
    }

    private fun dataNameUnitIsCorrect():Boolean{
        return _dataAddHabit.value.habitScreen.nameTextField.text.toString().isNotEmpty()
                && _dataAddHabit.value.habitScreen.numberTimesTextField.text.toString().isNotEmpty()
    }

    private fun cyclicDataIsCorrect():Boolean{
        return _dataAddHabit.value.habitScreen.intervalTextFieldState.text.toString().isNotEmpty()
    }

    private fun setNotifications(id:Long){
        val notifications = habitWithNotificacionRepo.getAllNotificationsWithId(id)

        notifications.forEach { notification ->
            notificationUtils.setUpAlarm(notification)
        }
    }

    private fun cancelNotifications(id:Long){
        val notifications = habitWithNotificacionRepo.getNotificationById(id)

        notifications.forEach { notification ->
            notificationUtils.cancelAlarm(notification.id)
        }
    }

}

sealed class AddHabitUIState{
    data object Success: AddHabitUIState()
    data object Loading: AddHabitUIState()
    data object Error: AddHabitUIState()
    data object ToHabit: AddHabitUIState()
}