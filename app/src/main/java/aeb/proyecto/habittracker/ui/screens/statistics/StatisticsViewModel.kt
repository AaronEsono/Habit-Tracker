package aeb.proyecto.habittracker.ui.screens.statistics

import aeb.proyecto.habittracker.data.entities.DailyHabit
import aeb.proyecto.habittracker.data.entities.Habit
import aeb.proyecto.habittracker.data.repo.DailyHabitRepo
import aeb.proyecto.habittracker.data.repo.HabitRepo
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val habitRepo: HabitRepo,
    private val dailyHabitRepo: DailyHabitRepo
) : ViewModel() {

    val habits:StateFlow<List<Habit>> = habitRepo.getAllHabits()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Mantener activo mientras hay suscriptores
            initialValue = emptyList() // Valor inicial vacío
        )

    private val _dailyHabits:MutableStateFlow<List<DailyHabit>> = MutableStateFlow(listOf())
    val dailyHabits:StateFlow<List<DailyHabit>> = _dailyHabits.asStateFlow()

    fun getDailyHabits(id:Long) = viewModelScope.launch(Dispatchers.IO) {
        _dailyHabits.value = dailyHabitRepo.getDailyHabits(id)
    }

    //Calendario dias completados
    //Numero veces completado
    //Racha actual
    //Mejor racha y fecha
    //Media de hora en la que el habito se hace
    //Chart circular con los completados, no completados y pendientes

}