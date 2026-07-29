package aeb.proyecto.addhabit.components.common.grid

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times

// ============================================================================
// GRID DIMENSIONAL ARCHITECTURE METRICS
// ============================================================================

const val ROWS = 3
val itemSize = 30.dp
val verticalPadding = spacing4
val verticalSpacing = spacing8

/**
 * Strict structural height constraint blueprint calculating the exact vertical boundary allocation.
 * Limits the container footprint dynamically to a clean 3-row viewport ceiling.
 */
val height: Dp = ((ROWS - 0.5) * itemSize) + ((ROWS - 1) * verticalSpacing) + (verticalPadding * 2)

/**
 * A highly tailored adaptive selection grid sheet designed to manage asset customization pipelines.
 * Displays a fluid, column-wrapping inventory dashboard accommodating either programmatic canvas vector
 * paint spots or illustrative symbols depending on the active [GridOption] branch state.
 *
 * @param modifier Structural [Modifier] assembly to alter or extend the layout constraints.
 * @param lazyGridState Backing interaction coordinator pipeline managing state indexing for the inner grid layout.
 * @param gridOption Structural filtering parameter flag defining whether to render a color palette array or an icon ledger sheet.
 * @param colorSelected The primary active design [Color] token defining the current configuration boundary.
 * @param iconSelected The targeted [ImageVector] asset token identifying the current functional choice index.
 * @param onClickGridOption Polymorphic data-result callback carrier lambda emitting selected values downstream.
 */
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
                                        .testTag("add_habit_grid_color_${color.hashCode()}"),
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
                                        }
                                        .testTag("add_habit_grid_icon_${icon.hashCode()}"),
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

/**
 * Calculates a dynamic [BorderStroke] focus marker to map around the currently selected active color node.
 */
@Composable
fun colorSelected(color: Color, colorSelected: Color): BorderStroke {
    return if (color == colorSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface)
    else BorderStroke(0.dp, Color.Transparent)
}

/**
 * Evaluates tactical tint distribution parameters across symbolic glyph grids based on active focus indices.
 */
@Composable
fun iconSelected(icon: ImageVector, iconSelected: ImageVector, color: Color): Color {
    return if (icon == iconSelected) color else MaterialTheme.colorScheme.onSurface
}