package aeb.proyecto.habittracker.components.toast

import aeb.proyecto.habittracker.R
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource

@Composable
fun ManageToastFinish(
    state:Boolean,
    clearToast:()->Unit
) {
    val context = LocalContext.current

    LaunchedEffect (state){
        if(state){
            Toast.makeText(
                context,
                context.getText(R.string.dialog_on_finish),
                Toast.LENGTH_LONG
            ).show()
            clearToast()
        }
    }
}