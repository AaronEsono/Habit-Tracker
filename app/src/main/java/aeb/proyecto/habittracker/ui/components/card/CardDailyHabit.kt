package aeb.proyecto.habittracker.ui.components.card

import aeb.proyecto.habittracker.ui.components.text.LabelLargeText
import aeb.proyecto.habittracker.ui.theme.colorBackgroundCard
import aeb.proyecto.habittracker.utils.Constans.dayOfWeek
import aeb.proyecto.habittracker.utils.Constans.requiredDays
import aeb.proyecto.habittracker.utils.Dimmens.spacing2
import aeb.proyecto.habittracker.utils.Dimmens.spacing3
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek
import java.time.LocalDate

@Composable
fun CardDailyHabit() {

    val dimens = remember { mutableStateOf(45.dp) }
    val dimensIcon = remember { mutableStateOf(30.dp) }
    val dimensDay = remember { mutableStateOf(9.dp) }

    val items = remember { mutableIntStateOf(requiredDays + getPrintDays()) }

    val lazyGridState = rememberLazyGridState(
        initialFirstVisibleItemIndex = items.intValue
    )

    Card(
        colors = CardDefaults.cardColors(
            containerColor = colorBackgroundCard,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(modifier = Modifier.wrapContentSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = spacing8, end = spacing8, top = spacing8)
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0x205BA149),
                    ),
                    modifier = Modifier
                        .size(dimens.value)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Filled.Add, contentDescription = "Add", tint = Color.White,
                            modifier = Modifier.size(dimensIcon.value)
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .height(dimens.value)
                        .padding(start = spacing8),
                    verticalArrangement = Arrangement.Center
                ) {
                    LabelLargeText("Texto de prueba")
                }


                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0x205BA149),
                    ),
                    modifier = Modifier
                        .size(dimens.value)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Filled.Check,
                            contentDescription = "Add",
                            tint = Color.White,
                            modifier = Modifier.size(dimensIcon.value)
                        )
                    }
                }
            }

            Column(modifier = Modifier.wrapContentSize()) {
                LazyHorizontalGrid(
                    rows = GridCells.Fixed(7), // Fijar 7 filas
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(
                            (dimensDay.value * 7) + // Altura de las celdas
                                    (spacing2 * 6) + // Espaciado entre filas (7 filas - 1 = 6 espaciados)
                                    (spacing8 * 2)
                        )
                        .padding(spacing8),
                    state = lazyGridState,
                    horizontalArrangement = Arrangement.spacedBy(spacing2), // Espaciado entre columnas
                    verticalArrangement = Arrangement.spacedBy(spacing2) // Espaciado entre filas
                ) {
                    items(items.intValue) { index ->
                        Card(
                            modifier = Modifier
                                .size(dimensDay.value), // Tamaño fijo de cada celda
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF5BA149)
                            ),
                            shape = RoundedCornerShape(spacing3)
                        ) {}
                    }
                }
            }
        }
    }
}

fun getPrintDays():Int{
    val todayDay = LocalDate.now().dayOfWeek.value
    val daySelected = dayOfWeek

    val difference = todayDay - daySelected

    return if(difference < 0){
        8 + difference
    }else{
        difference + 1
    }
}


@Preview
@Composable
fun CardDailyHabitPreview() {
    CardDailyHabit()
}