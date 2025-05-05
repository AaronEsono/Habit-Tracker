package aeb.proyecto.stopwatch.helper

import aeb.proyecto.stopwatch.constants.CANCEL_REQUEST_CODE
import aeb.proyecto.stopwatch.constants.CLICK_REQUEST_CODE
import aeb.proyecto.stopwatch.constants.RESUME_REQUEST_CODE
import aeb.proyecto.stopwatch.constants.STOPWATCH_STATE
import aeb.proyecto.stopwatch.constants.STOP_REQUEST_CODE
import aeb.proyecto.stopwatch.service.StopWatchService
import aeb.proyecto.stopwatch.service.StopwatchState
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
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
        val clickIntent = Intent(Intent.ACTION_VIEW, "app://main".toUri()).apply {
            putExtra(STOPWATCH_STATE, StopwatchState.Started.name)
        }
        return PendingIntent.getActivity(
            context, CLICK_REQUEST_CODE, clickIntent, flag
        )
    }

    fun stopPendingIntent(): PendingIntent {
        val stopIntent = Intent(context, StopWatchService::class.java).apply {
            putExtra(STOPWATCH_STATE, StopwatchState.Stopped.name)
        }
        return PendingIntent.getService(
            context, STOP_REQUEST_CODE, stopIntent, flag
        )
    }

    fun resumePendingIntent(): PendingIntent {
        val resumeIntent = Intent(context, StopWatchService::class.java).apply {
            putExtra(STOPWATCH_STATE, StopwatchState.Started.name)
        }
        return PendingIntent.getService(
            context, RESUME_REQUEST_CODE, resumeIntent, flag
        )
    }

    fun cancelPendingIntent(): PendingIntent {
        val cancelIntent = Intent(context, StopWatchService::class.java).apply {
            putExtra(STOPWATCH_STATE, StopwatchState.Canceled.name)
        }
        return PendingIntent.getService(
            context, CANCEL_REQUEST_CODE, cancelIntent, flag
        )
    }

    fun triggerForegroundService(action: String) {
        Intent(context, StopWatchService::class.java).apply {
            this.action = action
            context.startService(this)
        }
    }
}