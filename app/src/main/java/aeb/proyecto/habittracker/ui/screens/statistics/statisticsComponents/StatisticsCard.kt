package aeb.proyecto.habittracker.ui.screens.statistics.statisticsComponents

import aeb.proyecto.habittracker.ui.components.text.TextNumberStatistics
import aeb.proyecto.habittracker.ui.components.text.TitleSmallText
import aeb.proyecto.habittracker.utils.Dimmens.spacing12
import aeb.proyecto.habittracker.utils.Dimmens.spacing4
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun StatisticsCard(
    modifier: Modifier = Modifier,
    number:Int = 0,
    title:Int
){
    val numberVar = remember { number.toString() }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = spacing8, vertical = spacing4)
            .border(0.1.dp, MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(spacing12)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Column (
            modifier = Modifier.padding(spacing8),
            horizontalAlignment = Alignment.Start,
        ){
            TextNumberStatistics(numberVar)
            TitleSmallText(stringResource(title))
        }
    }

}