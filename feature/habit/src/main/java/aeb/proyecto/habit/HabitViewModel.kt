package aeb.proyecto.habit

import aeb.proyecto.domain.usecase.habit.GetDailyHabitUseCase
import aeb.proyecto.domain.usecase.habit.GetHabitUseCase
import aeb.proyecto.domain.usecase.habit.GetTypesOfHabitUseCase
import aeb.proyecto.domain.usecase.habit.HabitDatastoreUseCase
import aeb.proyecto.habit.constants.rangeDays
import aeb.proyecto.habit.constants.stopTimeOutMillis
import aeb.proyecto.habit.model.BottomSheetUIState
import aeb.proyecto.habit.model.TypeBottomSheet
import aeb.proyecto.habit.model.pager.PagerElement
import aeb.proyecto.habit.model.pager.PagerSelected
import aeb.proyecto.habit.model.pager.findPagerElement
import aeb.proyecto.habit.model.pager.orderPagerElements
import aeb.proyecto.habit.utils.initializeSelectedTypeIfNeeded
import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.HabitDay
import aeb.proyecto.room.entities.relations.HabitWithDailyHabit
import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.room.model.classes.DAILY_TAG
import aeb.proyecto.room.model.classes.MONTHLY_TAG
import aeb.proyecto.room.model.classes.RECURRING_TAG
import aeb.proyecto.room.model.classes.WEEKLY_TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

/**
 * Core transactional architecture engine managing the Habit Dashboard ecosystem.
 * Coordinates reactive data streams, evaluates complex user preferences asynchronously via DataStore,
 * and handles localized database state query pipelines to emit immutable structural UI states.
 *
 * @property getTypesOfHabitUseCase Resolves available metadata categories to feed the horizontal selection pager track.
 * @property getDailyHabitUseCase Manages granular iteration logs tracking operational habit completion inputs.
 * @property habitDatastoreUseCase Handles persistent low-latency user preferences configurations asynchronously.
 * @property getHabitUseCase Intercepts baseline structural habit definitions from core repository systems.
 */
@HiltViewModel
class HabitViewModel @Inject constructor(
    private val getTypesOfHabitUseCase: GetTypesOfHabitUseCase,
    private val getDailyHabitUseCase: GetDailyHabitUseCase,
    private val habitDatastoreUseCase: HabitDatastoreUseCase,
    private val getHabitUseCase: GetHabitUseCase
):ViewModel() {

    // ============================================================================
    // INTERNAL MUTABLE SYSTEM TRACK FLOWS (PRIVATE STATE TRACES)
    // ============================================================================

    /** Core baseline localized date coordinate currently focused by the user viewport session. */
    private val _selectedDate = MutableStateFlow(LocalDate.now())

    /** Functional navigation track mapping the active category node chosen within the top horizontal layout tab. */
    private val _currentPagerType  = MutableStateFlow<CurrentPagerSelection>(CurrentPagerSelection.Uninitialized)

    /** Encapsulates layout constraints and text buffers feeding dynamic interaction contextual trays. */
    private val _bottomSheetUIState = MutableStateFlow(BottomSheetUIState())

    /** Internal synchronization toggle commanding pipeline resets over calculated operational calendar windows. */
    private val _reStartSelectedRange = MutableStateFlow(false)

    /** Internal cold-to-hot data bridge tracking persistent localized calendar baseline metrics. */
    private val _startDayOfWeek:StateFlow<DayOfWeek?> = habitDatastoreUseCase.startDayOfWeek
        .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeOutMillis),
        initialValue = null
    )

    // ============================================================================
    // IMMUTABLE READ-ONLY CHANNELS EXPOSED UNIDIRECTIONALLY TO PRESENTATION LAYERS
    // ============================================================================
    val selectedDate:StateFlow<LocalDate> = _selectedDate.asStateFlow()
    val currentPagerType : StateFlow<CurrentPagerSelection> = _currentPagerType.asStateFlow()
    val bottomSheetUIState: StateFlow<BottomSheetUIState> = _bottomSheetUIState.asStateFlow()
    val startDayOfWeek: StateFlow<DayOfWeek?> = _startDayOfWeek

    // ============================================================================
    // ADVANCED REACTIVE DATA STREAMS (COMPUTED WORKSPACE LIFECYCLE CHANNELS)
    // ============================================================================

    /**
     * Resolves, sorts, and establishes runtime infrastructure checkmarks over the horizontal category paginator.
     * Intercepts structural database types, maps them onto structural presentation UI atoms, maps functional sorting
     * matrix layouts, and safely initializes operational selection memory anchors asynchronously.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val availablePagerTypesUiState: StateFlow<PagerTypesUiState> = getTypesOfHabitUseCase()
        .map { types ->
            types.map { findPagerElement(it) }
                .sortedBy { orderPagerElements.indexOf(it) }
        }
        .flatMapLatest { sortedTypes ->
            flow {
                val initialized = initializeSelectedTypeIfNeeded(
                    sortedTypes = sortedTypes,
                    selectedType = _currentPagerType,
                    updateSelected = { _currentPagerType.value = it },
                    habitDatastoreUseCase = habitDatastoreUseCase,
                )

                emit(
                    if (initialized) PagerTypesUiState.Success(sortedTypes)
                    else PagerTypesUiState.Error
                )
            }
        }
        .catch {
            emit(PagerTypesUiState.Error)
        }
        .flowOn(Dispatchers.Default) // Delegates list mutation processing away from the main interaction thread safely
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeOutMillis),
            initialValue = PagerTypesUiState.Loading
        )

    /**
     * Computes localized temporal evaluation boundaries reactively based on targeted workflow strategy tokens.
     * Monitors active focus changes across chosen dates, active category tabs, and system localization baselines.
     * Uses state memory operators to eliminate redundant computations if coordinates sit inside cached windows.
     */
    val selectedTimeRangeUiState: StateFlow<TimeRangeUiState> =
        combine(_selectedDate, _currentPagerType, _startDayOfWeek) { date, selected, dayOfWeek ->
            Triple(date, selected.getTag(), dayOfWeek)
        }.scan<Triple<LocalDate, String?, DayOfWeek?>, TimeRangeUiState>(TimeRangeUiState.Empty) { previousState, (date, tag, dayOfWeek) ->

            // Dynamic strategy routing mapping the active structural data token rules
            when (tag) {
                DAILY_TAG-> {
                    val prevRange = (previousState as? TimeRangeUiState.Daily)?.days.orEmpty()
                    if (date in prevRange && !_reStartSelectedRange.value) {
                        previousState // Defend layout state: drop calculation overhead if focused index remains valid
                    } else {
                        val newDays = rangeDays.map { date.plusDays(it.toLong()) }
                        TimeRangeUiState.Daily(newDays)
                    }
                }

                RECURRING_TAG-> {
                    val prevRange = (previousState as? TimeRangeUiState.Recurring)?.days.orEmpty()
                    if (date in prevRange && !_reStartSelectedRange.value) {
                        previousState // Defend layout state: drop calculation overhead if focused index remains valid
                    } else {
                        val newDays = rangeDays.map { date.plusDays(it.toLong()) }
                        TimeRangeUiState.Recurring(newDays)
                    }
                }

                WEEKLY_TAG -> {
                    if (dayOfWeek == null) return@scan TimeRangeUiState.Empty

                    // Calculate localized current weekly baseline span coordinates cleanly
                    val startOfWeek = date.with(TemporalAdjusters.previousOrSame(dayOfWeek))
                    val endOfWeek = startOfWeek.plusDays(6)
                    TimeRangeUiState.Weekly(startOfWeek, endOfWeek)
                }

                MONTHLY_TAG -> {
                    // Calculate accurate localized monthly limits tracking leap years dynamically
                    val start = date.withDayOfMonth(1)
                    val end = date.withDayOfMonth(date.lengthOfMonth())
                    TimeRangeUiState.Monthly(start, end)
                }

                else -> TimeRangeUiState.Empty
            }
        }.flowOn(Dispatchers.Default)
            .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeOutMillis),
            initialValue = TimeRangeUiState.Empty
        )

    // ============================================================================
    // PRIMARY DOMAIN INGESTION STREAM (REACTIVE REPOSITORY DATA COUPLER)
    // ============================================================================

    /**
     * Resolves and maps the finalized list of habits along with their operational progress logs.
     * Monitors updates across the active temporal boundaries and the focused session date, triggering
     * a polymorphic switch that dispatches optimal, non-blocking queries downstream to the Room repository.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val habitsForSelectedTimeUiState: StateFlow<FilteredHabitsUiState> = combine(
        selectedTimeRangeUiState,
        selectedDate
    ){timeState,date -> Pair(timeState,date) }
        .flatMapLatest { (timeState,date) ->
            // Route structural database fetchers based on active temporal strategy constraints
            when (timeState) {
                // STRATEGY A: COMPUTE SINGLE-DAY ACTIVE STANDARD DAILY RECORDS
                is TimeRangeUiState.Daily -> {
                    getDailyHabitUseCase.getDailyHabitsByType(date,date, DAILY_TAG) // O usa el tag real
                        .map<List<HabitWithDailyHabit>, FilteredHabitsUiState> {FilteredHabitsUiState.Success(it) }
                        .catch { emit(FilteredHabitsUiState.Error) }
                }

                // STRATEGY B: COMPUTE SINGLE-DAY RECURRING INTERVAL CYCLIC RECORDS
                is TimeRangeUiState.Recurring -> {
                    getDailyHabitUseCase.getDailyHabitsByType(date,date, RECURRING_TAG) // O usa el tag real
                        .map<List<HabitWithDailyHabit>, FilteredHabitsUiState> { FilteredHabitsUiState.Success(it) }
                        .catch { emit(FilteredHabitsUiState.Error) }
                }

                // STRATEGY C: COMPUTE GRANULAR FULL-WEEK RANGE RECORDS BOUNDARIES
                is TimeRangeUiState.Weekly -> {
                    getDailyHabitUseCase.getDailyHabitsByType(timeState.startOfWeek, timeState.endOfWeek, WEEKLY_TAG)
                        .map<List<HabitWithDailyHabit>, FilteredHabitsUiState> { FilteredHabitsUiState.Success(it) }
                        .catch { emit(FilteredHabitsUiState.Error) }
                }

                // STRATEGY D: COMPUTE FULL MONTHLY RANGE LIFECYCLE RECORDS BOUNDARIES
                is TimeRangeUiState.Monthly -> {
                    getDailyHabitUseCase.getDailyHabitsByType(timeState.startOfMonth, timeState.endOfMonth, MONTHLY_TAG)
                        .map<List<HabitWithDailyHabit>, FilteredHabitsUiState> { FilteredHabitsUiState.Success(it) }
                        .catch { emit(FilteredHabitsUiState.Error) }
                }

                // FALLBACK ROUTE: CLEAR WORKSPACE WORKFLOW TRACES
                else -> flowOf(FilteredHabitsUiState.Empty)
            }
        }
        .flowOn(Dispatchers.Default) // Ensures IO-bound mapping operations execute separated from rendering frame pipelines
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(stopTimeOutMillis),
            FilteredHabitsUiState.Loading
        )

    // ============================================================================
    // OPERATIONAL INTERACTION HANDLERS (USER-DRIVEN MUTATION DISPATCHERS)
    // ============================================================================

    /**
     * Commits category mutations across the horizontal pager system.
     * Evaluates active state instances defensively, extracts element positioning indexes,
     * shifts systemic focus parameters, and persists chosen tag structures asynchronously into DataStore memory fields.
     *
     * @param pagerElement Targeted structural item selected by user interaction over the TabRow matrix.
     */
    fun onPagerTypeSelected(pagerElement: PagerElement) = viewModelScope.launch{
        (availablePagerTypesUiState.value as? PagerTypesUiState.Success)?.availableTypes
            ?.indexOfFirst { it == pagerElement }
            ?.takeIf { it >= 0 }
            ?.let { index ->
                _currentPagerType.value = CurrentPagerSelection.Selected(PagerSelected(index, pagerElement))
                habitDatastoreUseCase.setSelectedHabitType(pagerElement.tag)
            }
    }

    /**
     * Commits temporal focus shifts inside active evaluation windows.
     *
     * @param date Targeted localized calendar marker chosen by the viewport cursor session.
     * @param reStart Synchronization indicator commanding internal pipeline overrides across cached window boundaries.
     */
    fun onClickTimeRange(date:LocalDate, reStart: Boolean){
        _reStartSelectedRange.value = reStart
        _selectedDate.update { date }
    }

    /**
     * Mounts and inflates the contextual date selector sheet onto the visible presentation tree layout.
     */
    fun onBottomSheetSelectDateSelected() {
        _bottomSheetUIState.update { currentState ->
            currentState.copy(
                enabledSelectDateState = TypeBottomSheet.SelectDate(true),
            )
        }
    }

    /**
     * Clears visual tracking indicators, shifting specific bottom sheets out of execution trees safely.
     *
     * @param typeBottomSheet Contextual polymorphic indicator tracking the active modal sheet category target.
     */
    fun onDismissBottomSheet(typeBottomSheet: TypeBottomSheet){
        when(typeBottomSheet){
            is TypeBottomSheet.ConfigureHabit -> {
                _bottomSheetUIState.update { currentState ->
                    currentState.copy(
                        enabledConfigureHabitState = TypeBottomSheet.ConfigureHabit(enabled =false)
                    )
                }
            }
            is TypeBottomSheet.SelectDate -> {
                _bottomSheetUIState.update { currentState ->
                    currentState.copy(
                        enabledSelectDateState = TypeBottomSheet.SelectDate(false),
                    )
                }
            }
            is TypeBottomSheet.EditHabit -> {
                _bottomSheetUIState.update { currentState ->
                    currentState.copy(
                        enabledEditHabitState = TypeBottomSheet.EditHabit(enabled = false),
                    )
                }
            }
            is TypeBottomSheet.DeleteHabit -> {
                _bottomSheetUIState.update { currentState ->
                    currentState.copy(
                        enabledDeleteHabitState = TypeBottomSheet.DeleteHabit(enabled = false),
                    )
                }
            }
        }
    }

    // ============================================================================
    // TRANSACTIONAL MUTATION WORKFLOWS (DATABASE UTILITY READ/WRITE DISPATCHERS)
    // ============================================================================

    /**
     * Gathers structural metadata and mounts the operational habit logging configuration tray.
     * Intercepts persistent tracking targets from background streams safely on an IO threads context,
     * evaluates baseline status structures, and triggers the dynamic configure sheet onto the active UI tree.
     *
     * @param id Persistent database identifier mapping the core structural habit node.
     * @param date Localized timeline marker target focused during execution.
     */
    fun onClick(id:Long,date: LocalDate) = viewModelScope.launch (Dispatchers.IO){
        val habit = findHabit(id)
        val habitDay = findDay(id,date) ?: HabitDay(id = habit.id, date = date)

        _bottomSheetUIState.update { currentState ->
            currentState.copy(
                enabledConfigureHabitState = TypeBottomSheet.ConfigureHabit(enabled =true, habitWithDay = HabitWithDay(habit,habitDay))
            )
        }
    }

    /**
     * Inflates the secondary configuration management sheet to alter core structural habit setups.
     *
     * @param id Persistent database identifier mapping the core structural habit node.
     */
    fun onClickCard(id:Long){
        _bottomSheetUIState.update { currentState ->
            currentState.copy(
                enabledEditHabitState = TypeBottomSheet.EditHabit(enabled = true, idHabit = id)
            )
        }
    }

    /**
     * Commits incremental progress logs updates into the local persistence schema.
     * Evaluates active iteration tracking logs dynamically on an IO execution pipeline: updates and accumulates
     * existing records using safe high-precision additions, or constructs a new transactional node if it's the
     * opening entry of the day.
     *
     * @param id Persistent database identifier mapping the core structural habit node.
     * @param date Localized timeline marker targeted to attach progress calculations.
     * @param goalDone Quantitative precision value tracking the new increment achieved.
     */
    fun onClickConfigureHabit(id:Long,date: LocalDate,goalDone: BigDecimal) = viewModelScope.launch (Dispatchers.IO){
        val habit = findHabit(id)
        val habitDay = findDay(id,date)

        if(habitDay != null){
            // TARGET LOCATED: Commit additive incremental update operations cleanly
            val updatedHabitDay = habitDay.copy(
                goalDone = habitDay.goalDone.plus(goalDone),
                hourFinishDate = LocalTime.now()
            )
            getDailyHabitUseCase.updateHabitDay(updatedHabitDay)
        }else{
            // TARGET EMPTY: Structure an initial baseline transactional record node
            val newHabitDay = HabitDay(
                idHabit = habit.id,
                date = date,
                goalDone = goalDone,
                hourFinishDate = LocalTime.now()
            )
            getDailyHabitUseCase.insertHabitDay(newHabitDay)
        }

    }

    /**
     * Fast-tracks complete metric fulfillment loops for the focused date criteria instantly.
     * Intercepts execution via long-press gestures, bypassing modal sheets entirely to force-inject
     * the maximum baseline metric targets directly into the persistence logs layer.
     *
     * @param id Persistent database identifier mapping the core structural habit node.
     * @param date Localized timeline marker targeted to force fulfillment logs.
     */
    fun onLongClick(id:Long,date: LocalDate) = viewModelScope.launch (Dispatchers.IO){
        val habit = findHabit(id)
        val habitDay = findDay(id,date)

        if(habitDay != null){
            // TARGET LOCATED: Elevate current metrics cleanly to maximum goal thresholds
            val updatedHabitDay = habitDay.copy(
                goalDone = habit.goal,
                hourFinishDate = LocalTime.now()
            )
            getDailyHabitUseCase.updateHabitDay(updatedHabitDay)
        }else{
            // TARGET EMPTY: Initialize a new record pre-filled directly with maximum target metrics
            val newHabitDay = HabitDay(
                idHabit = habit.id,
                date = date,
                goalDone = habit.goal,
                hourFinishDate = LocalTime.now()
            )
            getDailyHabitUseCase.insertHabitDay(newHabitDay)
        }
    }

    // ============================================================================
    // TRANSACTIONAL DESTRUCTION & PERSISTENT SERIALIZATION WORKFLOWS
    // ============================================================================

    /**
     * Resets progress log indices for the selected date criteria instantly.
     * Purges operational record metrics from the persistence database framework running isolated on an IO thread pool.
     *
     * @param id Persistent database identifier mapping the core structural habit node.
     * @param date Localized timeline marker targeted to wipe logged progress metrics.
     */
    fun onRestart(id:Long,date: LocalDate) = viewModelScope.launch (Dispatchers.IO){
        getDailyHabitUseCase.deleteHabitDay(id,date)
    }

    /**
     * Executes absolute cascade elimination loops over core structural habit entities.
     * Permanently purges structural setups along with historical progress tracker logs from backend storage tracks.
     *
     * @param id Persistent database identifier mapping the core structural habit node slated for deletion.
     */
    fun onAcceptDeleteHabit(id:Long) = viewModelScope.launch (Dispatchers.IO){
        getDailyHabitUseCase.deleteHabit(id)
    }

    /**
     * Mounts the destructive confirmation modal alert interface onto the active layout presentation tree.
     * Passes specific identity hashes and theme token signatures to maintain design system uniformity.
     *
     * @param id Persistent database identifier mapping the core structural habit node.
     * @param color Chromatic theme token integer signature attached to the targeted habit.
     */
    fun onClickDelete(id:Long, color:Int) {
        _bottomSheetUIState.update { currentState ->
            currentState.copy(
                enabledDeleteHabitState = TypeBottomSheet.DeleteHabit(enabled = true, id = id, color = color)
            )
        }
    }

    /**
     * Serializes execution snapshots within low-latency preference storage layers before navigating out of context.
     * Commits programmatic identification hashes, calendar footprints, and incremental metrics securely,
     * then executes the forward navigation routing bridge toward the chronometer dashboard workspace.
     *
     * @param data Combined metadata state token holding ID (Long), Target ISO-8601 Date (String), and Progress (BigDecimal).
     * @param navigate Asynchronous routing lambda callback commanding the application navigation controller graph.
     */
    fun onClickTimerHabit(data:Triple<Long,String,BigDecimal>, navigate: () -> Unit) = viewModelScope.launch{
        habitDatastoreUseCase.setTimerFromHabit(data.first, LocalDate.parse(data.second),data.third)
        navigate()
    }

    // ============================================================================
    // PRIVATE INTERNAL ATOMIC UTILITY SEARCHERS (CONTEXT-BOUND HELPERS)
    // ============================================================================

    /**
     * Queries internal persistence layers to locate specific single-day logging entries.
     * Runs synchronously, inheriting the operational thread dispatcher context of its calling block scope.
     */
    private fun findDay(id:Long,date: LocalDate): HabitDay? {
        return getDailyHabitUseCase.getDailyHabitByDate(id,date)
    }

    /**
     * Queries internal persistence layers to extract structural core habit schema metrics.
     * Runs synchronously, inheriting the operational thread dispatcher context of its calling block scope.
     */
    private fun findHabit(id:Long):Habit{
        return getHabitUseCase.getHabit(id)
    }
}

// ============================================================================
// PAGER TYPES WORKSPACE UI STATES
// ============================================================================

/**
 * Represents the asynchronous runtime lifecycle checkpoints for the top-level horizontal navigation pager elements.
 */
sealed class PagerTypesUiState(){

    /**
     * State emitted when pager options are successfully resolved and populated from the data layer.
     * @param availableTypes Immutable collection containing structural [PagerElement] structural coordinates.
     */
    data class Success(val availableTypes:List<PagerElement>):PagerTypesUiState()

    /**
     * Fallback state representing pipeline processing anomalies or infrastructure failures.
     */
    data object Error:PagerTypesUiState()

    /**
     * Defensive layout blocking state indicating active asynchronous compilation streams.
     */
    data object Loading:PagerTypesUiState()
}

// ============================================================================
// FILTERED HABITS DISPATCH UI STATES
// ============================================================================

/**
 * Represents the operational layout boundaries mapping queries targeted over custom habit aggregates.
 */
sealed class FilteredHabitsUiState(){

    /**
     * State emitted when query boundaries match valid database records.
     * @param habits The resolved collection of combined structural data tokens and relational iteration logs.
     */
    data class Success(val habits:List<HabitWithDailyHabit>):FilteredHabitsUiState()

    /**
     * Fallback state representing underlying database runtime query exceptions.
     */
    data object Error:FilteredHabitsUiState()

    /**
     * Indicates active background database fetching sequences.
     */
    data object Loading:FilteredHabitsUiState()

    /**
     * Structural success boundary indicating that the query criteria returned zero execution footprints.
     */
    data object Empty:FilteredHabitsUiState()
}

// ============================================================================
// CONTEXTUAL VIEWPORT NAVIGATION MARKS
// ============================================================================

/**
 * Orchestrates directional coordinate focal points tracking active navigation indexes over viewport swiping matrices.
 */
sealed class CurrentPagerSelection {

    /**
     * Safe uninitialized anchor preventing speculative recompositions during cold boots.
     */
    data object Uninitialized : CurrentPagerSelection()

    /**
     * Active state containing the physical metadata reference chosen by the user.
     * @param pagerSelected Operational wrapper model holding the currently selected coordinate parameters.
     */
    data class Selected(val pagerSelected: PagerSelected) : CurrentPagerSelection()

    /**
     * Safe evaluation bridge designed to poll identifier tokens directly out of the tracking layer.
     * @return The underlying tag string identifier if the stream holds active selections, null otherwise.
     */
    fun getTag(): String? = (this as? Selected)?.pagerSelected?.pagerElement?.tag
}

// ============================================================================
// TEMPORAL CALENDAR BOUNDARY METRICS
// ============================================================================

/**
 * Encapsulates the polymorphic calculation windows utilized to filter active habit evaluation routines.
 */
sealed class TimeRangeUiState {

    /**
     * Represents undefined temporal coordinates or cleared calendar scope trackers.
     */
    data object Empty : TimeRangeUiState()

    /**
     * Discrete collection array matching atomic standard daily timelines.
     * @param days Array sequence tracking the active localized date markers.
     */
    data class Daily(val days: List<LocalDate>) : TimeRangeUiState()

    /**
     * Discrete collection array tracking rolling interval customized recurrence targets.
     * @param days Array sequence mapping calculated target rolling interval dates.
     */
    data class Recurring(val days: List<LocalDate>) : TimeRangeUiState()

    /**
     * Isolated single-week boundary span mapping standard calendar matrices.
     * @param startOfWeek Localized timestamp pinning the exact configuration baseline day.
     * @param endOfWeek Localized timestamp marking the exact calculated operational ceiling day.
     */
    data class Weekly(val startOfWeek: LocalDate, val endOfWeek: LocalDate) : TimeRangeUiState()

    /**
     * Isolated single-month boundary span mapping full calendar cycle segments.
     * @param startOfMonth Localized timestamp pinning the opening baseline coordinate day.
     * @param endOfMonth Localized timestamp marking the closing programmatic ceiling day.
     */
    data class Monthly(val startOfMonth: LocalDate, val endOfMonth: LocalDate) : TimeRangeUiState()
}