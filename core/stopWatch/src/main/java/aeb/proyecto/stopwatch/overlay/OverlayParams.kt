package aeb.proyecto.stopwatch.overlay

import android.graphics.PixelFormat
import android.view.Gravity
import android.view.WindowManager

/**
 * Platform-enforced security window orchestration token boundary.
 * Authorizes the system execution layer to render high-priority floating layout canvases
 * on top of structural third-party software applications under strict platform governance rules.
 */
const val type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY

/**
 * Structural platform configuration blueprint defining low-level layout rendering vectors
 * and touch-interaction constraints for the system desktop overlay window environment.
 * Leverages defensive non-focusable flags to guarantee underlying running applications maintain
 * touch responsiveness outside the physical perimeter of the custom time-tracking interface.
 */
val LayoutParams = WindowManager.LayoutParams(
    WindowManager.LayoutParams.WRAP_CONTENT, // Dynamically bound width parameters to tightly fit layout dimensions
    WindowManager.LayoutParams.WRAP_CONTENT, // Dynamically bound height parameters to tightly fit layout dimensions
    type,
    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, // Isolates target focus variables to prevent background gesture trapping
    PixelFormat.TRANSLUCENT // Enforces dynamic hardware alpha channel mixing for seamless visual compositing
).apply {
    // Standardize the mathematical coordinate tracking origin straight to the top-left structural screen vertex
    gravity = Gravity.TOP or Gravity.START

    // Initial desktop spatial seeding offsets calculated in absolute device independent hardware pixels
    x = 100
    y = 300
}