package aeb.proyecto.stopwatch.helper

import aeb.proyecto.stopwatch.constants.ACTION_SERVICE_CANCEL
import aeb.proyecto.stopwatch.constants.ACTION_SERVICE_FINISH
import aeb.proyecto.stopwatch.constants.ACTION_SERVICE_RESUME
import aeb.proyecto.stopwatch.constants.ACTION_SERVICE_START_INTERVAL
import aeb.proyecto.stopwatch.constants.ACTION_SERVICE_START_STOPWATCH
import aeb.proyecto.stopwatch.constants.ACTION_SERVICE_START_TIMER
import aeb.proyecto.stopwatch.constants.ACTION_SERVICE_STOP
import aeb.proyecto.stopwatch.constants.CANCEL_REQUEST_CODE
import aeb.proyecto.stopwatch.constants.CLICK_REQUEST_CODE
import aeb.proyecto.stopwatch.constants.RESUME_REQUEST_CODE
import aeb.proyecto.stopwatch.constants.STOP_REQUEST_CODE
import aeb.proyecto.stopwatch.manager.StopwatchState
import aeb.proyecto.stopwatch.service.StopWatchService
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Mission-critical intent broker and navigation router coordinating communications
 * between presentation components, notification interaction channels, and the long-running
 * time tracking background framework service.
 *
 * This helper centralizes token abstractions ([PendingIntent]) for OS-level interactive widgets
 * alongside immediate control commands targeting [StopWatchService].
 *
 * @property context The systemic non-leaking application context framework link.
 */
@Singleton
class StopWatchHelper @Inject constructor(
    @ApplicationContext private val context: Context
){

    /** Security enforcement flag locking intent payloads against third-party framework tampering. */
    private val flag = PendingIntent.FLAG_IMMUTABLE

    /**
     * Constructs a deep-linked pending navigation token utilized when tapping the persistent notification.
     * Clears legacy tasks to land the user directly onto the destination tracking interface screen.
     *
     * @return A deep-linked, structural activity [PendingIntent].
     */
    fun clickPendingIntent(): PendingIntent {
        val clickIntent = Intent(Intent.ACTION_VIEW, "app://main/timer".toUri()).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("destination", "timer")
        }
        return PendingIntent.getActivity(
            context, CLICK_REQUEST_CODE, clickIntent, flag
        )
    }

    /**
     * Constructs a service action token signaling execution completion loops from background components.
     */
    fun finishPendingIntent(): PendingIntent {
        val finishIntent = Intent(context, StopWatchService::class.java).apply {
            action = ACTION_SERVICE_FINISH
        }

        return PendingIntent.getService(
            context, CLICK_REQUEST_CODE, finishIntent, flag
        )
    }

    /**
     * Issues an immediate lifecycle directive to terminate the active time tracking engine.
     */
    fun finishService(){
        val intent = Intent(context, StopWatchService::class.java).apply {
            action = ACTION_SERVICE_FINISH
        }
        context.startService(intent)
    }

    /**
     * Constructs a notification action token to unpause and resume active background calculation loops.
     */
    fun resumePendingIntent(): PendingIntent {
        val resumeIntent = Intent(context, StopWatchService::class.java).apply {
            action = ACTION_SERVICE_RESUME
        }

        return PendingIntent.getService(
            context, RESUME_REQUEST_CODE, resumeIntent, flag
        )
    }

    /**
     * Issues an immediate directive to resume tracking metrics from a paused position state.
     */
    fun resumeService(){
        val intent = Intent(context, StopWatchService::class.java).apply {
            action = ACTION_SERVICE_RESUME
        }
        context.startService(intent)
    }

    /**
     * Constructs a notification action token to temporarily halt background execution ticks.
     */
    fun stopPendingIntent(): PendingIntent {
        val stopIntent = Intent(context,StopWatchService::class.java).apply {
            action = ACTION_SERVICE_STOP
        }

        return PendingIntent.getService(
            context, STOP_REQUEST_CODE, stopIntent, flag
        )
    }

    /**
     * Issues an immediate directive to pause time tracking progress.
     */
    fun stopService(){
        val intent = Intent(context, StopWatchService::class.java).apply {
            action = ACTION_SERVICE_STOP
        }
        context.startService(intent)
    }

    /**
     * Constructs a destructive notification action token to entirely purge and reset active trackers.
     */
    fun cancelPendingIntent() : PendingIntent {
        val cancelIntent = Intent(context, StopWatchService::class.java).apply {
            action = ACTION_SERVICE_CANCEL
        }

        return PendingIntent.getService(
            context, CANCEL_REQUEST_CODE, cancelIntent, flag
        )
    }

    /**
     * Issues an immediate directive to abort current tasks and completely reset tracking sessions.
     */
    fun cancelService(){
        val intent = Intent(context, StopWatchService::class.java).apply {
            action = ACTION_SERVICE_CANCEL
        }

        context.startService(intent)
    }

    /**
     * Spawns a modern Foreground Service constraint loop bound to the progressive Stopwatch operational mode.
     * Attaches relational data keys to associate the session with a localized target habit layout.
     *
     * @param habitLinked Relational metadata packing the [Long] Primary Key ID alongside its [String] calendar reference date.
     */
    fun startForegroundServiceOnStopWatch(habitLinked:Pair<Long,String>){
        Intent(context, StopWatchService::class.java).apply {
            this.action = ACTION_SERVICE_START_STOPWATCH
            putExtra("habitId",habitLinked.first)
            putExtra("habitDay",habitLinked.second)
            ContextCompat.startForegroundService(context,this)
        }
    }

    /**
     * Spawns a modern Foreground Service constraint loop bound to the regressive Countdown Timer operational mode.
     *
     * @param time The targeted total execution lifespan allocated in raw milliseconds.
     * @param habitLinked Relational metadata packing the [Long] Primary Key ID alongside its [String] calendar reference date.
     */
    fun startForegroundServiceOnTimer(time:Long,habitLinked:Pair<Long,String>){
        Intent(context,StopWatchService::class.java).apply {
            putExtra("time",time)
            putExtra("habitId",habitLinked.first)
            putExtra("habitDay",habitLinked.second)
            this.action = ACTION_SERVICE_START_TIMER
            ContextCompat.startForegroundService(context,this)
        }
    }

    /**
     * Spawns a complex multi-segment Foreground Service constraint loop bound to the Interval training mode.
     *
     * @param time Active exertion segment duration limit threshold values in raw seconds/milliseconds.
     * @param rest Passive recovery segment duration limit threshold values in raw seconds/milliseconds.
     * @param interval Total target repetition cycles designated to complete the exercise profile grid.
     * @param habitLinked Relational metadata packing the [Long] Primary Key ID alongside its [String] calendar reference date.
     */
    fun startForegroundServiceOnInterval(time:Long,rest:Long,interval:Int,habitLinked:Pair<Long,String>){
        Intent(context,StopWatchService::class.java).apply {
            putExtra("time",time)
            putExtra("rest",rest)
            putExtra("interval",interval)
            putExtra("habitId",habitLinked.first)
            putExtra("habitDay",habitLinked.second)
            this.action = ACTION_SERVICE_START_INTERVAL
            ContextCompat.startForegroundService(context,this)
        }
    }

}