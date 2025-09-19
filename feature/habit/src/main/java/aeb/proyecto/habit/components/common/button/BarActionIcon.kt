package aeb.proyecto.habit.components.common.button

import aeb.proyecto.habit.R
import aeb.proyecto.habit.model.BottomSheetType
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.topbar.providers.ProvideAppBarActions
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun BarActionIcon(
    onBottomSheetSelected: (BottomSheetType) -> Unit = {},
){

    ProvideAppBarActions {
        Icon(
            painter = painterResource(R.drawable.ic_find_date),
            contentDescription = "calendar icon",
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(end = spacing6)
                .size(25.dp)
                .clickable {onBottomSheetSelected(BottomSheetType.SELECT_DATE)}
        )
    }

}

