package aeb.proyecto.habittracker.components.onboardScreen

import aeb.proyecto.ui.orientation.getOrientation
import aeb.proyecto.ui.text.LabelMediumText
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun OnboardScreen(){

    val orientation = getOrientation()

    Column(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {

        LabelMediumText("Holiwii")

    }

}