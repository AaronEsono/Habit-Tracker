package aeb.proyecto.habittracker.ui.components.items

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ColorItem(
    color: Color,
    modifier:Modifier = Modifier,
    sizeCanvas: Dp = 30.dp,
    onClick: (Color) -> Unit = {}
) {

    Canvas(
        modifier = modifier
            .size(sizeCanvas)
            .clickable { onClick(color) }
    ) {
        drawCircle(
            color = color,
            radius = size.minDimension / 2
        )
    }

}

@Preview
@Composable
fun ColorItemPreview() {
    ColorItem(Color.Red)
}