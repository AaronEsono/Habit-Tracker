package aeb.proyecto.save.components.vertical

import aeb.proyecto.save.CustomSpacerSave
import aeb.proyecto.save.R
import aeb.proyecto.save.SaveUIState
import aeb.proyecto.save.components.commom.button.SaveButton
import aeb.proyecto.save.components.commom.card.CardSave
import aeb.proyecto.save.components.commom.loading.SaveScreenLoading
import aeb.proyecto.save.components.vertical.components.bottomSheet.VerticalSaveBottomSheet
import aeb.proyecto.save.model.BottomSheetState
import aeb.proyecto.save.model.DataSaveScreen
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.text.BodyMediumText
import aeb.proyecto.ui.text.TitleLargeText
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign

@Composable
fun VerticalSaveScreen(
    saveUIState: SaveUIState,
    dataSaveScreen: DataSaveScreen,
    bottomSheetState: BottomSheetState,
    onImportScreen: () -> Unit = {},
    onSaveClick: () -> Unit = {},
    onRestoreClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onLogOutClick: () -> Unit = {},
    onDismiss: () -> Unit = {},
    onAccept: () -> Unit = {}
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
            .padding(vertical = spacing16, horizontal = spacing12)
            .verticalScroll(rememberScrollState()),
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

        SaveButton(
            title = stringResource(R.string.save_log_out), onClick = onLogOutClick,
            modifier = Modifier.windowInsetsPadding(
                WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)
            )
        )
    }

    if(bottomSheetState.showBottomSheet){
        VerticalSaveBottomSheet(
            dataBottomSheet = bottomSheetState.dataBottomSheet,
            onDismiss = onDismiss,
            onAccept =  onAccept,
        )
    }
}