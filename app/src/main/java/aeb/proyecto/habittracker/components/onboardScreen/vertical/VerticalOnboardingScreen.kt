package aeb.proyecto.habittracker.components.onboardScreen.vertical

import aeb.proyecto.habittracker.components.onboardScreen.components.constants.OnboardingPage
import aeb.proyecto.habittracker.components.onboardScreen.components.constants.ResultOptions
import aeb.proyecto.ui.text.LabelMediumText
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun VerticalOnboardingScreen(
    pageSelected: OnboardingPage,
    onClickResultOption: (ResultOptions) -> Unit
){

    Column(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .safeDrawingPadding()
    ) {
        LabelMediumText("Holaaa")

        Spacer(modifier = Modifier.weight(1f))

        LabelMediumText("Holaaa")
    }

}