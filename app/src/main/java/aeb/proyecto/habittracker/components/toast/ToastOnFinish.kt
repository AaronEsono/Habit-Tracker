package aeb.proyecto.habittracker.components.toast

import aeb.proyecto.habittracker.R
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource

/**
 * A side-effect driven Composable responsible for managing and displaying a native
 * application success toast notification upon operational completion.
 *
 * It listens to the reactive [state] flag inside a [LaunchedEffect]. When evaluated to true,
 * it triggers a platform-level [Toast] displaying a localized completion message, and
 * immediately dispatches the [clearToast] event boundary callback to reset the upstream source state.
 *
 * @param state A [Boolean] flag indicating whether the completion notification should be triggered.
 * @param clearToast A lambda callback invoked immediately after showing the toast to clear the persistent execution state.
 */
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