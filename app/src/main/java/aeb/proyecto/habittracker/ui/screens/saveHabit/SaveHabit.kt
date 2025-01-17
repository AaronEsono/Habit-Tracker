package aeb.proyecto.habittracker.ui.screens.saveHabit

import aeb.proyecto.habittracker.ui.screens.saveHabit.saveHabitComponents.SaveHabitScreen
import aeb.proyecto.habittracker.ui.screens.saveHabit.saveHabitComponents.SaveHabitScreenStates
import aeb.proyecto.habittracker.utils.setUpAlarm
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SaveHabit(
    saveHabitViewModel: SaveHabitViewModel = hiltViewModel(),
    navigateToImport: () -> Unit,
) {

    val name = remember { saveHabitViewModel.getName() }
    val date = saveHabitViewModel.date.collectAsState().value
    val stateSaveHabit = saveHabitViewModel.uiState.collectAsState().value
    val context = LocalContext.current

    SaveHabitScreen(name, logOut = {
        saveHabitViewModel.setContextDx(DxInfoSaveHabit.CloseSession)
    }, saveData = {
        saveHabitViewModel.setContextDx(DxInfoSaveHabit.RestoreUploadData)
    }, onDelete = {
        saveHabitViewModel.setContextDx(DxInfoSaveHabit.DeleteData)
    }, restoreData = {
        saveHabitViewModel.setContextDx(DxInfoSaveHabit.RestoreData)
    },date = date)

    SaveHabitScreenStates(stateSaveHabit, onClick = {
        saveHabitViewModel.handleActionDx(){
            navigateToImport()
        }
    }){
        saveHabitViewModel.closeGeneralDx()
    }

    LaunchedEffect (stateSaveHabit.setNotifications){
        if(stateSaveHabit.setNotifications){
            saveHabitViewModel.notificationsWithNames.value.forEach { notification ->
                setUpAlarm(context,notification)
            }
        }
        saveHabitViewModel.setNotifications(false)
    }
}