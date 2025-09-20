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

@HiltViewModel
class SelectDateViewModel @Inject constructor(
    private val calendarDataSource: CalendarDataSource,
    private val firstDayProvider: RegionFirstDayProvider
):ViewModel(){

    private val _yearMonth:MutableStateFlow<YearMonth> = MutableStateFlow(YearMonth.now())
    val yearMonth:StateFlow<YearMonth> = _yearMonth

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

    fun onMonthButtonClicked(yearMonth: YearMonth){
        _yearMonth.update {yearMonth}
    }

    fun initMonth(){
        _yearMonth.update { YearMonth.now() }
    }
}