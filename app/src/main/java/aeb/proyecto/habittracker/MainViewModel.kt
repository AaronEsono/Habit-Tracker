package aeb.proyecto.habittracker

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


    fun setTitleTopBar(title:Int){
        _titleTopBar.value = title

    }

}