package aeb.proyecto.habit.components.common.bottomSheet.editHabit.buttons

import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun ButtonsRow() {

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(spacing8))
                .background(MaterialTheme.colorScheme.background)
                .size(35.dp)
        ) {
            Icon(
                Icons.Filled.Edit,
                contentDescription = "icon edit button",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .fillMaxSize(0.65f)
                    .align(Alignment.Center)
            )
        }

        //Spacer(modifier = Modifier.weight(1f))

        Spacer(modifier = Modifier.padding(horizontal = spacing6))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(spacing8))
                .background(MaterialTheme.colorScheme.background)
                .size(35.dp)
        ) {
            Icon(
                Icons.Filled.Delete,
                contentDescription = "icon edit button",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .fillMaxSize(0.65f)
                    .align(Alignment.Center)
            )
        }
    }
}