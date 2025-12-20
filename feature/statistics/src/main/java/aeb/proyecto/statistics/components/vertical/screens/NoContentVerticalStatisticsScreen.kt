package aeb.proyecto.statistics.components.vertical.screens

import aeb.proyecto.statistics.R
import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelLargeText
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun NoContentVerticalStatisticsScreen(){

    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Icon(
            painter = painterResource(R.drawable.ic_no_statistics),
            contentDescription = "no content statistics",
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.padding(vertical = spacing8))

        LabelLargeText(
            stringResource(R.string.statistics_no_content),
            modifier = Modifier.padding(horizontal = spacing16)
        )
    }

}