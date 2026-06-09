package aeb.proyecto.habit.components.common.bottomSheet.selectDate

import aeb.proyecto.language.provider.RegionFirstDayProvider
import aeb.proyecto.ui.calendar.model.CalendarUIState
import aeb.proyecto.ui.calendar.source.CalendarDataSource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.YearMonth
import javax.inject.Inject

/**
 * ViewModel responsible for managing the state and logic of the date selection calendar.
 *
 * This ViewModel handles the navigation between months and provides a reactive
 * [CalendarUIState] stream based on the selected [YearMonth]. It utilizes Hilt for
 * dependency injection of the [CalendarDataSource] and [RegionFirstDayProvider].
 *
 * @param calendarDataSource Data source used to fetch the dates for a given month.
 * @param firstDayProvider Provider that determines the start of the week based on user locale.
 */
@HiltViewModel
class SelectDateViewModel @Inject constructor(
    private val calendarDataSource: CalendarDataSource,
    private val firstDayProvider: RegionFirstDayProvider
):ViewModel(){

    /**
     * Internal mutable state flow holding the currently selected year and month.
     */
    private val _yearMonth:MutableStateFlow<YearMonth> = MutableStateFlow(YearMonth.now())

    /**
     * Publicly exposed state flow for observing the current year and month.
     */
    val yearMonth:StateFlow<YearMonth> = _yearMonth

    /**
     * Reactive state stream that holds the [CalendarUIState] for the calendar.
     *
     * Reacts to changes in [_yearMonth] by fetching the corresponding dates through
     * the [CalendarDataSource] and updating the UI state.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val calendarUIState: StateFlow<CalendarUIState<Unit>> = _yearMonth
        .flatMapLatest { yearMonth ->
            flow {
                val dates = calendarDataSource.getDates(
                    firstDayProvider.getFirstDayOfWeekByLocale(),
                    yearMonth
                ) { _ -> Unit }
                emit(CalendarUIState(dates))
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CalendarUIState.init()
        )

    /**
     * Updates the current month state based on user interaction.
     *
     * @param yearMonth The new [YearMonth] to display in the calendar.
     */
    fun onMonthButtonClicked(yearMonth: YearMonth){
        _yearMonth.update {yearMonth}
    }

    /**
     * Resets the current [YearMonth] to the present month.
     */
    fun initMonth(){
        _yearMonth.update { YearMonth.now() }
    }
}