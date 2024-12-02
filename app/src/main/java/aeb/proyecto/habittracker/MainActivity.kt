package aeb.proyecto.habittracker

import aeb.proyecto.habittracker.ui.theme.HabitTrackerTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            HabitTrackerTheme {
                val navController = rememberNavController()

                AppContent()
            }
        }
    }
}

@Composable
fun AppContent(){
    Scaffold (
        topBar = {},
        bottomBar = {}
    ){ innerPadding ->
        Column (modifier = Modifier.padding(innerPadding).fillMaxSize()){

        }
    }
}