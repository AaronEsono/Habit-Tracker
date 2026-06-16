package aeb.proyecto.settings.utils

import aeb.proyecto.settings.BuildConfig
import aeb.proyecto.settings.R
import aeb.proyecto.settings.constants.SettingsConstants
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.content.ContextCompat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

/**
 * Navigates the user directly to the link selected
 */
fun openLink(context: Context, uri:String){
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
    context.startActivity(intent)
}

/**
 * Pre-populates an email client with app version and device information.
 * Useful for support and feedback loops.
 */
fun sendEmail(context:Context){
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = "mailto:".toUri() // Especifica el esquema mailto
        putExtra(Intent.EXTRA_EMAIL, arrayOf(SettingsConstants.EMAIL)) // Correo del destinatario
        putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.settings_email_title)) // Asunto opcional
        putExtra(
            Intent.EXTRA_TEXT,
            context.getString(R.string.settings_message,
                getLocalDataDevice(),
                getGetLocalDateTime(),
                BuildConfig.APP_VERSION)
        )
    }

    context.startActivity(intent)
}

/**
 * Navigates the user directly to the system overlay permission screen
 * for this specific package.
 */
fun openOverlayPermissionSettings(context: Context) {
    val packageName = context.packageName
    val intent = Intent(
        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
        "package:$packageName".toUri()
    )
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
}

/**
 * Composable observer that monitors if the 'Draw over other apps' permission
 * was granted when the user returns to the app.
 */
@Composable
fun OnChangeOverlay(isOverlayActivated: MutableState<Boolean>, context: Context){
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState = remember { mutableStateOf(Lifecycle.Event.ON_ANY) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            lifecycleState.value = event
            if (event == Lifecycle.Event.ON_RESUME) {
                isOverlayActivated.value = Settings.canDrawOverlays(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

/**
 * Returns a human-readable string representing the current device hardware.
 */
private fun getGetLocalDateTime():String{
    val localDateTime = LocalDateTime.now()
    val zoneId = ZoneId.systemDefault() // Obtienes la zona horaria por defecto del sistema
    val zonedDateTime = ZonedDateTime.of(localDateTime, zoneId)
    return zonedDateTime.toString()
}

/**
 * Returns the current date-time in the system's timezone,
 * formatted as a standardized ISO string for consistent logging.
 */
private fun getLocalDataDevice():String{
    val deviceName = Build.MODEL // Modelo del dispositivo
    val deviceBrand = Build.BRAND // Marca del dispositivo

    return "$deviceName $deviceBrand"
}