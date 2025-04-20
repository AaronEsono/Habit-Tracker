package aeb.proyecto.addhabit.components.grid

import aeb.proyecto.addhabit.constants.GridOption
import aeb.proyecto.addhabit.constants.GridOptionResult
import aeb.proyecto.ui.constants.listColors
import aeb.proyecto.ui.constants.listIcons
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times

const val ROWS = 3
val itemSize = 30.dp

val verticalPadding = spacing4
val verticalSpacing = spacing8

val height: Dp = ((ROWS - 0.5) * itemSize) + ((ROWS - 1) * verticalSpacing) + (verticalPadding * 2)

@Composable
fun AddHabitGrid(
    modifier:Modifier = Modifier,
    lazyGridState: LazyGridState = rememberLazyGridState(),
    gridOption: GridOption,
    colorSelected: Color,
    iconSelected: ImageVector,
    onClickGridOption: (GridOptionResult) -> Unit = {}
){

    Column (
        modifier = modifier.fillMaxWidth()
    ){
        ElevatedCard(
            modifier = Modifier.heightIn(max = height),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {

            LazyVerticalGrid(
                columns = GridCells.Adaptive(itemSize),
                state = lazyGridState,
                modifier = Modifier.wrapContentHeight().padding(vertical = verticalPadding),
                contentPadding = PaddingValues(horizontal = spacing8),
                verticalArrangement = Arrangement.spacedBy(verticalSpacing),
                horizontalArrangement = Arrangement.spacedBy(spacing6)
            ) {

                when(gridOption){
                    GridOption.COLORS -> {
                        listColors.forEach { color ->
                            item {
                                Canvas(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .border(colorSelected(color, colorSelected), CircleShape)
                                        .size(itemSize)
                                        .clickable {
                                            onClickGridOption(GridOptionResult.colorResult(color))
                                        }
                                ) {
                                    drawCircle(color = color,radius = size.minDimension / 2)
                                }
                            }
                        }
                    }

                    GridOption.ICONS -> {
                        listIcons.forEach { icon ->
                            item {
                                Icon(
                                    icon, contentDescription = "Icon grid option",
                                    modifier = Modifier.size(itemSize)
                                        .clickable {
                                            onClickGridOption(GridOptionResult.iconResult(icon))
                                        },
                                    tint = iconSelected(icon, iconSelected, colorSelected)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun colorSelected(color: Color, colorSelected: Color): BorderStroke {
    return if (color == colorSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface)
    else BorderStroke(0.dp, Color.Transparent)
}

@Composable
fun iconSelected(icon: ImageVector, iconSelected: ImageVector, color: Color): Color {
    return if (icon == iconSelected) color else MaterialTheme.colorScheme.onSurface
}