package aeb.proyecto.addhabit

import aeb.proyecto.addhabit.components.card.AddHabitCard
import aeb.proyecto.addhabit.components.card.AddHabitCardButton
import aeb.proyecto.addhabit.components.dialog.PickTypeHabitDialog
import aeb.proyecto.addhabit.components.grid.AddHabitGrid
import aeb.proyecto.addhabit.components.textField.AddHabitTextField
import aeb.proyecto.addhabit.constants.GridOption
import aeb.proyecto.addhabit.constants.GridOptionResult
import aeb.proyecto.addhabit.constants.PICK_TYPE_HABIT
import aeb.proyecto.addhabit.constants.TypeHabit
import aeb.proyecto.addhabit.model.DataAddHabit
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.LabelLargeText
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.EventNote
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Description
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun AddHabitScreen(
    habitIt:Long?,
    navigateToHabit: () -> Unit,
    viewModel: AddHabitViewModel = hiltViewModel()
){

    val dataAddHabit = viewModel.dataAddHabit.collectAsStateWithLifecycle().value

    AddHabitScreen(
        dataAddHabit = dataAddHabit,
        onClickCard = viewModel::onClickCard,
        onClickGridOption = viewModel::onClickGridOption,
        onClickDialog = viewModel::setDialog,
        onDismissDialog = viewModel::closeDialog,
        onClickTypeHabit = viewModel::onClickTypeHabit
    )

}

@Composable
internal fun AddHabitScreen(
    dataAddHabit: DataAddHabit,
    onClickCard: (GridOption) -> Unit,
    onClickGridOption: (GridOptionResult) -> Unit = {},
    onClickDialog: (Int) -> Unit = {},
    onDismissDialog: () -> Unit = {},
    onClickTypeHabit: (TypeHabit) -> Unit = {}
){

    val focusManager = LocalFocusManager.current

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(start = spacing12, end = spacing12, top = spacing12)
    ){
        AddHabitTextField(
            textFieldState = dataAddHabit.nameTextField,
            label = stringResource(R.string.add_habit_name_label),
            leadingIcon = Icons.AutoMirrored.Filled.EventNote,
            focusManager = focusManager,
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text
        )

        Spacer(modifier = Modifier.padding(vertical = spacing4))

        AddHabitTextField(
            textFieldState = dataAddHabit.descriptionTextField,
            label = stringResource(R.string.add_habit_description_label),
            leadingIcon = Icons.Filled.Description,
            focusManager = focusManager,
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text
        )

        Spacer(modifier = Modifier.padding(vertical = spacing8))

        LabelLargeText(stringResource(R.string.add_habit_pick_type_habit_title))

        Spacer(modifier = Modifier.padding(vertical = spacing4))

        AddHabitCardButton(
            title = stringResource(dataAddHabit.typeHabit.title),
            modifier = Modifier.fillMaxWidth(0.5f),
            onClick = { onClickDialog(PICK_TYPE_HABIT) }
        )

        Spacer(modifier = Modifier.padding(vertical = spacing12))

        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ){

            AddHabitCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.add_habit_color_label),
                icon = Icons.Filled.ColorLens,
                color = dataAddHabit.color,
                onClick = { onClickCard(GridOption.COLORS) }
            )

            AddHabitCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.add_habit_icon_label),
                icon = dataAddHabit.icon,
                color = dataAddHabit.color,
                onClick = { onClickCard(GridOption.ICONS) }
            )
        }

        Spacer(modifier = Modifier.padding(vertical = spacing8))

        AnimatedVisibility(
            visible = dataAddHabit.isColorSelected
        ) {
            AddHabitGrid(gridOption = GridOption.COLORS,
                colorSelected = dataAddHabit.color,
                iconSelected = dataAddHabit.icon,
                onClickGridOption = onClickGridOption
            )
        }

        AnimatedVisibility(
            visible = dataAddHabit.isIconSelected
        ) {
            AddHabitGrid(gridOption = GridOption.ICONS,
                colorSelected = dataAddHabit.color,
                iconSelected = dataAddHabit.icon,
                onClickGridOption = onClickGridOption
            )
        }

    }

    if (dataAddHabit.showDialog) {
        when (dataAddHabit.typeDialog) {
            PICK_TYPE_HABIT -> PickTypeHabitDialog(
                onDismissRequest = onDismissDialog,
                onClickButton = onClickTypeHabit )

            else -> {}
        }
    }

}