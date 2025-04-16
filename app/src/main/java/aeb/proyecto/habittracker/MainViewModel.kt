package aeb.proyecto.habittracker

import aeb.proyecto.datastore.DatastoreInterface
import aeb.proyecto.language.LanguageInterface
import aeb.proyecto.language.model.EnumLanguage
import aeb.proyecto.language.model.findLanguage
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.temporal.WeekFields
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val datastoreInterface: DatastoreInterface
) : ViewModel(){

    private val _dataSet = MutableStateFlow(false)

    val themeMode: StateFlow<Int> = datastoreInterface.themeMode.stateIn(
        scope = viewModelScope,
        initialValue = 0,
        started = SharingStarted.WhileSubscribed(5000)
    )

    fun setData() = viewModelScope.launch{
        if(!_dataSet.value){
            setDayWeek()
            setLanguage()
            _dataSet.value = true
        }
    }

    //Seteamos el primer dia se la semana si no está
    private suspend fun setDayWeek(){
        val day = datastoreInterface.getDayStartWeek()
        day ?: datastoreInterface.setFirstDayOfWeek()
    }

    //Seteamos el idioma si no esta
    private suspend fun setLanguage(){
        if(datastoreInterface.getLanguage() == null){
            val language = Locale.getDefault().language

            if(findLanguage(language) != null){
                datastoreInterface.setLanguage(language)
            }else{
                //Por defecto, inglés
                datastoreInterface.setLanguage(EnumLanguage.ENGLISH.value)
            }
        }
    }
}