package aeb.proyecto.habittracker.ui.components.dailyHabit

import aeb.proyecto.habittracker.utils.Dimmens.spacing3
import aeb.proyecto.habittracker.utils.Dimmens.spacing4
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CardDailyHabitDays(
    precomputedItems: List<Color>,
){

    val dimensDay = remember { 12.dp }

    val lazyGridState = rememberLazyGridState(
        initialFirstVisibleItemIndex = precomputedItems.size
    )

    Column(modifier = Modifier.wrapContentSize()) {
        LazyHorizontalGrid(
            rows = GridCells.Fixed(7), // Fijar 7 filas
            modifier = Modifier
                .fillMaxWidth()
                .height(
                    (dimensDay * 7) + // Altura de las celdas
                            (spacing4 * 6) + // Espaciado entre filas (7 filas - 1 = 6 espaciados)
                            (spacing8 * 2)
                )
                .padding(spacing8),
            state = lazyGridState,
            horizontalArrangement = Arrangement.spacedBy(spacing3), // Espaciado entre columnas
            verticalArrangement = Arrangement.spacedBy(spacing4) // Espaciado entre filas
        ) {
            itemsIndexed(items = precomputedItems, key = { int, _ -> int }) { _, color ->
                Canvas(modifier = Modifier.size(dimensDay)) {
                    drawRoundRect(
                        color = color,
                        size = size,
                        cornerRadius = CornerRadius(spacing3.toPx(), spacing3.toPx())
                    )
                }
            }
        }
    }
}