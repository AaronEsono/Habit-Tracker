package aeb.proyecto.save

import aeb.proyecto.save.components.bottomSheet.SaveBottomSheet
import aeb.proyecto.save.components.button.SaveButton
import aeb.proyecto.save.components.card.CardSave
import aeb.proyecto.save.model.DataBottomSheet
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.BodyMediumText
import aeb.proyecto.ui.text.TitleLargeText
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SaveScreen(
    onImportScreen: () -> Unit,
    viewModel: SaveViewModel = hiltViewModel()
){
    val bottomSheetState = viewModel.bottomSheetState.collectAsStateWithLifecycle().value

    SaveScreen(
        onSaveClick = { viewModel.setBottomSheetState(DataBottomSheet.SAVE_HABIT) },
        onDeleteClick = { viewModel.setBottomSheetState(DataBottomSheet.DELETE_HABIT) },
        onLogOutClick = { viewModel.setBottomSheetState(DataBottomSheet.LOG_OUT) }
    )

    if(bottomSheetState.showBottomSheet){
        SaveBottomSheet(
            dataBottomSheet = bottomSheetState.dataBottomSheet,
            onDismiss = { viewModel.closeBottomSheet() },
            onAccept =  { viewModel.requestAcceptBottomSheet() }
        )
    }
}

@Composable
internal fun SaveScreen(
    onSaveClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onLogOutClick: () -> Unit = {}
){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = spacing16, horizontal = spacing12),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TitleLargeText(stringResource(R.string.save_email, "aayronmaiden"))

        CustomSpacerSave(spacing6)

        BodyMediumText(stringResource(R.string.save_label), textAlign = TextAlign.Center)

        CustomSpacerSave()

        CardSave()

        CustomSpacerSave()

        SaveButton(title = stringResource(R.string.save_save_habit), onClick = onSaveClick)

        CustomSpacerSave(spacing6)

        SaveButton(title = stringResource(R.string.save_delete_habit), onClick = onDeleteClick)

        Spacer(modifier = Modifier.weight(1f))

        SaveButton(title = stringResource(R.string.save_log_out), onClick = onLogOutClick)
    }

}

@Composable
fun CustomSpacerSave(vertical: Dp = spacing8){
    Spacer(modifier = Modifier.padding(vertical = vertical))
}