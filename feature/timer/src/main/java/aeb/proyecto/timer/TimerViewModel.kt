package aeb.proyecto.timer

import aeb.proyecto.domain.usecase.timer.GetTimerDataUseCase
import aeb.proyecto.domain.usecase.timer.TimeEntriesUseCase
import aeb.proyecto.domain.usecase.timer.TimerData
import aeb.proyecto.domain.usecase.timer.TimerDataStoreUseCase
import aeb.proyecto.room.entities.relations.TimeEntryWithHabit
import aeb.proyecto.stopwatch.helper.StopWatchHelper
import aeb.proyecto.stopwatch.manager.StopWatchStateManager
import aeb.proyecto.stopwatch.manager.StopwatchState
import aeb.proyecto.timer.components.common.typeTimer.intervalSegmented.model.TypePickState
import aeb.proyecto.timer.model.HabitLinkedState
import aeb.proyecto.timer.model.HourSelectedState
import aeb.proyecto.timer.model.SegmentedButtonOptions
import aeb.proyecto.timer.model.TimeEntryState
import aeb.proyecto.timer.model.TimerDataUIState
import aeb.proyecto.timer.model.TimerServiceUIState
import aeb.proyecto.timer.model.getSegmentedButtonOptions
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

/**
 * ViewModel for the Timer feature.
 * Manages the state of the timer, including habit linking, stopwatch settings,
 * and timer data persistence.
 *
 * It transforms domain-level [TimerData] into a [TimerUiState] ready for consumption
 * by the Compose UI layer using a reactive [StateFlow].
 *
 * @param serviceHelper Helper for low-level stopwatch services.
 * @param getTimerDataUseCase Use case to fetch current timer configuration.
 * @param timerDataStoreUseCase Use case for persisting timer preferences.
 * @param stopWatchStateManager Manages the active state of the stopwatch.
 * @param timeEntriesUseCase Use case for recording timer history.
 */
@HiltViewModel
class TimerViewModel @Inject constructor(
    private val serviceHelper: StopWatchHelper,
    getTimerDataUseCase: GetTimerDataUseCase,
    private val timerDataStoreUseCase: TimerDataStoreUseCase,
    private val stopWatchStateManager: StopWatchStateManager,
    private val timeEntriesUseCase: TimeEntriesUseCase
):ViewModel(){

    /**
     * Exposes the current state of the timer UI.
     * Maps domain data to [TimerUiState] and handles loading/error states.
     * Uses [SharingStarted.WhileSubscribed] for efficient resource management.
     */
    val timerData: StateFlow<TimerUiState> = getTimerDataUseCase.timerData
        .map<TimerData, TimerUiState> { data ->
            // Map domain fields to UI-friendly state classes
            val hourSelected = data.hourSelected?.let {  HourSelectedState.Data(it) } ?: HourSelectedState.NoData
            val typeTimer = getSegmentedButtonOptions(data.typeTimer ?: 1)

            TimerUiState.Success(
                TimerDataUIState(
                    habitLinked = data.habitWithDay?.let { HabitLinkedState.Data(it) } ?: HabitLinkedState.NoData,
                    typeTimer = typeTimer,
                    hourSelected = hourSelected,
                    restHour = data.restHour?.let { HourSelectedState.Data(it) } ?: HourSelectedState.NoData,
                    sets = data.sets,

                    // Business logic for button enablement (validation check)
                    buttonEnabled = typeTimer == SegmentedButtonOptions.StopWatch || (
                            hourSelected is HourSelectedState.Data &&
                                    !(
                                    hourSelected.data.first == 0 &&
                                    hourSelected.data.second == 0 &&
                                    hourSelected.data.third == 0
                                            )
                            )
                )
            )
        }
        .catch { exception ->
            emit(TimerUiState.Error(exception.message ?: "Unknown error"))
        }
        .onStart {
            emit(TimerUiState.Loading)
        }
        .flowOn(Dispatchers.Default)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TimerUiState.Loading
        )

    /**
     * Observes the stopwatch service state changes.
     * Combines multiple flows into a single [TimerServiceUIState].
     * * Logic: Emits [TimerServiceUIState.NoTimer] when the service is Idle,
     * otherwise aggregates current timing data for the UI.
     */
    val timerStopWatchUIState: StateFlow<TimerServiceUIState> = combine(
        stopWatchStateManager.elapsedTime,
        stopWatchStateManager.typeTimer,
        stopWatchStateManager.currentState,
        stopWatchStateManager.timerString,
        stopWatchStateManager.habitLinked
    ) { elapsedTime, typeTimer, currentState, timerString, habitLinked ->

        if(currentState == StopwatchState.Idle) TimerServiceUIState.NoTimer
        else{
            TimerServiceUIState.TimerRunning(
                elapsedTime = elapsedTime,
                typeTimer = typeTimer,
                currentState = currentState,
                hourString = timerString,
                habitLinked = habitLinked
            )
        }

    }
        .flowOn(Dispatchers.Default)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TimerServiceUIState.NoTimer
        )

    /**
     * Observes the history of time entries.
     * Maps the raw list of entries into a [TimeEntryState] to handle empty states.
     */
    val historyEntries: StateFlow<TimeEntryState> = timeEntriesUseCase.getTimeEntries()
        .map { entries ->
            if (entries.isEmpty()) TimeEntryState.EmptyList
            else TimeEntryState.TimeEntries(entries)
        }
        .flowOn(Dispatchers.Default)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TimeEntryState.EmptyList
        )

    /**
     * Manages the visibility state of the bottom sheet UI components.
     */
    private val _bottomSheetState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val bottomSheetState: StateFlow<Boolean> = _bottomSheetState.asStateFlow()

    /**
     * SharedFlow used to trigger events for the segmented timer configuration.
     * Emits a [Triple] representing the selected time/interval values.
     */
    private val _triggerSegmentedTimer = MutableSharedFlow<Triple<Int, Int, Int>?>()
    val triggerSegmentedTimer: SharedFlow<Triple<Int, Int, Int>?> = _triggerSegmentedTimer.asSharedFlow()

    /**
     * Initializes and starts the appropriate foreground service based on the
     * current timer type (Stopwatch, Timer, or Interval).
     *
     * It extracts the necessary configuration from [timerData], resolves the
     * linked habit (if any), and delegates service startup to [serviceHelper].
     */
    fun startService(){
        val data = (timerData.value as? TimerUiState.Success)?.timerDataUIState

        // Convert UI states to service-compatible time values
        val time = getLongMillisecondsTime(data?.hourSelected ?: HourSelectedState.NoData)
        val rest = getLongMillisecondsTime(data?.restHour ?: HourSelectedState.NoData)
        val interval = data?.sets ?: 1

        // Resolve associated habit data
        val habitLinked = (timerData.value as? TimerUiState.Success)?.timerDataUIState?.habitLinked

        val habitData:Pair<Long,String> = if(habitLinked is HabitLinkedState.Data){
            habitLinked.data.habit.id to habitLinked.data.day.date.toString()
        }else{
            -1L to ""
        }

        // Determine and trigger the specific service mode
        when(data?.typeTimer){
            SegmentedButtonOptions.StopWatch -> {
                serviceHelper.startForegroundServiceOnStopWatch(habitData)
            }
            SegmentedButtonOptions.Timer -> {
                serviceHelper.startForegroundServiceOnTimer(time,habitData)
            }
            SegmentedButtonOptions.Interval -> {
                serviceHelper.startForegroundServiceOnInterval(time,rest,interval,habitData)
            }
            null -> {
                serviceHelper.startForegroundServiceOnStopWatch(habitData)
            }
        }
    }

    /**
     * Persists individual time components (hour) to the DataStore.
     * Executed on [Dispatchers.IO] to prevent blocking the Main Thread.
     */
    fun onHourChange(hour:String) = viewModelScope.launch (Dispatchers.IO){
        timerDataStoreUseCase.saveHourWheelTimer(hour.toIntOrNull() ?: 0)
    }

    /**
     * Persists individual time components (minutes) to the DataStore.
     * Executed on [Dispatchers.IO] to prevent blocking the Main Thread.
     */
    fun onMinuteChange(minute:String) = viewModelScope.launch (Dispatchers.IO){
        timerDataStoreUseCase.saveMinuteWheelTimer(minute.toIntOrNull() ?: 0)
    }

    /**
     * Persists individual time components (seconds) to the DataStore.
     * Executed on [Dispatchers.IO] to prevent blocking the Main Thread.
     */
    fun onSecondChange(second:String) = viewModelScope.launch (Dispatchers.IO){
        timerDataStoreUseCase.saveSecondWheelTimer(second.toIntOrNull() ?: 0)
    }

    /**
     * Persists a full time configuration (H, M, S) for the active timer session.
     */
    private fun setHour(value: Triple<Int,Int,Int>) = viewModelScope.launch (Dispatchers.IO){
        timerDataStoreUseCase.saveHourWheelTimer(value.first)
        timerDataStoreUseCase.saveMinuteWheelTimer(value.second)
        timerDataStoreUseCase.saveSecondWheelTimer(value.third)
    }

    /**
     * Persists a full time configuration (H, M, S) for the interval rest session.
     */
    private fun setHourRest(value: Triple<Int,Int,Int>) = viewModelScope.launch (Dispatchers.IO){
        timerDataStoreUseCase.saveRestHourWheelTimer(value.first)
        timerDataStoreUseCase.saveRestMinuteWheelTimer(value.second)
        timerDataStoreUseCase.saveRestSecondWheelTimer(value.third)
    }

    /**
     * Routes and saves timer configuration based on the type (Work or Rest period).
     * @param time A [Triple] of String values representing hours, minutes, and seconds.
     * @param type The identification constant for [TypePickState] (Work vs Rest).
     */
    fun setIntervalHour(time:Triple<String,String,String>, type:Int) = viewModelScope.launch(Dispatchers.IO){
        when(type){
            TypePickState.WORK_TIME.value -> {
                setHour(
                    Triple(
                        time.first.toIntOrNull() ?: 0,
                        time.second.toIntOrNull() ?: 0,
                        time.third.toIntOrNull() ?: 0
                    )
                )
            }
            TypePickState.REST_TIME.value -> {
                setHourRest(
                    Triple(
                        time.first.toIntOrNull() ?: 0,
                        time.second.toIntOrNull() ?: 0,
                        time.third.toIntOrNull() ?: 0
                    )
                )
            }
        }
    }

    /**
     * Updates the selected timer type (e.g., Stopwatch, Timer, Interval).
     * Resets the active segmented timer trigger and persists the new selection.
     *
     * @param value The integer representing the selected [SegmentedButtonOptions].
     */
    fun onTypeButtonChange(value:Int) = viewModelScope.launch(Dispatchers.IO){
        _triggerSegmentedTimer.emit(null)
        timerDataStoreUseCase.saveTypeButtonTimer(value)
    }

    /**
     * Updates the number of sets for interval training.
     * Validates the input to ensure it falls within the allowed range (1-99).
     *
     * @param value Number of sets.
     */
    fun onSetChange(value:Int) = viewModelScope.launch(Dispatchers.IO){
        if(value in 1..99){
            timerDataStoreUseCase.setTimer(value)
        }
    }

    /**
     * Increments or decrements the work timer duration by 5-minute intervals.
     *
     * @param plusTime If true, adds time; if false, subtracts time.
     */
    fun addHourTimer(plusTime:Boolean) = viewModelScope.launch (Dispatchers.IO){
        setHour(editTime(getTime(),5,plusTime))
    }

    /**
     * Increments or decrements the rest timer duration by 5-minute intervals.
     *
     * @param plusTime If true, adds time; if false, subtracts time.
     */
    fun addRestTimer(plusTime:Boolean) = viewModelScope.launch (Dispatchers.IO){
        setHourRest(editTime(getRestTime(),5,plusTime))
    }

    /**
     * Adjusts a given time by a specific delta (in minutes), rounding to the nearest
     * multiple and keeping the value within valid bounds (0 to 99h 59m 59s).
     *
     * @param time The current time as a [Triple] of (Hours, Minutes, Seconds).
     * @param delta The interval increment/decrement in minutes.
     * @param plusTime If true, adds the interval; if false, subtracts it.
     * @return A new [Triple] representing the updated time.
     */
    private fun editTime(time:Triple<Int,Int,Int>, delta:Int, plusTime:Boolean):Triple<Int,Int,Int>{
        val (hours, minutes, seconds) = time

        // Convert to total seconds for easier arithmetic
        var currentTotalSeconds = hours * 3600 + minutes * 60 + seconds
        val remainder = currentTotalSeconds % delta
        val updatedTotalSeconds = when {

            // Align to the nearest multiple of delta and increment/decrement
            remainder != 0 -> {
                if (plusTime) {
                    (currentTotalSeconds + (delta - remainder)).coerceAtMost(99 * 3600 + 59 * 60 + 59)
                } else {
                    (currentTotalSeconds - remainder).coerceAtLeast(0)
                }
            }
            else -> {
                if (plusTime) {
                    (currentTotalSeconds + delta).coerceAtMost(99 * 3600 + 59 * 60 + 59)
                } else {
                    (currentTotalSeconds - delta).coerceAtLeast(0)
                }
            }
        }


        // Convertir total de segundos a h:m:s
        val newHours = updatedTotalSeconds / 3600
        val newMinutes = (updatedTotalSeconds % 3600) / 60
        val newSeconds = updatedTotalSeconds % 60

        return Triple(newHours, newMinutes, newSeconds)
    }

    /**
     * Extracts the current work duration from the UI state.
     * @return A [Triple] representing (Hours, Minutes, Seconds).
     */
    private fun getTime():Triple<Int,Int,Int>{
        val time = (timerData.value as? TimerUiState.Success)?.timerDataUIState?.hourSelected ?: HourSelectedState.NoData
        var respond:Triple<Int,Int,Int> = Triple(0,0,0)

        if(time is HourSelectedState.Data){
            respond = Triple(time.data.first,time.data.second,time.data.third)
        }

        return respond
    }

    /**
     * Extracts the current rest duration from the UI state.
     * @return A [Triple] representing (Hours, Minutes, Seconds).
     */
    private fun getRestTime():Triple<Int,Int,Int>{
        val time = (timerData.value as? TimerUiState.Success)?.timerDataUIState?.restHour ?: HourSelectedState.NoData
        var respond:Triple<Int,Int,Int> = Triple(0,0,0)

        if(time is HourSelectedState.Data){
            respond = Triple(time.data.first,time.data.second,time.data.third)
        }

        return respond
    }

    /**
     * Converts [HourSelectedState] into total milliseconds for service consumption.
     *
     * @param hour The state containing the time selection.
     * @return Total time in milliseconds as a [Long].
     */
    private fun getLongMillisecondsTime(hour: HourSelectedState):Long{
        return when(hour){
            is HourSelectedState.Data -> {
                (hour.data.first * 3600 + hour.data.second * 60 + hour.data.third) * 1000L
            }
            HourSelectedState.NoData -> {
                0
            }
        }
    }

    /**
     * Triggers the completion of the active service.
     */
    fun finishService(){
        try {
            serviceHelper.finishService()
        }catch (e:Exception){
            Log.e("Error","${e.message}")
        }
    }

    /**
     * Cancels the active service session immediately.
     */
    fun cancelService(){
        try {
            serviceHelper.cancelService()
        }catch (e:Exception){
            Log.e("Error","${e.message}")
        }
    }

    /**
     * Resumes a previously paused service session.
     */
    fun resumeService(){
        try {
            serviceHelper.resumeService()
        }catch (e:Exception){
            Log.e("Error","${e.message}")
        }
    }

    /**
     * Pauses or stops the service activity.
     */
    fun stopService(){
        try {
            serviceHelper.stopService()
        }catch (e:Exception){
            Log.e("Error","${e.message}")
        }
    }

    /**
     * Toggles the visibility of the habit selection bottom sheet.
     */
    fun onClickHabitButton(){
        _bottomSheetState.update { true }
    }

    /**
     * Toggles the visibility of the habit selection bottom sheet.
     */
    fun onDismissHabitBottomSheet(){
        _bottomSheetState.update { false }
    }

    /**
     * Links a selected habit and date to the current timer session.
     */
    fun onAcceptBottomSheetPickHabit(id:Long, date:LocalDate) = viewModelScope.launch{
        timerDataStoreUseCase.setHabitLinked(id,date)
    }

    /**
     * Removes the habit linkage from the current timer session.
     */
    fun cancelHabitLinked() = viewModelScope.launch {
        timerDataStoreUseCase.removeHabitLinked()
    }

    /**
     * Toggles the favorite status of a specific time entry in the history.
     */
    fun onClickFavorite(id:Long,favorite:Boolean) = viewModelScope.launch (Dispatchers.IO){
        timeEntriesUseCase.changeFavorite(id,favorite)
    }

    /**
     * Deletes a time entry record from the history.
     */
    fun onDeleteHistoryEntry(id:Long) = viewModelScope.launch (Dispatchers.IO){
        timeEntriesUseCase.deleteTimeEntry(id)
    }

    /**
     * Retrieves a specific time entry and prepares the timer configuration based on it.
     * Emits the entry data to trigger the timer UI setup.
     */
    fun onClickTimeEntry(id:Long) = viewModelScope.launch(Dispatchers.IO){
        val timeEntry = getTimeEntryWithHabitLinked(id)
        timeEntriesUseCase.setDataFromTimeEntry(timeEntry){ timer ->
            viewModelScope.launch {
                _triggerSegmentedTimer.emit(timer)
            }
        }
    }

    /**
     * Helper to find a time entry in the current state by its unique ID.
     */
    private fun getTimeEntryWithHabitLinked(id:Long):TimeEntryWithHabit?{
        return when (historyEntries.value){
            TimeEntryState.EmptyList -> null
            is TimeEntryState.TimeEntries -> {
                (historyEntries.value as TimeEntryState.TimeEntries).timeEntries.find { it.timeEntry.id == id }
            }
        }
    }

}

/**
 * Represents the UI state for the Timer screen.
 * This sealed class ensures exhaustive state handling in the UI layer.
 */
sealed class TimerUiState{
    /**
     * Represents the successful state, carrying the necessary UI data.
     * @property timerDataUIState The data model containing current timer values.
     */
    data class Success(val timerDataUIState: TimerDataUIState) : TimerUiState()

    /**
     * Represents an error state, containing the error message.
     * @property error The description of the error that occurred.
     */
    data class Error(val error:String) : TimerUiState()

    /**
     * Represents the initial loading state while the timer service/data is initialized.
     */
    data object Loading : TimerUiState()
}