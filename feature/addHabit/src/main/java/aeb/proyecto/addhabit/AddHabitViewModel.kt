package aeb.proyecto.addhabit

import aeb.proyecto.addhabit.constants.GridOption
import aeb.proyecto.addhabit.constants.GridOptionResult
import aeb.proyecto.addhabit.constants.PICK_NOTIFICATION
import aeb.proyecto.addhabit.constants.TypeHabit
import aeb.proyecto.addhabit.constants.TypeNotificationResult
import aeb.proyecto.addhabit.converter.fromHabitScreen
import aeb.proyecto.addhabit.converter.toHabitScreen
import aeb.proyecto.addhabit.model.AddHabitNotification
import aeb.proyecto.addhabit.model.BottomSheetState
import aeb.proyecto.addhabit.model.DEFAULT_TIME
import aeb.proyecto.addhabit.model.DataAddHabitScreen
import aeb.proyecto.addhabit.model.DataBottomSheet
import aeb.proyecto.domain.usecase.addHabit.DataStoreAddHabitUseCase
import aeb.proyecto.domain.usecase.addHabit.RoomRepositoryAddHabitUseCase
import aeb.proyecto.domain.usecase.addHabit.SetNotificationAddHabitUseCase
import aeb.proyecto.room.model.classes.UnitType
import aeb.proyecto.room.model.classes.TypeNotification
import aeb.proyecto.room.model.classes.UnitHabit
import aeb.proyecto.room.model.classes.unitsHourMode
import aeb.proyecto.ui.constants.getContrastColor
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
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

/**
 * Architectural Presentation Layer Controller (ViewModel) designed to orchestrate states,
 * dynamic layouts, and user interactions for the habit creation and modification environment.
 *
 * This controller serves as a highly isolated gateway that bridges user UI intents with localized
 * domain actions—coordinating transactional database persistence, preference initialization matrices,
 * and hardware reminder alignments via asynchronous dependency execution.
 *
 * @property roomRepositoryAddHabitUseCase Handles relational core entity persistence blueprints in local storage.
 * @property setNotificationAddHabitUseCase Calculates future execution offsets to bind system-level kernel reminders.
 * @property dataStoreAddHabitUseCase Manages transient user state synchronization and atomic preference updates.
 */
@HiltViewModel
class AddHabitViewModel @Inject constructor(
    private val roomRepositoryAddHabitUseCase: RoomRepositoryAddHabitUseCase,
    private val setNotificationAddHabitUseCase: SetNotificationAddHabitUseCase,
    private val dataStoreAddHabitUseCase: DataStoreAddHabitUseCase
):ViewModel() {

    /**
     * Internal mutable state backing property carrying the reactive, rich configuration
     * matrix parameters representing the active form inputs on the add/edit screen.
     */
    private val _dataAddHabit = MutableStateFlow(DataAddHabitScreen())

    /**
     * Public read-only stream exposing the consolidated data snapshot of the habit form layout configurations.
     */
    val dataAddHabit = _dataAddHabit.asStateFlow()

    /**
     * Internal mutable state boundary managing the terminal lifecycle progression states
     * (e.g., Success, Error, Loading) of the screen operation.
     */
    private val _addHabitUIState = MutableStateFlow<AddHabitUIState>(AddHabitUIState.Success)

    /**
     * Public read-only stream delivering sequential UI lifecycle operations and structural rendering states.
     */
    val addHabitUIState = _addHabitUIState.asStateFlow()

    /**
     * Internal tracking primitive monitoring whether localized record validation or search lookup
     * queries have successfully executed against historical layers.
     */
    private val _dataSearched = MutableStateFlow(false)

    /**
     * Processes selected element returns emitted from expanded overlay grid components (Colors/Icons).
     * Automatically routes specialized payload abstractions into their dedicated state mutation tracks.
     *
     * @param gridOptionResult The polymorphic result payload token detailing the specific entity modification targets.
     */
    fun onClickGridOption(gridOptionResult: GridOptionResult){
        when(gridOptionResult){
            is GridOptionResult.colorResult -> setColor(gridOptionResult.color)
            is GridOptionResult.iconResult -> setIcon(gridOptionResult.icon)
        }
    }

    /**
     * Handles interactive tap boundaries over primary expansion menu cards.
     * Toggles layout states to mount or unmount target selector grids across view segments.
     *
     * @param gridOption The targeted layout configuration option designated to toggle visibility matrices.
     */
    fun onClickCard( gridOption: GridOption){
        when(gridOption){
            GridOption.COLORS -> colorGridState()
            GridOption.ICONS -> iconGridState()
        }
    }

    /**
     * Commits a selected color configuration into the underlying state engine.
     * Calculates an optimized accessible text/icon overlay shade and collapses the active picker panel.
     *
     * @param color The specific target [Color] token selected by the user to represent the habit.
     */
    private fun setColor(color:Color){
        _dataAddHabit.update { currentState ->
            currentState.copy(
                habitScreen = currentState.habitScreen.copy(color = color),
                contrastColor = getContrastColor(color),
                isColorSelected = false
            )
        }
    }

    /**
     * Commits a selected icon asset into the underlying screen configuration state.
     * Automatically minimizes the active icon picker panel once the target vector is resolved.
     *
     * @param icon The targeted [ImageVector] configuration chosen to represent the habit visually.
     */
    private fun setIcon(icon: ImageVector){
        _dataAddHabit.update { currentState ->
            currentState.copy(
                habitScreen = currentState.habitScreen.copy(icon = icon),
                isIconSelected = false
            )
        }
    }

    /**
     * Toggles the open/collapsed rendering matrix state of the color selection grid overlay.
     * Enforces layout predictability by automatically dismissing the active icon grid segment.
     */
    private fun colorGridState(){
        _dataAddHabit.update { currentState ->
            currentState.copy(
                isColorSelected =  !currentState.isColorSelected,
                isIconSelected = false
            )
        }
    }

    /**
     * Toggles the open/collapsed rendering matrix state of the icon selection grid overlay.
     * Enforces layout predictability by automatically dismissing the active color grid segment.
     */
    private fun iconGridState(){
        _dataAddHabit.update { currentState ->
            currentState.copy(
                isIconSelected =  !currentState.isIconSelected,
                isColorSelected = false
            )
        }
    }

    /**
     * Mounts and displays a contextual dialogue overlay screen element.
     * Binds an operational layout router key code to target and render the appropriate dialogue asset.
     *
     * @param typeDialog The categorical integer identifier token matching the requested modal layout.
     */
    fun setDialog(typeDialog:Int){
        _dataAddHabit.update { currentState ->
            currentState.copy(
                showDialog = true,
                typeDialog = typeDialog
            )
        }
    }

    /**
     * Dismisses the active dialogue modal element, restoring baseline interface focus layers.
     */
    fun closeDialog(){
        _dataAddHabit.update { currentState ->
            currentState.copy(
                showDialog = false
            )
        }
    }

    /**
     * Modifies the operational behavior format tracking metric configuration for the target habit.
     * Switches baseline constraints between discrete targets (e.g., Checkbox, Quantizable Counters, Time).
     *
     * @param typeHabit The structural [TypeHabit] specification designation to map.
     */
    fun onClickTypeHabit(typeHabit: TypeHabit){
        _dataAddHabit.update { currentState ->
            currentState.copy(
                habitScreen = currentState.habitScreen.copy(typeHabit = typeHabit)
            )
        }
    }

    /**
     * Registers the targeted weekly recurrence frequency cap target inside the form state tracking model.
     *
     * @param numberDays The absolute count of execution targets allocated over a standard 7-day window.
     */
    fun onClickWeekly(numberDays:Int){
        _dataAddHabit.update { currentState ->
            currentState.copy(
                habitScreen = currentState.habitScreen.copy(numberOfDaysWeek = numberDays)
            )
        }
    }

    /**
     * Registers the targeted monthly recurrence frequency cap target inside the form state tracking model.
     *
     * @param numberDays The absolute count of execution targets allocated over a standard monthly calendar interval.
     */
    fun monthNumberSelected(numberDays:Int){
        _dataAddHabit.update { currentState ->
            currentState.copy(
                habitScreen = currentState.habitScreen.copy(numberOfDaysMonth = numberDays)
            )
        }
    }

    /**
     * Establishes the foundational calendar starting boundary date for the targeted habit recurrence sequence.
     *
     * @param localDate The [LocalDate] configuration marking the formal starting point of tracking.
     */
    fun onClickDate(localDate:LocalDate){
        _dataAddHabit.update { currentState->
            currentState.copy(
                habitScreen = currentState.habitScreen.copy(dateRecurringStartDate = localDate)
            )
        }
    }

    /**
     * Mutates the structural tracking unit parameter allocated to the active form state.
     * Automatically filters and truncates fractional decimal points from the input buffer
     * if the newly designated measurement type shifts to a time-based metric layout.
     *
     * @param unit The targeted [UnitHabit] metrics specification selected by the user.
     */
    fun onPickUnit(unit: UnitHabit){
        // Enforce integer-only constraints over temporal units to prevent trailing fractional layout drift
        if(unit.unitType == UnitType.TIME){
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

    /**
     * Prepares and initializes the notification reminder modification pipeline overlay.
     * Sets a baseline timestamp anchor using the active hardware instance time and mounts
     * the specialized time-picker dialog interface.
     *
     * @param typeNotification The categorical [TypeNotification] frequency classification to route.
     */
    fun onClickTypeNotification(typeNotification: TypeNotification){
        _dataAddHabit.update { currentState ->
            currentState.copy(
                notificationSelected = DEFAULT_TIME.copy(type = typeNotification, time = LocalTime.now()),
                showDialog = true,
                typeDialog = PICK_NOTIFICATION
            )
        }
    }

    /**
     * Confirms the chronological time selection for a reminder slot and commits it to the form state.
     * Evaluates the active notification identity via a sentinel token ("-1") to determine whether
     * to append a completely fresh reminder instance or perform an inline modification over an existing entry.
     *
     * @param time The [LocalTime] instance configured by the user to trigger the system alarm.
     */
    fun onTimeSelected(time:LocalTime){
        val idSelected = _dataAddHabit.value.notificationSelected.id

        // Evaluates sentinel token to append a completely fresh notification profile
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
            // Iterates the structural list state to map modifications onto the targeted instance match
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

    /**
     * Intercepts the user intention to remove a specific reminder notification from the active form.
     * Caches the target reminder reference keys and mounts an interactive confirmation BottomSheet
     * layout to safely guard destructive state mutations.
     *
     * @param id The unique string identifier token of the targeted notification profile slated for removal.
     */
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

    /**
     * Dismisses the active structural BottomSheet interface component and hides it from the viewport hierarchy.
     * Evaluates the categorical type payload of the dismissed sheet to automatically restore the global
     * presentation lifecycle boundaries back to a stable success state if an error wrapper was previously mounted.
     */
    fun closeBottomSheet(){
        _dataAddHabit.update { currentState ->
            currentState.copy(
                bottomSheetState = currentState.bottomSheetState.copy(isVisible = false)
            )
        }
        when(_dataAddHabit.value.bottomSheetState.dataBottomSheet){
            DataBottomSheet.GENERAL_ERROR,DataBottomSheet.ERROR_HOUR,DataBottomSheet.ERROR_NAME_UNIT,DataBottomSheet.ERROR_INTERVAL_UNIT -> {
                _addHabitUIState.update { AddHabitUIState.Success }
            }
            DataBottomSheet.DELETE_NOTIFICATION -> Unit
        }
    }

    /**
     * Dispatches operational routing events when the primary confirmation action is triggered inside the active BottomSheet.
     * Delegates destructive pipelines to specialized processors (e.g., executing structural deletions) or flushes
     * transient transactional tracking flags depending on the contextual layout category.
     */
    fun onAcceptBottomSheet(){
        when(_dataAddHabit.value.bottomSheetState.dataBottomSheet){
            DataBottomSheet.DELETE_NOTIFICATION -> {deleteNotification()}
            DataBottomSheet.ERROR_NAME_UNIT,DataBottomSheet.ERROR_HOUR,DataBottomSheet.ERROR_INTERVAL_UNIT,DataBottomSheet.GENERAL_ERROR -> {
                _addHabitUIState.update { AddHabitUIState.Success }
            }
        }
    }

    /**
     * Performs an in-place destructive mutation across the transient notification list array state.
     * Filters out the targeted reminder asset whose identity criteria matches the cached selection token,
     * automatically triggering defensive garbage collection over old system context keys.
     */
    private fun deleteNotification() {
        _dataAddHabit.update { currentState ->
            currentState.copy(
                habitScreen = currentState.habitScreen.copy(
                    notifications = currentState.habitScreen.notifications.filter { it.id != _dataAddHabit.value.notificationSelected.id }
                )
            )
        }
    }

    /**
     * Routes incoming polymorphic structural interaction tokens emitted from interactive reminder panels.
     * Delegates specific mutation payloads to dedicated state handlers depending on the notification schedule classification.
     *
     * @param typeNotificationResult The target configuration result payload representation to parse and evaluate.
     */
    fun onClickTypeNotificationResult(typeNotificationResult: TypeNotificationResult){
        when(typeNotificationResult){
            is TypeNotificationResult.Daily -> { editNotificationDaily(typeNotificationResult) }
            is TypeNotificationResult.Recurring -> { editNotificationRecurring(typeNotificationResult)  }
        }
    }

    /**
     * Modifies the selective tracking weekday matrix allocation for a designated daily notification profile.
     * Implements an atomic toggle operation that adds or removes weekdays while enforcing a defensive system rule:
     * a daily alert configuration boundary must never contain an empty execution day set.
     *
     * @param editNotification The payload descriptor details targeting the daily reminder item slate to mutate.
     */
    private fun editNotificationDaily(editNotification: TypeNotificationResult.Daily) {
        val notification = findNotification(editNotification.id)

        if (notification.type is TypeNotification.Daily) {
            val currentDays = notification.type.days

            // Guard clause to block empty sets from corrupting systemic platform notification scheduler rules
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

    /**
     * Modifies the operational recurrence interval frequency scale for a targeted recurring notification profile.
     * Evaluates action modifiers to increment or decrement day gaps while enforcing defensive thresholds
     * to prevent configuration metrics from dropping below a 1-day step interval.
     *
     * @param editNotification The payload descriptor details targeting the recurring reminder item slate to mutate.
     */
    private fun editNotificationRecurring(editNotification: TypeNotificationResult.Recurring) {
        val notification = findNotification(editNotification.id)

        if (notification.type is TypeNotification.Recurring) {
            val intervalDays = notification.type.interval

            // Guard validation to block intervals from falling below single-day cycle bounds
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

    /**
     * Traverses the active persistent form notification array state to locate a specific reminder profile tracking key.
     * Enforces strict type consistency by applying non-null state assertion operations over target queries.
     *
     * @param id The unique identifier string code of the requested notification item.
     * @return The matching [AddHabitNotification] configuration instance state.
     */
    private fun findNotification(id:String):AddHabitNotification{
        return _dataAddHabit.value.habitScreen.notifications.find { it.id == id }!!
    }

    /**
     * Intercepts user requests to reconfigure chronological parameters over an existing reminder node.
     * Caches operational context identifiers and historical timestamps into selection vectors before mounting
     * the target time-picker component dialogue screen overlay.
     *
     * @param id The unique identifier token of the targeted notification profile slated for adjustment.
     * @param time The active [LocalTime] configuration parameter attached to the target reminder.
     */
    fun onEditNotification(id:String,time:LocalTime){
        _dataAddHabit.update { currentState ->
            currentState.copy(
                notificationSelected = DEFAULT_TIME.copy(id = id,time = time),
                showDialog = true,
                typeDialog = PICK_NOTIFICATION
            )
        }
    }

    /**
     * Executes sequential validation checks over the comprehensive habit form input fields.
     * Intercepts formatting errors, zero bounds, or logical recurrence conflicts to deploy
     * descriptive error BottomSheets; on validation success, seamlessly branches execution paths
     * to perform a new entity generation or an inline record overwrite based on identity footprint keys.
     */
    fun saveData(){
        val unit = _dataAddHabit.value.habitScreen.unit

        // 1. Validate hours format constraints if applicable
        if (unit in unitsHourMode && !isHourCorrect()) {
            setBottomSheetError(DataBottomSheet.ERROR_HOUR)
            return
        }

        // 2. Validate numeric input constraints for non-hour configuration modes
        if (unit !in unitsHourMode && !textFieldCorrect()) {
            setBottomSheetError(DataBottomSheet.ERROR_NAME_UNIT)
            return
        }

        // 3. Enforce structural text validation over the master habit identity title
        if (!dataNameIsCorrect()) {
            setBottomSheetError(DataBottomSheet.ERROR_NAME_UNIT)
            return
        }

        // 4. Enforce interval period boundaries if structural behavior matches cyclic routines
        if (!cyclicDataIsCorrect() && _dataAddHabit.value.habitScreen.typeHabit == TypeHabit.CYCLIC) {
            setBottomSheetError(DataBottomSheet.ERROR_INTERVAL_UNIT)
            return
        }

        // 5. Evaluate identity footprints to dispatch structural insertion or persistence updates
        val habitId = _dataAddHabit.value.habitScreen.id
        if (habitId == null || habitId == -1L) {
            saveHabit()
        } else {
            updateHabit()
        }
    }

    /**
     * Dispatches an asynchronous transactional operations thread to parse, map, and insert a new habit
     * entry along with its contextual reminder structures into the baseline storage layers.
     * Automatically coordinates kernel alarm registrations upon successful relational database commits.
     */
    private fun saveHabit() = viewModelScope.launch(Dispatchers.IO){
        try {
            _addHabitUIState.update { AddHabitUIState.Loading }
            val habitWithNotifications = fromHabitScreen(_dataAddHabit.value.habitScreen)

            // Commit core operational model to relational sheets and capture generated tracking ID
            val id = roomRepositoryAddHabitUseCase.insertHabit(habitWithNotifications)

            // Register specific device hardware alarms for the newly generated habit sequence
            setNotifications(id)

            _addHabitUIState.update { AddHabitUIState.ToHabit }
        }catch (e:Exception){
            _addHabitUIState.update { AddHabitUIState.Error }
            setBottomSheetError(DataBottomSheet.GENERAL_ERROR)
        }
    }

    /**
     * Dispatches an asynchronous transactional operations thread to perform a structural record overwrite
     * across existing persistence layers. Safely flushes historical hardware alarm keys inside the OS system
     * kernel before mapping updates, preventing phantom background triggers.
     */
    private fun updateHabit() = viewModelScope.launch(Dispatchers.IO){
        try {
            _addHabitUIState.update { AddHabitUIState.Loading }
            val habitWithNotifications = fromHabitScreen(_dataAddHabit.value.habitScreen)
            habitWithNotifications.habit.id = _dataAddHabit.value.habitScreen.id ?: 0L

            // Clear legacy OS hardware triggers allocated to this entity identifier to avoid scheduling conflicts
            cancelNotifications(habitWithNotifications.habit.id)

            // Commit structural updates to localized database layers and fetch transaction confirmation keys
            val id = roomRepositoryAddHabitUseCase.updateNotification(habitWithNotifications)

            // Re-bind fresh operational platform hardware alarms using updated parameters
            setNotifications(id)

            _addHabitUIState.update { AddHabitUIState.ToHabit }
        }catch (e:Exception){
            _addHabitUIState.update { AddHabitUIState.Error }
            setBottomSheetError(DataBottomSheet.GENERAL_ERROR)
        }
    }

    /**
     * Initializes the form environment setups by retrieving structural day configuration parameters.
     * If the environment manages an active record overwrite modification session, it executes an asynchronous
     * persistence lookup query exactly once to rehydrate past entity tokens into active UI layouts.
     *
     * @param id The unique database identifier tracking footprint code of the target habit (-1L if creating a new entry).
     */
    fun getData(id: Long) = viewModelScope.launch(Dispatchers.IO) {
        _dataAddHabit.update { currentState ->
            currentState.copy(
                dayStartWeek = dataStoreAddHabitUseCase.getDayOfWeek()
            )
        }

        // Defensive execution boundary guard to prevent redundant re-queries during configuration shifts
        if (!_dataSearched.value && id != -1L) {
            _addHabitUIState.update { AddHabitUIState.Loading }

            val habit = roomRepositoryAddHabitUseCase.getHabitById(id)

            _dataAddHabit.update { currentState ->
                currentState.copy(
                    habitScreen = toHabitScreen(habit),
                )
            }

            _dataSearched.update { true }
            _addHabitUIState.update { AddHabitUIState.Success }
        }
    }

    /**
     * Halts standard operational presentation lifecycles and registers an infrastructure error state.
     * Automatically formats and mounts a designated error BottomSheet configuration descriptor across the viewport.
     *
     * @param dataBottomSheet The explicit [DataBottomSheet] semantic error identifier type targeting the UI overlay.
     */
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

    /**
     * Validates whether the current primary habit identification name field configuration is populated.
     *
     * @return True if the internal buffer contains a non-empty, usable string literal sequence.
     */
    private fun dataNameIsCorrect():Boolean{
        return _dataAddHabit.value.habitScreen.nameTextField.text.toString().isNotEmpty()
    }

    /**
     * Assesses formatting criteria for generic standard quantizable numeric inputs.
     * Enforces strict safety barriers to reject blank entries, pure zero bounds, and transient decimal artifacts.
     *
     * @return True if the alphanumeric buffer evaluates to a non-zero operational target integer or float.
     */
    private fun textFieldCorrect():Boolean{
        return _dataAddHabit.value.habitScreen.numberTimesTextField.text.toString().isNotEmpty()
                && _dataAddHabit.value.habitScreen.numberTimesTextField.text.toString() != "0"
                && _dataAddHabit.value.habitScreen.numberTimesTextField.text.toString() != "0."
    }

    /**
     * Validates the configuration consistency of recurring gaps for cyclic behavioral tracking frameworks.
     *
     * @return True if the internal operational interval text input buffer contains valid tracking entries.
     */
    private fun cyclicDataIsCorrect():Boolean{
        return _dataAddHabit.value.habitScreen.intervalTextFieldState.text.toString().isNotEmpty()
    }

    /**
     * Checks temporal consistency across hour-split interactive data entry fields.
     * Casts fragmented input fields into standard primitives to guarantee total requested duration yields values above zero.
     *
     * @return True if the aggregated duration elements represent an actual physical window of operational tracking.
     */
    private fun isHourCorrect():Boolean{
        val firstText = _dataAddHabit.value.habitScreen.firstHourTimesTextField.text.toString().toIntOrNull() ?: 0
        val secondText = _dataAddHabit.value.habitScreen.secondHourTimesTextField.text.toString().toIntOrNull() ?: 0

        return (firstText + secondText) > 0
    }

    /**
     * Fetches current transaction-committed reminder datasets from localized repository layers
     * and maps precise hardware scheduling metrics onto the device platform system alarm core kernel.
     *
     * @param id The unique relational entity identifier key mapping to the active habit model.
     */
    private fun setNotifications(id:Long){
        val notifications = roomRepositoryAddHabitUseCase.getAllNotifications(id)
        setNotificationAddHabitUseCase.setAlarm(notifications)
    }

    /**
     * Retrieves historically stored active alarm descriptors assigned to a specific target tracking ID
     * and wipes their registered scheduling slots from the system OS kernel layers.
     *
     * @param id The unique relational identity footprint code of the target habit whose reminders require unbinding.
     */
    private fun cancelNotifications(id:Long){
        val notifications = roomRepositoryAddHabitUseCase.getNotificationsById(id)
        setNotificationAddHabitUseCase.cancelAlarms(notifications)
    }

    /**
     * Performs an inverse structural evaluation (toggle) over the active weekly metric goal visibility tracker.
     */
    fun onCheckedWeeklyChange(){
        _dataAddHabit.update { currentState ->
            currentState.copy(
                habitScreen = currentState.habitScreen.copy(weeklyGoal = !currentState.habitScreen.weeklyGoal)
            )
        }
    }

    /**
     * Performs an inverse structural evaluation (toggle) over the active monthly metric goal visibility tracker.
     */
    fun onCheckedMonthlyChange(){
        _dataAddHabit.update { currentState ->
            currentState.copy(
                habitScreen = currentState.habitScreen.copy(monthlyGoal = !currentState.habitScreen.monthlyGoal)
            )
        }
    }
}

/**
 * Sealed architectural state boundary representing the explicit terminal lifecycles
 * and operational rendering states of the habit creation/modification UI screen loop.
 */
sealed class AddHabitUIState{

    /**
     * Represents a stable presentation layer where form user interactions are fully unlocked,
     * fields are rehydrated, and validation barriers remain satisfied.
     */
    data object Success: AddHabitUIState()

    /**
     * Indicates an active structural transaction pipeline background execution block (e.g., database
     * lookup lookups, cloud ledge synchronizations, or persistent IO commits) during which standard
     * user interactive inputs must be shielded or frozen.
     */
    data object Loading: AddHabitUIState()

    /**
     * Signals an infrastructure transactional execution failure or validation error boundary trigger,
     * typically used to freeze baseline mutation states and deploy contextual error layouts.
     */
    data object Error: AddHabitUIState()

    /**
     * Represents a terminal navigation command event flag signaling that transactions have safely concluded
     * and execution focus should be popped back to the parent master dashboard tracking layout.
     */
    data object ToHabit: AddHabitUIState()
}