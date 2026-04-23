package aeb.proyecto.statistics.components.common.donutChart

import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val prueba = mapOf<String,Int>(
    "Lunes" to 10,
    "Martes" to 20,
    "Miercoles" to 30,
    "Jueves" to 40,
    "Viernes" to 50
)

// Ahora mismo no utilizamos un height, por la tanto, el donut y los textos son muy grandes, hay que tratar ese 0.35 directamente
// al donut, al igual que los textos

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun PieChart(
    modifier: Modifier = Modifier,
    data: Map<String,Int> = prueba,
    chartHeight: Dp = 200.dp
){

    val total = data.values.sum()
    val floatValues = remember(data) { data.values.map { 360f * it / total } }
    val colors = listOf(Color.Red, Color.Cyan, Color.Blue, Color.Green, Color.Yellow)

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(chartHeight)
            .padding(horizontal = spacing6)
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(spacing6))
            .background(MaterialTheme.colorScheme.surfaceTint)
    ) {
        // Guardamos las dimensiones del contenedor
        val scopeMaxHeight = maxHeight
        val scopeMaxWidth = maxWidth

        // Usamos el ancho como referencia para el escalado de fuentes y tamaños
        // Limitamos la referencia para que en tablets no se vea desproporcionado
        val referenceSize = scopeMaxWidth.coerceAtMost(400.dp)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing16),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing16)
        ) {
            val canvasSize = (scopeMaxHeight * 0.45f)
            // 1. EL GRÁFICO (Arriba)
            // Usamos un tamaño relativo al ancho disponible
            ChartCanvas(
                floatValues = floatValues,
                colors = colors,
                modifier = Modifier
                    .size(canvasSize) // Ocupa el 60% del ancho del Box
                    .aspectRatio(1f)     // Mantiene la forma circular
            )

            Spacer(modifier = Modifier.height(spacing8))

            // 2. LAS LEYENDAS (Abajo)
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(spacing4)
            ) {
                data.keys.forEachIndexed { index, title ->
                    PieChartLabel(
                        containerHeight = scopeMaxHeight
                    )
                }
            }
        }
    }
}

// Extraemos el Canvas a una función para no repetir código
@Composable
fun ChartCanvas(floatValues: List<Float>, colors: List<Color>, modifier: Modifier) {
    Canvas(modifier = modifier) {
        val dynamicStrokeWidth = size.width * 0.18f // Un poco más grueso para que se vea mejor
        var lastValue = 0f
        val arcSize = Size(size.width - dynamicStrokeWidth, size.height - dynamicStrokeWidth)
        val offset = Offset(dynamicStrokeWidth / 2, dynamicStrokeWidth / 2)

        floatValues.forEachIndexed { index, sweepAngle ->
            drawArc(
                color = colors.getOrElse(index) { Color.Gray },
                startAngle = lastValue,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = offset,
                size = arcSize,
                style = Stroke(dynamicStrokeWidth, cap = StrokeCap.Butt)
            )
            lastValue += sweepAngle
        }
    }
}