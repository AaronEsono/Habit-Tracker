package aeb.proyecto.habit.components.vertical.components.bottomSheet.editHabit

import aeb.proyecto.domain.usecase.habit.GetHabitUseCase
import aeb.proyecto.habit.components.common.bottomSheet.editHabit.state.EditHabitState
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class VerticalEditHabitVM @Inject constructor(
    getHabitUseCase: GetHabitUseCase
): ViewModel() {

    private val _idHabit: MutableStateFlow<Long?> = MutableStateFlow(null)
    val idHabit: Flow<Long?> = _idHabit.asStateFlow()

    fun getIdHabit(id:Long){
        _idHabit.value = id
    }

    val bottomSheetState: StateFlow<EditHabitState> = _idHabit
        .map { id ->
            id ?: return@map EditHabitState.Error("Error inesperado")

            val habit = getHabitUseCase.getHabit(id)
            return@map EditHabitState.Success(habit)
        }
        .catch { id ->
            emit(EditHabitState.Error("Error inesperado"))
        }
        .flowOn(Dispatchers.Default)
        .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = EditHabitState.Loading
    )

}