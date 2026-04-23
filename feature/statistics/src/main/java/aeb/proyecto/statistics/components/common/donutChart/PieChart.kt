package aeb.proyecto.statistics.components.common.donutChart

import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelMediumText
import aeb.proyecto.ui.text.LabelSmallText
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp

val prueba = mapOf<String,Int>(
    "Lunes" to 10,
    "Martes" to 20,
    "Miercoles" to 30,
    "Jueves" to 40,
    "Viernes" to 50
)

@Composable
fun PieChart(
    modifier: Modifier = Modifier,
    data: Map<String,Int> = prueba,
){

    val total = data.values.sum()

    // Usamos remember para no recalcular esto en cada recomposición
    val floatValues = remember(data) {
        data.values.map { 360f * it / total }
    }

    val colors = listOf(Color.Red, Color.Cyan, Color.Blue, Color.Green, Color.Yellow)

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

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(spacing8),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // LADO IZQUIERDO: Gráfico (50%)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize(0.85f) // Ocupa el 80% del espacio asignado para dejar margen
                        .aspectRatio(1f)   // Obliga a que sea un círculo perfecto
                ) {
                    val dynamicStrokeWidth = size.width * 0.15f
                    var lastValue = 0f

                    // Ajuste para que el trazo no se corte en los bordes
                    val arcSize = Size(
                        width = size.width - dynamicStrokeWidth,
                        height = size.height - dynamicStrokeWidth
                    )
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

            // LADO DERECHO: Textos (50%)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(start = spacing8),
                verticalArrangement = Arrangement.Center
            ) {
                PieChardLabel()
                PieChardLabel()
                PieChardLabel()
            }
        }
    }
}

@Composable
fun PieChardLabel(){

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = spacing4)
                .size(20.dp)
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(spacing6))
                .background(Color.Cyan)

        ){}

        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = spacing12)
        ) {
            LabelMediumText("Holaaaaaa")

            LabelSmallText(
                text = "Adios",
                color = MaterialTheme.colorScheme.outline
            )
        }
    }

}

