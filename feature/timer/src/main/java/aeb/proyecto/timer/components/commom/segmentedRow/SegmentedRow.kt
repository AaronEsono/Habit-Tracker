package aeb.proyecto.timer.components.commom.segmentedRow

import aeb.proyecto.timer.model.SegmentedButtonOptions
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.orientation.Orientation
import aeb.proyecto.ui.orientation.getOrientation
import aeb.proyecto.ui.text.LabelLargeText
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.window.core.layout.WindowWidthSizeClass

@Composable
fun SegmentedRow(
    modifier:Modifier = Modifier,
    segmentedList:List<SegmentedButtonOptions>,
    onClickOption: (Int) -> Unit,
    typeTimer: SegmentedButtonOptions
){

    val adaptiveInfo = currentWindowAdaptiveInfo()
    val windowSizeClass = adaptiveInfo.windowSizeClass
    val orientation = getOrientation()

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {

        SingleChoiceSegmentedButtonRow {

            segmentedList.forEachIndexed { index, segmentedOption ->
                val shape = remember (index){
                    when (index) {
                        0 -> RoundedCornerShape(
                            topStart = spacing8,
                            bottomStart = spacing8,
                            topEnd = 0.dp,
                            bottomEnd = 0.dp
                        )

                        segmentedList.lastIndex -> RoundedCornerShape(
                            topStart = 0.dp,
                            bottomStart = 0.dp,
                            topEnd = spacing8,
                            bottomEnd = spacing8
                        )

                        else -> RoundedCornerShape(0.dp)
                    }
                }

                SegmentedButton(
                    shape = shape,
                    onClick = { onClickOption(segmentedOption.key) },
                    colors = SegmentedButtonDefaults.colors(
                        activeContentColor = MaterialTheme.colorScheme.inverseOnSurface,
                        disabledActiveContentColor = MaterialTheme.colorScheme.onSurface,
                        activeContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                    ),
                    label = {
                        val colorSelected = if (segmentedOption == typeTimer) {
                            MaterialTheme.colorScheme.inverseOnSurface
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        }

                        // Ajustes personalizados según el widthSizeClass
                        val iconSize = remember (windowSizeClass.windowWidthSizeClass,orientation){
                            when(orientation){
                                Orientation.Portrait -> {
                                    when (windowSizeClass.windowWidthSizeClass) {
                                        WindowWidthSizeClass.COMPACT -> 24.dp
                                        WindowWidthSizeClass.MEDIUM -> 28.dp
                                        WindowWidthSizeClass.EXPANDED -> 32.dp
                                        else -> 28.dp
                                    }
                                }
                                Orientation.Landscape -> {
                                    when (windowSizeClass.windowWidthSizeClass) {
                                        WindowWidthSizeClass.COMPACT -> 16.dp
                                        WindowWidthSizeClass.MEDIUM -> 20.dp
                                        WindowWidthSizeClass.EXPANDED -> 24.dp
                                        else -> 28.dp
                                    }
                                }
                            }
                        }

                        val fontSize = remember (windowSizeClass.windowWidthSizeClass, orientation){
                            when(orientation){
                                Orientation.Portrait -> {
                                    when (windowSizeClass.windowWidthSizeClass) {
                                        WindowWidthSizeClass.COMPACT -> 14.sp
                                        WindowWidthSizeClass.MEDIUM -> 16.sp
                                        WindowWidthSizeClass.EXPANDED -> 18.sp
                                        else -> 16.sp
                                    }
                                }
                                Orientation.Landscape -> {
                                    when (windowSizeClass.windowWidthSizeClass) {
                                        WindowWidthSizeClass.COMPACT -> 10.sp
                                        WindowWidthSizeClass.MEDIUM -> 12.sp
                                        WindowWidthSizeClass.EXPANDED -> 14.sp
                                        else -> 16.sp
                                    }
                                }
                            }
                        }

                        Column (
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Icon(
                                imageVector = segmentedOption.icon,
                                contentDescription = "icon type timer button",
                                modifier = Modifier.size(iconSize)
                            )

                            LabelLargeText(
                                stringResource(segmentedOption.title),
                                modifier = Modifier.padding(top = spacing2),
                                color = colorSelected,
                                maxLines = 1,
                                fontSize = fontSize,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    },
                    selected = segmentedOption == typeTimer,
                    icon = {},
                )

            }

        }

    }

}