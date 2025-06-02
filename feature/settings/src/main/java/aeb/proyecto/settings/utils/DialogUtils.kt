package aeb.proyecto.settings.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import java.time.DayOfWeek

@Composable
fun setContainerColorButton(theme: Int, themeSelected: Int): Color {
    return if (theme == themeSelected) MaterialTheme.colorScheme.surfaceContainer else MaterialTheme.colorScheme.background
}

@Composable
fun setContainerColorButton(language: String, languageSelected: String): Color {
    return if (language == languageSelected) MaterialTheme.colorScheme.surfaceContainer else MaterialTheme.colorScheme.background
}

@Composable
fun setContainerColorButton(day: DayOfWeek, daySelected: String): Color {
    return if (day.toString() == daySelected) MaterialTheme.colorScheme.surfaceContainer else MaterialTheme.colorScheme.background
}