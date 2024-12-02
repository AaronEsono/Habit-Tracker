package aeb.proyecto.habittracker

import aeb.proyecto.habittracker.ui.components.text.LabelSmallText
import aeb.proyecto.habittracker.ui.components.text.TitleLargeText
import aeb.proyecto.habittracker.ui.components.text.TitleMediumText
import aeb.proyecto.habittracker.ui.theme.HabitTrackerTheme
import aeb.proyecto.habittracker.ui.theme.primaryColorApp
import aeb.proyecto.habittracker.ui.theme.secondaryColorApp
import aeb.proyecto.habittracker.utils.Constans.NAVIGATIONBARITEMS
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
fun AppContent() {
    Scaffold(
        topBar = { TopBatHabit() },
        bottomBar = {
            BottomNavigationHabit()
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .background(primaryColorApp)) {

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBatHabit(){
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = secondaryColorApp,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            TitleLargeText(text = stringResource(R.string.topbar_habit), textAlign = TextAlign.Center)
        }
    )
}

@Composable
fun BottomNavigationHabit() {

    val menuItems = NAVIGATIONBARITEMS

    NavigationBar(
        contentColor = secondaryColorApp,
        containerColor = secondaryColorApp
    ) {
        menuItems.forEach {
            NavigationBarItem(
                selected = it.title == R.string.bottombar_habit,
                onClick = { /*TODO*/ },
                icon = {
                    Icon(
                        painter = painterResource(it.icon),
                        contentDescription = stringResource(it.title),
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    LabelSmallText(stringResource(it.title))
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Black,
                    indicatorColor = Color.White
                )
            )
        }
    }

}