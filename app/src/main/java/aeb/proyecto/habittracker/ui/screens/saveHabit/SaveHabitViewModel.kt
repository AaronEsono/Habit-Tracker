package aeb.proyecto.habittracker.ui.screens.saveHabit

import aeb.proyecto.authentication.AuthenticationInterface
import aeb.proyecto.datastore.DatastoreInterface
import aeb.proyecto.firestore.AuthResponseFirestore
import aeb.proyecto.firestore.FirestoreInterface
import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.data.model.firestoreHabit.CompleteHabitCompressed
import aeb.proyecto.habittracker.data.model.firestoreHabit.DailyHabitCompressed
import aeb.proyecto.habittracker.data.model.firestoreHabit.HabitCompressed
import aeb.proyecto.habittracker.data.model.firestoreHabit.NotificationCompressed
import aeb.proyecto.authentication.AuthenticationManager
import aeb.proyecto.habittracker.utils.SharedState
import aeb.proyecto.room.entities.DailyHabit
import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.Notification
import aeb.proyecto.room.entities.relations.EntireHabit
import aeb.proyecto.room.model.NotificationWithNameAndColor
import aeb.proyecto.room.repository.EntireHabitRepo
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.util.Base64
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream
import javax.inject.Inject

@HiltViewModel
class SaveHabitViewModel @Inject constructor(
    private val authenticationInterface: AuthenticationInterface,
    private val datastoreInterface: DatastoreInterface,
    private val firestoreInterface: FirestoreInterface,
    private val completeHabitRepo: EntireHabitRepo,
    private val sharedState: SharedState,
):ViewModel() {

    private val _date = MutableStateFlow<String?>(null)
    val date: StateFlow<String?> = _date.asStateFlow()

    private val _uiState = MutableStateFlow(UiStateSaveHabits())
    val uiState: StateFlow<UiStateSaveHabits> = _uiState.asStateFlow()

    private val _notificationsWithNames = MutableStateFlow<List<NotificationWithNameAndColor>>(emptyList())
    val notificationsWithNames: StateFlow<List<NotificationWithNameAndColor>> = _notificationsWithNames.asStateFlow()

    init {
        searchData()
    }

    private fun logOut(){
        try{
            authenticationInterface.logOut()
        }catch (e:Exception){
            setError(R.string.error_invalid_default)
        }
    }

    private fun searchData() {
        try {
            viewModelScope.launch {
                val lastSearchedDataStore = datastoreInterface.getLastSearched()

                if (!lastSearchedDataStore.searched || lastSearchedDataStore.uid != getCurrentId()) {
                    setLoading()

                    val response = firestoreInterface.getDataUser(getCurrentId())
                        if(response is AuthResponseFirestore.Success){
                            val data = response.data
                            datastoreInterface.setLastSearched(getCurrentId(), data?.date.orEmpty())
                            _date.value = data?.date?.let { convertDateFormat(it) }
                            setNeutral()
                        }else{
                            setError(R.string.error_invalid_default)
                        }
                }else{
                    _date.value = lastSearchedDataStore.date?.let { if(it.isNotEmpty()) convertDateFormat(it) else null }
                }
            }
        }catch (e:Exception){
            setError(R.string.error_invalid_default)
        }
    }

    fun getName():String{
        return authenticationInterface.getName()
    }

    private fun saveData() = viewModelScope.launch(Dispatchers.IO){
        try {
            setLoading()
            closeGeneralDx()

            //Pillamos todos los habitos y comprimimos
            val habits = completeHabitRepo.getAll()
            val jsonCompressed = jsonCompressed(habits)

            //Subimos a firestore
            val firestoreData = aeb.proyecto.firestore.model.FirestoreData(habit = jsonCompressed)

            val response = firestoreInterface.saveDataUser(firestoreData,getCurrentId())
                if(response is AuthResponseFirestore.Success){
                    val date = LocalDateTime.now().toString()
                    setDatainDataStore(date)
                    _date.value = convertDateFormat(date)
                    setNeutral()
                    setContextDx(DxInfoSaveHabit.RestoreUploadDataSuccess)
                }else{
                    setError(R.string.error_invalid_default)
                }
        }catch (e:Exception){
            setError(R.string.error_invalid_default)
        }
    }

    private fun deleteData() = viewModelScope.launch(Dispatchers.IO){
        try {
            setLoading()
            closeGeneralDx()

            val response = firestoreInterface.deleteDataUser(getCurrentId())
                if(response is AuthResponseFirestore.Success){
                    setDatainDataStore("")
                    _date.value = ""
                    setNeutral()
                    setContextDx(DxInfoSaveHabit.DeleteDataSuccess)
                }else{
                    setError(R.string.error_invalid_default)
                }
        }catch (e:Exception){
            setError(R.string.error_invalid_default)
        }
    }

    fun getCurrentId():String{
        return authenticationInterface.getCurrentId()
    }

    private fun restoreData() = viewModelScope.launch(Dispatchers.IO) {
        try {
            setLoading()
            closeGeneralDx()

            val response = firestoreInterface.getDataUser(getCurrentId())
                if (response is AuthResponseFirestore.Success) {
                    response.data?.let {
                        val dataRestored = decompressJsonFirestore(it.habit)

                        viewModelScope.launch (Dispatchers.IO){
                            _notificationsWithNames.value = completeHabitRepo.setData(dataRestored)
                            setContextDx(DxInfoSaveHabit.RestoreDataSuccess)
                            setNotifications(true)
                            setNeutral()
                        }
                    }
                } else {
                    setError(R.string.error_invalid_default)
                }
        } catch (e: Exception) {
            setError(R.string.error_invalid_default)
        }
    }

    private suspend fun setDatainDataStore(date: String){
        datastoreInterface.setLastSearched(getCurrentId(), date)
    }

    private fun decompressJsonFirestore(compressed: String): List<EntireHabit> {
        val decompressed = decompressJson(compressed)
        val habits = Gson().fromJson(decompressed, Array<CompleteHabitCompressed>::class.java).toList()

        val habitsComplete = habits.map { habitCompressed ->
            val description = if(habitCompressed.habit.description.isNullOrEmpty()) null else habitCompressed.habit.description

            EntireHabit(
                habit = Habit(name = habitCompressed.habit.name, description = description, color = habitCompressed.habit.color, icon = habitCompressed.habit.icon, times =  habitCompressed.habit.times, unit =  habitCompressed.habit.unit),
                dailyHabits = habitCompressed.dailyHabits.map { DailyHabit(timesDone = it.timesDone, date =  it.date) }.toMutableList(),
                notifications = habitCompressed.notifications.map { Notification(hour = it.hour, minute =  it.minute) }.toMutableList()
            )
        }

        return habitsComplete
    }

    private fun jsonCompressed(habits:List<EntireHabit>):String{

        val filteredHabitsAndCompressed = habits.map { habit ->
            CompleteHabitCompressed(
                habit = HabitCompressed(habit.habit.name, habit.habit.description, habit.habit.color, habit.habit.icon, habit.habit.times, habit.habit.unit),
                dailyHabits = habit.dailyHabits.filter { it.timesDone != 0 }.map { DailyHabitCompressed(it.timesDone, it.date) },
                notifications = habit.notifications.map { NotificationCompressed(it.hour, it.minute) }.toList()
            )
        }

        val toJson = Gson().toJson(filteredHabitsAndCompressed)
        return compressJson(toJson)
    }

    fun compressJson(json: String): String {
        val outputStream = ByteArrayOutputStream()
        GZIPOutputStream(outputStream).use { it.write(json.toByteArray(Charsets.UTF_8)) }
        return Base64.getEncoder().encodeToString(outputStream.toByteArray())
    }

    fun decompressJson(compressed: String): String {
        val compressedBytes = Base64.getDecoder().decode(compressed)
        return GZIPInputStream(ByteArrayInputStream(compressedBytes)).bufferedReader(Charsets.UTF_8).use { it.readText() }
    }

    fun setNotifications(notifications:Boolean){
        _uiState.update { currentState ->
            currentState.copy(setNotifications = notifications)
        }
    }

    fun setLoading(){
        sharedState.setLoading()
    }

    fun setNeutral(){
        sharedState.setNeutral()
    }

    fun setError(message:Int){
        sharedState.setError(message)
    }

    fun convertDateFormat(dateString: String): String {
        val dateTime = LocalDateTime.parse(dateString)

        // Obtener los componentes de la fecha y hora
        val day = dateTime.dayOfMonth.toString().padStart(2, '0')
        val month = dateTime.monthValue.toString().padStart(2, '0')
        val year = dateTime.year.toString().takeLast(2)
        val hour = dateTime.hour.toString().padStart(2, '0')
        val minute = dateTime.minute.toString().padStart(2, '0')

        return "$day/$month/$year-$hour:$minute"
    }

    fun closeGeneralDx(){
        _uiState.update { currentState ->
            currentState.copy(showDx = false)
        }
    }

    fun setContextDx(dxInfo: DxInfoSaveHabit) {
        _uiState.update { currentState ->
            currentState.copy(showDx = true, dxInfo = dxInfo)
        }
    }

    fun handleActionDx(navigateToImport: () -> Unit = {}) {
        when (uiState.value.dxInfo) {
            DxInfoSaveHabit.CloseSession -> {
                navigateToImport()
                logOut()
            }

            DxInfoSaveHabit.RestoreData -> {
                restoreData()
            }

            DxInfoSaveHabit.RestoreUploadData -> {
                saveData()
            }

            DxInfoSaveHabit.DeleteData -> {
                deleteData()
            }

            else -> {
                closeGeneralDx()
            }
        }
    }

}

data class UiStateSaveHabits(
    val showDx:Boolean = false,
    val dxInfo:DxInfoSaveHabit = DxInfoSaveHabit.CloseSession,
    val setNotifications:Boolean = false
)

enum class DxInfoSaveHabit(val subtitle:Int,val showCancel:Boolean) {
    CloseSession(R.string.save_habit_close_session,true),
    RestoreData(R.string.save_habit_restore_data,true),
    RestoreUploadData(R.string.save_habit_restore_upload_data,true),
    DeleteData(R.string.save_habit_delete_data,true),
    DeleteDataSuccess(R.string.save_habit_delete_data_success,false),
    RestoreDataSuccess(R.string.save_habit_restore_data_success,false),
    RestoreUploadDataSuccess(R.string.save_habit_restore_upload_data_success,false),
}