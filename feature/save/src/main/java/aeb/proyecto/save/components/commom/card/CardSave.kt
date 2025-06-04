package aeb.proyecto.save.components.commom.card

import aeb.proyecto.save.R
import aeb.proyecto.ui.dimmens.Dimmens.spacing1
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.month.getMonth
import aeb.proyecto.ui.text.BodyMediumText
import aeb.proyecto.ui.text.BodySmallText
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime

@Composable
fun CardSave(
    localDateTime: LocalDateTime? = LocalDateTime.now()
) {

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        )
    ) {
        Row(
            modifier = Modifier
                .wrapContentSize()
                .padding(vertical = spacing6, horizontal = spacing8),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                painter = painterResource(R.drawable.ic_file),
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = "file image",
                modifier = Modifier.size(40.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = spacing8)
            ) {
                when (localDateTime) {
                    null -> {
                        BodyMediumText(stringResource(R.string.save_no_data))
                    }

                    else -> {
                        BodyMediumText(
                            stringResource(R.string.save_data),
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.padding(vertical = spacing1))

                        BodySmallText(getDate(localDateTime))
                        BodySmallText(getTime(localDateTime))
                    }
                }
            }

        }
    }
}

@Composable
fun getDate(localDateTime: LocalDateTime): String {
    val year = localDateTime.year
    val month = stringResource(getMonth(localDateTime.monthValue))
    val day = localDateTime.dayOfMonth

    return stringResource(R.string.save_date_data, day, month, year)
}

@Composable
fun getTime(localDateTime: LocalDateTime): String {
    val hour = localDateTime.hour.toString()
    var minute = localDateTime.minute.toString()
    minute = if (minute.length == 1) "0$minute" else minute

    return stringResource(R.string.save_time_data, hour, minute)
}