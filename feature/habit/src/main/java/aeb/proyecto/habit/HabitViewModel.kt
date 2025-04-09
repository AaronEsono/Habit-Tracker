package aeb.proyecto.habit

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HabitViewModel @Inject constructor(

):ViewModel() {
}

sealed class HabitUIState(){
    data object Success:HabitUIState()
    data object Error:HabitUIState()
    data object Loading:HabitUIState()
}