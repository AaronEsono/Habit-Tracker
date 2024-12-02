package aeb.proyecto.habittracker

import aeb.proyecto.habittracker.ui.components.text.TitleLargeText
import aeb.proyecto.habittracker.ui.components.text.TitleMediumText
import aeb.proyecto.habittracker.ui.components.text.TitleSmallText
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import aeb.proyecto.habittracker.ui.theme.HabitTrackerTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.text.font.FontFamily

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HabitTrackerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Column (modifier = Modifier.fillMaxSize()){
        TitleLargeText(
            text = "Esto es un texto de prueba para verificar como se ve el texto especificado",
            modifier = modifier
        )

        TitleMediumText(
            text = "Esto es un texto de prueba para verificar como se ve el texto especificado",
            modifier = modifier
        )

        TitleSmallText(
            text = "Esto es un texto de prueba para verificar como se ve el texto especificado",
            modifier = modifier,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HabitTrackerTheme {
        Greeting("Android")
    }
}