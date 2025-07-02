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

@Singleton
class StopWatchHelper @Inject constructor(
    @ApplicationContext private val context: Context
){

    private val flag = PendingIntent.FLAG_IMMUTABLE

    fun clickPendingIntent(): PendingIntent {
        val clickIntent = Intent(Intent.ACTION_VIEW, "app://main/timer".toUri()).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("destination", "timer")
        }
        return PendingIntent.getActivity(
            context, CLICK_REQUEST_CODE, clickIntent, flag
        )
    }

    fun finishPendingIntent(): PendingIntent {
        val finishIntent = Intent(context, StopWatchService::class.java).apply {
            action = ACTION_SERVICE_FINISH
        }

        return PendingIntent.getService(
            context, CLICK_REQUEST_CODE, finishIntent, flag
        )
    }

    fun finishService(){
        val intent = Intent(context, StopWatchService::class.java).apply {
            action = ACTION_SERVICE_FINISH
        }
        context.startService(intent)
    }

    fun resumePendingIntent(): PendingIntent {
        val resumeIntent = Intent(context, StopWatchService::class.java).apply {
            action = ACTION_SERVICE_RESUME
        }

        return PendingIntent.getService(
            context, RESUME_REQUEST_CODE, resumeIntent, flag
        )
    }

    fun resumeService(){
        val intent = Intent(context, StopWatchService::class.java).apply {
            action = ACTION_SERVICE_RESUME
        }
        context.startService(intent)
    }

    fun stopPendingIntent(): PendingIntent {
        val stopIntent = Intent(context,StopWatchService::class.java).apply {
            action = ACTION_SERVICE_STOP
        }

        return PendingIntent.getService(
            context, STOP_REQUEST_CODE, stopIntent, flag
        )
    }

    fun stopService(){
        val intent = Intent(context, StopWatchService::class.java).apply {
            action = ACTION_SERVICE_STOP
        }
        context.startService(intent)
    }

    fun cancelPendingIntent() : PendingIntent {
        val cancelIntent = Intent(context, StopWatchService::class.java).apply {
            action = ACTION_SERVICE_CANCEL
        }

        return PendingIntent.getService(
            context, CANCEL_REQUEST_CODE, cancelIntent, flag
        )
    }

    fun cancelService(){
        val intent = Intent(context, StopWatchService::class.java).apply {
            action = ACTION_SERVICE_CANCEL
        }

        context.startService(intent)
    }

    fun startForegroundServiceOnStopWatch(habitLinked:Pair<Long,String>){
        Intent(context, StopWatchService::class.java).apply {
            this.action = ACTION_SERVICE_START_STOPWATCH
            putExtra("habitId",habitLinked.first)
            putExtra("habitDay",habitLinked.second)
            ContextCompat.startForegroundService(context,this)
        }
    }

    fun startForegroundServiceOnTimer(time:Long,habitLinked:Pair<Long,String>){
        Intent(context,StopWatchService::class.java).apply {
            putExtra("time",time)
            putExtra("habitId",habitLinked.first)
            putExtra("habitDay",habitLinked.second)
            this.action = ACTION_SERVICE_START_TIMER
            ContextCompat.startForegroundService(context,this)
        }
    }

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