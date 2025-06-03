package aeb.proyecto.login

import aeb.proyecto.login.components.horizontal.HorizontalLoginScreen
import aeb.proyecto.login.components.vertical.VerticalLoginScreen
import aeb.proyecto.login.model.DataLoginBottomSheet
import aeb.proyecto.ui.navigationIcon.NavigationIcon
import aeb.proyecto.ui.orientation.Orientation
import aeb.proyecto.ui.orientation.getOrientation
import aeb.proyecto.ui.text.LabelLargeText
import aeb.proyecto.ui.topbar.providers.ProvideAppBarNavigationIcon
import aeb.proyecto.ui.topbar.providers.ProvideAppBarTitle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun LoginScreen(
    onSaveNavigate: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
){

    val orientation = getOrientation()

    val dataLoginScreen = viewModel.dataLoginScreen.collectAsStateWithLifecycle().value
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val bottomSheetState = viewModel.dataBottomSheet.collectAsStateWithLifecycle().value

    ProvideAppBarTitle {
        LabelLargeText(stringResource(R.string.login_topbar_title),fontSize = 20.sp)
    }

    ProvideAppBarNavigationIcon {
        NavigationIcon()
    }

    LaunchedEffect(Unit){
        viewModel.getSaveCredentials()
    }

    //Variables pantalla
    val onClickChecked = { viewModel.setChecked() }
    val onClickLoginMode = { viewModel.setLoginMode() }
    val onClickAccept = { viewModel.handleAcceptButton() }
    val onClickResetPassword = { viewModel.setDataBottomSheet(DataLoginBottomSheet.FORGOT_PASSWORD) }
    val onClickGoogle = { viewModel.signInGoogle() }
    val toSaveScreen = { onSaveNavigate() }

    //Varibles bottomSheet
    val onDismiss = { viewModel.closeBottomSheet() }
    val onAccept = { viewModel.requestAcceptBottomSheet() }

    when(orientation){
        Orientation.Portrait -> {
            VerticalLoginScreen(
                uiState = uiState,
                dataLoginScreen = dataLoginScreen,
                bottomSheetState = bottomSheetState,
                onClickChecked = onClickChecked,
                onClickLoginMode = onClickLoginMode,
                onClickAccept = onClickAccept,
                onClickGoogle = onClickGoogle,
                onClickResetPassword = onClickResetPassword,
                toSaveScreen = toSaveScreen,
                onDismiss = onDismiss,
                onAccept = onAccept,
            )
        }
        Orientation.Landscape -> {
            HorizontalLoginScreen(
                uiState = uiState,
                dataLoginScreen = dataLoginScreen,
                bottomSheetState = bottomSheetState,
                onClickChecked = onClickChecked,
                onClickLoginMode = onClickLoginMode,
                onClickAccept = onClickAccept,
                onClickGoogle = onClickGoogle,
                onClickResetPassword = onClickResetPassword,
                toSaveScreen = toSaveScreen,
                onDismiss = onDismiss,
                onAccept = onAccept,
            )
        }
    }
}