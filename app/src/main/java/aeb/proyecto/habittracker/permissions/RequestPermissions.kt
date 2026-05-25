package aeb.proyecto.habittracker.permissions

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

/**
 * The specific runtime permission required to display local and scheduled notifications
 * starting from Android 13 (API level 33, Tiramisu) onwards.
 */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
val permissions = android.Manifest.permission.POST_NOTIFICATIONS

/**
 * A side-effect driven Composable function responsible for requesting runtime user permissions.
 *
 * It utilizes [rememberLauncherForActivityResult] to safe-register an activity result contract
 * with the operating system. Triggered precisely once inside a [LaunchedEffect], it targets
 * dynamic system notifications setup rules for Android 13+ devices, ensuring the user is prompted
 * to grant alert capabilities right at the application's foundational initialization stage.
 */
@Composable
fun RequestPermissions(){
    val request = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ){}
    LaunchedEffect(Unit) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            request.launch(permissions)
        }
    }
}