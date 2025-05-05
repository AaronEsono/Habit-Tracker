package aeb.proyecto.timer.components.infinitePicker

import aeb.proyecto.timer.R
import aeb.proyecto.timer.components.textField.TimerTextField
import aeb.proyecto.timer.constants.TypeUnitDate
import aeb.proyecto.ui.dialog.CustomDialog
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing20
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.regexTextField.IsOnlyZeroTo59
import aeb.proyecto.ui.regexTextField.IsOnlyZeroTo99
import aeb.proyecto.ui.ripple.CustomRipple
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.LabelMediumText
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch


@Composable
fun InfinitePicker(
    modifier: Modifier = Modifier,
    items: List<String>,
    alertDialogTitle:String,
    typeList: TypeUnitDate,
    fontSizeItem: TextUnit = 48.sp,
    startIndex:Int = 1,
    visibleItemsCount: Int = 3,
    onTextSelected: (String) -> Unit = {_ -> }
){

    // Mitad para que haya la misma cantidad de elementos tanto atras como hacia delante.
    val initialIndex = (Int.MAX_VALUE / 2) - ((Int.MAX_VALUE / 2) % items.size) + startIndex
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)

    val density = LocalDensity.current

    val heightItem = remember {
        with(density) { fontSizeItem.toDp() * 1.5f }
    }

    val scope = rememberCoroutineScope()
    var showInputDialog by remember { mutableStateOf(false) }

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
        onTextSelected(items[currentItemIndex.value])
    }

    val context = LocalContext.current

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
                                showInputDialog = true
                            } else {
                                scope.launch {
                                    listState.scrollToItem(target)
                                }
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
                            MaterialTheme.colorScheme.background,
                            Color.Transparent,
                            MaterialTheme.colorScheme.background
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

    if(showInputDialog){
        AlertDialog(
            initialText = items[currentItemIndex.value],
            label = alertDialogTitle,
            typeList = typeList,
            onDismissRequest = { showInputDialog = false },
            onAccept = {number ->
                val newIndex = (Int.MAX_VALUE / 2) - ((Int.MAX_VALUE / 2) % items.size) + number
                scope.launch {
                    listState.animateScrollToItem(newIndex)
                }
            }
        )
    }

}

@Composable
fun AlertDialog(
    label:String,
    typeList:TypeUnitDate,
    initialText:String = "00",
    onDismissRequest: () -> Unit = {},
    onAccept: (Int) -> Unit = {}
){
    val textFieldState = rememberTextFieldState(initialText = initialText)

    when(typeList){
        TypeUnitDate.Minutes,TypeUnitDate.Seconds -> {
            IsOnlyZeroTo59(textFieldState)
        }
        TypeUnitDate.Hours -> {
            IsOnlyZeroTo99(textFieldState)
        }
    }

    CustomDialog(
        onDismissRequest = onDismissRequest,
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        Column (
            modifier = Modifier.fillMaxWidth(0.8f)
                .padding(start = spacing12, end = spacing4, bottom = spacing8, top = spacing20),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Column {
                TimerTextField(
                    textFieldState = textFieldState,
                )

                LabelMediumText(label,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(top = spacing2))
            }

            Row (
                modifier = Modifier.fillMaxWidth().padding(top = spacing20),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ){

                CustomRipple {
                    TextButton(
                        onClick = onDismissRequest,
                        shape = RoundedCornerShape(spacing12)
                    ) {
                        LabelLargeText(
                            stringResource(R.string.timer_cancel),
                            fontSize = 16.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                CustomRipple {
                    TextButton(
                        onClick = {
                            val number = textFieldState.text.toString().toIntOrNull() ?: 0
                            onAccept(number)
                            onDismissRequest()
                        },
                        shape = RoundedCornerShape(spacing12)
                    ) {
                        LabelLargeText(
                            stringResource(R.string.timer_accept),
                            fontSize = 16.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}