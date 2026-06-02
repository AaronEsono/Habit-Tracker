package aeb.proyecto.stopwatch.service

import aeb.proyecto.datastore.DatastoreInterface
import aeb.proyecto.room.entities.TimeEntry
import aeb.proyecto.room.entities.relations.HabitWithDay
import aeb.proyecto.room.repository.HabitWithDailyHabitRepo
import aeb.proyecto.room.repository.TimerEntryRepo
import aeb.proyecto.stopwatch.R
import aeb.proyecto.stopwatch.constants.ACTION_SERVICE_CANCEL
import aeb.proyecto.stopwatch.constants.ACTION_SERVICE_FINISH
import aeb.proyecto.stopwatch.constants.ACTION_SERVICE_RESUME
import aeb.proyecto.stopwatch.constants.ACTION_SERVICE_START_INTERVAL
import aeb.proyecto.stopwatch.constants.ACTION_SERVICE_START_STOPWATCH
import aeb.proyecto.stopwatch.constants.ACTION_SERVICE_START_TIMER
import aeb.proyecto.stopwatch.constants.ACTION_SERVICE_STOP
import aeb.proyecto.stopwatch.constants.CLICK_REQUEST_CODE
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
import aeb.proyecto.stopwatch.overlay.LayoutParams
import aeb.proyecto.stopwatch.overlay.OverlayContent
import aeb.proyecto.stopwatch.utils.getPausedTitle
import aeb.proyecto.stopwatch.utils.getSecondsPassed
import aeb.proyecto.stopwatch.utils.getTextToday
import aeb.proyecto.stopwatch.utils.longToSeconds
import aeb.proyecto.stopwatch.utils.openAppIntoTimer
import aeb.proyecto.stopwatch.utils.prepareInitialTimerTitle
import aeb.proyecto.stopwatch.utils.setIntervalTitle
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
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
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontVariation
import androidx.core.net.toUri
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
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

/**
 * High-performance, multi-modality Foreground Service orchestrating real-time
 * background time tracking loops, system alert notifications, physical haptic signals,
 * and persistent interactive window overlay grids.
 *
 * Implements [LifecycleOwner] and [SavedStateRegistryOwner] to manually provision
 * the structural contextual infrastructure required to host Jetpack Compose UI trees
 * within window manager overlays detached from standard Activity classes.
 */
@AndroidEntryPoint
class StopWatchService : Service(), LifecycleOwner, SavedStateRegistryOwner{

    // ============================================================================
    // System Infrastructure Injections
    // ============================================================================
    /** Platform hardware broker driving local system notification panels. */

    /** Platform hardware broker driving local system notification panels. */
    @Inject
    lateinit var notificationManager: NotificationManager

    /** Custom decorator abstraction layer manipulating real-time notification layout mutations. */
    @Inject
    lateinit var notificationBuilderHelper: NotificationBuilderHelper

    /** Systemic non-leaking application context framework link. */
    @Inject
    lateinit var context: Context

    /** Physical hardware actuator driving tactical vibration alert patterns. */
    @Inject
    lateinit var vibrator: Vibrator

    // ============================================================================
    // Local Framework Modules Injections
    // ============================================================================

    /** Intent routing bridge providing operational PendingTokens to the Android OS subsystem. */
    @Inject
    lateinit var serviceHelper: StopWatchHelper

    /** Reactive single Source of Truth repository governing macroscopic state synchronization. */
    @Inject
    lateinit var stateManager: StopWatchStateManager

    // ============================================================================
    // Relational Database Repositories
    // ============================================================================

    /** Room DB broker coordinating aggregated structural logs linking habits with specific calendar days. */
    @Inject
    lateinit var habitWithDailyHabitRepo: HabitWithDailyHabitRepo

    /** Room DB broker capturing granular raw duration session entry transactions. */
    @Inject
    lateinit var timerEntryRepo: TimerEntryRepo

    // ============================================================================
    // Datastore Repositories
    // ============================================================================

    /** Primitive key-value encryption/persistence bridge driving application preference configuration. */
    @Inject
    lateinit var dataStoreInterface: DatastoreInterface

    // ============================================================================
    // Internal Control Constraints
    // ============================================================================

    /** Communication binder pipeline linking structural activity connections to this service. */
    private val binder = StopWatchBinder()

    /** Constant refresh pace (200ms) optimizing visual rendering fidelity without exhausting CPU cycles. */
    private val updateInterval = 200L

    // ============================================================================
    // Concurrency & Audio Management
    // ============================================================================

    /** Dedicated coroutine routine tracking raw temporal clock calculations. */
    private var timerJob: Job? = null

    /** Dedicated coroutine routine refreshing the system curtain alert UI metrics. */
    private var notificationJob: Job? = null

    /** Isolation barrier locking task disruptions from crashing sister async calculations. */
    private val serviceJob = SupervisorJob()

    /** Scope boundary safely isolating infrastructure background operations on optimized IO threads. */
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)

    /** Low-level audio player engine driving completion buzzer or interval shift sound clips. */
    private var mediaPlayer: MediaPlayer? = null

    // ============================================================================
    // Power & Energy Preservation
    // ============================================================================

    /** Hardware-level CPU lock token enforcing chronological accuracy when the device screen suspends. */
    private var wakeLock: PowerManager.WakeLock? = null

    // ============================================================================
    // Custom System Window Overlay Infrastructure
    // ============================================================================

    /** Platform window manager subsystem tasked with attaching root views onto the mobile desktop layer. */
    private lateinit var windowManager: WindowManager

    /** Explicit lifecycle engine required to anchor compose compositions on non-activity view hierarchies. */
    private val _lifecycleRegistry = LifecycleRegistry(this)

    /** Explicit state restoration engine required to back compose view trees on non-activity view hierarchies. */
    private val _savedStateRegistryController: SavedStateRegistryController = SavedStateRegistryController.create(this)

    /** The active, structural floating root view layout rendering the screen overlay interface. */
    private var overlayView: View? = null

    // ============================================================================
    // Lifecycle & SavedState Framework Overrides
    // ============================================================================
    override fun onBind(intent: Intent?) = binder
    override val savedStateRegistry: SavedStateRegistry = _savedStateRegistryController.savedStateRegistry
    override val lifecycle: Lifecycle = _lifecycleRegistry

    /**
     * Specialized UI interaction observer managing context shifting boundaries.
     * Automatically destroys floating window panels when the user steps into the app foreground,
     * and projects drawing layouts when the user steps away.
     */
    private val lifecycleObserver = object : DefaultLifecycleObserver {
        override fun onResume(owner: LifecycleOwner) {
            closeOverlay()
        }

        override fun onStop(owner: LifecycleOwner) {
            if(Settings.canDrawOverlays(context)){
                startOverlay()
            }
        }
    }

    /**
     * Initializes the framework-level capabilities of the service execution lifecycle.
     * Spawns structural window attachment systems, restores state layout registers,
     * and binds global application foreground/background process lifecycle event observers.
     */
    override fun onCreate() {
        super.onCreate()
        // Resolve platform window boundaries for overlay insertions
        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        // Wire manually the structural framework components required by Jetpack Compose view hierarchies
        _savedStateRegistryController.performAttach()
        _savedStateRegistryController.performRestore(null)
        _lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)

        // Attach local background state switcher observer to the global process timeline
        ProcessLifecycleOwner.get().lifecycle.addObserver(lifecycleObserver)
    }

    /**
     * Spawns the central structural asynchronous tracking thread.
     * Cancels existing operational job scopes to safeguard against concurrent thread duplication bugs,
     * routing sub-second calculation metrics down to specific instrument runners based on the active modality rules.
     */
    private fun startTimerCoroutine(){
        timerJob?.cancel() // Defensive check: Prevents cumulative overlapping ticker loop allocation bugs
        timerJob = CoroutineScope(Dispatchers.Default).launch {
            while(stateManager.runningTimer.value){

                // Route execution ticks down to specialized modular computation blocks
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

                // Sleep the coroutine thread loop just enough to balance visual fidelity and battery strain
                delay(updateInterval)
            }
        }
    }

    /**
     * Executes a single chronological evaluation tick under the complex multi-segment Interval training modality.
     * Calculates dynamic time differences using monolithic hardware clocks, executing sub-state
     * mutations to seamlessly cycle through active exercise sets and recovery rest phases.
     */
    private fun timerRunnableInterval() {
        val currentState = stateManager.typeTimer.value as? TypeTimer.INTERVAL ?: return
        val now = SystemClock.elapsedRealtime()
        val elapsed = now - stateManager.startTime
        val totalElapsed = stateManager.timeElapsedBeforePause + elapsed

        // Determine target milestone based on internal interval sub-state
        val intervalTime = when (currentState.state) {
            IntervalState.Work -> currentState.time
            IntervalState.Rest -> currentState.rest
        }

        val remaining = intervalTime - totalElapsed

        if (remaining > 0) {
            updateTotalElapsed(remaining)
        } else {
            updateTotalElapsed(0L) // Defensive absolute floor reset

            // Execute atomic state transitions when reaching a segment boundary
            when (currentState.state) {
                IntervalState.Rest -> {
                    // Transition back to physical work/exertion phase and increment round index
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
                        // Entire compound matrix completed successfully
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
                        // Transition down into passive recovery/rest phase
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

    /**
     * Executes a single chronological evaluation tick under the progressive upward-counting Stopwatch modality.
     * Projects linear, non-bounded real-time duration deltas straight onto the reactive state pipelines.
     */
    private fun timerRunnableStopWatch() {
        val now = SystemClock.elapsedRealtime()
        val elapsed = now - stateManager.startTime
        val totalElapsed = stateManager.timeElapsedBeforePause + elapsed

        updateTotalElapsed(totalElapsed)
    }

    /**
     * Executes a single chronological evaluation tick under the regressive fixed-duration Countdown Timer modality.
     * Subtracts absolute delta metrics against fixed session duration ceilings, firing terminal completion triggers.
     *
     * @param type The typed immutable configuration model locking down target time ceilings.
     */
    private fun timerRunnableTimer(type:TypeTimer.TIMER){
        val now = SystemClock.elapsedRealtime()
        val elapsed = now - stateManager.startTime
        val totalElapsed = stateManager.timeElapsedBeforePause + elapsed
        val remaining = type.time - totalElapsed

        if (remaining > 0) {
            updateTotalElapsed(remaining)
        } else {
            updateTotalElapsed(0L) // Defensive absolute floor reset

            // Terminate execution and notify the system layers of the successful completion
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

    /**
     * Initiates the multi-stream reactive monitoring pipeline driving system alert panels.
     * Cancels legacy instances to eliminate memory leaks, leveraging backpressure mitigation
     * operations ([conflate]) to guarantee low-overhead, synchronized rendering updates.
     */
    private fun startObservingNotificationState() {
        notificationJob?.cancel() // Defensive check: Destroys overlapping notification observation loops
        notificationJob = CoroutineScope(Dispatchers.Default).launch {
            combine(
                stateManager.timerString,
                stateManager.currentState,
                stateManager.notificationTitle,
                stateManager.habitLinked
            ) { time, currentState, title, habitLinked -> NotificationInfo(time, currentState, title, habitLinked) }
                .distinctUntilChanged() // Halts redundant UI redrawing processes if the state remains identical
                .conflate() // Drops backlogged intermediate time ticks under heavy load to prevent memory or layout stutter
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

    /**
     * Dispatches calculated real-time millisecond durations up to the global state manager architecture.
     * Safely forces an asynchronous contextual switch over to the Main dispatcher thread to guarantee
     * thread-safe consumption across active Jetpack Compose UI composition trees.
     *
     * @param elapsed The aggregate total elapsed or remaining tracking lifespan calculated in milliseconds.
     */
    private fun updateTotalElapsed(elapsed:Long){
        CoroutineScope(Dispatchers.Main).launch {
            stateManager.updateElapsedTime(elapsed)
        }
    }

    /**
     * Maps and orchestrates incoming operational command tokens dispatched to the background process layer.
     * Decodes intent payload actions to initialize specialized time instruments, alternate ticking
     * postures, or trigger safe, isolated teardown processes.
     *
     * @param intent The structural command vehicle containing target operational payloads.
     * @param flags Additional institutional metadata configuration matrices concerning the start request.
     * @param startId A unique framework allocation handle tracking this specific invocation token instance.
     * @return Lifecycle persistence directive ([START_STICKY]) instructing the OS to automatically
     * reconstitute the service infrastructure if terminated under memory duress.
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Intercept and route commands based on the standard intent action registry dictionary
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

    /**
     * Orchestrates the atomic, step-by-step sequential startup pipeline of the tracking framework.
     * Provisions foreground permanence status, mutates baseline configurations, starts reactive
     * notification listener streams, and claims physical hardware wake locks.
     *
     * @param type The targeted structural behavioral timing profile rule (Stopwatch, Timer, or Interval).
     */
    private fun handleStart(type:TypeTimer){
        startForegroundService()
        setTimerState(type)
        startNotificationService()
        acquireWakeLock()
    }

    /**
     * Extracts database entity coordinates from incoming intent communication vectors and asynchronously
     * fetches the corresponding habit relation profile context sheet.
     * Parses historical calendar dates defensively, falling back seamlessly to current local system time
     * parameters to safeguard execution against corrupt metadata payloads.
     *
     * @param intent The structural command vehicle containing the targeted habit identification extras.
     */
    private fun getHabitLinked(intent: Intent){
        val id = intent.getLongExtra("habitId",0L)
        val dateString = intent.getStringExtra("habitDay")

        // Parse the date parameter defensively using structural failure resilience fallbacks
        val date = try {
            LocalDate.parse(dateString)
        }catch (e:Exception){
            LocalDate.now()
        }

        // Isolate database access execution inside an asynchronous thread context pool
        serviceScope.launch {
            val habitDay = habitWithDailyHabitRepo.getHabitWithDayOrNull(id,date)
            stateManager.setHabitLinked(habitDay)
        }
    }

    /**
     * Boots the unified time orchestration pipeline.
     * Mutates core state metrics, computes initial notification layouts, activates real-time
     * reactive layout streaming observers, and ignites the asynchronous computation coroutine loop.
     */
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

    /**
     * Halts ongoing chronological calculation loops temporarily, capturing and caching precision
     * duration deltas before entering a suspended posture.
     * Commits the cumulative time slices into memory to ensure flawless structural preservation upon resumption.
     */
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

    /**
     * Executes a destructive teardown operation, resetting state controllers and purging hardware allocations.
     * Flushes active session time investment logs down to structural database entry tables, silences audio alarms,
     * unclaims hardware locks, destroys floating desktop layers, and detaches the service framework.
     */
    private fun cancelStopwatch() {
        checkHabitLinked()
        setOnHistory()
        stateManager.setRunningTimer(false)
        stateManager.setState(StopwatchState.Idle)
        cancelAlarm()
        releaseWakeLock()
        closeOverlay()
        stopForegroundService()
    }

    /**
     * Re-ignites a paused or suspended timing session, anchoring new hardware timeline parameters.
     * Restores active execution state flags, updates visual layout components, and re-launches
     * the primary computational background coroutine thread pool.
     */
    private fun resumeStopwatch() {
        stateManager.apply {
            startTime = SystemClock.elapsedRealtime()
            setState(StopwatchState.InProgress)
        }

        stateManager.setRunningTimer(true)
        prepareInitialTimerTitle(stateManager, context)
        startTimerCoroutine()
    }

    /**
     * Executes terminal completion procedures for the active tracking timeline.
     * Delegates execution straight to the standard teardown infrastructure to guarantee
     * synchronized database flushes and complete resource deallocations.
     */
    private fun finishStopWatch(){
        cancelStopwatch()
    }

    /**
     * Mutates the active structural tracking behavior profile embedded inside the reactive single source of truth.
     *
     * @param type The targeted configuration modality model (Stopwatch, Timer, or Interval).
     */
    private fun setTimerState(type:TypeTimer){
        stateManager.setTimerType(type)
    }

    /**
     * Initializes and fires the hardware audio player engine to broadcast structural status changes.
     * Configures professional system alarm priority channel routing attributes to bypass non-essential
     * sound attenuation constraints, enforcing maximum audio volume outputs.
     *
     * @param alarm The platform resource identification handle pointing to the raw audio track asset.
     * @param lopped Conditional flag forcing the hardware audio stream to cycle endlessly until explicitly halted.
     */
    private fun playAlarm(alarm:Int, lopped:Boolean = false){
        cancelAlarm() // Defensive check: Purges ongoing audio playbacks prior to allocating new streams
        try {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM) // Forces routing through high-priority physical alarm channels
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()

            mediaPlayer = MediaPlayer.create(context, alarm).apply {
                setAudioAttributes(audioAttributes)
                isLooping = lopped
                setVolume(1.0f, 1.0f) // Maximizes gain amplification across both stereo fields
                start()
            }

            vibrate()
        }catch (e: Exception) {
            Log.e("StopWatchService", "Error al reproducir alarma: ${e.message}")
        }
    }

    /**
     * Fires a single-shot tactical physical haptic pulse through the device vibration actuator.
     * Verifies physical component presence prior to issuing commands to prevent framework crashes on unsupported hardware.
     *
     * @param duration The physical longevity length of the haptic response slice in milliseconds.
     */
    private fun vibrate(duration: Long = 500L) {
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }

    /**
     * Destroys active audio playback instances and explicitly flushes underlying framework media pipelines.
     * Releases hardware native multi-media decoders back to the operating system to neutralize potential memory leaks.
     */
    private fun cancelAlarm(){
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    /**
     * Elevates the active background execution thread into a prioritized, non-killable Foreground Service status.
     * Initializes required OS communication delivery pipelines and attaches a baseline immutable notification
     * shell to bypass platform-level aggressive resource reclamation policies.
     */
    private fun startForegroundService(){
        createNotificationChannel()

        val initialNotification = notificationBuilderHelper.createNotification()
        startForeground(NOTIFICATION_ID, initialNotification.build())
    }

    /**
     * Triggers an atomic structural teardown sequence to cleanly disconnect the service from the Android OS subsystem.
     * Purges active notification indicators, unclaims hardware processor locks, and issues an immediate [stopSelf]
     * directive to return allocated memory blocks back to the operational system pool.
     */
    private fun stopForegroundService() {
        notificationManager.cancel(NOTIFICATION_ID)
        stopForeground(STOP_FOREGROUND_REMOVE)
        releaseWakeLock()
        stopSelf()
    }

    /**
     * Registers a dedicated communication delivery channel within the platform notification subsystem.
     * Enforces a high importance configuration tier ([NotificationManager.IMPORTANCE_HIGH]) to authorize
     * immediate heads-up alert visual breakthroughs across shifting application states.
     */
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)
    }

    /**
     * Orchestrates real-time state mutations and dispatches repainted visual layouts to the Android notification drawer.
     * Dynamically resolves localized relationship sub-texts linking habits to relative date tags (e.g., "Gym (Today)")
     * and structurally switches action button layouts based on the active tracking posture.
     *
     * @param title The current status heading designated to the notification structure.
     * @param state The macroscopic operational lifecycle posture of the tracking loop.
     * @param time The pre-formatted ready-to-render tick string (e.g., "00:59:02").
     * @param subtext The optional database relationship profile context sheet.
     */
    private fun updateNotification(
        title: String,
        state: StopwatchState,
        time: String,
        subtext: HabitWithDay?
    ) {
        if (state == StopwatchState.Idle) return // Optimization: Suppress redundant updates on uninitialized or reset states

        // Resolve relational subtext structures defensively
        val subTextHabit = if (subtext != null) {
            context.getString(
                R.string.timer_title_habit,
                subtext.habit.name,
                getTextToday(subtext.day.date, context)
            )
        } else {
            null
        }

        // Map structural state flags to update button layouts within the notification framing layer
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

    /**
     * Communication binder token interface exposing the direct memory reference of this long-running service.
     * Utilized by local active platform components to interact with operational APIs without incurring IPC overhead.
     */
    inner class StopWatchBinder: Binder(){

        /**
         * Fetches the direct systemic reference of the active [StopWatchService] instance.
         *
         * @return The non-leaking local memory anchor of this background service execution framework.
         */
        fun getService(): StopWatchService = this@StopWatchService
    }

    /**
     * Requests an explicit hardware CPU execution lock token from the platform power management subsystem.
     * Employs a non-reference counted [PowerManager.PARTIAL_WAKE_LOCK] configuration boundary to guarantee
     * chronological calculations continue uninhibited when the mobile screen enters a suspended posture.
     */
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

    /**
     * Safely releases hardware CPU execution lock tokens back to the mobile operating system layer.
     * Mitigates background power leak vectors by verifying active engagement states before disengaging hardware.
     */
    private fun releaseWakeLock() {
        if (wakeLock?.isHeld == true) {
            wakeLock?.release()
        }
    }

    /**
     * Inflates, structures, and mounts the interactive Jetpack Compose floating overlay window onto the system desktop.
     * Manually advances local lifecycle state machine structures ([Lifecycle.Event.ON_RESUME]) to authorize composition
     * routine loops, wiring core application actions directly into the composable bridge interfaces.
     */
    private fun startOverlay(){

        // Elevate manually the custom lifecycle registry to authorize and ignite reactive Compose structures
        _lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        _lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

        // Build the declarative view tree root structure detached from typical Activity scopes
        overlayView = ComposeView(this).apply {
            // Bind framework infrastructure hooks required to support lifecycle and state retention capabilities
            setViewTreeLifecycleOwner(this@StopWatchService)
            setViewTreeSavedStateRegistryOwner(this@StopWatchService)

            // Inject the root visual declaration content sheet
            setContent { OverlayContent(
                stateManager = stateManager,
                onDrag = { dx, dy ->
                    // Mutate low-level window coordinates dynamically to drive smooth hardware translations
                    LayoutParams.x += dx
                    LayoutParams.y += dy
                    windowManager.updateViewLayout(this, LayoutParams)
                },
                onCloseOverlay = { closeOverlay() },
                onOpenApp = { openAppIntoTimer(context) },
                onPaused = { stopStopwatch() },
                onCancel = { cancelStopwatch() },
                onResumed = { resumeStopwatch() },
                onFinished = { finishStopWatch() }
            ) }
        }

        // Attach the fully configured floating view straight onto the platform window hierarchy layout matrix
        windowManager.addView(overlayView,LayoutParams)
    }

    /**
     * Evaluates current session constraints to process and cache accumulated usage metrics into the Datastore repository.
     * Computes net time investments in seconds using polymorphic duration subtractors, flipping functional flags
     * to instruct downstream UI layers to trigger post-session confirmation prompts if an active habit connection exists.
     */
    private fun checkHabitLinked(){
        if(stateManager.habitLinked.value != null){
            // Active habit relationship profile discovered; compute structural metrics
            val timePassedInSeconds = getSecondsPassed(stateManager.elapsedTime.value,stateManager.typeTimer.value)
            serviceScope.launch {
                dataStoreInterface.setTimePassedTimer(timePassedInSeconds)
                dataStoreInterface.setIsLinkedHabitAndFinished(true)
            }
        }else{
            // Global independent instrument run; bypass post-session habit confirmation workflows
            serviceScope.launch {
                dataStoreInterface.setIsLinkedHabitAndFinished(false)
            }
        }
    }

    /**
     * Transmutes the active multidimensional [TypeTimer] configuration metrics into a structural [TimeEntry]
     * relational database entity.
     * Resolves implicit conditional keys and normalizes framework millisecond counts into flat seconds parameters
     * before dispatching the compiled model token asynchronously to the historical persistence repository.
     */
    private fun setOnHistory(){
        val timeEntry: TimeEntry

        // Map polymorphically the active instrument state into a flat relational persistence model
        when(val type = stateManager.typeTimer.value){
            is TypeTimer.INTERVAL -> {
                timeEntry = TimeEntry(
                    typeTimer = 2, // Hardcoded mapping token representative of Interval instrumentation
                    time = longToSeconds(type.time),
                    restTime = longToSeconds(type.rest),
                    intervals = type.interval.toLong(),
                    idHabit = stateManager.habitLinked.value?.habit?.id
                )
            }
            TypeTimer.STOPWATCH -> {
                timeEntry = TimeEntry(
                    typeTimer = 0, // Hardcoded mapping token representative of progressive Stopwatch instrumentation
                    idHabit = stateManager.habitLinked.value?.habit?.id
                )
            }
            is TypeTimer.TIMER -> {
                timeEntry = TimeEntry(
                    typeTimer = 1, // Hardcoded mapping token representative of regressive Countdown Timer instrumentation
                    time = longToSeconds(type.time),
                    idHabit = stateManager.habitLinked.value?.habit?.id
                )
            }
        }

        // Isolate transactional database injection within the non-blocking IO execution scope context
        serviceScope.launch {
            timerEntryRepo.findTimeEntry(timeEntry)
        }
    }

    /**
     * Conducts a synchronized teardown of the floating desktop window layout hierarchy.
     * Manually dispatches the terminal [Lifecycle.Event.ON_DESTROY] event registry token to force
     * the disengagement of internal Jetpack Compose side-effects before physically removing
     * the root layout from the platform window manager channel.
     */
    private fun closeOverlay(){
        _lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY) // Purges dynamic Compose routine scopes
        if(overlayView != null){
            windowManager.removeView(overlayView) // Physical detachment from the OS desktop layer
        }
        overlayView = null
    }

    /**
     * Terminal systemic framework callback orchestrating the absolute deallocation of the background engine.
     * Purges hardware multimedia players, silences physical haptic responses, invalidates and cancels ongoing
     * concurrent coroutine thread scopes, unclaims power locks, and unbinds application process lifecycle hooks.
     */
    override fun onDestroy() {
        // Force absolute tracking loop termination flags
        stateManager.setRunningTimer(false)

        // Silence multimedia alerts and physical haptics immediately
        cancelAlarm()
        vibrator.cancel()

        // Annihilate active asynchronous processing structures to neutralize memory leak vectors
        timerJob?.cancel()
        notificationJob?.cancel()
        serviceJob.cancel() // Terminal collapse of the underlying SupervisorJob pipeline

        // Disengage processor power retention holding keys
        releaseWakeLock()

        // Demolish custom floating desktop layouts and detach framework process observers
        closeOverlay()
        ProcessLifecycleOwner.get().lifecycle.removeObserver(lifecycleObserver)

        super.onDestroy() // Final systemic detachment invocation
    }

}