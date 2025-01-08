package aeb.proyecto.habittracker.ui.screens.addHabit.AddHabitComponents

import aeb.proyecto.habittracker.ui.components.items.ColorItem
import aeb.proyecto.habittracker.ui.components.items.IconItem
import aeb.proyecto.habittracker.ui.screens.addHabit.AddHabitViewModel
import aeb.proyecto.habittracker.ui.theme.pickColors
import aeb.proyecto.habittracker.utils.Constans.ListIcons
import aeb.proyecto.habittracker.utils.Dimmens.spacing12
import aeb.proyecto.habittracker.utils.Dimmens.spacing4
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun GridOptions(
    color: Color,
    showColors: Boolean,
    addHabitViewModel: AddHabitViewModel,
    imageVector: ImageVector,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 120.dp)
    ) {
        Card(
            modifier = Modifier
                .wrapContentSize()
                .padding(bottom = spacing12), elevation = CardDefaults.cardElevation(
                defaultElevation = spacing8
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(30.dp),
                modifier = Modifier
                    .wrapContentSize()
                    .padding(vertical = spacing4),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (showColors) {
                    items(pickColors.size) { item ->
                        val selected = color == pickColors[item]

                        ColorItem(
                            color = pickColors[item],
                            modifier = if (selected)
                                Modifier.border(2.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                            else Modifier,
                        ) {
                            addHabitViewModel.closeColor(it)
                        }

                    }
                } else {
                    items(ListIcons.size) { item ->
                        IconItem(
                            imageVector = ListIcons[item],
                            color = if(imageVector == ListIcons[item]) color else MaterialTheme.colorScheme.onSurface,
                        ) {
                            addHabitViewModel.closeIcon(it)
                        }
                    }
                }
            }
        }
    }
}