package aeb.proyecto.stopwatch.di

import android.content.Context
import android.os.Build
import android.os.Vibrator
import android.os.VibratorManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

/**
 * Singleton-scoped dependency injection module provisioning the physical hardware
 * haptic vibration engine across shifting Android OS platform layers.
 *
 * This structural implementation abstracts framework-level API deprecations to provide
 * an uniform, reliable haptic feedback bridge for timing alerts and interval completions.
 */
@Module
@InstallIn(SingletonComponent::class)
object VibratorModule {

    /**
     * Resolves and provides the appropriate hardware [Vibrator] channel instance
     * based on runtime platform SDK capability boundaries.
     *
     * - Uses [VibratorManager] on Android 12 (API 31, SDK 'S') and above.
     * - Falls back to legacy [Context.VIBRATOR_SERVICE] on older versions.
     *
     * @param context The systemic non-leaking application context framework link.
     * @return The active hardware-level [Vibrator] mechanism.
     */
    @Provides
    fun provideVibrator(@ApplicationContext context: Context): Vibrator {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            manager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }
}
