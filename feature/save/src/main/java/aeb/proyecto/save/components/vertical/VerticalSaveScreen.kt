package aeb.proyecto.save.components.vertical

import aeb.proyecto.save.R
import aeb.proyecto.save.SaveUIState
import aeb.proyecto.save.components.common.button.SaveButton
import aeb.proyecto.save.components.common.card.CardSave
import aeb.proyecto.save.components.common.loading.SaveScreenLoading
import aeb.proyecto.save.components.common.spacer.CustomSpacerSave
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
import androidx.compose.foundation.layout.navigationBars
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign

/**
 * Portrait-optimized layout for the Save/Sync screen.
 *
 * @param saveUIState The current operational state (Loading, Success, Error, LogOut).
 * @param dataSaveScreen The data model containing user profile and sync metadata.
 * @param bottomSheetState The state managing the visibility and content of the bottom sheet.
 * @param onImportScreen Callback triggered to navigate to the import/export screen.
 * @param onSaveClick Callback triggered to initiate the cloud save operation.
 * @param onRestoreClick Callback triggered to initiate the cloud restore operation.
 * @param onDeleteClick Callback triggered to initiate the cloud data deletion.
 * @param onLogOutClick Callback triggered to initiate the session logout.
 * @param onDismiss Callback triggered when the bottom sheet is dismissed.
 * @param onAccept Callback triggered when the primary action in the bottom sheet is confirmed.
 */
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
            .windowInsetsPadding(WindowInsets.navigationBars)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AnimatedContent(
            targetState = dataSaveScreen.name
        ) { targetState ->
            when (targetState) {
                null,"" -> Unit
                else -> {
                    TitleLargeText(
                        stringResource(R.string.save_email, targetState),
                        modifier = Modifier.testTag("save_title_user")
                    )
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
                    SaveButton(
                        title = stringResource(R.string.save_restore_habit), onClick = onRestoreClick,
                        modifier = Modifier.testTag("save_button_restore")
                    )
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
                    SaveButton(
                        title = stringResource(R.string.save_delete_habit), onClick = onDeleteClick,
                        modifier = Modifier.testTag("save_button_delete")
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        SaveButton(
            title = stringResource(R.string.save_log_out), onClick = onLogOutClick,
            modifier = Modifier.windowInsetsPadding(
                WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)
            ).testTag("save_button_logOut")
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