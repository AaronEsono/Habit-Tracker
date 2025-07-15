package aeb.proyecto.habittracker.components.dialog

import aeb.proyecto.domain.usecase.main.ShowDialogState
import aeb.proyecto.ui.dialog.CustomDialog
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelMediumText
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun ManageDialogScreen (
   state: ShowDialogState,
   onDismissRequest: () -> Unit,
   onConfirm: () -> Unit
){

    when(state){
        ShowDialogState.NoShowDialog -> Unit
        is ShowDialogState.ShowDialog -> {
            CustomDialog(
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth(),
                onDismissRequest = onDismissRequest
            ) {
                Column (
                    modifier = Modifier.fillMaxWidth()
                        .padding(spacing8)
                ){
                    LabelMediumText(state.habit.toString())
                }
            }
        }
    }

}