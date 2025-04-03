package aeb.proyecto.habittracker.permissions

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
val permissions = android.Manifest.permission.POST_NOTIFICATIONS

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