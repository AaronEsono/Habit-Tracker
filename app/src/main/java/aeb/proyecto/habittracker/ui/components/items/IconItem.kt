package aeb.proyecto.habittracker.ui.components.items


import aeb.proyecto.habittracker.utils.ColorsTheme
import aeb.proyecto.habittracker.utils.Dimmens.spacing16
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun IconItem(
    imageVector: ImageVector,
    selected: Boolean = false,
    color: Color = Color.White,
    onClick: (ImageVector) -> Unit = {}
) {

    Card(
        modifier = Modifier
            .size(30.dp)
            .clickable { onClick(imageVector) },
        shape = RoundedCornerShape(spacing16),
        colors = CardDefaults.cardColors(
            containerColor = ColorsTheme.secondaryColorApp
        )
    ) {
        Row(
            modifier = Modifier
                .wrapContentSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = "",
                modifier = Modifier.size(30.dp),
                tint = if(selected) color else Color.White
            )
        }
    }
}

@Preview
@Composable
fun IconItemPreview() {
    IconItem(imageVector = Icons.Filled.AddBox)
}