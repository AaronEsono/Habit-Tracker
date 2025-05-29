package aeb.proyecto.ui.orientation

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

enum class Orientation {
    Portrait, Landscape
}

@Composable
fun getOrientation():Orientation{
    val localization = LocalConfiguration.current
    val orientation = localization.orientation

    return when(orientation){
        Configuration.ORIENTATION_PORTRAIT -> Orientation.Portrait
        Configuration.ORIENTATION_LANDSCAPE -> Orientation.Landscape
        else -> Orientation.Portrait
    }
}