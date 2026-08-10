package aeb.proyecto.save

import aeb.proyecto.save.components.horizontal.HorizontalSaveScreen
import aeb.proyecto.save.components.vertical.VerticalSaveScreen
import aeb.proyecto.save.model.DataBottomSheet
import aeb.proyecto.ui.navigationIcon.NavigationIcon
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.orientation.Orientation
import aeb.proyecto.ui.orientation.getOrientation
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.topbar.providers.ProvideAppBarNavigationIcon
import aeb.proyecto.ui.topbar.providers.ProvideAppBarTitle
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

// Testing

/**
 * Main entry point for the Save/Sync screen.
 * Orchestrates the UI layout based on device orientation and delegates
 * business logic to the [SaveViewModel].
 *
 * @param onImportScreen Navigation callback to the Import/Export module.
 * @param viewModel The Hilt-injected ViewModel managing sync state and data.
 */
@Composable
fun SaveScreen(
    onImportScreen: () -> Unit,
    viewModel: SaveViewModel = hiltViewModel()
){

    val orientation = getOrientation()

    // Collect UI state from ViewModel
    val bottomSheetState = viewModel.bottomSheetState.collectAsStateWithLifecycle().value
    val saveUIState = viewModel.saveUIState.collectAsStateWithLifecycle().value
    val dataSaveScreen = viewModel.dataSaveScreen.collectAsStateWithLifecycle().value

    // Setup Top Bar environment
    ProvideAppBarTitle {
        LabelLargeText(stringResource(R.string.save_topbar_title),fontSize = 20.sp)
    }

    ProvideAppBarNavigationIcon {
        NavigationIcon()
    }

    // Initial data fetch
    LaunchedEffect (Unit){
        viewModel.getDataUser()
    }

    // Callback definitions for UI actions
    val onSaveClick = { viewModel.setBottomSheetState(DataBottomSheet.SAVE_HABIT) }
    val onRestoreClick = { viewModel.setBottomSheetState(DataBottomSheet.RESTORE_HABIT) }
    val onDeleteClick = { viewModel.setBottomSheetState(DataBottomSheet.DELETE_HABIT) }
    val onLogOutClick = { viewModel.setBottomSheetState(DataBottomSheet.LOG_OUT) }
    val onDeleteAccountClick = { viewModel.setBottomSheetState(DataBottomSheet.DELETE_ACCOUNT) }

    val onDismiss = { viewModel.closeBottomSheet() }
    val onAccept =  { viewModel.requestAcceptBottomSheet() }

    // Screen Layout Strategy based on Orientation
    when(orientation){
        Orientation.Portrait -> {
            VerticalSaveScreen(
                saveUIState = saveUIState,
                dataSaveScreen = dataSaveScreen,
                bottomSheetState = bottomSheetState,
                onImportScreen = onImportScreen,
                onSaveClick = onSaveClick,
                onRestoreClick = onRestoreClick,
                onDeleteClick = onDeleteClick,
                onLogOutClick = onLogOutClick,
                onDeleteAccountClick = onDeleteAccountClick,
                onDismiss = onDismiss,
                onAccept = onAccept
            )
        }
        Orientation.Landscape -> {
            HorizontalSaveScreen(
                saveUIState = saveUIState,
                dataSaveScreen = dataSaveScreen,
                bottomSheetState = bottomSheetState,
                onImportScreen = onImportScreen,
                onSaveClick = onSaveClick,
                onRestoreClick = onRestoreClick,
                onDeleteClick = onDeleteClick,
                onLogOutClick = onLogOutClick,
                onDeleteAccountClick = onDeleteAccountClick,
                onDismiss = onDismiss,
                onAccept = onAccept
            )
        }
    }
}