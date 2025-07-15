package aeb.proyecto.habittracker

import aeb.proyecto.datastore.DatastoreInterface
import aeb.proyecto.domain.usecase.main.ManageDatastoreUseCase
import aeb.proyecto.domain.usecase.main.ManageDialogTimerUseCase
import aeb.proyecto.domain.usecase.main.ShowDialogState
import aeb.proyecto.language.model.EnumLanguage
import aeb.proyecto.language.model.findLanguage
import aeb.proyecto.language.provider.RegionFirstDayProvider
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val manageDatastoreUseCase: ManageDatastoreUseCase,
    private val firstDayProvider: RegionFirstDayProvider,
    manageDialogTimerUseCase: ManageDialogTimerUseCase
) : ViewModel(){

    private val _dataSet = MutableStateFlow(false)

    val showDialogTimer:StateFlow<ShowDialogState> = manageDialogTimerUseCase.showDialogTimer
        .stateIn(
            scope = viewModelScope,
            initialValue = ShowDialogState.NoShowDialog,
            started = SharingStarted.WhileSubscribed(5000)
        )

    val themeMode: StateFlow<Int> = manageDatastoreUseCase.themeMode.stateIn(
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
        val day = manageDatastoreUseCase.getDayOfWeek()
        if(day == null){
            val firstDay = firstDayProvider.getFirstDayOfWeekByLocale().name
            manageDatastoreUseCase.setDayOfWeek(firstDay)
        }
    }

    //Seteamos el idioma si no esta
    private suspend fun setLanguage(){
        if(manageDatastoreUseCase.getLanguage() == null){
            val language = Locale.getDefault().language

            if(findLanguage(language) != null){
                manageDatastoreUseCase.setLanguage(language)
            }else{
                //Por defecto, inglés
                manageDatastoreUseCase.setLanguage(EnumLanguage.ENGLISH.value)
            }
        }
    }

    fun closeDialog() = viewModelScope.launch{
        manageDatastoreUseCase.closeDialog()
    }

    fun updateHabit() = viewModelScope.launch {

    }

}