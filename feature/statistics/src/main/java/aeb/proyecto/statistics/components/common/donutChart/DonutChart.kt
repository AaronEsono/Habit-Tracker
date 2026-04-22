package aeb.proyecto.statistics.components.common.donutChart

import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelMediumText
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import kotlin.collections.mutableListOf

@Composable
fun DonutChart(
    modifier: Modifier = Modifier,
    data: DonutChartDataCollection,
    chartSize: Dp = 350.dp,
    gapPercentage: Float = 0.04f,
    selectionView: @Composable (selectedItem: DonutChartData?) -> Unit = {_ -> },
){


    val anglesList: MutableList<DrawingAngles> = remember { mutableListOf() }
    val gapAngle = data.calculateGapAngle(gapPercentage)

    // El índice del elemento pulsado
    var selectedIndex by remember { mutableIntStateOf(-1) }

    // Creamos una lista de grosores animados, uno para cada arco
    val animValues = data.items.mapIndexed { index, _ ->
        animateDpAsState(
            targetValue = if (selectedIndex == index) STROKE_SIZE_SELECTED else STROKE_SIZE_UNSELECTED,
            animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing),
            label = "StrokeAnimation"
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = spacing6)
            .border(
                1.dp,
                MaterialTheme.colorScheme.outline,
                RoundedCornerShape(spacing6)
            )
            .background(MaterialTheme.colorScheme.surfaceTint),
        contentAlignment = Alignment.Center
    ) {
        // 2
        Canvas(
            modifier = Modifier
                .padding(vertical = spacing8)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { tapOffset ->
                            val index = findTappedAngleIndex(
                                tapOffset = tapOffset,
                                canvasSize = size.toSize(),
                                // Usamos el grosor base para la detección
                                strokeWidthPx = STROKE_SIZE_UNSELECTED.toPx(),
                                anglesList = anglesList
                            )

                            selectedIndex = if (selectedIndex == index) -1 else index

                        }
                    )
                }
                .size(chartSize),
            // 3
            onDraw = {
                anglesList.clear()
                var lastAngle = 0f

                // 1. Definimos el grosor máximo que alcanzará el donut para dejar espacio
                val maxPossibleStroke = STROKE_SIZE_SELECTED.toPx()

                // 2. El "centro" del trazo debe estar a una distancia fija del borde
                // Usamos el maxPossibleStroke para que incluso el arco más grande no se salga del Canvas
                val offsetFixed = maxPossibleStroke / 2
                val sizeFixed = Size(
                    width = size.width - maxPossibleStroke,
                    height = size.height - maxPossibleStroke
                )

                data.items.forEachIndexed { ind, item ->
                    val sweepAngle = data.findSweepAngle(ind, gapPercentage)
                    val currentStrokeWidth = animValues[ind].value.toPx()

                    anglesList.add(DrawingAngles(lastAngle, sweepAngle))

                    drawArc(
                        color = item.color,
                        startAngle = lastAngle,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        // 3. Usamos el offset y tamaño FIJOS
                        // Esto hace que el "esqueleto" del círculo no se mueva
                        topLeft = Offset(offsetFixed, offsetFixed),
                        size = sizeFixed,
                        // 4. El grosor dinámico hará que crezca hacia ambos lados del esqueleto
                        style = Stroke(width = currentStrokeWidth, cap = StrokeCap.Butt)
                    )

                    lastAngle += sweepAngle + gapAngle
                }
            }
        )
    }
}