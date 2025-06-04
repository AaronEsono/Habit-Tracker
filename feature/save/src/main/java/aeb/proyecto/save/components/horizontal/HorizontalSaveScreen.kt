package aeb.proyecto.save.components.horizontal

import aeb.proyecto.save.CustomSpacerSave
import aeb.proyecto.save.R
import aeb.proyecto.save.SaveUIState
import aeb.proyecto.save.components.commom.button.SaveButton
import aeb.proyecto.save.components.commom.card.CardSave
import aeb.proyecto.save.components.commom.loading.SaveScreenLoading
import aeb.proyecto.save.components.horizontal.components.bottomSheet.HorizontalSaveBottomSheet
import aeb.proyecto.save.model.BottomSheetState
import aeb.proyecto.save.model.DataSaveScreen
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing16
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.text.BodyMediumText
import aeb.proyecto.ui.text.TitleLargeText
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign

@Composable
fun HorizontalSaveScreen(
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

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = spacing16, horizontal = spacing12),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column (
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
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

            CustomSpacerSave(spacing4)

            BodyMediumText(stringResource(R.string.save_label), textAlign = TextAlign.Center)

            CustomSpacerSave(spacing8)

            CardSave(dataSaveScreen.localDateTime)
        }

        Spacer(modifier = Modifier.padding(horizontal = spacing6))

        Column (
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            SaveButton(title = stringResource(R.string.save_save_habit), onClick = onSaveClick)

            CustomSpacerSave(spacing4)

            AnimatedContent(
                targetState = dataSaveScreen.localDateTime
            ) { targetState ->
                when(targetState){
                    null -> Unit
                    else -> {
                        Column {
                            SaveButton(title = stringResource(R.string.save_restore_habit), onClick = onRestoreClick)

                            CustomSpacerSave(spacing4)
                        }
                    }
                }
            }

            AnimatedContent(
                targetState = dataSaveScreen.localDateTime
            ) { targetState ->
                when(targetState){
                    null -> Unit
                    else -> {
                        Column {
                            SaveButton(title = stringResource(R.string.save_delete_habit), onClick = onDeleteClick)

                            CustomSpacerSave(spacing4)
                        }
                    }
                }
            }

            SaveButton(title = stringResource(R.string.save_log_out), onClick = onLogOutClick)
        }
    }

    if(bottomSheetState.showBottomSheet){
        HorizontalSaveBottomSheet(
            dataBottomSheet = bottomSheetState.dataBottomSheet,
            onDismiss = onDismiss,
            onAccept =  onAccept,
        )
    }
}