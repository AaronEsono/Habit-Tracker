package aeb.proyecto.habittracker.ui.screens.saveHabit

import aeb.proyecto.habittracker.data.model.user.UserData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SaveHabitViewModel @Inject constructor():ViewModel() {

    fun logOut(){
        val data = UserData
        data.email = null
        data.uid = null
    }

}