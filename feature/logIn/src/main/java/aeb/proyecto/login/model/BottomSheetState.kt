package aeb.proyecto.login.model

import androidx.compose.foundation.text.input.TextFieldState

data class BottomSheetState(
    var showBottomSheet:Boolean = false,
    var dataBottomSheet: DataLoginBottomSheet = DataLoginBottomSheet.ERROR,
    var emailSentForgotPassword: TextFieldState = TextFieldState(),
)