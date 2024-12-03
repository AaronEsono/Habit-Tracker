package aeb.proyecto.habittracker

import aeb.proyecto.habittracker.data.model.action.ActionIcon
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(

) : ViewModel(){

    private val _titleTopBar =  MutableStateFlow(R.string.bottombar_habit)
    val titleTopBar:StateFlow<Int> = _titleTopBar.asStateFlow()

    private val _iconsTopBar: MutableStateFlow<List<ActionIcon>> = MutableStateFlow(listOf())
    val iconsTopBar: StateFlow<List<ActionIcon>> = _iconsTopBar.asStateFlow()


    fun setTitleTopBar(title:Int){
        _titleTopBar.value = title

    }

    fun setIconsTopBar(icons:List<ActionIcon>){
        _iconsTopBar.value = icons

    }

    fun clearIcons(){
        _iconsTopBar.value = listOf()
    }

}