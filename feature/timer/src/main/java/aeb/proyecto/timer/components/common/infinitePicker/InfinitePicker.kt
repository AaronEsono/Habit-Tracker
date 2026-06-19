package aeb.proyecto.timer.components.common.infinitePicker

import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.text.LabelLargeText
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


/**
 * A highly customizable infinite wheel picker component.
 * It uses a [LazyListState] to calculate which item is centered and provides
 * haptic feedback upon selection.
 *
 * @param modifier Applied to the picker container.
 * @param listState The state of the LazyColumn.
 * @param items The data list to display.
 * @param colorGradient Background color for the overlay effect.
 * @param fontSizeItem The font size of the picker items.
 * @param visibleItemsCount Number of items visible in the picker area.
 * @param currentItemSelected Callback triggered when a new item becomes centered.
 * @param onClickCenter Action triggered when clicking the center item.
 * @param scrollToItem Action to programmatically scroll the picker.
 */
@Composable
fun InfinitePicker(
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    items: List<String>,
    colorGradient:Color = MaterialTheme.colorScheme.background,
    fontSizeItem: TextUnit = 48.sp,
    visibleItemsCount: Int = 3,
    currentItemSelected: (String) -> Unit = {_ ->},
    onClickCenter: (String) -> Unit = {},
    scrollToItem: (Int) -> Unit = {}
){

    val density = LocalDensity.current
    val haptic = LocalHapticFeedback.current

    val heightItem = remember {
        with(density) { fontSizeItem.toDp() * 1.5f }
    }

    val currentItemIndex = remember {
        derivedStateOf {
            //Items en pantalla
            val visibleItems = listState.layoutInfo.visibleItemsInfo
            // Centro de la pantalla
            val centerY = heightItem * visibleItemsCount / 2

            var closestIndex = -1
            var closestDistance = Float.MAX_VALUE

            // Buscamos el item más cercano al centro
            visibleItems.forEach { item ->
                // Calculamos el centro de cada item
                val itemCenter = item.offset + (item.size / 2)

                // Calculamos la distancia del centro del item al centro visible
                val distance = kotlin.math.abs(itemCenter.toFloat() - centerY.value)

                if (distance < closestDistance) {
                    closestDistance = distance
                    closestIndex = item.index
                }
            }

            closestIndex % items.size
        }
    }

    LaunchedEffect (currentItemIndex.value){
        currentItemSelected(items[currentItemIndex.value])
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
    }

    Box(
        modifier = modifier.height(heightItem * visibleItemsCount).width(heightItem)
    ){
        LazyColumn (
            state = listState,
            flingBehavior = rememberSnapFlingBehavior(
                lazyListState = listState
            ),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = heightItem * (visibleItemsCount / 2)),
            verticalArrangement = Arrangement.spacedBy(spacing4),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            items(Int.MAX_VALUE){ index ->
                val realIndex = index % items.size

                //Cada elemento
                Box(
                    modifier = Modifier
                        .height(heightItem)
                        .wrapContentWidth()
                        .clickable (
                            interactionSource = null,
                            indication = null
                        ){
                            val offset = (visibleItemsCount / 2) - 1
                            val target = index - offset

                            if ((index % items.size) == currentItemIndex.value) {
                                onClickCenter(items[realIndex])
                            } else {
                                scrollToItem(target)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    LabelLargeText(
                        text = items[realIndex],
                        textAlign = TextAlign.Center,
                        fontSize = fontSizeItem
                    )
                }
            }
        }

        // Filtro de transparencia en los bordes
        Box(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer { alpha = 0.99f } // Necesario para que Compose use el layer
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            colorGradient,
                            Color.Transparent,
                            colorGradient
                        ),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
        )

        //Selected UI
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .height(heightItem)
        ) {
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline,
                thickness = 2.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
            )

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline,
                thickness = 2.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            )
        }
    }
}

fun getCenteredIndex(itemCount: Int, startIndex: Int): Int {
    return (Int.MAX_VALUE / 2) - ((Int.MAX_VALUE / 2) % itemCount) + startIndex
}