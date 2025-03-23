package aeb.proyecto.addhabit

import aeb.proyecto.addhabit.constants.GridOption
import aeb.proyecto.addhabit.constants.GridOptionResult
import aeb.proyecto.addhabit.model.DataAddHabit
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AddHabitViewModel @Inject constructor(

):ViewModel() {

    private val _dataAddHabit = MutableStateFlow(DataAddHabit())
    val dataAddHabit = _dataAddHabit.asStateFlow()

    fun onClickGridOption(gridOptionResult: GridOptionResult){
        when(gridOptionResult){
            is GridOptionResult.colorResult -> setColor(gridOptionResult.color)
            is GridOptionResult.iconResult -> setIcon(gridOptionResult.icon)
        }
    }

    fun onClickCard( gridOption: GridOption){
        when(gridOption){
            GridOption.COLORS -> colorGridState()
            GridOption.ICONS -> iconGridState()
        }
    }

    private fun setColor(color:Color){
        _dataAddHabit.update { currentState ->
            currentState.copy(
                color = color,
                isColorSelected = false
            )
        }
    }

    private fun setIcon(icon: ImageVector){
        _dataAddHabit.update { currentState ->
            currentState.copy(
                icon = icon,
                isIconSelected = false
            )
        }
    }

    private fun colorGridState(){
        _dataAddHabit.update { currentState ->
            currentState.copy(
                isColorSelected =  !currentState.isColorSelected,
                isIconSelected = false
            )
        }
    }

    private fun iconGridState(){
        _dataAddHabit.update { currentState ->
            currentState.copy(
                isIconSelected =  !currentState.isIconSelected,
                isColorSelected = false
            )
        }
    }

}