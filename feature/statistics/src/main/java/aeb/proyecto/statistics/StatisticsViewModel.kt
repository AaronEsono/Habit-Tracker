package aeb.proyecto.statistics

import aeb.proyecto.domain.usecase.statistics.GetHabitSelectedUseCase
import aeb.proyecto.domain.usecase.statistics.GetHabitsStatisticsUseCase
import aeb.proyecto.statistics.model.StatisticsState
import aeb.proyecto.statistics.model.StatisticsSuccessState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val getHabitsStatisticsUseCase: GetHabitsStatisticsUseCase,
    private val getHabitSelectedUseCase: GetHabitSelectedUseCase
) : ViewModel() {

    // Poner flow que escuche

    val statisticsState: StateFlow<StatisticsState> = combine(
        getHabitsStatisticsUseCase.getAllHabits(),
        getHabitSelectedUseCase.getHabitSelected()
    ) { habits, habitSelected ->
        habits to habitSelected
    }
        .map { (habits, habitSelected) ->
            if (habits.isEmpty()) {
                StatisticsState.Success(StatisticsSuccessState.Empty)
            } else {
                val idSelected = habits.map { it.id }.find { it == habitSelected } ?: habits.first().id
                val habitWithDailyHabit = getHabitsStatisticsUseCase.getHabitWithDailyHabit(idSelected)

                StatisticsState.Success(StatisticsSuccessState.Habits(habits, habitWithDailyHabit))
            }
        }
        .onStart {
            StatisticsState.Loading
        }
        .catch {
            StatisticsState.Error("Error")
        }
        .flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = StatisticsState.Loading
        )

}