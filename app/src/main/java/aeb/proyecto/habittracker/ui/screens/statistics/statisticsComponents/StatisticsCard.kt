package aeb.proyecto.habittracker.ui.screens.statistics.statisticsComponents

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.ui.components.text.TextNumberStatistics
import aeb.proyecto.habittracker.ui.components.text.TitleMediumText
import aeb.proyecto.habittracker.ui.components.text.TitleSmallText
import aeb.proyecto.habittracker.ui.theme.borderTextField
import aeb.proyecto.habittracker.ui.theme.containerTextFieldColor
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
import androidx.compose.runtime.Composable
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

    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = spacing8, vertical = spacing4)
            .border(0.1.dp, borderTextField, RoundedCornerShape(spacing12)),
        colors = CardDefaults.cardColors(
            containerColor = containerTextFieldColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Column (
            modifier = Modifier.padding(spacing8),
            horizontalAlignment = Alignment.Start,
        ){
            TextNumberStatistics(number.toString())
            TitleSmallText(stringResource(title))
        }
    }

}