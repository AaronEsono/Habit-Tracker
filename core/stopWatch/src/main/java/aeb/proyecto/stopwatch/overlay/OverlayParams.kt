package aeb.proyecto.stopwatch.overlay

import android.graphics.PixelFormat
import android.view.Gravity
import android.view.WindowManager

    const val type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY

    val LayoutParams = WindowManager.LayoutParams(
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.WRAP_CONTENT,
        type,
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
        PixelFormat.TRANSLUCENT
    ).apply {
        gravity = Gravity.TOP or Gravity.START
        x = 100
        y = 300
    }