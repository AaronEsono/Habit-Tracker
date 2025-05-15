package aeb.proyecto.stopwatch.service

import aeb.proyecto.stopwatch.R
import aeb.proyecto.stopwatch.constants.ACTION_SERVICE_CANCEL
import aeb.proyecto.stopwatch.constants.ACTION_SERVICE_FINISH
import aeb.proyecto.stopwatch.constants.ACTION_SERVICE_RESUME
import aeb.proyecto.stopwatch.constants.ACTION_SERVICE_START_INTERVAL
import aeb.proyecto.stopwatch.constants.ACTION_SERVICE_START_STOPWATCH
import aeb.proyecto.stopwatch.constants.ACTION_SERVICE_START_TIMER
import aeb.proyecto.stopwatch.constants.ACTION_SERVICE_STOP
import aeb.proyecto.stopwatch.constants.NOTIFICATION_CHANNEL_ID
import aeb.proyecto.stopwatch.constants.NOTIFICATION_CHANNEL_NAME
import aeb.proyecto.stopwatch.constants.NOTIFICATION_ID
import aeb.proyecto.stopwatch.helper.StopWatchHelper
import aeb.proyecto.stopwatch.manager.IntervalState
import aeb.proyecto.stopwatch.manager.StopWatchStateManager
import aeb.proyecto.stopwatch.manager.StopwatchState
import aeb.proyecto.stopwatch.manager.TypeTimer
import aeb.proyecto.stopwatch.notification.NotificationBuilderHelper
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Binder
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import androidx.core.net.toUri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch


@AndroidEntryPoint
class StopWatchService : Service(){

    //Inyeccion de dependencias
    @Inject
    lateinit var notificationManager: NotificationManager
    @Inject
    lateinit var notificationBuilderHelper: NotificationBuilderHelper
    @Inject
    lateinit var context: Context

    //Clases para el service
    @Inject
    lateinit var serviceHelper: StopWatchHelper
    @Inject
    lateinit var stateManager: StopWatchStateManager

    private val binder = StopWatchBinder()

    private val handler = Handler(Looper.getMainLooper())
    private val updateInterval = 50L // 50 Milisegundos
    private var job: Job? = null

    private var mediaPlayer: MediaPlayer? = null

    override fun onBind(p0: Intent?) = binder


    private val timerRunnable = object : Runnable {
        override fun run(){
            if(stateManager.isTimerRunning.value){
                when(stateManager.typeTimer.value){
                    is TypeTimer.INTERVAL -> {
                        timerRunnableInterval()
                    }
                    TypeTimer.STOPWATCH -> {
                        timerRunnableStopWatch()
                    }
                    is TypeTimer.TIMER -> {
                        timerRunnableTimer( stateManager.typeTimer.value as TypeTimer.TIMER)
                    }
                }
            }
        }
    }

    private fun timerRunnableInterval() {
        val currentState = stateManager.typeTimer.value as? TypeTimer.INTERVAL ?: return
        val now = SystemClock.elapsedRealtime()
        val elapsed = now - stateManager.startTime
        val totalElapsed = stateManager.timeElapsedBeforePause + elapsed

        val intervalTime = when (currentState.state) {
            IntervalState.Work -> currentState.time
            IntervalState.Rest -> currentState.rest
        }

        val remaining = intervalTime - totalElapsed

        if (remaining > 0) {
            stateManager.updateElapsedTime(remaining)
            handler.postDelayed(timerRunnable, updateInterval)
        } else {
            stateManager.updateElapsedTime(0L)

            when (currentState.state) {
                IntervalState.Rest -> {
                    stateManager.setTimerType(
                        currentState.copy(
                            state = IntervalState.Work,
                            currentInterval = currentState.currentInterval + 1
                        )
                    )
                    stateManager.timeElapsedBeforePause = 0L
                    stateManager.startTime = SystemClock.elapsedRealtime()
                    setIntervalTitle()
                    handler.post(timerRunnable)
                    playAlarm(R.raw.worknotification, false)
                }

                IntervalState.Work -> {
                    if (currentState.currentInterval == currentState.interval) {
                        stateManager.updateElapsedTime(0L)

                        stateManager.setNotificationTitle(
                            context.getString(
                                R.string.service_finished,
                                context.getString(R.string.service_interval)
                            )
                        )

                        stateManager.setState(StopwatchState.Finished)
                        playAlarm(R.raw.finishnotification, true)
                    } else {
                        stateManager.setTimerType(currentState.copy(state = IntervalState.Rest))
                        stateManager.timeElapsedBeforePause = 0L
                        stateManager.startTime = SystemClock.elapsedRealtime()
                        setIntervalTitle()
                        handler.post(timerRunnable)
                        playAlarm(R.raw.restnotification, false)
                    }
                }
            }
        }
    }


    private fun timerRunnableStopWatch() {
        val now = SystemClock.elapsedRealtime()
        val elapsed = now - stateManager.startTime
        val totalElapsed = stateManager.timeElapsedBeforePause + elapsed

        stateManager.updateElapsedTime(totalElapsed)
        handler.postDelayed(timerRunnable, updateInterval)
    }

    private fun timerRunnableTimer(type:TypeTimer.TIMER){
        val now = SystemClock.elapsedRealtime()
        val elapsed = now - stateManager.startTime
        val totalElapsed = stateManager.timeElapsedBeforePause + elapsed
        val remaining = type.time - totalElapsed

        if (remaining > 0) {
            stateManager.updateElapsedTime(remaining)
            handler.postDelayed(timerRunnable, updateInterval)
        } else {
            stateManager.updateElapsedTime(0L)

            stateManager.setNotificationTitle(
                context.getString(
                    R.string.service_finished,
                    context.getString(R.string.service_timer)
                )
            )

            stateManager.setState(StopwatchState.Finished)
            playAlarm(R.raw.finishnotification,true)
        }
    }

    private fun startObservingNotificationState() {
        job?.cancel()
        job = CoroutineScope(Dispatchers.Default).launch {
            combine(
                stateManager.timerString,
                stateManager.currentState,
                stateManager.notificationTitle
            ) { time, state, title -> Triple(time, state, title) }
                .collect { (time, state, title) ->
                    updateNotification(title, state, time)
                }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.action.let {
            when (it) {
                ACTION_SERVICE_START_STOPWATCH -> {
                    setTimerState(TypeTimer.STOPWATCH)

                    startNotificationService()
                }
                ACTION_SERVICE_START_TIMER -> {
                    val time = intent?.getLongExtra("time", 0L) ?: 0L
                    setTimerState(TypeTimer.TIMER(time))

                    startNotificationService()
                }
                ACTION_SERVICE_START_INTERVAL -> {
                    val time = intent?.getLongExtra("time",0L) ?: 0L
                    val rest = intent?.getLongExtra("rest", 0L) ?: 0L
                    val interval = intent?.getIntExtra("interval",1) ?: 1

                    setTimerState(TypeTimer.INTERVAL(time,rest,interval))

                    startNotificationService()
                }


                ACTION_SERVICE_CANCEL -> {
                    cancelStopwatch()
                    stopForegroundService()
                }

                ACTION_SERVICE_STOP -> {
                    stopStopwatch()
                }

                ACTION_SERVICE_RESUME -> {
                    resumeStopwatch()
                }

                ACTION_SERVICE_FINISH -> {
                    finishStopWatch()
                    stopForegroundService()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startNotificationService(){
        stateManager.setTimerRunning(true)
        startObservingNotificationState()
        stateManager.setState(StopwatchState.InProgress)
        stateManager.timeElapsedBeforePause = 0L

        when (val typeTimer = stateManager.typeTimer.value) {
            is TypeTimer.INTERVAL -> {
                stateManager.updateElapsedTime(typeTimer.time)
                setIntervalTitle()
            }

            TypeTimer.STOPWATCH -> {
                stateManager.updateElapsedTime(0L)
                stateManager.setNotificationTitle(context.getString(R.string.service_stopwatch))
            }

            is TypeTimer.TIMER -> {
                stateManager.updateElapsedTime(typeTimer.time)
                stateManager.setNotificationTitle(context.getString(R.string.service_timer))
            }
        }

        startForegroundService()
        stateManager.startTime = SystemClock.elapsedRealtime()
        handler.post(timerRunnable)
    }

    private fun setTimerState(type:TypeTimer){
        stateManager.typeTimer.value = type
    }

    private fun playAlarm(alarm:Int, lopped:Boolean = false){
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ALARM) // Asegura máxima prioridad
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        mediaPlayer = MediaPlayer.create(context, alarm).apply {
            setAudioAttributes(audioAttributes)
            isLooping = lopped
            setVolume(1.0f, 1.0f) // Máximo volumen
            start()
        }
    }

    private fun cancelAlarm(){
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun stopStopwatch() {
        handler.removeCallbacks(timerRunnable)
        stateManager.setTimerRunning(false)

        val now = SystemClock.elapsedRealtime()
        val elapsed = now - stateManager.startTime
        stateManager.timeElapsedBeforePause += elapsed

        val title = when(stateManager.typeTimer.value){
            is TypeTimer.INTERVAL -> R.string.service_interval
            TypeTimer.STOPWATCH -> R.string.service_stopwatch
            is TypeTimer.TIMER -> R.string.service_timer
        }

        stateManager.setNotificationTitle(
            context.getString(
                R.string.service_paused,
                context.getString(title)
            )
        )

        stateManager.setState(StopwatchState.Stopped)
    }

    private fun cancelStopwatch() {
        handler.removeCallbacks(timerRunnable)
        stateManager.setTimerRunning(false)
        stateManager.setState(StopwatchState.Idle)
        cancelAlarm()
    }

    private fun resumeStopwatch() {
        handler.post(timerRunnable)
        stateManager.setTimerRunning(true)

        when(stateManager.typeTimer.value){
            is TypeTimer.INTERVAL -> setIntervalTitle()
            TypeTimer.STOPWATCH -> {
                stateManager.setNotificationTitle(context.getString(R.string.service_stopwatch))
            }
            is TypeTimer.TIMER ->  {
                stateManager.setNotificationTitle(context.getString(R.string.service_timer))
            }
        }

        stateManager.startTime = SystemClock.elapsedRealtime()
        stateManager.setState(StopwatchState.InProgress)
    }

    private fun finishStopWatch(){
        cancelStopwatch()
    }

    private fun startForegroundService(){
        createNotificationChannel()

        val initialNotification = notificationBuilderHelper.buildNotification()
        startForeground(NOTIFICATION_ID, initialNotification.build())
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

    private fun setIntervalTitle(){
        val type = stateManager.typeTimer.value as? TypeTimer.INTERVAL ?: return

        when(type.state){
            IntervalState.Rest -> {
                stateManager.setNotificationTitle(context.getString(R.string.service_interval_rest))
            }
            IntervalState.Work -> {
                if(type.currentInterval == type.interval){
                    stateManager.setNotificationTitle(context.getString(R.string.service_interval_last_round))
                }else{
                    stateManager.setNotificationTitle(context.getString(R.string.service_interval_work,
                        type.currentInterval.toString(), type.interval.toString()))
                }
            }
        }

    }

    private fun updateNotification(
        title: String,
        state: StopwatchState,
        time: String
    ) {
        if (state == StopwatchState.Idle) return

        val builder = when (state) {
            StopwatchState.InProgress -> notificationBuilderHelper.buildNotification(
                title = title,
                contentText = time,
                showStop = true,
                showCancel = true,
                showFinish = false,
                showResume = false
            )
            StopwatchState.Stopped -> notificationBuilderHelper.buildNotification(
                title = title,
                contentText = time,
                showResume = true,
                showCancel = true,
                showStop = false,
                showFinish = false
            )
            StopwatchState.Finished -> notificationBuilderHelper.buildNotification(
                title = title,
                contentText = time,
                showFinish = true,
                showCancel = false,
                showResume = false,
                showStop = false
            )
            else -> return
        }

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    inner class StopWatchBinder: Binder(){
        fun getService(): StopWatchService = this@StopWatchService
    }

}