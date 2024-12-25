package aeb.proyecto.habittracker.ui.components.items

import aeb.proyecto.habittracker.ui.theme.secondaryColorApp
import aeb.proyecto.habittracker.utils.Dimmens.spacing16
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ColorItem(
    color: Color,
    selected:Boolean = false,
    onClick: (Color) -> Unit = {}
) {

    var border = Modifier.border(
        2.dp,
        Color.White,
        RoundedCornerShape(spacing16)
    )

    if(!selected)
        border = Modifier

    Card(
        modifier = Modifier
            .size(30.dp)
            .clickable { onClick(color) },
        shape = RoundedCornerShape(spacing16),
        colors = CardDefaults.cardColors(
            containerColor = secondaryColorApp
        )
    ) {
        Row(modifier = border
            .background(color)
            .size(30.dp)) { }
    }

}

@Preview
@Composable
fun ColorItemPreview() {
    ColorItem(Color.Red)
}