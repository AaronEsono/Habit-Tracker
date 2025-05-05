package aeb.proyecto.stopwatch.service

import aeb.proyecto.stopwatch.constants.ACTION_SERVICE_CANCEL
import aeb.proyecto.stopwatch.constants.ACTION_SERVICE_START
import aeb.proyecto.stopwatch.constants.ACTION_SERVICE_STOP
import aeb.proyecto.stopwatch.constants.NOTIFICATION_CHANNEL_ID
import aeb.proyecto.stopwatch.constants.NOTIFICATION_CHANNEL_NAME
import aeb.proyecto.stopwatch.constants.NOTIFICATION_ID
import aeb.proyecto.stopwatch.constants.STOPWATCH_STATE
import aeb.proyecto.stopwatch.helper.StopWatchHelper
import aeb.proyecto.stopwatch.manager.StopWatchStateManager
import aeb.proyecto.stopwatch.utils.formatTime
import aeb.proyecto.stopwatch.utils.pad
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.NotificationCompat
import dagger.hilt.android.AndroidEntryPoint
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds


@AndroidEntryPoint
class StopWatchService : Service(){

    @Inject
    lateinit var notificationManager: NotificationManager
    @Inject
    lateinit var notificationBuilder: NotificationCompat.Builder

    //Funciones para iniciar/detener el servicio
    @Inject
    lateinit var serviceHelper: StopWatchHelper
    //Variables para el servicio
    @Inject
    lateinit var stateManager: StopWatchStateManager

    private val binder = StopWatchBinder()
    private lateinit var timer: Timer

    override fun onBind(p0: Intent?) = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("StopWatchService", "onStartCommand:")
        when (intent?.getStringExtra(STOPWATCH_STATE)) {
            StopwatchState.Started.name -> {
                setStopButton()
                startForegroundService()
                startStopwatch { hours, minutes, seconds ->
                    updateNotification(hours = hours, minutes = minutes, seconds = seconds)
                }
            }
            StopwatchState.Stopped.name -> {
                stopStopwatch()
                setResumeButton()
            }
            StopwatchState.Canceled.name -> {
                stopStopwatch()
                cancelStopwatch()
                stopForegroundService()
            }
        }
        intent?.action.let {
            when (it) {
                ACTION_SERVICE_START -> {
                    setStopButton()
                    startForegroundService()
                    startStopwatch { hours, minutes, seconds ->
                        updateNotification(hours = hours, minutes = minutes, seconds = seconds)
                    }
                }
                ACTION_SERVICE_STOP -> {
                    stopStopwatch()
                    setResumeButton()
                }
                ACTION_SERVICE_CANCEL -> {
                    stopStopwatch()
                    cancelStopwatch()
                    stopForegroundService()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startStopwatch(onTick: (h: String, m: String, s: String) -> Unit) {
        stateManager.setState(StopwatchState.Started)
        timer = fixedRateTimer(initialDelay = 1000L, period = 1000L) {
            stateManager.duration = stateManager.duration.plus(1.seconds)
            updateTimeUnits()
            onTick(stateManager.hours.value, stateManager.minutes.value, stateManager.seconds.value)
        }
    }

    private fun stopStopwatch() {
        if (this::timer.isInitialized) {
            timer.cancel()
        }
        stateManager.setState(StopwatchState.Stopped)
    }

    private fun cancelStopwatch() {
        stateManager.duration = Duration.ZERO
        stateManager.setState(StopwatchState.Idle)
        updateTimeUnits()
    }

    private fun updateTimeUnits() {
        stateManager.duration.toComponents { hours, minutes, seconds, _ ->
            stateManager.hours.value = hours.toInt().pad()
            stateManager.minutes.value = minutes.pad()
            stateManager.seconds.value = seconds.pad()
        }
    }

    private fun startForegroundService(){
        createNotificationChannel()
        startForeground(NOTIFICATION_ID,notificationBuilder.build())
    }

    private fun stopForegroundService() {
        notificationManager.cancel(NOTIFICATION_ID)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }

    private fun setStopButton() {
        notificationBuilder.mActions.removeAt(0)
        notificationBuilder.mActions.add(
            0,
            NotificationCompat.Action(
                0,
                "Stop",
                serviceHelper.stopPendingIntent()
            )
        )
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun setResumeButton() {
        notificationBuilder.mActions.removeAt(0)
        notificationBuilder.mActions.add(
            0,
            NotificationCompat.Action(
                0,
                "Resume",
                serviceHelper.resumePendingIntent()
            )
        )
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun updateNotification(hours: String, minutes: String, seconds: String) {
        notificationManager.notify(
            NOTIFICATION_ID,
            notificationBuilder.setContentText(
                formatTime(
                    hours = hours,
                    minutes = minutes,
                    seconds = seconds,
                )
            ).build()
        )
    }


    inner class StopWatchBinder: Binder(){
        fun getService(): StopWatchService = this@StopWatchService
    }

}


enum class StopwatchState {
    Idle,
    Started,
    Stopped,
    Canceled
}