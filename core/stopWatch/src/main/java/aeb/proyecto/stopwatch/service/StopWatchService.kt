package aeb.proyecto.stopwatch.service

import aeb.proyecto.datastore.DatastoreInterface
import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.room.repository.HabitWithDailyHabitRepo
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
import aeb.proyecto.stopwatch.model.NotificationInfo
import aeb.proyecto.stopwatch.notification.NotificationBuilderHelper
import aeb.proyecto.stopwatch.utils.getPausedTitle
import aeb.proyecto.stopwatch.utils.getSecondsPassed
import aeb.proyecto.stopwatch.utils.getTextToday
import aeb.proyecto.stopwatch.utils.prepareInitialTimerTitle
import aeb.proyecto.stopwatch.utils.setIntervalTitle
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Binder
import android.os.PowerManager
import android.os.SystemClock
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject


@AndroidEntryPoint
class StopWatchService : Service(){

    //Inyeccion de dependencias
    @Inject
    lateinit var notificationManager: NotificationManager
    @Inject
    lateinit var notificationBuilderHelper: NotificationBuilderHelper
    @Inject
    lateinit var context: Context
    @Inject
    lateinit var vibrator: Vibrator

    //Clases para el service
    @Inject
    lateinit var serviceHelper: StopWatchHelper
    @Inject
    lateinit var stateManager: StopWatchStateManager

    //Room repository
    @Inject
    lateinit var habitWithDailyHabitRepo: HabitWithDailyHabitRepo

    //Datastore repository
    @Inject
    lateinit var dataStoreInterface: DatastoreInterface

    private val binder = StopWatchBinder()

    private val updateInterval = 200L
    private var timerJob: Job? = null
    private var notificationJob: Job? = null

    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)
    private var mediaPlayer: MediaPlayer? = null

    private var wakeLock: PowerManager.WakeLock? = null

    override fun onBind(intent: Intent?) = binder

    private fun startTimerCoroutine(){
        timerJob?.cancel()
        timerJob = CoroutineScope(Dispatchers.Default).launch {
            while(stateManager.runningTimer.value){

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

                delay(updateInterval)
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
            updateTotalElapsed(remaining)
        } else {
            updateTotalElapsed(0L)

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
                    setIntervalTitle(stateManager, context)
                    playAlarm(R.raw.worknotification, false)
                }

                IntervalState.Work -> {
                    if (currentState.currentInterval == currentState.interval) {
                        updateTotalElapsed(0L)

                        stateManager.setNotificationTitle(
                            context.getString(
                                R.string.service_finished,
                                context.getString(R.string.service_interval)
                            )
                        )

                        stateManager.setRunningTimer(false)
                        stateManager.setState(StopwatchState.Finished)
                        playAlarm(R.raw.finishnotification, true)
                    } else {
                        stateManager.setTimerType(currentState.copy(state = IntervalState.Rest))
                        stateManager.timeElapsedBeforePause = 0L
                        stateManager.startTime = SystemClock.elapsedRealtime()
                        setIntervalTitle(stateManager, context)
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

        updateTotalElapsed(totalElapsed)
    }

    private fun timerRunnableTimer(type:TypeTimer.TIMER){
        val now = SystemClock.elapsedRealtime()
        val elapsed = now - stateManager.startTime
        val totalElapsed = stateManager.timeElapsedBeforePause + elapsed
        val remaining = type.time - totalElapsed

        if (remaining > 0) {
            updateTotalElapsed(remaining)
        } else {
            updateTotalElapsed(0L)

            stateManager.setNotificationTitle(
                context.getString(
                    R.string.service_finished,
                    context.getString(R.string.service_timer)
                )
            )

            stateManager.setState(StopwatchState.Finished)
            playAlarm(R.raw.finishnotification,true)
            stateManager.setRunningTimer(false)
        }
    }

    private fun startObservingNotificationState() {
        notificationJob?.cancel()
        notificationJob = CoroutineScope(Dispatchers.Default).launch {
            combine(
                stateManager.timerString,
                stateManager.currentState,
                stateManager.notificationTitle,
                stateManager.habitLinked
            ) { time, currentState, title, habitLinked -> NotificationInfo(time, currentState, title, habitLinked) }
                .distinctUntilChanged()
                .conflate()
                .collect { notificationInfo ->
                    updateNotification(
                        notificationInfo.title,
                        notificationInfo.currentState,
                        notificationInfo.time,
                        notificationInfo.subText
                    )
                }
        }
    }

    private fun updateTotalElapsed(elapsed:Long){
        CoroutineScope(Dispatchers.Main).launch {
            stateManager.updateElapsedTime(elapsed)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_SERVICE_START_STOPWATCH -> {
                getHabitLinked(intent)
                handleStart(TypeTimer.STOPWATCH)
            }
            ACTION_SERVICE_START_TIMER -> {
                getHabitLinked(intent)
                val time = intent.getLongExtra("time", 0L)
                handleStart(TypeTimer.TIMER(time))
            }
            ACTION_SERVICE_START_INTERVAL -> {
                getHabitLinked(intent)
                val time = intent.getLongExtra("time",0L)
                val rest = intent.getLongExtra("rest", 0L)
                val interval = intent.getIntExtra("interval",1) ?: 1

                handleStart(TypeTimer.INTERVAL(time,rest,interval))
            }
            ACTION_SERVICE_CANCEL -> cancelStopwatch()
            ACTION_SERVICE_STOP ->  stopStopwatch()
            ACTION_SERVICE_RESUME -> resumeStopwatch()
            ACTION_SERVICE_FINISH -> finishStopWatch()
        }
        return START_STICKY
    }

    private fun handleStart(type:TypeTimer){
        startForegroundService()
        setTimerState(type)
        startNotificationService()
        acquireWakeLock()
    }

    private fun getHabitLinked(intent: Intent){
        val id = intent.getLongExtra("habitId",0L)
        val dateString = intent.getStringExtra("habitDay")

        val date = try {
            LocalDate.parse(dateString)
        }catch (e:Exception){
            LocalDate.now()
        }

        serviceScope.launch {
            val habitDay = habitWithDailyHabitRepo.getHabitWithDayOrNull(id,date)
            stateManager.setHabitLinked(habitDay)
        }
    }

    private fun startNotificationService(){
        stateManager.apply {
            setState(StopwatchState.InProgress)
            timeElapsedBeforePause = 0L
            startTime = SystemClock.elapsedRealtime()
        }

        prepareInitialTimerTitle(stateManager, context)
        startObservingNotificationState()
        stateManager.setRunningTimer(true)
        startTimerCoroutine()
    }

    private fun stopStopwatch() {
        stateManager.setRunningTimer(false)
        timerJob?.cancel()
        val now = SystemClock.elapsedRealtime()
        val elapsed = now - stateManager.startTime

        stateManager.apply {
            setNotificationTitle(getPausedTitle(stateManager, context))
            setState(StopwatchState.Stopped)
            timeElapsedBeforePause += elapsed
        }
    }

    private fun cancelStopwatch() {
        checkHabitLinked()
        stateManager.setRunningTimer(false)
        stateManager.setState(StopwatchState.Idle)
        cancelAlarm()
        releaseWakeLock()
        stopForegroundService()
    }

    private fun resumeStopwatch() {
        stateManager.apply {
            startTime = SystemClock.elapsedRealtime()
            setState(StopwatchState.InProgress)
        }

        stateManager.setRunningTimer(true)
        prepareInitialTimerTitle(stateManager, context)
        startTimerCoroutine()
    }

    private fun finishStopWatch(){
        cancelStopwatch()
    }

    private fun setTimerState(type:TypeTimer){
        stateManager.setTimerType(type)
    }

    private fun playAlarm(alarm:Int, lopped:Boolean = false){
        cancelAlarm()
        try {
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

            vibrate()
        }catch (e: Exception) {
            Log.e("StopWatchService", "Error al reproducir alarma: ${e.message}")
        }
    }


    private fun vibrate(duration: Long = 500L) {
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }

    private fun cancelAlarm(){
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }


    private fun startForegroundService(){
        createNotificationChannel()

        val initialNotification = notificationBuilderHelper.createNotification()
        startForeground(NOTIFICATION_ID, initialNotification.build())
    }

    private fun stopForegroundService() {
        notificationManager.cancel(NOTIFICATION_ID)
        stopForeground(STOP_FOREGROUND_REMOVE)
        releaseWakeLock()
        stopSelf()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)
    }

    private fun updateNotification(
        title: String,
        state: StopwatchState,
        time: String,
        subtext: HabitWithDay?
    ) {
        if (state == StopwatchState.Idle) return

        val subTextHabit = if (subtext != null) {
            context.getString(
                R.string.timer_title_habit,
                subtext.habit.name,
                getTextToday(subtext.day.date, context)
            )
        } else {
            null
        }

        val builder = when (state) {
            StopwatchState.InProgress -> notificationBuilderHelper.updateNotification(
                newTitle = title,
                newTime = time,
                subText = subTextHabit,
                showStop = true,
                showCancel = true,
                showFinish = false,
                showResume = false
            )
            StopwatchState.Stopped -> notificationBuilderHelper.updateNotification(
                newTitle = title,
                newTime = time,
                subText = subTextHabit,
                showResume = true,
                showCancel = true,
                showStop = false,
                showFinish = false
            )
            StopwatchState.Finished -> notificationBuilderHelper.updateNotification(
                newTitle = title,
                newTime = time,
                subText = subTextHabit,
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

    private fun acquireWakeLock() {
        if (wakeLock == null) {
            val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
            wakeLock = powerManager.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK,
                "MiApp::TimerWakeLock"
            ).apply {
                setReferenceCounted(false)
            }
        }

        if (wakeLock?.isHeld == false) {
            wakeLock?.acquire()
        }
    }

    private fun releaseWakeLock() {
        if (wakeLock?.isHeld == true) {
            wakeLock?.release()
        }
    }

    private fun checkHabitLinked(){
        if(stateManager.habitLinked.value != null){
            //Hay habito vinculado, preguntar al usuario luego
            val timePassedInSeconds = getSecondsPassed(stateManager.elapsedTime.value,stateManager.typeTimer.value)
            serviceScope.launch {
                dataStoreInterface.setTimePassedTimer(timePassedInSeconds)
                dataStoreInterface.setIsLinkedHabitAndFinished(true)
            }
        }else{
            serviceScope.launch {
                dataStoreInterface.setIsLinkedHabitAndFinished(false)
            }
        }
    }

    override fun onDestroy() {
        stateManager.setRunningTimer(false)
        timerJob?.cancel()
        notificationJob?.cancel()
        cancelAlarm()
        vibrator.cancel()
        serviceJob.cancel()
        releaseWakeLock()
        super.onDestroy()
    }
}