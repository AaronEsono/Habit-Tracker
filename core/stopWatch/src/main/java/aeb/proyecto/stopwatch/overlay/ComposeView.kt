package aeb.proyecto.stopwatch.overlay

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.compose.ui.platform.ComposeView


@SuppressLint("ClickableViewAccessibility")

fun layoutMovement(layout:ComposeView, windowManager: WindowManager){
    layout.setOnTouchListener(object : View.OnTouchListener {
        private var initialX = 0
        private var initialY = 0
        private var touchX = 0f
        private var touchY = 0f
        override fun onTouch(v: View, event: MotionEvent): Boolean {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialX = LayoutParams.x
                    initialY = LayoutParams.y
                    touchX = event.rawX
                    touchY = event.rawY
                    return true
                }
                MotionEvent.ACTION_MOVE -> {
                    val dx = (event.rawX - touchX).toInt()
                    val dy = (event.rawY - touchY).toInt()
                    LayoutParams.x = initialX + dx
                    LayoutParams.y = initialY + dy
                    windowManager.updateViewLayout(layout, LayoutParams)
                    return true
                }
                MotionEvent.ACTION_UP -> {
                    v.performClick()
                    return true
                }
            }
            return false
        }
    })
}