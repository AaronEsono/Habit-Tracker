package aeb.proyecto.habittracker.ui.components.bottomSheets

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.ui.components.buttons.CustomFilledButton
import aeb.proyecto.habittracker.ui.components.buttons.CustomOutlinedButtonButton
import aeb.proyecto.habittracker.ui.components.card.CardPickUnitAddHabit
import aeb.proyecto.habittracker.ui.components.items.ColorItem
import aeb.proyecto.habittracker.ui.components.items.IconItem
import aeb.proyecto.habittracker.ui.components.text.LabelMediumText
import aeb.proyecto.habittracker.ui.components.text.TitleLargeText
import aeb.proyecto.habittracker.ui.components.text.TitleMediumText
import aeb.proyecto.habittracker.ui.components.text.TitleSmallText
import aeb.proyecto.habittracker.ui.theme.secondaryColorApp
import aeb.proyecto.habittracker.utils.Constans
import aeb.proyecto.habittracker.utils.Constans.ListColors
import aeb.proyecto.habittracker.utils.Constans.ListIcons
import aeb.proyecto.habittracker.utils.Dimmens.spacing12
import aeb.proyecto.habittracker.utils.Dimmens.spacing16
import aeb.proyecto.habittracker.utils.Dimmens.spacing2
import aeb.proyecto.habittracker.utils.Dimmens.spacing4
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun BottomSheetPickUnit(
    showBottomSheet: MutableState<Boolean>,
    color: MutableState<Color>,
    units: MutableState<Constans.Units>
) {

    val sheetState = rememberModalBottomSheetState()
    val selected = remember { mutableStateOf(units.value) }

    ModalBottomSheet(
        onDismissRequest = {
            showBottomSheet.value = false
        },
        sheetState = sheetState,
        containerColor = secondaryColorApp
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = spacing16, vertical = spacing8),
            horizontalAlignment = Alignment.CenterHorizontally) {

            TitleLargeText(text = stringResource(R.string.add_habit_bottom_sheet_add_habit_title), textAlign = TextAlign.Center
            ,modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.padding(vertical = spacing8))

            LabelMediumText(
                text = stringResource(R.string.add_habit_bottom_sheet_add_habit_subtitle),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Left
            )

            Spacer(modifier = Modifier.padding(vertical = spacing4))

            FlowRow (modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = spacing8),
                verticalArrangement = Arrangement.spacedBy(spacing8, Alignment.CenterVertically),
                horizontalArrangement = Arrangement.spacedBy(spacing8)){
                Constans.Units.entries.forEach {
                    CardPickUnitAddHabit(selected = it == selected.value, unit = it, color = color, onClick = {
                        selected.value = it
                    })
                }
            }

            Spacer(modifier = Modifier.padding(vertical = spacing4))

            Row(modifier = Modifier.fillMaxWidth()) {

                CustomOutlinedButtonButton(
                    title = R.string.buttons_cancel, icon = R.drawable.ic_cancel,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        showBottomSheet.value = false
                    }
                )

                Spacer(modifier = Modifier.padding(horizontal = spacing8))

                CustomFilledButton(
                    title = R.string.buttons_accept,
                    icon = R.drawable.ic_check,
                    color = color.value,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        units.value = selected.value
                        showBottomSheet.value = false
                    }
                )

            }

        }

    }
}