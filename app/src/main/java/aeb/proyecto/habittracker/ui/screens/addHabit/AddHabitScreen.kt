package aeb.proyecto.habittracker.ui.screens.addHabit

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.ui.components.bottomSheets.BottomSheetPickUnit
import aeb.proyecto.habittracker.ui.components.card.CardInfoAddHabit
import aeb.proyecto.habittracker.ui.components.card.CardPickColorAddHabit
import aeb.proyecto.habittracker.ui.components.items.ColorItem
import aeb.proyecto.habittracker.ui.components.items.IconItem
import aeb.proyecto.habittracker.ui.components.text.BodySmallText
import aeb.proyecto.habittracker.ui.components.text.TitleMediumText
import aeb.proyecto.habittracker.ui.components.text.TitleSmallText
import aeb.proyecto.habittracker.ui.components.textField.CustomOutlinedTextField
import aeb.proyecto.habittracker.ui.components.timePicker.DialExample
import aeb.proyecto.habittracker.utils.Constans
import aeb.proyecto.habittracker.utils.Constans.ListColors
import aeb.proyecto.habittracker.utils.Constans.ListIcons
import aeb.proyecto.habittracker.utils.Dimmens.spacing12
import aeb.proyecto.habittracker.utils.Dimmens.spacing16
import aeb.proyecto.habittracker.utils.Dimmens.spacing2
import aeb.proyecto.habittracker.utils.Dimmens.spacing4
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndSelectAll
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.unit.dp
import kotlin.reflect.KProperty

@Composable
fun AddHabitScreen() {
    val nameHabit = rememberTextFieldState("")
    val descriptionHabit = rememberTextFieldState("")
    val timesHabit = rememberTextFieldState("")

    val color = remember { mutableStateOf(ListColors[0]) }
    val icon = remember { mutableStateOf(ListIcons[0]) }

    val colorSelected = remember { mutableStateOf(false) }
    val iconSelected = remember { mutableStateOf(false) }

    val showBottomSheet = remember { mutableStateOf(false) }

    val unitPicked = remember { mutableStateOf(Constans.Units.TIMES) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacing16)
            .verticalScroll(rememberScrollState())
    ) {

        CustomOutlinedTextField(
            rememberTextFieldState = nameHabit,
            label = R.string.add_habit_screen_name,
            isNeeded = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(vertical = spacing8))

        CustomOutlinedTextField(
            rememberTextFieldState = descriptionHabit,
            label = R.string.add_habit_screen_description,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(vertical = spacing12))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            CardPickColorAddHabit(
                text = R.string.add_habit_screen_color,
                color = color.value,
                icon = Icons.Filled.ColorLens,
                modifier = Modifier.weight(1f)
            ) {
                colorSelected.value = !colorSelected.value
                iconSelected.value = false
            }


            CardPickColorAddHabit(
                text = R.string.add_habit_screen_icon,
                color = color.value,
                icon = icon.value,
                modifier = Modifier.weight(1f)
            ) {
                iconSelected.value = !iconSelected.value
                colorSelected.value = false
            }
        }

        Spacer(modifier = Modifier.padding(vertical = spacing12))

        AnimatedVisibility(
            visible = colorSelected.value,
        ) {
            GridOptions(color, colorSelected, true, icon, iconSelected)
        }

        AnimatedVisibility(
            visible = iconSelected.value,
        ) {
            GridOptions(color, colorSelected, false, icon, iconSelected)
        }

        Spacer(modifier = Modifier.padding(vertical = spacing2))

        BodySmallText(stringResource(unitPicked.value.question))

        Spacer(modifier = Modifier.padding(vertical = spacing4))

        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.fillMaxWidth(0.5f)) {
                CustomOutlinedTextField(
                    rememberTextFieldState = timesHabit,
                    label = R.string.add_habit_screen_name,
                    labelError = R.string.add_habit_screen_no_units,
                    isNeeded = true,
                    showLabel = false,
                    isNumeric = true,
                )
            }

            Spacer(modifier = Modifier.padding(horizontal = spacing8))

            CardInfoAddHabit(
                title = if (timesHabit.text.toString() == "1" || timesHabit.text.toString() == "") unitPicked.value.title else unitPicked.value.pluralTitle,
                finalIcon = Icons.Filled.KeyboardArrowDown,
                color = color,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = spacing8),
                onClick = {showBottomSheet.value = true}
            )
        }

        Spacer(modifier = Modifier.padding(vertical = spacing12))

        BodySmallText(stringResource(R.string.add_habit_pick_date))

        CardInfoAddHabit(
            title = R.string.add_habit_add_hour,
            icon = Icons.Filled.DateRange,
            finalIcon = Icons.Filled.KeyboardArrowRight,
            color = color,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = spacing8)
        )

    }


    if(showBottomSheet.value){
        BottomSheetPickUnit(showBottomSheet,color = color,unitPicked)
    }

}

@Composable
fun GridOptions(
    color: MutableState<Color>,
    colorSelected: MutableState<Boolean>,
    isColors: Boolean,
    imageVector: MutableState<ImageVector>,
    iconSelected: MutableState<Boolean>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 120.dp)
    ) {
        Card(
            modifier = Modifier
                .wrapContentSize()
                .padding(bottom = spacing12), elevation = CardDefaults.cardElevation(
                defaultElevation = spacing8
            )
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(30.dp),
                modifier = Modifier
                    .wrapContentSize()
                    .padding(vertical = spacing4),
                contentPadding = PaddingValues(8.dp), // Espaciado externo
                horizontalArrangement = Arrangement.spacedBy(8.dp), // Espaciado horizontal entre ítems
                verticalArrangement = Arrangement.spacedBy(8.dp) // Espaciado vertical entre filas
            ) {
                if (isColors) {
                    items(ListColors.size) { item ->
                        ColorItem(
                            color = ListColors[item],
                            selected = color.value == ListColors[item]
                        ) {
                            color.value = it
                            colorSelected.value = false
                        }
                    }
                } else {
                    items(ListIcons.size) { item ->
                        IconItem(
                            imageVector = ListIcons[item],
                            selected = imageVector.value == ListIcons[item],
                            color = color.value
                        ) {
                            imageVector.value = it
                            iconSelected.value = false
                        }
                    }
                }
            }
        }
    }
}