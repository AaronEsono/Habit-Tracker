package aeb.proyecto.habittracker.ui.components.items


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun IconItem(
    imageVector: ImageVector,
    size: Dp = 30.dp,
    modifierBox: Modifier = Modifier,
    modifierIcon: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
    onClick: (ImageVector) -> Unit = {}
) {

    Box(modifier = modifierBox
        .clickable { onClick(imageVector) }) {
        Icon(
            imageVector = imageVector,
            contentDescription = "",
            modifier = modifierIcon.size(size),
            tint = color
        )
    }
}

@Preview
@Composable
fun IconItemPreview() {
    IconItem(imageVector = Icons.Filled.AddBox)
}