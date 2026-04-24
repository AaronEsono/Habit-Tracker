package aeb.proyecto.statistics.components.common.donutChart

import aeb.proyecto.ui.dimmens.Dimmens.spacing2
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.LabelMediumText
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp

@Composable
fun PieChartLabel(
    containerHeight: Dp,
    pieChartData: PieChartData
){
    // Definimos proporciones:
    // Título: 8% del alto del contenedor | Subtítulo: 6% | Caja: 12%
    val titleSize = with(LocalDensity.current) { (containerHeight.toPx() * 0.08f).toSp() }
    val subtitleSize = with(LocalDensity.current) { (containerHeight.toPx() * 0.06f).toSp() }
    val boxSize = containerHeight * 0.16f


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = spacing2),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(boxSize)
                .background(
                    color = Color(pieChartData.color),
                    shape = RoundedCornerShape(containerHeight * 0.02f) // Bordes también relativos
                )
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = spacing8)
        ) {
            // Título Dinámico
            LabelLargeText(
                text = pieChartData.title,
                fontSize = titleSize,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Subtítulo Dinámico
            LabelMediumText(
                text = pieChartData.value.toString(),
                fontSize = subtitleSize,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}