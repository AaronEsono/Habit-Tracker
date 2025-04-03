package aeb.proyecto.save

import aeb.proyecto.save.components.bottomSheet.SaveBottomSheet
import aeb.proyecto.save.components.button.SaveButton
import aeb.proyecto.save.components.card.CardSave
import aeb.proyecto.save.components.loading.SaveScreenLoading
import aeb.proyecto.save.model.DataBottomSheet
import aeb.proyecto.save.model.DataSaveScreen
import aeb.proyecto.ui.navigationIcon.NavigationIcon
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.BodyMediumText
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.text.TitleLargeText
import aeb.proyecto.ui.topbar.providers.ProvideAppBarNavigationIcon
import aeb.proyecto.ui.topbar.providers.ProvideAppBarTitle
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

// Testing

@Composable
fun SaveScreen(
    onImportScreen: () -> Unit,
    viewModel: SaveViewModel = hiltViewModel()
){
    val bottomSheetState = viewModel.bottomSheetState.collectAsStateWithLifecycle().value
    val saveUIState = viewModel.saveUIState.collectAsStateWithLifecycle().value
    val dataSaveScreen = viewModel.dataSaveScreen.collectAsStateWithLifecycle().value

    ProvideAppBarTitle {
        LabelLargeText(stringResource(R.string.save_topbar_title),fontSize = 20.sp)
    }

    ProvideAppBarNavigationIcon {
        NavigationIcon()
    }

    LaunchedEffect (Unit){
        viewModel.getDataUser()
    }

    SaveScreen(
        saveUIState = saveUIState,
        dataSaveScreen = dataSaveScreen,
        onImportScreen = onImportScreen,
        onSaveClick = { viewModel.setBottomSheetState(DataBottomSheet.SAVE_HABIT) },
        onRestoreClick = { viewModel.setBottomSheetState(DataBottomSheet.RESTORE_HABIT) },
        onDeleteClick = { viewModel.setBottomSheetState(DataBottomSheet.DELETE_HABIT) },
        onLogOutClick = { viewModel.setBottomSheetState(DataBottomSheet.LOG_OUT) }
    )

    if(bottomSheetState.showBottomSheet){
        SaveBottomSheet(
            dataBottomSheet = bottomSheetState.dataBottomSheet,
            onDismiss = { viewModel.closeBottomSheet() },
            onAccept =  { viewModel.requestAcceptBottomSheet() },
        )
    }
}

@Composable
internal fun SaveScreen(
    saveUIState: SaveUIState,
    dataSaveScreen: DataSaveScreen,
    onImportScreen: () -> Unit = {},
    onSaveClick: () -> Unit = {},
    onRestoreClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onLogOutClick: () -> Unit = {}
){

    when (saveUIState) {
        SaveUIState.Success, SaveUIState.Error -> Unit
        SaveUIState.LogOut -> {
            LaunchedEffect(Unit) {
                onImportScreen()
            }
        }
        SaveUIState.Loading -> {
            SaveScreenLoading()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = spacing16, horizontal = spacing12),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AnimatedContent(
            targetState = dataSaveScreen.name
        ) { targetState ->
            when (targetState) {
                null,"" -> Unit
                else -> {
                    TitleLargeText(stringResource(R.string.save_email, targetState))
                }
            }
        }

        CustomSpacerSave(spacing6)

        BodyMediumText(stringResource(R.string.save_label), textAlign = TextAlign.Center)

        CustomSpacerSave()

        CardSave(dataSaveScreen.localDateTime)

        CustomSpacerSave()

        SaveButton(title = stringResource(R.string.save_save_habit), onClick = onSaveClick)

        CustomSpacerSave(spacing6)

        AnimatedContent(
            targetState = dataSaveScreen.localDateTime
        ) { targetState ->
            when(targetState){
                null -> Unit
                else -> {
                    SaveButton(title = stringResource(R.string.save_restore_habit), onClick = onRestoreClick)
                }
            }
        }

        CustomSpacerSave(spacing6)

        AnimatedContent(
            targetState = dataSaveScreen.localDateTime
        ) { targetState ->
            when(targetState){
                null -> Unit
                else -> {
                    SaveButton(title = stringResource(R.string.save_delete_habit), onClick = onDeleteClick)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        SaveButton(title = stringResource(R.string.save_log_out), onClick = onLogOutClick)
    }

}

@Composable
fun CustomSpacerSave(vertical: Dp = spacing8){
    Spacer(modifier = Modifier.padding(vertical = vertical))
}