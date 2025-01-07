package aeb.proyecto.habittracker.ui.components.cardGoogle

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.ui.components.text.LabelMediumText
import aeb.proyecto.habittracker.utils.Dimmens.spacing12
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp

@Composable
fun GoogleCard(
    modifierCard: Modifier = Modifier,
    modifierRow: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(spacing8),
    defaultElevation: Dp = spacing8,
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    onClick: () -> Unit = {}
) {

    Card(
        shape = shape,
        modifier = modifierCard
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(
            defaultElevation = defaultElevation
        ),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        )
    ) {
        Row(
            modifier = modifierRow
                .fillMaxWidth()
                .padding(spacing8),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(painterResource(R.drawable.ic_google), contentDescription = "Google")

            LabelMediumText(
                stringResource(id = R.string.import_habit_screen_google),
                modifier = Modifier.padding(start = spacing12)
            )
        }
    }
}