package aeb.proyecto.habittracker.ui.screens.saveHabit

import aeb.proyecto.habittracker.utils.AuthenticationManager
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SaveHabitViewModel @Inject constructor(
    private val authenticationManager: AuthenticationManager
):ViewModel() {

    fun logOut(){
        authenticationManager.logOut()
    }

    fun getName():String{
        return authenticationManager.getName()
    }

}