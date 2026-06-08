package aeb.proyecto.addhabit.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

/**
 * Fires an explicit platform intent redirection leading straight into the system settings application
 * details dashboard allocated to this explicit application package sandbox wrapper.
 * Typically utilized as a seamless fallback route if a system runtime permission is permanently denied.
 *
 * @param context The hosting platform execution context pipeline framework.
 */
fun goToAppSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
    }
    context.startActivity(intent)
}

/**
 * Commits a lifecycle-aware reactive observer component loop over the current composable viewport environment.
 * Intercepts Android OS window focus restorations on the application layer [Lifecycle.Event.ON_RESUME] phase
 * to evaluate hardware state alignment markers against required runtime notification permission tokens.
 *
 * @param isPermissionGranted State communication wrapper carrying the dynamic boolean outcome status checklist.
 * @param context The active localization environment framework instance query.
 */
@Composable
fun OnChangePermissions(isPermissionGranted: MutableState<Boolean>, context: Context){
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState = remember { mutableStateOf(Lifecycle.Event.ON_ANY) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            lifecycleState.value = event

            // Re-verify framework status structures precisely as the user regains active viewport focus
            if (event == Lifecycle.Event.ON_RESUME) {
                isPermissionGranted.value = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            // Guarantee localized garbage collection to eliminate lingering leak footprints across lifecycle scopes
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}